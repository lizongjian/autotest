package com.autotest.beans;

import com.alibaba.excel.annotation.ExcelProperty;

import lombok.Data;
@Data
public class UICase {
	@ExcelProperty("组件名称")
	private String module;

	@ExcelProperty("关键字")
	private String keyword;

	@ExcelProperty("对象")
	private String objectName;

	@ExcelProperty("输入参数")
	private String inParam;

	@ExcelProperty("输出参数")
	private String outParam;
}
