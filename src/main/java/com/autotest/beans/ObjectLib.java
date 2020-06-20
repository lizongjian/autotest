package com.autotest.beans;

import com.alibaba.excel.annotation.ExcelProperty;

import lombok.Data;

@Data
public class ObjectLib {
	@ExcelProperty("对象名称")
	private String objectName;
	
	@ExcelProperty("定位方式")
	private String locateType;
	
	@ExcelProperty("定位值")
	private String locateValue;
	
	@ExcelProperty("等待时间")
	private String waitTime;
}
