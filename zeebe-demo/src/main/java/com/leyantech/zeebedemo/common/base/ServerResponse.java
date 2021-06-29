package com.leyantech.zeebedemo.common.base;


import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * 保证序列化json的时候,如果是null的对象,key也会消失
 * @param <T>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServerResponse<T> implements Serializable {
    private Integer resultCode;
    private String resultMessage;
    private T data;

    private ServerResponse(Integer resultCode) {
        this.resultCode = resultCode;
    }

    private ServerResponse(Integer resultCode, T data) {
        this.resultCode = resultCode;
        this.data = data;
    }

    private ServerResponse(Integer resultCode, String resultMessage, T data) {
        this.resultCode = resultCode;
        this.resultMessage = resultMessage;
        this.data = data;
    }

    private ServerResponse(Integer resultCode, String resultMessage) {
        this.resultCode = resultCode;
        this.resultMessage = resultMessage;
    }

//    //使之不在JSON序列化的结果中
//    @JsonIgnore
//    public boolean isSuccess() {
//        return this.status == ResponseCode.SUCCESS.getCode();
//    }

    public Integer getResultCode() {
        return resultCode;
    }

    public T getData() {
        return data;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    /**
     * 封装静态方法给外部调用
     * @param <T>
     * @return
     */
    public static <T> ServerResponse<T> createBySuccess() {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), "success");
    }

    public static <T> ServerResponse<T> createBySuccessMessage(String msg) {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), msg);
    }

    public static <T> ServerResponse<T> createBySuccess(T data) {
        //当data是String类型会调用哪一个构造方法
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), "success", data);
    }

    public static <T> ServerResponse<T> createBySuccess(String msg, T data) {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), msg, data);
    }

    /**
     * 错误的封装
     * @param <T>
     * @return
     */
    public static <T> ServerResponse<T> createByError() {
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getDesc());
    }

    public static <T> ServerResponse<T> createByErrorMessage(String errorMessage) {
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(), errorMessage);
    }

    public static <T> ServerResponse<T> createByErrorCodeMessage(int errorCode, String errorMessage) {
        return new ServerResponse<T>(errorCode, errorMessage);
    }
}
