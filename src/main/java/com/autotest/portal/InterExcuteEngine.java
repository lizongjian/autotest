package com.autotest.portal;

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
import com.autotest.common.Parse;
import com.autotest.utils.EvnListener;
import com.autotest.utils.HttpClientUtils;
import com.autotest.utils.InterTestCaseListener;
import com.autotest.beans.InterTestCase;
import com.autotest.beans.InterTestCaseLog;
import com.jayway.jsonpath.JsonPath;

public class InterExcuteEngine {
	private static final Logger logger = LoggerFactory.getLogger(InterExcuteEngine.class);
	// 保存全局变量
	public Map<String, String> context = new HashMap<String, String>();

	// 存储单接口用例模块
	public static Map<String, List<InterTestCase>> testCase = new HashMap<String, List<InterTestCase>>();
	
	//保存日志对象
	public static List<InterTestCaseLog> testCaseLogsList = new ArrayList<InterTestCaseLog>();
	
	public static void run() {
		// 1.读取环境
		EasyExcel.read("C:\\Users\\zonja\\Desktop\\interface.xlsx", Evn.class, new EvnListener()).sheet(0).doRead();
		// 2.读取用例
		EasyExcel.read("C:\\Users\\zonja\\Desktop\\interface.xlsx", InterTestCase.class, new InterTestCaseListener()).sheet(1).doRead();
		
		Evn evn = EvnListener.evn;
		List<Map<String, List<InterTestCase>>> testCases = InterTestCaseListener.testCases;
		
		// 3.当前的用例
		for (int i = 0; i <= testCases.size() - 1; i++) {
			testCase = testCases.get(i);
			for (Map.Entry<String, List<InterTestCase>> entry : testCase.entrySet()) {
				execute(entry.getValue(),evn);
			}
		}
		//生成测试报告
		EasyExcel.write(System.getProperty("user.dir")+File.separator+"report"+File.separator+System.currentTimeMillis()+".xlsx", InterTestCaseLog.class).sheet("测试报告").doWrite(testCaseLogsList);
		System.out.println(System.getProperty("user.dir")+File.separator+"report"+File.separator+System.currentTimeMillis()+".xlsx");
	}

	// 执行一个模块
	public static void execute(List<InterTestCase> testCase,Evn evn) {
		
		//保存在一个用例范围内 输出变量
		Map<String, String> context = new HashMap<String, String>();
		
		//步骤
		for(int i=0;i<=testCase.size()-2;i++) {
			InterTestCaseLog testCaseLog = new InterTestCaseLog();
			InterTestCase t = testCase.get(i);
			testCaseLog.setModule(t.getModule());
			testCaseLog.setInterName(t.getInterName());
			testCaseLog.setRequestMethod(t.getRequestMethod());
			//1.发请求
			ResponseResult respResult = HttpClientUtils.sendRequest(t,evn,context,testCaseLog);
			logger.info(respResult.toString());
			testCaseLog.setResponseResult(respResult.getContent());
			
			//保存输出变量
			Map<String,String> out_parame = t.toMap(t.getOutParam());//{"data.0.children.0.label":"var_label2",}
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
					Map<String, String> actual = Parse.parse(oc.getValue(), context);
					if(expect.equals(actual.get("result"))) {
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
		InterExcuteEngine.run();
	}
}
