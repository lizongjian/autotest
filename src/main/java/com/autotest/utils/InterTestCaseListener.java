package com.autotest.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.autotest.beans.InterfaceTestCase;

public class InterTestCaseListener extends AnalysisEventListener<InterfaceTestCase>{
	
	//存储，模块名和用例步骤
	public static List<Map<String,List<InterfaceTestCase>>> testCases = new ArrayList<Map<String,List<InterfaceTestCase>>>();
	
	public void doAfterAllAnalysed(AnalysisContext arg0) {
		
	}

	public void invoke(InterfaceTestCase t, AnalysisContext context) {
		//1.封装 成 testCases
		//System.out.println(t);
		String moduleName = t.getModule();
		Map<String,List<InterfaceTestCase>> m = new HashMap<String, List<InterfaceTestCase>>();
		List<InterfaceTestCase> l = new ArrayList<InterfaceTestCase>();
		if(moduleName != null) {
			l.add(t);
			m.put(moduleName, l);
			testCases.add(m);
		}else {
			List<InterfaceTestCase> ll = new ArrayList<InterfaceTestCase>();
			//在最后一个追加
			Map<String, List<InterfaceTestCase>>  mm = testCases.get(testCases.size()-1);
			String s ="";
			for(Map.Entry<String, List<InterfaceTestCase>> mmm : mm.entrySet()) {
				s = mmm.getKey();
			}
			mm.get(s).add(t);
		}
	}
	

	public static void main(String[] args) {
		//映射	@ExcelProperty("out_check") 都相同了，没有注意到，因为是复制粘贴的，多个一样，就是null了
		EasyExcel.read("case\\interface.xlsx", InterfaceTestCase.class, new InterTestCaseListener()).sheet().doRead();
		List<Map<String, List<InterfaceTestCase>>> l = InterTestCaseListener.testCases;
//		int i = 0;
//		for(Map.Entry<String, List<TestCase>> m : map.entrySet()) {
//			System.out.println(m.getKey()+"---"+m.getValue());
//		}
		System.out.println(l);
	}

}


