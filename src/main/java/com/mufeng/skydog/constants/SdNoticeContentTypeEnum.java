package com.mufeng.skydog.constants;

public enum SdNoticeContentTypeEnum {

    JAVA_EXPRESS("javaExpress","Java表达式"),
    SQL_RESULT("sqlResult","SQL结果");

    private String code;
    private String name;

    private SdNoticeContentTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static SdNoticeContentTypeEnum getEnumByCode(String code) {
        for (SdNoticeContentTypeEnum an : SdNoticeContentTypeEnum.values()) {
            if (an.getCode().equals(code))
                return an;
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
