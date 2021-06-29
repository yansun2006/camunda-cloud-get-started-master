package com.leyantech.zeebedemo.common.exception;

import com.leyantech.zeebedemo.common.base.ResponseCode;
import com.leyantech.zeebedemo.common.base.ServerResponse;

/**
 * @author yan.sun, yan.sun@leyantech.com
 * @date 2019-01-10.
 */
public class RestException extends RuntimeException {

  protected ServerResponse result;

  public RestException(String resultMessage) {
    this(ResponseCode.ERROR.getCode(), resultMessage);
  }

  public RestException(Integer resultCode, String resultMessage) {
    super(resultMessage);
    this.result = ServerResponse.createByErrorCodeMessage(resultCode, resultMessage);
  }

  public static void throwException(String resultMessage) {
    throwException(ResponseCode.ERROR.getCode(), resultMessage);
  }

  public static void throwException(Integer resultCode, String resultMessage) {
    throw new RestException(ResponseCode.ERROR.getCode(), resultMessage);
  }

  public ServerResponse getResult() {
    return result;
  }

  public void setResult(ServerResponse result) {
    this.result = result;
  }
}
