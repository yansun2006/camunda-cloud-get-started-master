package com.leyantech.zeebedemo.service;

import static com.leyantech.zeebedemo.db.zeebe_demo_db.tables.TProcessInfo.T_PROCESS_INFO;

import com.leyantech.zeebedemo.common.exception.RestException;
import com.leyantech.zeebedemo.common.file.FileUtil;
import com.leyantech.zeebedemo.db.zeebe_demo_db.tables.pojos.TProcessInfo;

import io.camunda.zeebe.spring.client.EnableZeebeClient;
import io.camunda.zeebe.spring.client.ZeebeClientLifecycle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author yan.sun, {@literal yan.sun@leyantech.com}
 * @date 2021-06-23.
 */
@Service
@EnableZeebeClient
public class ProcessInfoService {
  private static final Logger LOG = LogManager.getLogger(ProcessInfoService.class);

  @Autowired
  private DSLContext dslContext;

  @Autowired
  private ZeebeClientLifecycle client;

  public List<TProcessInfo> getProcessInfoList() {
    return dslContext.select()
        .from(T_PROCESS_INFO)
        .fetch()
        .into(TProcessInfo.class);
  }

  /**
   * 部署流程.
   */
  public void depolyProcess(Long processId) {
    TProcessInfo processInfo = dslContext.select()
        .from(T_PROCESS_INFO)
        .where(T_PROCESS_INFO.ID.eq(processId))
        .fetchAny()
        .into(TProcessInfo.class);

    if (processInfo == null) {
      throw new RestException("不存在的流程");
    }

    String path = FileUtil.getBpmnFilePath(processId);

    // 部署流程
    client.newDeployCommand()
        .addResourceFile(path)
        .send()
        .join();
    LOG.info("deploy success.");
  }

  /**
   * 启动流程.
   */
  public void startProcess(Long processId) {
    var tradeId = "200";
    TProcessInfo processInfo = dslContext.select()
        .from(T_PROCESS_INFO)
        .where(T_PROCESS_INFO.ID.eq(processId))
        .fetchAny()
        .into(TProcessInfo.class);

    if (processInfo == null) {
      throw new RestException("不存在的流程");
    }
    client.newCreateInstanceCommand()
        .bpmnProcessId(processInfo.getProcessDefId())
        .latestVersion()
        .variables(Map.of("tradeId", tradeId))
        .send()
        .join();
  }
}
