package com.leyantech.zeebedemo.worker;

import com.leyantech.zeebedemo.common.utils.ZeebeClientFactory;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.EnableZeebeClient;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * @author yan.sun, {@literal yan.sun@leyantech.com}
 * @date 2021-06-24.
 */
@Component
@EnableZeebeClient
public class BlockingLogisticsHandler {
  private static final Logger LOG = LogManager.getLogger(BlockingLogisticsHandler.class);

  @Autowired
  @Qualifier("jobWorkerThreadPool")
  private ExecutorService executorService;

  @ZeebeWorker(type = "blocking-logistics-type")
  public void handle(final JobClient client, final ActivatedJob job) {
    // here: business logic that is executed with every job
    LOG.info(job);
    LOG.info("物流拦截 execute");
    Map<String, Object> variable = job.getVariablesAsMap();
    String tradeId = (String) variable.get("tradeId");

    // 异步调用拦截API
    executorService.submit(() -> {
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