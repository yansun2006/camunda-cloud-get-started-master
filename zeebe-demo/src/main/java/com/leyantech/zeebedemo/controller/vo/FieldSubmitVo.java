package com.leyantech.zeebedemo.controller.vo;

import com.leyantech.zeebedemo.controller.vo.NodeDefVo.FieldOptionVo;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

/**
 * @author yan.sun, {@literal yan.sun@leyantech.com}
 * @date 2021-06-28.
 */
@Data
public class FieldSubmitVo {
  private Long nodeId;
  private String nodeBpmnId;
  private List<FieldAnswerVo> answerList;

  @Data
  public static class FieldAnswerVo {
    private Long fieldId;
    private List<FieldOptionVo> optionList = Lists.newArrayList();
  }
}
