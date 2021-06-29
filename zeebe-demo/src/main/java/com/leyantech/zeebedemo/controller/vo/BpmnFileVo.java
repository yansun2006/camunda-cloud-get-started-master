package com.leyantech.zeebedemo.controller.vo;

import lombok.Data;

/**
 * @author yan.sun, {@literal yan.sun@leyantech.com}
 * @date 2021-06-22.
 */
@Data
public class BpmnFileVo {
  private Long processId;
  private String processDefId;
  private String processName;
  private String content;
}
