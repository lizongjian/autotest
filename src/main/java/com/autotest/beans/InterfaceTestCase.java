package com.autotest.beans;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.formula.functions.T;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;
import com.autotest.common.Parse;
import com.jayway.jsonpath.JsonPath;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.UtilityClass;

@Data
public class InterfaceTestCase {

	@ExcelProperty("用例名称")
	private String module;

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

	@ExcelProperty("替换")
	private String inReplace;

	@ExcelProperty("校验")
	private String outCheck;

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
		InterfaceTestCase t = new InterfaceTestCase();
		String str = "{\"0\":\"zhangsan\",\"1\":\"lisi\",\"2\":\"wangwu\",\"3\":[{\"0\":\"chenliu\",\"1\":\"zhouliu\"},{\"0\":\"sunqi\",\"1\":\"zhouba\"}]}";
		String str1 = null;
		System.out.println(t.toMap(str));

		String s = JsonPath.parse(str).set("$.3[1].0", "jiba").jsonString();
		String s1 = JsonPath.parse(str).read("$.3[1].0");
		System.out.println(s1);
		
		Map<String,String> m = new HashMap<String, String>();
		m.put("aa", "aaaa");
		m.put("b", "bb");
		System.out.println(Parse.parse("1212$<aa>$<b>", m));
	}
}
