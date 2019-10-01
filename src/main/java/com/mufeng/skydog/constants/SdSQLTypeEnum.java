package com.mufeng.skydog.constants;

public enum SdSQLTypeEnum {

    CHAR("CHAR",java.lang.String.class),
    BLOB("BLOB",java.lang.Byte[].class),
    VARCHAR("VARCHAR",java.lang.String.class),
    INTEGER("INTEGER",java.lang.Long.class),
    TINYINT("TINYINT",java.lang.Integer.class),
    SMALLINT("SMALLINT",java.lang.Integer.class),
    MEDIUMINT("MEDIUMINT",java.lang.Integer.class),
    BIT("BIT",java.lang.Boolean.class),
    BIGINT("BIGINT",java.lang.Long.class),
    FLOAT("FLOAT",java.lang.Float.class),
    DOUBLE("DOUBLE",java.lang.Double.class),
    DECIMAL("DECIMAL",java.math.BigDecimal.class),
    BOOLEAN("BOOLEAN",java.lang.Boolean.class),
    DATE("DATE",java.sql.Date.class),
    TIME("TIME",java.sql.Time.class),
    DATETIME("DATETIME",java.sql.Timestamp.class),
    TIMESTAMP("TIMESTAMP",java.sql.Timestamp.class),
    YEAR("YEAR",java.sql.Date.class);

    private String code;
    private Class cls;

    private SdSQLTypeEnum(String code, Class cls) {
        this.code = code;
        this.cls = cls;
    }

    public static SdSQLTypeEnum getEnumByCode(String code) {
        for (SdSQLTypeEnum an : SdSQLTypeEnum.values()) {
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

    public Class getCls() {
        return cls;
    }

    public void setCls(Class cls) {
        this.cls = cls;
    }
}
