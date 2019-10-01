package com.mufeng.skydog.bean;

import lombok.Data;

/**
 * 配置项
 */
@Data
public class SdConfigValue {

    /**
     * 配置项CODE
     */
    private String code;

    /**
     * 配置项值
     */
    private String configValue;

    public SdConfigValue(String code,String configValue){
        this.code = code;
        this.configValue = configValue;
    }

}
