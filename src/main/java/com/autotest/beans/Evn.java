package com.autotest.beans;

import com.alibaba.excel.annotation.ExcelProperty;

import lombok.Data;


@Data // 注在类上，提供类的get、set、equals、hashCode、canEqual、toString方法
public class Evn {

	@ExcelProperty("ip")
	private String ip;
	
	@ExcelProperty("port")
	private String port;
	
	@ExcelProperty("db_type")
	private String dbType;
	
	@ExcelProperty("db_url")
	private String dbUrl;
	
	@ExcelProperty("db_user")
	private String dbUser;
	
	@ExcelProperty("db_password")
	private String dbPassword;
	
	@ExcelProperty("is_seleced")
	private String isSeleced;
	
	
	public static void main(String[] args) {
		
	}
}
