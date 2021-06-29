package com.leyantech.zeebedemo.common.exception;

import com.leyantech.zeebedemo.common.base.ServerResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import org.mybatis.spring.MyBatisSystemException;
//import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

/**
 * 全局异常拦截器
 * @author yan.sun, yan.sun@leyantech.com
 * @date 2020/1/10.
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {
  private final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

  /**
   * RestException.
   */
  @ExceptionHandler({RestException.class})
  public ServerResponse restExceptionHandler(RestException e, WebRequest request) {
    logger.error("{}, url:{}, errorMessage:{}", e.getClass().getName(),
      ((ServletWebRequest)request).getRequest().getRequestURL().toString(), e.getMessage(), e);

    return e.getResult() == null ? ServerResponse.createByErrorMessage(e.getMessage()) : e.getResult();
  }

  /**
   * 其他错误.
   */
  @ExceptionHandler({Exception.class})
  public ServerResponse exceptionHandler(Exception e, WebRequest request) {
    logger.error("{}, url:{}, errorMessage:{}", e.getClass().getName(),
      ((ServletWebRequest)request).getRequest().getRequestURL().toString(), e.getMessage(), e);

//    if (e instanceof MyBatisSystemException || e instanceof DataAccessException) {
//      return ServerResponse.createByErrorMessage(String.format("Error querying database, contact the admin. (%s)", e.getClass().getName()));
//    }
    return ServerResponse.createByErrorMessage(e.getMessage());
  }
}
