package io.camunda.getstarted;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.client.api.worker.JobHandler;
import io.camunda.zeebe.client.api.worker.JobWorker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author yan.sun, {@literal yan.sun@leyantech.com}
 * @date 2021-06-18.
 */
public class SyExecuteMain {
  private static final Logger LOG = LogManager.getLogger(SyExecuteMain.class);
  private static ExecutorService poolExecutor = Executors.newCachedThreadPool();

  public static void main(String[] args) {
    // 成功
    startProcessInstance("200");
    // 订单未发货
//    startProcessInstance("201");
    // 订单获取失败
//    startProcessInstance("202");

    // 物流拦截失败
//    startProcessInstance("203");
  }

  /**
   * 启动物流拦截流程实例.
   */
  private static void startProcessInstance(String tradeId) {
    // 启动ZeebeClient
    ZeebeClient client = ZeebeClientFactory.getZeebeClient();

    // 注册 获取订单 JobWorker
    JobWorker orderJobWorker = client.newWorker()
        .jobType("get-orderInfo-type")
        .handler(new GetOrderInfoHandler())
        .timeout(Duration.ofSeconds(15))
        .open();

    // 注册 物流拦截 JobWorker
    JobWorker logisticsJobWorker = client.newWorker()
        .jobType("blocking-logistics-type")
        .handler(new BlockingLogisticsHandler())
        .timeout(Duration.ofSeconds(15))
        .open();

    // 注册 拦截发货 JobWorker
    JobWorker shippingJobWorker = client.newWorker()
        .jobType("blocking-shipping-type")
        .handler(new BlockingShippingHandler())
        .timeout(Duration.ofSeconds(15))
        .open();

    // 注册 通知买家 JobWorker
    JobWorker notifyBuyerJobWorker = client.newWorker()
        .jobType("notify-buyer-type")
        .handler(new NotifyBuyerHandler())
        .timeout(Duration.ofSeconds(15))
        .open();

    // 注册 通知客服 JobWorker
    JobWorker notifyAssistantJobWorker = client.newWorker()
        .jobType("notify-assistant-type")
        .handler(new NotifyAssistantHandler())
        .timeout(Duration.ofSeconds(15))
        .open();

    // 启动流程实例
    client.newCreateInstanceCommand()
        .bpmnProcessId("sy-process1")
        .latestVersion()
        .variables(Map.of("tradeId", tradeId))
        .send()
        .join();

    waitUntilSystemInput("exit");

    // 关闭客户端
    notifyAssistantJobWorker.close();
    notifyBuyerJobWorker.close();
    shippingJobWorker.close();
    logisticsJobWorker.close();
    orderJobWorker.close();
    client.close();
  }

  /**
   * 获取订单 JobWorker.
   */
  private static class GetOrderInfoHandler implements JobHandler {
    @Override
    public void handle(final JobClient client, final ActivatedJob job) {
      // here: business logic that is executed with every job
      LOG.info(job);
      LOG.info("获取订单 execute");
      Map<String, Object> variable = job.getVariablesAsMap();
      TradeVo tradeVo = getTradeInfo((String) variable.get("tradeId"));
      if (tradeVo == null) {
        variable.put("orderCondition", "2");
      } else if (tradeVo.getStatus() == TradeStatus.WAIT_BUYER_CONFIRM_GOODS) {
        variable.put("orderCondition", "0");
      } else if (tradeVo.getStatus() == TradeStatus.WAIT_SELLER_SEND_GOODS) {
        variable.put("orderCondition", "1");
      } else {
        variable.put("orderCondition", "2");
      }
      client.newCompleteCommand(job.getKey()).variables(variable).send().join();
    }
  }

  /**
   * 物流拦截 JobWorker.
   */
  private static class BlockingLogisticsHandler implements JobHandler {
    @Override
    public void handle(final JobClient client, final ActivatedJob job) {
      // here: business logic that is executed with every job
      LOG.info(job);
      LOG.info("物流拦截 execute");
      Map<String, Object> variable = job.getVariablesAsMap();
      String tradeId = (String) variable.get("tradeId");

      // 异步调用拦截API
      poolExecutor.submit(() -> {
        // sleep
        try {
          Thread.sleep(30000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        boolean result = callBlockingLogisticsApi(tradeId);
        if (result) {
          variable.put("blockingCondition", "0");
        } else {
          variable.put("blockingCondition", "1");
        }
        // 物流拦截 异步消息
        ZeebeClient zeebeClient = ZeebeClientFactory.getZeebeClient();
        zeebeClient.newPublishMessageCommand()
            .messageName("Message_0h8to5v")
            .correlationKey(tradeId)
            .variables(variable)
            .send()
            .join();
      });

      client.newCompleteCommand(job.getKey()).send().join();
    }
  }

  /**
   * 发货拦截 JobWorker.
   */
  private static class BlockingShippingHandler implements JobHandler {
    @Override
    public void handle(final JobClient client, final ActivatedJob job) {
      // here: business logic that is executed with every job
      LOG.info(job);
      LOG.info("发货拦截 execute");
      client.newCompleteCommand(job.getKey()).send().join();
    }
  }

  /**
   * 通知买家 JobWorker.
   */
  private static class NotifyBuyerHandler implements JobHandler {
    @Override
    public void handle(final JobClient client, final ActivatedJob job) {
      // here: business logic that is executed with every job
      LOG.info(job);
      LOG.info("通知买家 execute");
      client.newCompleteCommand(job.getKey()).send().join();
    }
  }

  /**
   * 通知客服 JobWorker.
   */
  private static class NotifyAssistantHandler implements JobHandler {
    @Override
    public void handle(final JobClient client, final ActivatedJob job) {
      // here: business logic that is executed with every job
      LOG.info(job);
      LOG.info("通知客服 execute");
      client.newCompleteCommand(job.getKey()).send().join();
    }
  }

  /**
   * 通知客服 JobWorker.
   */
  private static class AssistantCommitJobWorker implements JobHandler {
    @Override
    public void handle(final JobClient client, final ActivatedJob job) {
      // here: business logic that is executed with every job
      LOG.info(job);
      LOG.info("获取客服提交信息 execute");

      Map<String, Object> variable = job.getVariablesAsMap();
      String remark = (String) variable.get("textfieldRemark");
      LOG.info("获取客服提交信息 {}", remark);
      client.newCompleteCommand(job.getKey()).send().join();
    }
  }

  /**
   * 阻塞.
   */
  private static void waitUntilSystemInput(final String exitCode) {
    try (final Scanner scanner = new Scanner(System.in)) {
      while (scanner.hasNextLine()) {
        final String nextLine = scanner.nextLine();
        if (nextLine.contains(exitCode)) {
          return;
        }
      }
    }
  }

  private static TradeVo getTradeInfo(String tradeId) {
    if (tradeId.equals("200") || tradeId.equals("203") || tradeId.equals("204")) {
      TradeVo tradeVo = new TradeVo();
      tradeVo.setTradeId("tradeId-1");
      tradeVo.setStatus(TradeStatus.WAIT_BUYER_CONFIRM_GOODS);
      return tradeVo;
    } else if (tradeId.equals("201")) {
      TradeVo tradeVo = new TradeVo();
      tradeVo.setTradeId("tradeId-1");
      tradeVo.setStatus(TradeStatus.WAIT_SELLER_SEND_GOODS);
      return tradeVo;
    } else {
      return null;
    }
  }

  /**
   * 调用拦截物流API.
   */
  private static boolean callBlockingLogisticsApi(String tradeId) {
    if (tradeId.equals("200")) {
      return true;
    } else {
      return false;
    }
  }
}
