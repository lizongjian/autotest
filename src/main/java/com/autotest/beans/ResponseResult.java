package com.autotest.beans;


import lombok.Data;


@Data
public class ResponseResult {

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
