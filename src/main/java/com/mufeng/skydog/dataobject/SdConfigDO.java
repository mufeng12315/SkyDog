package com.mufeng.skydog.dataobject;

import lombok.Data;

/**
 * 配置项
 */
@Data
public class SdConfigDO {

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
    private String configValue;
    
    private String gmtCreated;

    private String gmtModified;
}
