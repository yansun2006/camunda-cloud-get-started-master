package com.leyantech.zeebedemo.controller.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author yan.sun, {@literal yan.sun@leyantech.com}
 * @date 2021-06-28.
 */
@Data
public class NodeDefListVo {

  private String name;
  private String uri;
  private String prefix;
  private XmlDefVo xml;
  private List<NodeDefVo> types;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class XmlDefVo {
    private String tagAlias;
  }
}
