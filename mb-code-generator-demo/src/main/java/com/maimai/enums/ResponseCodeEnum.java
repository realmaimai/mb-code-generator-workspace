package com.maimai.enums;

public enum ResponseCodeEnum {

    CODE_200( 200, "request success"),
    CODE_404( 404, "no such resources"),
    CODE_600( 600, "request parameter error"),
    CODE_601( 601, "information duplicated"),
    CODE_500( 500, "server error, please contact admin");

    private Integer code;
    private String msg;

    ResponseCodeEnum( Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
