package com.mufeng.skydog.utils;

import com.mufeng.skydog.bean.SdSqlContent;

public class SkyDogBeanUtils {

    /**
     * 解析sql字符串
     * @param sqlContentStr
     */
    public static SdSqlContent parseSqlContent(String sqlContentStr){
        //格式为：数据源名称1:sql1;数据源名称2:sql2
        if(sqlContentStr==null || sqlContentStr.isEmpty()){
            return null;
        }
        String[] sqlStrs  = sqlContentStr.split(":");
        if(sqlStrs.length!=2){
            throw new RuntimeException("sqlContent格式错误！sqlContent格式为:数据源名称1:sql1");
        }
        SdSqlContent sdSqlContent = new SdSqlContent();
        sdSqlContent.setDataSourceCode(sqlStrs[0]);
        sdSqlContent.setSqlContent(sqlStrs[1]);
        return sdSqlContent;
    }
}
