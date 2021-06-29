package io.camunda.getstarted;

import io.camunda.zeebe.client.ZeebeClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author yan.sun, {@literal yan.sun@leyantech.com}
 * @date 2021-06-18.
 */
public class SyDeployMain {
  private static final Logger LOG = LogManager.getLogger(SyDeployMain.class);

  public static void main(String[] args) {
    try (ZeebeClient client = ZeebeClientFactory.getZeebeClient()) {
      // 部署流程
      client.newDeployCommand()
//          .addResourceFromClasspath("sy-process1.bpmn")
          .addResourceFromClasspath("sy-process1.bpmn")
          .send()
          .join();
      LOG.info("deploy success.");
    }
  }
}
