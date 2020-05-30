package com.autotest.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.excel.EasyExcel;
import com.autotest.beans.Evn;
import com.autotest.beans.TestCase;

public class ExcuteEngine {
	
	//保存全局变量
	public Map<String,Object> context = new HashMap<String, Object>();
	
	//存储单接口用例模块 
	public static Map<String, List<TestCase>> testCase = new HashMap<String, List<TestCase>>();
	
	public static void run() {
		//1.读取环境
		EasyExcel.read("C:\\Users\\zonja\\Desktop\\evn.xlsx", Evn.class, new EvnListener()).sheet().doRead();
		//2.读取用例
		EasyExcel.read("C:\\Users\\zonja\\Desktop\\interface.xlsx", TestCase.class, new TestCaseListener()).sheet().doRead();
		
		Evn evn = EvnListener.evn;
		
		List<Map<String, List<TestCase>>> testCases = TestCaseListener.testCases;
		//3.当前的用例
		
		for(int i=0;i<=testCases.size()-1;i++) {
			testCase = testCases.get(i);
			System.out.println(testCase);
		}
	}
	
	//执行一个模块
	public static void execute() {
		
	}
	
	public static void parse() {
		
	}
	
	public static void main(String[] args) {
		ExcuteEngine.execute();
	}
}
