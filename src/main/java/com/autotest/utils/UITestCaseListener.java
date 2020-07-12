package com.autotest.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.autotest.beans.Evn;
import com.autotest.beans.UICase;
import com.autotest.beans.UITestCase;

public class UITestCaseListener extends AnalysisEventListener<UITestCase> {

	// 存储，用例名和用例步骤 多步骤
	public static List<Map<String, List<UITestCase>>> uiTestCases = new ArrayList<Map<String, List<UITestCase>>>();
	// 获取组件库
	List<Map<String, List<UICase>>> uiTests = UICaseListener.uiCases;

	public void doAfterAllAnalysed(AnalysisContext arg0) {
		System.out.println(uiTestCases);
	}

	public void invoke(UITestCase t, AnalysisContext context) {
		// 1.封装 成 testCases
		// 2.如果有组件，替换对应的步骤
		String moduleName = t.getModule();
		// 判断是否包含comp
		String keyWord = t.getKeyword();
		//最终要执行的用例名称和用例步骤
		Map<String, List<UITestCase>> m = new HashMap<String, List<UITestCase>>();
		//list的测试用例
		List<UITestCase> l = new ArrayList<UITestCase>();
		if (moduleName != null) {
			// 假如包含comp 则替换组件库
			if ("comp".equals(keyWord)) {
				//找到用例中的组件名称
				String objName = t.getObjectName();
				//遍历组件库
				for(Map<String, List<UICase>> uiTest : uiTests) {
					//遍历组件库的步骤 找到对应 comp 对应的组件
					for(Map.Entry<String, List<UICase>> ui : uiTest.entrySet()) {
						//如果组件名等于组件库的名称 就把组件库中所有的步骤加进来
						if(objName.equals(ui.getKey())) {
							for(UICase uiCase : ui.getValue()) {
								UITestCase uiTestCase = new UITestCase();
								uiTestCase.setModule(moduleName);
								uiTestCase.setKeyword(uiCase.getKeyword());
								uiTestCase.setObjectName(uiCase.getObjectName());
								uiTestCase.setInParam(uiCase.getInParam());
								uiTestCase.setOutParam(uiCase.getOutParam());
								l.add(uiTestCase);
							}
						}
					}
				}
				m.put(moduleName, l);
				uiTestCases.add(m);
			} else {//不包含comp 
				l.add(t);
				m.put(moduleName, l);
				uiTestCases.add(m);
			}
		} else {
			// 在最后一个追加
			Map<String, List<UITestCase>> mm = uiTestCases.get(uiTestCases.size() - 1);
			//获取最后一个用例的用例名称
			String s = "";
			for (Map.Entry<String, List<UITestCase>> mmm : mm.entrySet()) {
				s = mmm.getKey();
			}
			//判断是否包含comp
			if ("comp".equals(keyWord)) {
				//找到用例中的组件名称
				String objName = t.getObjectName();
				//遍历组件库
				for(Map<String, List<UICase>> uiTest : uiTests) {
					//遍历组件库的步骤 找到对应 comp 对应的组件
					for(Map.Entry<String, List<UICase>> ui : uiTest.entrySet()) {
						//如果组件名等于组件库的名称 就把组件库中所有的步骤加进来
						if(objName.equals(ui.getKey())) {
							for(UICase uiCase : ui.getValue()) {
								UITestCase uiTestCase = new UITestCase();
								uiTestCase.setModule(moduleName);
								uiTestCase.setKeyword(uiCase.getKeyword());
								uiTestCase.setObjectName(uiCase.getObjectName());
								uiTestCase.setInParam(uiCase.getInParam());
								uiTestCase.setOutParam(uiCase.getOutParam());
								mm.get(s).add(uiTestCase);
							}
						}
					}
				}
			} else {
				mm.get(s).add(t);
			}
		}
	}

	public static void main(String[] args) {
		EasyExcel.read("F:\\workspace\\autotest\\autotest\\case\\UI.xlsx", UICase.class, new UICaseListener())
		.sheet("组件库").doRead();
		// 2.读取用例
		EasyExcel.read("F:\\workspace\\autotest\\autotest\\case\\UI.xlsx", UITestCase.class, new UITestCaseListener())
				.sheet("测试用例").doRead();
	}
}
