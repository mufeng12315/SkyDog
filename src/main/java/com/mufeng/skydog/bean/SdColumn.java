package com.mufeng.skydog.bean;

import lombok.Data;

@Data
public class SdColumn {
    private Integer colIndex;
    private String colName;
    private String colType;
    private Object colValue;
}
