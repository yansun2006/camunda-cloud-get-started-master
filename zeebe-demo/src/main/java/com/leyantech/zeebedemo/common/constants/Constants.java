package com.leyantech.zeebedemo.common.constants;

import java.time.format.DateTimeFormatter;

/**
 * @author yan.sun, {@literal yan.sun@leyantech.com}
 * @date 2021-06-08.
 */
public class Constants {
  public static final DateTimeFormatter YYYY_MM_DD = DateTimeFormatter.ofPattern("yyyyMMdd");
  public static final String LOG_S_S = "%s : %s";
  public static final String LOG_S = "%s";
  public static final long SECOND_TO_MS_UNIT = 1000;
  public static final String BPMN_NODE_LIST_NAME = "leyanModdle";
  public static final String BPMN_URI = "http://some-company/schema/bpmn/qa";
  public static final String BPMN_PREFIX = "leyan";
  public static final String BPMN_TAG_ALIAS = "lowerCase";
  public static final String BPMN_EVENT_SUPER_CLASS = "bpmn:Event";
  public static final String BPMN_SERVICE_TASK_SUPER_CLASS = "bpmn:serviceTask";


  /*------------------ Field ---------------------*/
  public static final String FIELD_FORMAT_STRING = "string";          //字符
  public static final String FIELD_FORMAT_INT = "int";                //整数
  public static final String FIELD_FORMAT_FLOAT = "float";            //小数
  public static final String FIELD_FORMAT_BOOLEAN = "boolean";        //是否
  public static final String FIELD_FORMAT_LIST = "list";              //选项


  private Constants() {
  }
}
