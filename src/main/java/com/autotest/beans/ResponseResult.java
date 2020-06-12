package com.autotest.beans;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;


@Data
public class ResponseResult {
	private static final long serialVersionUID = 2168152194164783950L;

	/**
	 * 响应状态码
	 */
	private int code;

	/**
	 * 响应数据
	 */
	private String content;
	
	
	
	public ResponseResult(int code, String content) {
		super();
		this.code = code;
		this.content = content;
	}



	public ResponseResult(int code) {
		super();
		this.code = code;
	}
	
	public ResponseResult() {
		
	}
}
