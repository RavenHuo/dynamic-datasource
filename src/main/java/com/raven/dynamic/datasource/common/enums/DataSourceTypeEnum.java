package com.raven.dynamic.datasource.common.enums;

/**
 * @description:
 * @author: huorw
 * @create: 2020-05-23 22:20
 */
public enum DataSourceTypeEnum {

    MASTER(0,"MASTER"), SLAVE(1,"SLAVE");

    private int code;

    private String value;


    private DataSourceTypeEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
