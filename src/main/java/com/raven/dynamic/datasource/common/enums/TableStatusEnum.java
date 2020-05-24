package com.raven.dynamic.datasource.common.enums;

public enum TableStatusEnum {

	NORMAL_STATUS (1, "正常"),
	HIS_STATUS (2, "历史"),
	DEL_STATUS (3, "删除"),;
	
	private Integer code;
	private String name;
	
	TableStatusEnum(Integer code, String name) {
		this.code = code;
		this.name = name;
	}
	
	public Integer getCode() {
		return code;
	}
	
	public String getName() {
		return name;
	}
}
