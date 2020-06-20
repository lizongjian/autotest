package com.autotest.beans;

import com.alibaba.excel.annotation.ExcelProperty;

import lombok.Data;


@Data // 注在类上，提供类的get、set、equals、hashCode、canEqual、toString方法
public class Evn {

	@ExcelProperty("域名")
	private String ip;
	
	@ExcelProperty("端口")
	private String port;
	
	@ExcelProperty("数据库类型")
	private String dbType;
	
	@ExcelProperty("数据库端口")
	private String dbPort;
	
	@ExcelProperty("数据库用户")
	private String dbUser;
	
	@ExcelProperty("数据库密码")
	private String dbPassword;
	
	@ExcelProperty("数据库名称")
	private String dbName;
	
	@ExcelProperty("是否选用")
	private String isSelected;
	
	public static void main(String[] args) {
		
	}
}
