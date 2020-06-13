package com.autotest.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.excel.EasyExcel;
import com.autotest.beans.Evn;
import com.autotest.beans.ResponseResult;
import com.autotest.beans.TestCase;
import com.autotest.beans.TestCaseLog;
import com.jayway.jsonpath.JsonPath;

public class ExcuteEngine {
	private static final Logger logger = LoggerFactory.getLogger(ExcuteEngine.class);
	// 保存全局变量
	public Map<String, String> context = new HashMap<String, String>();

	// 存储单接口用例模块
	public static Map<String, List<TestCase>> testCase = new HashMap<String, List<TestCase>>();
	
	//保存日志对象
	public static List<TestCaseLog> testCaseLogsList = new ArrayList<TestCaseLog>();
	
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
			}
		}
		//生成测试报告
		EasyExcel.write(System.getProperty("user.dir")+File.separator+"report"+File.separator+System.currentTimeMillis()+".xlsx", TestCaseLog.class).sheet("模板").doWrite(testCaseLogsList);
		System.out.println(System.getProperty("user.dir")+File.separator+"report"+File.separator+System.currentTimeMillis()+".xlsx");
	}

	// 执行一个模块
	public static void execute(List<TestCase> testCase,Evn evn) {
		
		//保存在一个用例范围内 输出变量
		Map<String, String> context = new HashMap<String, String>();
		
		//步骤
		for(int i=0;i<=testCase.size()-2;i++) {
			TestCaseLog testCaseLog = new TestCaseLog();
			TestCase t = testCase.get(i);
			testCaseLog.setModule(t.getModule());
			testCaseLog.setKeyword(t.getKeyword());
			testCaseLog.setRequestMethod(t.getRequestMethod());
			//1.发请求
			ResponseResult respResult = HttpClientUtils.sendRequest(t,evn,context,testCaseLog);
			logger.info(respResult.toString());
			testCaseLog.setResponseResult(respResult.getContent());
			
			//保存输出变量
			Map<String,String> out_parame = t.toMap(t.getOutParame());//{"data.0.children.0.label":"var_label2",}
			if(out_parame!=null) {
				for (Map.Entry<String,String> op : out_parame.entrySet()) {
					try {
						context.put(op.getValue(), JsonPath.parse(respResult.getContent()).read(op.getKey()));
					} catch (Exception e) {
						logger.info(op.getKey() + "没有匹配");
					}
					
				}
			}
		
			
			//输出校验  断言
			Map<String,String> out_check = t.toMap(t.getOutCheck());
			if(out_check!=null) {
				for (Map.Entry<String,String> oc : out_check.entrySet()) {
					//期望值
					String expect = JsonPath.read(respResult.getContent(), oc.getKey());
					//actual
					String actual = t.parse(oc.getValue(), context);
					if(expect.equals(actual)) {
						testCaseLog.setCheck(expect+""+actual);
						System.out.println("预期"+expect+"----"+"实际"+actual);
						testCaseLog.setStatus("success");
					}else {
						testCaseLog.setStatus("fail");
					}
				}
			}
			testCaseLogsList.add(testCaseLog);
		}
	}

	public static void main(String[] args) {
		System.setProperty ("WORKDIR", System.getProperty("user.dir"));
		ExcuteEngine.run();
	}
}
