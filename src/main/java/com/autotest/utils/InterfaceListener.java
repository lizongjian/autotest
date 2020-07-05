package com.autotest.utils;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.autotest.beans.Interface;

public class InterfaceListener extends AnalysisEventListener<Interface>{
	
	public static List<Interface> interfaces = new ArrayList<Interface>();
	@Override
	public void invoke(Interface inter, AnalysisContext context) {
		//有空行不处理
		if(!CheckObjectIsNullUtils.objCheckIsNull(inter)) {
			interfaces.add(inter);
		}
		
	}

	@Override
	public void doAfterAllAnalysed(AnalysisContext context) {
		System.out.println(interfaces);
		
	}

}
