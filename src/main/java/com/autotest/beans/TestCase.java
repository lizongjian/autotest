package com.autotest.beans;


import com.alibaba.excel.annotation.ExcelProperty;

import lombok.Data;

@Data
public class TestCase {

	@ExcelProperty("module")
	private String module;
	
	@ExcelProperty("keyword")
	private String keyword;
	
	@ExcelProperty("request_type")
	private String requestType;
	
	@ExcelProperty("request_protocol")
	private String requestProtocol;
	
	@ExcelProperty("request_method")
	private String requestMethod;
	
	@ExcelProperty("request_path")
	private String requestPath;
	
	@ExcelProperty("request_header")
	private String requestHeader;
	
	@ExcelProperty("default_parame")
	private String defaultParame;
	
	@ExcelProperty("in_parame")
	private String inParame;
	
	@ExcelProperty("out_parame")
	private String outParame;
	
	@ExcelProperty("in_replace")
	private String inReplace;
	
	@ExcelProperty("out_check")
	private String outCheck;
	
	@ExcelProperty("log")
	private String log;
}
