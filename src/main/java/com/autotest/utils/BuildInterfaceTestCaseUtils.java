package com.autotest.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.excel.EasyExcel;
import com.autotest.beans.Interface;
import com.autotest.beans.InterfaceCase;
import com.autotest.beans.InterfaceTestCase;

public class BuildInterfaceTestCaseUtils {
	
	//
	public static List<Map<String, List<InterfaceTestCase>>> build(List<Interface> interfaces,List<Map<String, List<InterfaceCase>>> interfaceCasesList){
		
		//要返回的 所有用例
		List<Map<String, List<InterfaceTestCase>>> interfaceTestCaseListResult = new ArrayList<Map<String,List<InterfaceTestCase>>>();
		
		//循环接口用例 封装成可执行单元 不用管名称 直接把list<InterfaceCase> 换成 list<InterfaceTestCase> 用例名称已经有了 不用处理
		for(Map<String, List<InterfaceCase>> interfaceCase : interfaceCasesList) {
			//要替换成的 （一个完整用例 和 用例名称）
			Map<String, List<InterfaceTestCase>> interfaceTestCaseMap = new HashMap<String, List<InterfaceTestCase>>();
			//要替换成的 （一个完整用例） 
			List<InterfaceTestCase> interfaceTestCaseList = new ArrayList<InterfaceTestCase>();
			
			//循环接口用例关系  用例名称和接口步骤
			 for(Map.Entry<String, List<InterfaceCase>> interfaceCaseMap : interfaceCase.entrySet()) {
				 String testCaseName = interfaceCaseMap.getKey();
				for(InterfaceCase interfaceCase0 : interfaceCaseMap.getValue()) {
					InterfaceTestCase interfaceTestCase = new InterfaceTestCase();
					Interface interFace = getInterfaceByName(interfaces, interfaceCase0.getInterfaceName());
					interfaceTestCase.setModule(interfaceCase0.getModule());
					interfaceTestCase.setInterfaceName(interfaceCase0.getInterfaceName());
					interfaceTestCase.setRequestType(interFace.getRequestType());
					interfaceTestCase.setRequestProtocol(interFace.getRequestProtocol());
					interfaceTestCase.setRequestMethod(interFace.getRequestMethod());
					interfaceTestCase.setRequestPath(interFace.getRequestPath());
					interfaceTestCase.setRequestHeader(interFace.getRequestHeader());
					interfaceTestCase.setInParam(interFace.getInParam());
					interfaceTestCase.setOutParam(interFace.getOutParam());
					interfaceTestCase.setInReplace(interfaceCase0.getInReplace());
					interfaceTestCase.setOutCheck(interfaceCase0.getOutCheck());
					//完整用例
					interfaceTestCaseList.add(interfaceTestCase);
				}
				//完整一个用例和用例名称
				interfaceTestCaseMap.put(testCaseName, interfaceTestCaseList);	
			 }
			 //全部用例
			 interfaceTestCaseListResult.add(interfaceTestCaseMap);
		}
		return interfaceTestCaseListResult;
	}
	
	//通过接口名称获取接口对象
	public static Interface getInterfaceByName(List<Interface> interfaces,String interfaceName) {
		try {
			for(Interface inter : interfaces) {
				if(inter.getInterfaceName().equals(interfaceName)) {
					return inter;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}
	
	//通过list<InterfaceCase> 返回  list<InterfaceTestCase> 
	
	
	public static void main(String[] args) {
		EasyExcel.read("F:\\workspace\\autotest\\autotest\\case\\inter_v2.xlsx", Interface.class, new InterfaceListener()).sheet("接口信息").doRead();
		// 3.读取测试用例
		EasyExcel.read("F:\\workspace\\autotest\\autotest\\case\\inter_v2.xlsx", InterfaceCase.class, new InterfaceCaseListener()).sheet("测试用例").doRead();
	
		System.out.println(BuildInterfaceTestCaseUtils.build(InterfaceListener.interfaces, InterfaceCaseListener.interfaceCases));
	}
}
