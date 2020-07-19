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

public class UICaseListener extends AnalysisEventListener<UICase>{
	
	//存储，用例名和用例步骤  多步骤
	public static List<Map<String,List<UICase>>> uiCases = new ArrayList<Map<String,List<UICase>>>();
	 
	public void doAfterAllAnalysed(AnalysisContext arg0) {
		System.out.println(uiCases);
	}

	public void invoke(UICase t, AnalysisContext context) {
		//1.封装 成 testCases
		//System.out.println(t);
		String moduleName = t.getModule();
		Map<String,List<UICase>> m = new HashMap<String, List<UICase>>();
		List<UICase> l = new ArrayList<UICase>();
		if(moduleName != null) {
			l.add(t);
			m.put(moduleName, l);
			uiCases.add(m);
		}else {
			//在最后一个追加
			Map<String, List<UICase>>  mm = uiCases.get(uiCases.size()-1);
			String s ="";
			for(Map.Entry<String, List<UICase>> mmm : mm.entrySet()) {
				s = mmm.getKey();
			}
			mm.get(s).add(t);
		}
	}
	
	
	public static void main(String[] args) {
		EasyExcel.read("case\\UI.xlsx", UICase.class, new UICaseListener()).sheet("组件库")
				.doRead();
	}
}


