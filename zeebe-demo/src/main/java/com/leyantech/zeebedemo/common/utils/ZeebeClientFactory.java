package com.leyantech.zeebedemo.common.utils;

import io.camunda.zeebe.client.ZeebeClient;

public class ZeebeClientFactory {

  public static ZeebeClient getZeebeClient() {
//    return ZeebeClient.newCloudClientBuilder()
//        .withClusterId("365eed98-16c1-4096-bb57-eb8828ed131e")
//        .withClientId("GZVO3ALYy~qCcD3MYq~sf0GIszNzLE_z")
//        .withClientSecret(".RPbZc6q0d6uzRbB4LW.B8lCpsxbBEpmBX0AHQGzINf3.KK9RkzZW1aDaZ-7WYNJ")
//        .build();

    final String broker = "127.0.0.1:26500";
    return ZeebeClient.newClientBuilder()
        .gatewayAddress(broker)
        .usePlaintext()
        .build();
  }

}
