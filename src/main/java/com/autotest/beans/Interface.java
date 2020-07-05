package com.autotest.beans;

import com.alibaba.excel.annotation.ExcelProperty;

import lombok.Data;

@Data
public class Interface {

	@ExcelProperty("接口名称")
	private String interfaceName;

	@ExcelProperty("请求类型")
	private String requestType;

	@ExcelProperty("请求协议")
	private String requestProtocol;

	@ExcelProperty("请求方法")
	private String requestMethod;

	@ExcelProperty("请求路径")
	private String requestPath;

	@ExcelProperty("请求头")
	private String requestHeader;

	@ExcelProperty("输入参数")
	private String inParam;

	@ExcelProperty("输出参数")
	private String outParam;


}
