package com.leyantech.zeebedemo.controller.vo;

import com.leyantech.zeebedemo.controller.vo.FieldSubmitVo.FieldAnswerVo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author yan.sun, {@literal yan.sun@leyantech.com}
 * @date 2021-06-28.
 */
@Data
public class NodeDefVo {
  private String name;
  private String jobType;
  private List<String> superClass;
  private List<PropertyVo> properties;
  private List<FieldAnswerVo> answers;

  @Data
  public static class PropertyVo {
    private Long fieldId;
    private String name;
    private boolean isAttr;
    private String type;
    private List<FieldOptionVo> fieldOptionVoList;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class FieldOptionVo {
    private Long id;
    private Long fieldId;
    private String optionKey;
    private String optionValue;
  }
}
