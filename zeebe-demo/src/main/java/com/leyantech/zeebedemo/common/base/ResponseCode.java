package com.leyantech.zeebedemo.common.base;

public enum ResponseCode {
    /**
     * 其实是调用构造方法，所以第一个值对应code,第二个值对应desc
     */
    SUCCESS(0, "SUCCESS"),
    ERROR(1, "ERROR"),
    ILLEGAL_ARGUMENT(2, "ILLEGAL_ARGUMENT"),
    NEED_LOGIN(11, "NEED_LOGIN"),
    COMPANY_NAME_REPEAT(12, "企业名称有重复");

    private final Integer code;
    private final String desc;

    ResponseCode(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
