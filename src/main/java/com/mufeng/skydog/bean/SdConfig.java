package com.mufeng.skydog.bean;

import lombok.Data;

/**
 * 配置项
 */
@Data
public class SdConfig<T> {

    /**
     * id
     */
    private String id;

    /**
     * 配置项CODE
     */
    private String code;

    /**
     * 配置项值
     */
    private T configValue;

    private String gmtCreated;

    private String gmtModified;
}
