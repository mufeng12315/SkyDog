package com.mufeng.skydog.bean;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SdRow {

    private Map<String,SdColumn> columnMap;

}
