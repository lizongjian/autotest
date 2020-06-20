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
import com.autotest.beans.UITestCase;
import com.autotest.beans.UITestCaseLog;
import com.autotest.utils.EvnListener;
import com.autotest.utils.ObjectLibListener;
import com.autotest.utils.SeleniumUtils;
import com.autotest.utils.UITestCaseListener;
import com.autotest.beans.InterTestCaseLog;
import com.autotest.beans.ObjectLib;

public class UIExcuteEngine {
	private static final Logger logger = LoggerFactory.getLogger(UIExcuteEngine.class);
	// 保存全局变量
	public Map<String, String> context = new HashMap<String, String>();

	// 存储单个用例
	public static Map<String, List<UITestCase>> uiTestCase = new HashMap<String, List<UITestCase>>();
	
	//保存日志对象
	public static List<UITestCaseLog> uiTestCaseLogsList = new ArrayList<UITestCaseLog>();
	
	public static void run() {
		// 1.读取环境
		EasyExcel.read("C:\\Users\\zonja\\Desktop\\UI.xlsx", Evn.class, new EvnListener()).sheet("全局配置信息").doRead();
		// 2.读取用例
		EasyExcel.read("C:\\Users\\zonja\\Desktop\\UI.xlsx", UITestCase.class, new UITestCaseListener()).sheet("测试用例").doRead();
		// 3.读取对象库
		EasyExcel.read("C:\\Users\\zonja\\Desktop\\UI.xlsx", ObjectLib.class, new ObjectLibListener()).sheet("对象库").doRead();
		
		Evn evn = EvnListener.evn;
		List<Map<String, List<UITestCase>>> uiTestCases = UITestCaseListener.uiTestCases;
		List<Map<String,ObjectLib>> objectLib  = ObjectLibListener.objectLib;
		
		// 3.当前的用例
		for (int i = 0; i <= uiTestCases.size() - 1; i++) {
			uiTestCase = uiTestCases.get(i);
			//4.当前用例的步骤
			for (Map.Entry<String, List<UITestCase>> entry : uiTestCase.entrySet()) {
				execute(entry.getValue(),evn,objectLib);
			}
		}
		//生成测试报告
		//EasyExcel.write(System.getProperty("user.dir")+File.separator+"report"+File.separator+System.currentTimeMillis()+".xlsx", InterTestCaseLog.class).sheet("测试报告").doWrite(uiTestCaseLogsList);
		//System.out.println(System.getProperty("user.dir")+File.separator+"report"+File.separator+System.currentTimeMillis()+".xlsx");
	}

	// 执行一个模块/用例
	public static void execute(List<UITestCase> testCase,Evn evn,List<Map<String,ObjectLib>> objectLibs) {
		
		//保存在一个用例范围内 输出变量
		Map<String, String> context = new HashMap<String, String>();
		
		//步骤
		for(int i=0;i<=testCase.size()-1;i++) {
			UITestCaseLog uiTestCaseLog = new UITestCaseLog();
			UITestCase uiTestCase = testCase.get(i);
			//返回值说明 执行正常 则返回有输出变量的map  执行异常 则返回 error+错误信息
			Map<String,String> result = SeleniumUtils.execute(uiTestCase, evn, objectLibs, context, uiTestCaseLog);
			if(!result.isEmpty()) {
				context.put(uiTestCase.getOutParam(), result.get(uiTestCase.getOutParam()));
			}
			uiTestCaseLogsList.add(uiTestCaseLog);
		}
		context.clear();
		SeleniumUtils.closeDriver();
	}

	public static void main(String[] args) {
		UIExcuteEngine.run();
	}
}
