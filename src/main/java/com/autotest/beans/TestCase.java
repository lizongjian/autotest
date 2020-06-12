package com.autotest.beans;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.formula.functions.T;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;
import com.jayway.jsonpath.JsonPath;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.UtilityClass;

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

	// 解析
	public String parse(String str, Map<String, String> context) {
		// $<>
		if(str != null) {
			Pattern pattern = Pattern.compile("\\$<(.?)>");  //+：一次或多次  ？ 一次或一次也没有
			Matcher matcher = pattern.matcher(str);
			// 1.先获取$<>的key
			while (matcher.find()) {
				String s = matcher.group(0);
				String s1 = s.substring(2, s.length()-1);
				String value = context.get(s1);
				str = str.replaceFirst("\\$<(.?)>", value);
			}
			// 2.从context获取值
			// 3.正则替换
		}
		return str;
	}

	// json字符串转map<String,String>
	public Map<String, String> toMap(String str) {
		Map<String, String> m = new HashMap<String, String>();
		try {
			m = (Map<String, String>) JSON.parse(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return m;
	}

	public static void main(String[] args) {
		TestCase t = new TestCase();
		String str = "{\"0\":\"zhangsan\",\"1\":\"lisi\",\"2\":\"wangwu\",\"3\":[{\"0\":\"chenliu\",\"1\":\"zhouliu\"},{\"0\":\"sunqi\",\"1\":\"zhouba\"}]}";
		String str1 = null;
		System.out.println(t.toMap(str1));

		String s = JsonPath.parse(str).set("$.3[1].0", "jiba").jsonString();
		String s1 = JsonPath.parse(str).read("$.3[1].0");
		System.out.println(s1);
		
		Map<String,String> m = new HashMap<String, String>();
		m.put("a", "caonima");
		m.put("b", "caonibb");
		System.out.println(t.parse("1212$<a>$<b>", m));
	}
}
