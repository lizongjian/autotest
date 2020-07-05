package com.autotest.beans;

import com.alibaba.excel.annotation.ExcelProperty;

import lombok.Data;

@Data
public class InterfaceCase {
	
	@ExcelProperty("用例名称")
	private String module;
	
	@ExcelProperty("接口名称")
	private String interfaceName;

	@ExcelProperty("替换")
	private String inReplace;

	@ExcelProperty("校验")
	private String outCheck;
	
	
}	
