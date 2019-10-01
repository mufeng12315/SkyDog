package com.mufeng.skydog.bean;

import lombok.Data;

import java.util.List;

/**
 * 数据来源
 */
@Data
public class SdSqlContent {
    /**
     * SQL
     */
    private String sqlContent;

    /**
     * sql对应的数据源
     */
    private String dataSourceCode;
}
