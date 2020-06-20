package com.autotest.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.autotest.beans.InterTestCase;
import com.autotest.beans.UITestCase;

public class UITestCaseListener extends AnalysisEventListener<UITestCase>{
	
	//存储，模块名和用例步骤
	public static List<Map<String,List<UITestCase>>> uiTestCases = new ArrayList<Map<String,List<UITestCase>>>();
	
	private String moduleName = "";
	
	 
	public void doAfterAllAnalysed(AnalysisContext arg0) {
		
	}

	public void invoke(UITestCase t, AnalysisContext context) {
		//1.封装 成 testCases
		//System.out.println(t);
		String moduleName = t.getModule();
		Map<String,List<UITestCase>> m = new HashMap<String, List<UITestCase>>();
		List<UITestCase> l = new ArrayList<UITestCase>();
		if(moduleName != null) {
			l.add(t);
			m.put(moduleName, l);
			uiTestCases.add(m);
		}else {
			List<UITestCase> ll = new ArrayList<UITestCase>();
			//在最后一个追加
			Map<String, List<UITestCase>>  mm = uiTestCases.get(uiTestCases.size()-1);
			String s ="";
			for(Map.Entry<String, List<UITestCase>> mmm : mm.entrySet()) {
				s = mmm.getKey();
			}
			mm.get(s).add(t);
		}
	}
}


