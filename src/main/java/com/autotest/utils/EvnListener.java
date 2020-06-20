package com.autotest.utils;


import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.autotest.beans.Evn;

public class EvnListener extends AnalysisEventListener<Evn> {

	public static Evn evn = new Evn();
	
	public void doAfterAllAnalysed(AnalysisContext arg0) {
	}

	public void invoke(Evn e, AnalysisContext context) {
		evn.setIp(e.getIp());
		evn.setPort(e.getPort());
	}


}
