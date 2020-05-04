package com.autotest.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.autotest.beans.TestCase;

public class TestCaseListener extends AnalysisEventListener<TestCase>{
	
	private Map<String,List<TestCase>> testCases = new HashMap<String,List<TestCase>>();
	
	private static final int BATCH_COUNT = 5;
	
	public void doAfterAllAnalysed(AnalysisContext arg0) {
		//2.执行
		//3.回写测试报告
	}

	public void invoke(TestCase t, AnalysisContext context) {
		//1.封装 成 testCases
		List<TestCase> l = new ArrayList<TestCase>();
		l.add(t);
		testCases.put(t.getModule(), l);
	}
	

	public static void main(String[] args) {
		//映射	@ExcelProperty("out_check") 都相同了，没有注意到，因为是复制粘贴的，多个一样，就是null了
		EasyExcel.read("C:\\Users\\zonja\\Desktop\\interface.xlsx", TestCase.class, new TestCaseListener()).sheet().doRead();
	}

}


