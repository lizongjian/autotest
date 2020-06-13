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
import com.autotest.beans.TestCase;

public class TestCaseListener extends AnalysisEventListener<TestCase>{
	
	//存储，模块名和用例步骤
	public static List<Map<String,List<TestCase>>> testCases = new ArrayList<Map<String,List<TestCase>>>();
	
	private String moduleName = "";
	private static final int BATCH_COUNT = 5;
	
	 
	public void doAfterAllAnalysed(AnalysisContext arg0) {
		//2.执行
		//3.回写测试报告
	}

	public void invoke(TestCase t, AnalysisContext context) {
		//1.封装 成 testCases
		//System.out.println(t);
		String moduleName = t.getModule();
		Map<String,List<TestCase>> m = new HashMap<String, List<TestCase>>();
		List<TestCase> l = new ArrayList<TestCase>();
		if(moduleName != null) {
			l.add(t);
			m.put(moduleName, l);
			testCases.add(m);
		}else {
			List<TestCase> ll = new ArrayList<TestCase>();
			//在最后一个追加
			Map<String, List<TestCase>>  mm = testCases.get(testCases.size()-1);
			String s ="";
			for(Map.Entry<String, List<TestCase>> mmm : mm.entrySet()) {
				s = mmm.getKey();
			}
			mm.get(s).add(t);
		}
	}
	

	public static void main(String[] args) {
		//映射	@ExcelProperty("out_check") 都相同了，没有注意到，因为是复制粘贴的，多个一样，就是null了
		EasyExcel.read("C:\\Users\\zonja\\Desktop\\interface.xlsx", TestCase.class, new TestCaseListener()).sheet().doRead();
		List<Map<String, List<TestCase>>> l = TestCaseListener.testCases;
//		int i = 0;
//		for(Map.Entry<String, List<TestCase>> m : map.entrySet()) {
//			System.out.println(m.getKey()+"---"+m.getValue());
//		}
		System.out.println(l);
	}

}


