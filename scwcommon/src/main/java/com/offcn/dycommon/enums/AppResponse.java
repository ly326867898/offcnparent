package com.offcn.dycommon.enums;

public class AppResponse<T> {


    private Integer code;
    private String meg;
    private T data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMeg() {
        return meg;
    }

    public void setMeg(String meg) {
        this.meg = meg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static<T> AppResponse<T> ok(T data){

        AppResponse<T> response = new AppResponse<T>();
        response.setCode(ResponseCodeEnume.SUCCESSS.getCode());
        response.setMeg(ResponseCodeEnume.SUCCESSS.getMessage());
        response.setData(data);
        return response;

    }

    public static<T> AppResponse<T> fail(T data){

        AppResponse<T> response = new AppResponse<T>();
        response.setCode(ResponseCodeEnume.FAIl.getCode());
        response.setMeg(ResponseCodeEnume.FAIl.getMessage());
        response.setData(data);
        return response;

    }

}
