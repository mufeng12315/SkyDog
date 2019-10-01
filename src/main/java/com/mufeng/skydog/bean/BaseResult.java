package com.mufeng.skydog.bean;

import lombok.Data;

@Data
public class BaseResult<T> {

    /**
     * code：0时为成功
     */
    private String code;
    private String message;

    /**
     * 业务数据
     */
    private T result;

    public void setSuccess(){
        this.code = "0";
    }
}
