package com.leyantech.zeebedemo.worker;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.EnableZeebeClient;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * 发货拦截 JobWorker.
 * @author yan.sun, {@literal yan.sun@leyantech.com}
 * @date 2021-06-24.
 */
@Component
@EnableZeebeClient
public class BlockingShippingHandler {
  private static final Logger LOG = LogManager.getLogger(BlockingShippingHandler.class);

  @ZeebeWorker(type = "blocking-shipping-type")
  public void handle(final JobClient client, final ActivatedJob job) {
    // here: business logic that is executed with every job
    LOG.info(job);
    LOG.info("发货拦截 execute");
    client.newCompleteCommand(job.getKey()).send().join();
  }
}
