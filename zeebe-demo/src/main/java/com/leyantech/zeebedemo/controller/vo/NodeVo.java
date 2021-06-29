package com.leyantech.zeebedemo.controller.vo;

import lombok.Data;

/**
 * @author yan.sun, {@literal yan.sun@leyantech.com}
 * @date 2021-06-28.
 */
@Data
public class NodeVo {

  private Long id;

  /**
   * 流程主键ID.
   */
  private Long processId;

  /**
   * 节点bpmn定义ID（建议UUID）.
   */
  private String nodeBpmnId;

  /**
   * 节点类型(作业类型).
   */
  private String nodeType;
}
