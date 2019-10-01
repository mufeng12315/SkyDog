package com.mufeng.skydog.bean;

import lombok.Data;

@Data
public class SdMessageNoticeResult {

    /**
     * 返回码:0为成功
     */
    private String code;

    /**
     * 返回信息
     */
    private String message;

    /**
     * 判断是否成功
     * @return
     */
    public Boolean isSucceed(){
        if("0".equals(code)){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public void setSucceedCode(){
        this.code = "0";
    }
}
