package com.autotest.utils;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.autotest.beans.Evn;

public class EvnListener extends AnalysisEventListener<Evn> {

	public static Evn evn = new Evn();
	
	public void doAfterAllAnalysed(AnalysisContext arg0) {
		System.out.println(evn);
	}

	public void invoke(Evn e, AnalysisContext context) {
		if("di".equals(e.getEvnName())) {
			evn.setIp(e.getIp());
			evn.setPort(e.getPort());
		}
	}
	
	public static void main(String[] args) {
		// 1.读取环境
		EasyExcel.read("case\\inter_v2.xlsx", Evn.class, new EvnListener()).sheet("全局配置信息").doRead();
	}

}
