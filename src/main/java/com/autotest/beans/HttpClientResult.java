package com.autotest.beans;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;


@Data
public class HttpClientResult {
	private static final long serialVersionUID = 2168152194164783950L;

	/**
	 * 响应状态码
	 */
	private int code;

	/**
	 * 响应数据
	 */
	private String content;
	
	
	
	public HttpClientResult(int code, String content) {
		super();
		this.code = code;
		this.content = content;
	}



	public HttpClientResult(int code) {
		super();
		this.code = code;
	}
	
	
}
