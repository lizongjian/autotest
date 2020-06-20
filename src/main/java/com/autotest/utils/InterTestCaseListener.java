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

public class InterTestCaseListener extends AnalysisEventListener<InterTestCase>{
	
	//存储，模块名和用例步骤
	public static List<Map<String,List<InterTestCase>>> testCases = new ArrayList<Map<String,List<InterTestCase>>>();
	
	private String moduleName = "";
	
	 
	public void doAfterAllAnalysed(AnalysisContext arg0) {
		
	}

	public void invoke(InterTestCase t, AnalysisContext context) {
		//1.封装 成 testCases
		//System.out.println(t);
		String moduleName = t.getModule();
		Map<String,List<InterTestCase>> m = new HashMap<String, List<InterTestCase>>();
		List<InterTestCase> l = new ArrayList<InterTestCase>();
		if(moduleName != null) {
			l.add(t);
			m.put(moduleName, l);
			testCases.add(m);
		}else {
			List<InterTestCase> ll = new ArrayList<InterTestCase>();
			//在最后一个追加
			Map<String, List<InterTestCase>>  mm = testCases.get(testCases.size()-1);
			String s ="";
			for(Map.Entry<String, List<InterTestCase>> mmm : mm.entrySet()) {
				s = mmm.getKey();
			}
			mm.get(s).add(t);
		}
	}
	

	public static void main(String[] args) {
		//映射	@ExcelProperty("out_check") 都相同了，没有注意到，因为是复制粘贴的，多个一样，就是null了
		EasyExcel.read("C:\\Users\\zonja\\Desktop\\interface.xlsx", InterTestCase.class, new InterTestCaseListener()).sheet().doRead();
		List<Map<String, List<InterTestCase>>> l = InterTestCaseListener.testCases;
//		int i = 0;
//		for(Map.Entry<String, List<TestCase>> m : map.entrySet()) {
//			System.out.println(m.getKey()+"---"+m.getValue());
//		}
		System.out.println(l);
	}

}


