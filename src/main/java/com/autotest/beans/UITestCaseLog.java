package com.autotest.beans;

import com.alibaba.excel.annotation.ExcelProperty;

public class UITestCaseLog {
	@ExcelProperty("模块名称")
	private String module;

	@ExcelProperty("关键字")
	private String keyword;

	@ExcelProperty("对象/函数")
	private String object;

	@ExcelProperty("输入参数")
	private String inParam;

	@ExcelProperty("输出参数")
	private String outParam;
	
	@ExcelProperty("状态")
	private String status;
}
