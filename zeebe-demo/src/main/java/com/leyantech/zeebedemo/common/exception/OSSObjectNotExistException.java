package com.leyantech.zeebedemo.common.exception;

/**
 * @author yangyb, <yanbin.yang@leyantech.com>
 * @date 2019-10-25.
 */
public class OSSObjectNotExistException extends RuntimeException {

  private String path;

  public OSSObjectNotExistException(String path) {
    super("oss_object_path " + path);
    this.path = path;
  }

  public String getPath() {
    return path;
  }
}
