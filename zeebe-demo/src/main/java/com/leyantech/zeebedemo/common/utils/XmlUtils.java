package com.leyantech.zeebedemo.common.utils;

import com.leyantech.zeebedemo.common.exception.RestException;
import com.leyantech.zeebedemo.controller.vo.BpmnFileVo;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yan.sun, {@literal yan.sun@leyantech.com}
 * @date 2021-06-24.
 */
public class XmlUtils {
  private static final Logger LOGGER = LogManager.getLogger(XmlUtils.class);
  private static Map<String, String> bpmnXmlMap = new HashMap<>();

  static {
    bpmnXmlMap.put("bpmn", "http://www.omg.org/spec/BPMN/20100524/MODEL");
  }

  public static BpmnFileVo getProcessInfoFromBpmn(String xmlContent) {
    var reader = new SAXReader();
    Document document;
    BpmnFileVo bpmnFileVo;
    try {
      //result是需要解析的字符串
      //解析字符串需要转换成流的形式，可以指定转换字符编码
      document = reader.read(new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8)));
      reader.getDocumentFactory().setXPathNamespaceURIs(bpmnXmlMap);

      // 提取流程id 和 name
      var processElement = (Element) document.selectSingleNode("//bpmn:process");
      String id = processElement.attributeValue("id");
      if (StringUtils.isBlank(id)) {
        throw new RestException("bpmn file is error, not found id attribute.");
      }
      String name = processElement.attributeValue("name");

      bpmnFileVo = new BpmnFileVo();
      bpmnFileVo.setProcessDefId(id);
      bpmnFileVo.setProcessName(name);
    } catch (DocumentException e) {
      e.printStackTrace();
      LOGGER.error("parse xml error {}", e.getMessage());
      throw new RestException("parse bpmn xml error");
    }
    return bpmnFileVo;
  }
}

