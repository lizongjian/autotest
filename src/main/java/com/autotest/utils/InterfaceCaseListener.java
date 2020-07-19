package com.autotest.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.autotest.beans.InterfaceCase;

public class InterfaceCaseListener extends AnalysisEventListener<InterfaceCase> {

	public static List<Map<String, List<InterfaceCase>>> interfaceCases = new ArrayList<Map<String, List<InterfaceCase>>>();

	@Override
	public void invoke(InterfaceCase t, AnalysisContext context) {
		//有一行为空 不处理
		if(!CheckObjectIsNullUtils.objCheckIsNull(t)) {
			String moduleName = t.getModule();
			Map<String, List<InterfaceCase>> m = new HashMap<String, List<InterfaceCase>>();
			List<InterfaceCase> l = new ArrayList<InterfaceCase>();
			if (moduleName != null) {
				l.add(t);
				m.put(moduleName, l);
				interfaceCases.add(m);
			} else {
				// 在最后一个追加
				Map<String, List<InterfaceCase>> mm = interfaceCases.get(interfaceCases.size() - 1);
				String s = "";
				for (Map.Entry<String, List<InterfaceCase>> mmm : mm.entrySet()) {
					s = mmm.getKey();
				}
				mm.get(s).add(t);
			}
		}
	
	}

	@Override
	public void doAfterAllAnalysed(AnalysisContext context) {
		//1.不知道怎么判断是最后一行,所以在这里去掉
		System.out.println(interfaceCases);
	}
	
	public static void main(String[] args) {
		EasyExcel.read("case\\interface.xlsx", InterfaceCase.class, new InterfaceCaseListener()).sheet("测试用例").doRead();
	}
}
