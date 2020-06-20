package com.autotest.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.autotest.beans.ObjectLib;

public class ObjectLibListener extends AnalysisEventListener<ObjectLib> {
	public static List<Map<String,ObjectLib>> objectLib = new ArrayList<Map<String,ObjectLib>>();
	@Override
	public void invoke(ObjectLib data, AnalysisContext context) {
		Map<String,ObjectLib> m = new HashMap<String, ObjectLib>();
		m.put(data.getObjectName(), data);
		objectLib.add(m);
	}

	@Override
	public void doAfterAllAnalysed(AnalysisContext context) {
		
		
	}

}
