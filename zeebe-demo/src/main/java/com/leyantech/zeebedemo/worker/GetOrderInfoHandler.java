package com.leyantech.zeebedemo.worker;

import com.leyantech.zeebedemo.controller.vo.TradeVo;
import com.leyantech.zeebedemo.enums.TradeStatus;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.client.api.worker.JobHandler;
import io.camunda.zeebe.spring.client.EnableZeebeClient;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 获取订单 JobWorker.
 *
 * @author yan.sun, {@literal yan.sun@leyantech.com}
 * @date 2021-06-24.
 */
@Component
@EnableZeebeClient
public class GetOrderInfoHandler {
  private static final Logger LOG = LogManager.getLogger(GetOrderInfoHandler.class);

  @ZeebeWorker(type = "get-orderInfo-type")
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

}
