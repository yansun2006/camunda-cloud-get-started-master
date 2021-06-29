package com.leyantech.zeebedemo.worker;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.EnableZeebeClient;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * 通知客服 JobWorker.
 * @author yan.sun, {@literal yan.sun@leyantech.com}
 * @date 2021-06-24.
 */
@Component
@EnableZeebeClient
public class NotifyAssistantHandler {
  private static final Logger LOG = LogManager.getLogger(NotifyAssistantHandler.class);

  @ZeebeWorker(type = "notify-assistant-type")
  public void handle(final JobClient client, final ActivatedJob job) {
    // here: business logic that is executed with every job
    LOG.info(job);
    LOG.info("通知客服 execute");
    client.newCompleteCommand(job.getKey()).send().join();
  }
}