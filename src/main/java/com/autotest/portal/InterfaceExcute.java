package com.autotest.portal;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.excel.EasyExcel;
import com.autotest.beans.Evn;
import com.autotest.beans.Interface;
import com.autotest.beans.InterfaceCase;
import com.autotest.beans.ResponseResult;
import com.autotest.common.Parse;
import com.autotest.utils.BuildInterfaceTestCaseUtils;
import com.autotest.utils.EvnListener;
import com.autotest.utils.HttpClientUtils;
import com.autotest.utils.InterTestCaseListener;
import com.autotest.utils.InterfaceCaseListener;
import com.autotest.utils.InterfaceListener;
import com.autotest.beans.InterfaceTestCase;
import com.autotest.beans.InterfaceTestCaseLog;
import com.jayway.jsonpath.JsonPath;

public class InterfaceExcute {
	private static final Logger logger = LoggerFactory.getLogger(InterfaceExcute.class);
	// 保存全局变量
	public Map<String, String> context = new HashMap<String, String>();

	// 存储单接口用例模块
	public static Map<String, List<InterfaceTestCase>> testCase = new HashMap<String, List<InterfaceTestCase>>();
	
	//保存日志对象
	public static List<InterfaceTestCaseLog> testCaseLogsList = new ArrayList<InterfaceTestCaseLog>();
	
	public static void run() {
		// 1.读取环境
		EasyExcel.read("F:\\workspace\\autotest\\autotest\\case\\inter_v2.xlsx", Evn.class, new EvnListener()).sheet("全局配置信息").doRead();
		// 2.读取接口信息
		EasyExcel.read("F:\\workspace\\autotest\\autotest\\case\\inter_v2.xlsx", Interface.class, new InterfaceListener()).sheet("接口信息").doRead();
		// 3.读取测试用例
		EasyExcel.read("F:\\workspace\\autotest\\autotest\\case\\inter_v2.xlsx", InterfaceCase.class, new InterfaceCaseListener()).sheet("测试用例").doRead();
		//4.拼装成可执行用例对象 InterfaceTestCase
		
		Evn evn = EvnListener.evn;
		
		List<Map<String, List<InterfaceTestCase>>> testCases = BuildInterfaceTestCaseUtils.build(InterfaceListener.interfaces,InterfaceCaseListener.interfaceCases);
		
		// 3.当前的用例
		for (int i = 0; i <= testCases.size() - 1; i++) {
			testCase = testCases.get(i);
			for (Map.Entry<String, List<InterfaceTestCase>> entry : testCase.entrySet()) {
				execute(entry.getValue(),evn);
			}
		}
		//生成测试报告
		EasyExcel.write(System.getProperty("user.dir")+File.separator+"report"+File.separator+System.currentTimeMillis()+".xlsx", InterfaceTestCaseLog.class).sheet("测试报告").doWrite(testCaseLogsList);
		System.out.println(System.getProperty("user.dir")+File.separator+"report"+File.separator+System.currentTimeMillis()+".xlsx");
	}

	// 执行一个模块
	public static void execute(List<InterfaceTestCase> testCase,Evn evn) {
		
		//保存在一个用例范围内 输出变量
		Map<String, String> context = new HashMap<String, String>();
		
		//步骤
		for(int i=0;i<=testCase.size()-1;i++) {
			InterfaceTestCaseLog testCaseLog = new InterfaceTestCaseLog();
			InterfaceTestCase t = testCase.get(i);
			testCaseLog.setModule(t.getModule());//记录用例名称
			testCaseLog.setInterfaceName(t.getInterfaceName());//记录接口名称
			testCaseLog.setRequestMethod(t.getRequestMethod());//记录请求方法
			//1.发请求
			ResponseResult respResult = HttpClientUtils.sendRequest(t,evn,context,testCaseLog);
			logger.info(respResult.toString());
			testCaseLog.setResponseResult(respResult.getContent());
			
			//保存输出变量
			Map<String,String> out_parame = t.toMap(t.getOutParam());//{"data.0.children.0.label":"var_label2",}
			if(out_parame!=null) {
				for (Map.Entry<String,String> op : out_parame.entrySet()) {
					try {
						context.put(op.getValue(), JsonPath.parse(respResult.getContent()).read(op.getKey()));
					} catch (Exception e) {
						logger.info(op.getKey() + "没有匹配");
					}
					
				}
			}
		
			
			//输出校验  断言
			Map<String,String> out_check = t.toMap(t.getOutCheck());
			if(out_check!=null) {
				for (Map.Entry<String,String> oc : out_check.entrySet()) {
					//期望值
					String expect = "";
					try {
						expect = JsonPath.read(respResult.getContent(), oc.getKey());
						//实际值
						Map<String, String> actual = Parse.parse(oc.getValue(), context);
						if(expect.equals(actual.get("result"))) {
							testCaseLog.setCheck(expect+""+actual);
							System.out.println("预期"+expect+"----"+"实际"+actual);
							testCaseLog.setStatus("success");
						}else {
							testCaseLog.setStatus("fail");
							testCaseLog.setStatusMes("实际值与期望值不相等：实际值为【"+actual+"】"+"期望值为【"+expect+"】");
						}
					} catch (Exception e) {
						testCaseLog.setStatus("fail");
						testCaseLog.setStatusMes("获取实际值或期望值失败："+e.getMessage());
						e.printStackTrace();
					}
				}
			}
			testCaseLogsList.add(testCaseLog);
		}
	}

	public static void main(String[] args) {
		InterfaceExcute.run();
	}
}
