package com.autotest.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.excel.EasyExcel;
import com.autotest.beans.Evn;
import com.autotest.beans.ResponseResult;
import com.autotest.beans.TestCase;
import com.jayway.jsonpath.JsonPath;

public class ExcuteEngine {

	// 保存全局变量
	public Map<String, String> context = new HashMap<String, String>();

	// 存储单接口用例模块
	public static Map<String, List<TestCase>> testCase = new HashMap<String, List<TestCase>>();

	public static void run() {
		// 1.读取环境
		EasyExcel.read("C:\\Users\\zonja\\Desktop\\evn.xlsx", Evn.class, new EvnListener()).sheet().doRead();
		// 2.读取用例
		EasyExcel.read("C:\\Users\\zonja\\Desktop\\22.xlsx", TestCase.class, new TestCaseListener()).sheet()
				.doRead();

		//Evn evn = EvnListener.evn;
		Evn evn = new Evn();
		evn.setIp("https://suggest.taobao.com");
		evn.setPort("443");
		List<Map<String, List<TestCase>>> testCases = TestCaseListener.testCases;
		// 3.当前的用例
		for (int i = 0; i <= testCases.size() - 1; i++) {
			testCase = testCases.get(i);
			for (Map.Entry<String, List<TestCase>> entry : testCase.entrySet()) {
				execute(entry.getValue(),evn);
				System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
			}
		}
	}

	// 执行一个模块
	public static void execute(List<TestCase> testCase,Evn evn) {
		
		//保存在一个用例范围内 输出变量
		Map<String, String> context = new HashMap<String, String>();
		
		//步骤
		for(int i=0;i<=testCase.size()-1;i++) {
			TestCase t = testCase.get(i);
//			String Keyword = t.getKeyword();
//			
//			String request_type = t.getRequestType();
//			String request_protocol = t.getRequestProtocol();
//			String request_method = t.getRequestMethod();
//			
//			String request_path = t.getRequestPath();
//			
//			String request_header = t.getRequestHeader();//需要解析 正则 替换 并且要转换map
//			String default_parame = t.getDefaultParame();//需要解析 正则 替换 并且要转换map
//			String in_parame = t.getInParame();//需要解析 正则 替换  并且要转换map
//			
//			String out_parame = t.getOutParame();//需要解析 正则 替换  并且要转换map
//			String in_replace = t.getInReplace();//需要解析 正则 替换
//			String out_check = t.getOutCheck();//需要解析 正则 替换
			
			ResponseResult respResult = HttpClientUtils.sendRequest(t,evn,context);
			
			//后置处理
			Map<String,String> out_parame = t.toMap(t.getOutParame());//{"data.0.children.0.label":"var_label2",}
			for (Map.Entry<String,String> op : out_parame.entrySet()) {
				context.put(op.getValue(), JsonPath.parse(respResult).read(op.getKey()));
			}
			//回写日志 生成报告
			//String log = HttpClientUtils.sendRequest(t);
			
			
		}
	}

	public static void main(String[] args) {
		ExcuteEngine.run();
	}
}
