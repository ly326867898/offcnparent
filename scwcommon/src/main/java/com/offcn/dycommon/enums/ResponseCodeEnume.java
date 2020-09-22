package com.offcn.dycommon.enums;

public enum ResponseCodeEnume {

    //user模块响应状态
    SUCCESSS(200,"操作成功"),
    FAIl(1,"服务器异常"),
    NOT_FOUND(404,"资源找不到"),
    NOT_AUTHED(403,"无权限,拒绝访问"),
    PARAM_INVAILD(400,"提交非法操作");


    private Integer code;
    private String message;

    ResponseCodeEnume() {
    }

    ResponseCodeEnume(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

