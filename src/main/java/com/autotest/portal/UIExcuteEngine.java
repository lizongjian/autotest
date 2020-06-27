package com.autotest.portal;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.excel.EasyExcel;
import com.autotest.beans.Evn;
import com.autotest.beans.UITestCase;
import com.autotest.beans.UITestCaseLog;
import com.autotest.common.Parse;
import com.autotest.utils.EvnListener;
import com.autotest.utils.ObjectLibListener;
import com.autotest.utils.ObjectLibUtils;
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

	// 保存日志对象
	public static List<UITestCaseLog> uiTestCaseLogsList = new ArrayList<UITestCaseLog>();

	public static void run() {
		// 1.读取环境
		EasyExcel.read("C:\\Users\\zonja\\Desktop\\UI.xlsx", Evn.class, new EvnListener()).sheet("全局配置信息").doRead();
		// 2.读取用例
		EasyExcel.read("C:\\Users\\zonja\\Desktop\\UI.xlsx", UITestCase.class, new UITestCaseListener()).sheet("测试用例")
				.doRead();
		// 3.读取对象库
		EasyExcel.read("C:\\Users\\zonja\\Desktop\\UI.xlsx", ObjectLib.class, new ObjectLibListener()).sheet("对象库")
				.doRead();

		Evn evn = EvnListener.evn;
		List<Map<String, List<UITestCase>>> uiTestCases = UITestCaseListener.uiTestCases;
		List<Map<String, ObjectLib>> objectLib = ObjectLibListener.objectLib;

		// 3.遍历所有用例
		for (int i = 0; i <= uiTestCases.size() - 1; i++) {
			uiTestCase = uiTestCases.get(i);
			// 4.遍历当前用例（步骤）
			for (Map.Entry<String, List<UITestCase>> entry : uiTestCase.entrySet()) {
				// 5.执行步骤
				execute(entry.getValue(), evn, objectLib);
			}
		}
		// 生成测试报告
		EasyExcel
				.write(System.getProperty("user.dir") + File.separator + "report" + File.separator
						+ System.currentTimeMillis() + ".xlsx", UITestCaseLog.class)
				.sheet("测试报告").doWrite(uiTestCaseLogsList);
		System.out.println(System.getProperty("user.dir") + File.separator + "report" + File.separator
				+ System.currentTimeMillis() + ".xlsx");
	}

	// 执行一个模块/用例
	public static void execute(List<UITestCase> testCase, Evn evn, List<Map<String, ObjectLib>> objectLibs) {

		// 保存在一个用例范围内 输出变量
		Map<String, String> context = new HashMap<String, String>();

		// 步骤:
		start: for (int i = 0; i <= testCase.size() - 1; i++) {
			UITestCase uiTestCase = testCase.get(i);
			UITestCaseLog uiTestCaseLog = new UITestCaseLog();
			uiTestCaseLog.setModule(uiTestCase.getModule());
			uiTestCaseLog.setKeyword(uiTestCase.getKeyword());
			String objectName = uiTestCase.getObject();
			uiTestCaseLog.setObject(uiTestCase.getObject());
			ObjectLib object = null;
			// 入参解析
			Map<String, String> inmap = Parse.parse(uiTestCase.getInParam(), context);
			for (Map.Entry<String, String> in : inmap.entrySet()) {
				String inKey = in.getKey();
				if ("result".equals(inKey)) {//解析成功结果集
					uiTestCase.setInParam(in.getValue());
					uiTestCaseLog.setInParam(in.getValue());
				} else if ("error".equals(inKey)) {//解析失败结果集
					uiTestCaseLog.setInParam(uiTestCase.getInParam());
					uiTestCaseLog.setStatus("fail");
					uiTestCaseLog.setStatusMes(in.getValue());
					uiTestCaseLogsList.add(uiTestCaseLog);
					// 用例有错误就跳出本次的用例执行 执行下一个用例
					break start;
				}
			}
			
			//对象解析 
			//1.获取对象实例   2.解析对象路径
			// 根据对象名称获取对应对象库对象
			if(objectName!=null) {
				object = ObjectLibUtils.getObjectLib(uiTestCase.getObject(), objectLibs);
				if(object == null) {
					uiTestCaseLog.setStatus("fail");
					uiTestCaseLog.setStatusMes("找不到【"+objectName+"】该对象!请检查对象库");
					uiTestCaseLogsList.add(uiTestCaseLog);
					// 用例有一个错误 就跳出本次的用例执行 执行下一个用例
					break start;
				}
			}
			// 执行
			Map<String, String> result = SeleniumUtils.execute(uiTestCase, evn, object, context, uiTestCaseLog);
			if (!result.isEmpty()) {//正常处理解析成功 
				context.put(uiTestCase.getOutParam(), result.get(uiTestCase.getOutParam()));
				uiTestCaseLog.setOutParam(uiTestCase.getOutParam());
				uiTestCaseLog.setStatus("success");
				uiTestCaseLog.setStatusMes(uiTestCase.getOutParam()+"="+result.get(uiTestCase.getOutParam()));
			} else if (result.isEmpty()) {//不用处理成功
				uiTestCaseLog.setStatus("success");
			} else {//处理失败 对象点击失败 
				uiTestCaseLog.setStatus("fail");
				uiTestCaseLog.setStatusMes(result.get("error"));
			}
			uiTestCaseLogsList.add(uiTestCaseLog);
		}
		context.clear();
		SeleniumUtils.closeDriver();
	}

	public static void main(String[] args) {
		UIExcuteEngine.run();
		// EasyExcel.read("C:\\Users\\zonja\\Desktop\\UI.xlsx", UITestCase.class, new
		// UITestCaseListener()).sheet();
	}
}
