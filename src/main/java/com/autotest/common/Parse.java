package com.autotest.common;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.autotest.utils.ExceptionToStringUtils;

public class Parse {
	
	// 解析输入参数 
	//没有输入参数：就返回空的map 
	//有输入参数：正则解析 有匹配 就去context替换 没匹配就直接map返回 result+值
	public static Map<String, String> parse(String str, Map<String, String> context) {
		Map<String, String> result = new HashMap<String, String>();
		// $<>
		if (str != null) {
			try {
				Pattern pattern = Pattern.compile("\\$<(.*?)>"); // +：一次或多次 ？ 一次或一次也没有
				Matcher matcher = pattern.matcher(str);
				// 1.先获取$<>的key
				while (matcher.find()) {
					String s = matcher.group(0);
					String s1 = s.substring(2, s.length() - 1);
					String value = context.get(s1);
					str = str.replaceFirst("\\$<(.*?)>", value);
					result.put("result", str);
				}
				result.put("result", str);
			} catch (Exception e) {
				e.printStackTrace();
				result.put("error",ExceptionToStringUtils.get(e));
			}
			// 2.从context获取值
			// 3.正则替换
		}else {
			result.put("result", str);
		}
		return result;
	}
	
	// 解析对象库
	public static String parse(String str, String target) {
		// $<>
		try {
			if (str != null) {
				Pattern pattern = Pattern.compile("\\$<(.*?)>"); // +：一次或多次 ？ 一次或一次也没有
				Matcher matcher = pattern.matcher(str);
				// 1.先获取$<>的key
				while (matcher.find()) {
					String s = matcher.group(0);
					String s1 = s.substring(2, s.length() - 1);
					str = str.replaceFirst("\\$<(.*?)>", target);
				}
				// 2.从context获取值
				// 3.正则替换
			}
		} catch (Exception e) {
			e.printStackTrace();
			return str;
		}
		return str;
	}
	
	
	public static void main(String[] args) {
		Map<String,String> m = new HashMap<String, String>();
		m.put("a", "lizj");
		System.out.println(Parse.parse("{\"auth\":\"$<aa>\"}", m));
	}
}
