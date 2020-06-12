package com.autotest.utils;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.autotest.beans.Evn;
import com.autotest.beans.TestCase;

public class EvnListener extends AnalysisEventListener<Evn> {

	private static final int BATCH_COUNT = 5;
	
	public static Evn evn = new Evn();
	
	public void doAfterAllAnalysed(AnalysisContext arg0) {
		// 2.执行
		// 3.回写测试报告
	}

	public void invoke(Evn data, AnalysisContext context) {
		// 1.封装
		evn.setIp("https://suggest.taobao.com/");
		evn.setPort("443");
	}

	public static void main(String[] args) {
	}

}
