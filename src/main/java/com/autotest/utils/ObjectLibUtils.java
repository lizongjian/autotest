package com.autotest.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.autotest.beans.ObjectLib;
import com.autotest.common.Parse;

public class ObjectLibUtils {
	// 通过对象名称、输入参数、返回对象库对象 并解析
	public static Map<String,Object> getObject(String objectName, List<Map<String, ObjectLib>> objectLibs,String inParam) {
		Map<String,Object> result = new HashMap<String, Object>();
			ObjectLib objectLib = null;
			for (Map<String, ObjectLib> m : objectLibs) {
				try {
					objectLib = m.get(objectName);
					if (objectLib != null) {
						break;
					} else {
						continue;
					}
				} catch (Exception e) {
					e.printStackTrace();
					result.put("error", "找不到【"+objectName+"】对象,请检查对象库!");
					return result;
				}
			}
			//1.从输入参数替换
			if(null != objectLib) {
				String objectValue = Parse.parse(objectLib.getLocateValue(), inParam);
				objectLib.setLocateValue(objectValue);
				result.put("result", objectLib);
			}
			
			//2.局部变量替换
			//3.全局变量替换
			return result;
		}
	
	// 通过对象名称返回对象库对象
	public static Map<String,Object> getObject(String objectName, List<Map<String, ObjectLib>> objectLibs) {
		Map<String,Object> result = new HashMap<String, Object>();
			ObjectLib objectLib = null;
			for (Map<String, ObjectLib> m : objectLibs) {
				try {
					objectLib = m.get(objectName);
					if (objectLib != null) {
						break;
					} else {
						continue;
					}
				} catch (Exception e) {
					e.printStackTrace();
					result.put("error", "找不到【"+objectName+"】对象,请检查对象库!");
					return result;
				}
			}
			result.put("result", objectLib);
			return result;
		}
	
	
	
	public static void main(String[] args) {
		String s = Parse.parse("//*[@id=\"$<搜索按钮>\"]", "su");
		System.out.println(s);
	}
}
