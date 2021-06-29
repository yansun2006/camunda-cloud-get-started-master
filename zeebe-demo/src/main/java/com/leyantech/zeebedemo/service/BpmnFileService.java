package com.leyantech.zeebedemo.service;

import static com.leyantech.zeebedemo.db.zeebe_demo_db.tables.TProcessInfo.T_PROCESS_INFO;

import com.leyantech.zeebedemo.common.exception.RestException;
import com.leyantech.zeebedemo.common.file.FileUtil;
import com.leyantech.zeebedemo.common.utils.XmlUtils;
import com.leyantech.zeebedemo.controller.vo.BpmnFileVo;
import com.leyantech.zeebedemo.db.zeebe_demo_db.tables.TProcessInfo;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author yan.sun, {@literal yan.sun@leyantech.com}
 * @date 2021-06-22.
 */
@Service
public class BpmnFileService {

  private static final TProcessInfo TABLE = TProcessInfo.T_PROCESS_INFO;
  private static final Long ORG_ID_1 = 1L;
  private static final String PROCESS_NAME_PREFIX = "process_";

  @Autowired
  private DSLContext dslContext;

  public BpmnFileVo getProcessFile(Long processId) throws IOException {
    String path = FileUtil.getBpmnFilePath(processId);
    var fileContent = FileUtils.readFileToString(new File(path), StandardCharsets.UTF_8);

    var fileVo = new BpmnFileVo();
    fileVo.setProcessId(processId);
    fileVo.setContent(fileContent);
    return fileVo;
  }

  public BpmnFileVo saveProcessFile(BpmnFileVo bpmnFileVo) throws IOException {
    if (bpmnFileVo.getProcessId() == null) {
      // add
      if (StringUtils.isBlank(bpmnFileVo.getContent())) {
        throw new RestException("content is empty");
      }

      BpmnFileVo xmlData = XmlUtils.getProcessInfoFromBpmn(bpmnFileVo.getContent());
      bpmnFileVo.setProcessDefId(xmlData.getProcessDefId());
      bpmnFileVo.setProcessName(xmlData.getProcessName());

      String processName = bpmnFileVo.getProcessName();
      if (StringUtils.isBlank(processName)) {
        processName = PROCESS_NAME_PREFIX + System.currentTimeMillis();
      }
      Long processId = Objects.requireNonNull(dslContext.insertInto(TABLE)
          .columns(TABLE.ORG_ID, TABLE.PROCESS_NAME, TABLE.PROCESS_DEF_ID)
          .values(ORG_ID_1, processName, bpmnFileVo.getProcessDefId())
          .returning(TABLE.ID)
          .fetchOne())
          .getId();
      if (processId == null) {
        throw new RestException("add process error");
      }

      String path = FileUtil.getBpmnFilePath(processId);
      FileUtils.writeStringToFile(new File(path), bpmnFileVo.getContent(), StandardCharsets.UTF_8);
      bpmnFileVo.setProcessId(processId);
    } else {
      // update
      com.leyantech.zeebedemo.db.zeebe_demo_db.tables.pojos.TProcessInfo processInfo = dslContext.select()
          .from(T_PROCESS_INFO)
          .where(T_PROCESS_INFO.ID.eq(bpmnFileVo.getProcessId()))
          .fetchAny()
          .into(com.leyantech.zeebedemo.db.zeebe_demo_db.tables.pojos.TProcessInfo.class);
      if (processInfo == null) {
        throw new RestException("不存在的流程");
      }

      String path = FileUtil.getBpmnFilePath(bpmnFileVo.getProcessId());
      FileUtils.writeStringToFile(new File(path), bpmnFileVo.getContent(), StandardCharsets.UTF_8);

      // 更新processId 和 processName
      BpmnFileVo xmlData = XmlUtils.getProcessInfoFromBpmn(bpmnFileVo.getContent());
      if (!processInfo.getProcessDefId().equals(xmlData.getProcessDefId())
          || !processInfo.getProcessName().equals(xmlData.getProcessName())) {
        dslContext.update(TABLE)
            .set(TABLE.PROCESS_DEF_ID, xmlData.getProcessDefId())
            .set(TABLE.PROCESS_NAME, xmlData.getProcessName())
            .where(TABLE.ID.eq(processInfo.getId()))
            .execute();
      }
    }
    bpmnFileVo.setContent(StringUtils.EMPTY);
    return bpmnFileVo;
  }
}
