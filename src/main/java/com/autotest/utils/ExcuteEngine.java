package com.autotest.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.excel.EasyExcel;
import com.autotest.beans.Evn;
import com.autotest.beans.TestCase;

public class ExcuteEngine {

	// 保存全局变量
	public Map<String, Object> context = new HashMap<String, Object>();

	// 存储单接口用例模块
	public static Map<String, List<TestCase>> testCase = new HashMap<String, List<TestCase>>();

	public static void run() {
		// 1.读取环境
		EasyExcel.read("C:\\Users\\zonja\\Desktop\\evn.xlsx", Evn.class, new EvnListener()).sheet().doRead();
		// 2.读取用例
		EasyExcel.read("C:\\Users\\zonja\\Desktop\\interface.xlsx", TestCase.class, new TestCaseListener()).sheet()
				.doRead();

		Evn evn = EvnListener.evn;

		List<Map<String, List<TestCase>>> testCases = TestCaseListener.testCases;
		// 3.当前的用例
		for (int i = 0; i <= testCases.size() - 1; i++) {
			testCase = testCases.get(i);
			for (Map.Entry<String, List<TestCase>> entry : testCase.entrySet()) {
				execute(entry.getValue());
				System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
			}
		}
	}

	// 执行一个模块
	public static void execute(List<TestCase> testCase) {
		
		//保存在一个用例范围内
		Map<String, Object> context = new HashMap<String, Object>();
		
		//步骤
		for(int i=0;i<=testCase.size()-1;i++) {
			TestCase t = testCase.get(i);
			String Keyword = t.getKeyword();
			String request_type = t.getRequestType();
			String request_protocol = t.getRequestProtocol();
			String request_method = t.getRequestMethod();
			String request_path = t.getRequestPath();
			String request_header = t.getRequestHeader();
			String default_parame = t.getDefaultParame();
			String in_parame = t.getInParame();
			String out_parame = t.getOutParame();
			String in_replace = t.getInReplace();
			String out_check = t.getOutCheck();
			
			//回写日志
			String log = HttpClientUtils.sendRequest(t);
		}
	}

	public static void parse() {

	}

	public static void main(String[] args) {
		ExcuteEngine.run();
	}
}
