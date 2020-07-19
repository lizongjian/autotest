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

	// 保存日志对象
	public static List<InterfaceTestCaseLog> testCaseLogsList = new ArrayList<InterfaceTestCaseLog>();

	public static void run() {
		// 1.读取环境
		EasyExcel.read("case\\interface.xlsx", Evn.class, new EvnListener())
				.sheet("全局配置信息").doRead();
		// 2.读取接口信息
		EasyExcel.read("case\\interface.xlsx", Interface.class,
				new InterfaceListener()).sheet("接口信息").doRead();
		// 3.读取测试用例
		EasyExcel.read("case\\interface.xlsx", InterfaceCase.class,
				new InterfaceCaseListener()).sheet("测试用例").doRead();
		// 4.拼装成可执行用例对象 InterfaceTestCase

		Evn evn = EvnListener.evn;

		List<Map<String, List<InterfaceTestCase>>> testCases = BuildInterfaceTestCaseUtils
				.build(InterfaceListener.interfaces, InterfaceCaseListener.interfaceCases);

		// 3.当前的用例
		for (int i = 0; i <= testCases.size() - 1; i++) {
			testCase = testCases.get(i);
			for (Map.Entry<String, List<InterfaceTestCase>> entry : testCase.entrySet()) {
				execute(entry.getValue(), evn);
			}
		}
		// 生成测试报告
		EasyExcel
				.write(System.getProperty("user.dir") + File.separator + "report" + File.separator
						+ System.currentTimeMillis() + ".xlsx", InterfaceTestCaseLog.class)
				.sheet("测试报告").doWrite(testCaseLogsList);
		System.out.println(System.getProperty("user.dir") + File.separator + "report" + File.separator
				+ System.currentTimeMillis() + ".xlsx");
	}

	// 执行一个模块
	public static void execute(List<InterfaceTestCase> testCase, Evn evn) {

		// 保存在一个用例范围内 输出变量
		Map<String, String> context = new HashMap<String, String>();

		// 步骤
		for (int i = 0; i <= testCase.size() - 1; i++) {
			InterfaceTestCaseLog testCaseLog = new InterfaceTestCaseLog();
			InterfaceTestCase t = testCase.get(i);
			testCaseLog.setModule(t.getModule());// 记录用例名称
			testCaseLog.setInterfaceName(t.getInterfaceName());// 记录接口名称
			testCaseLog.setRequestMethod(t.getRequestMethod());// 记录请求方法
			// 1.发请求
			ResponseResult respResult = HttpClientUtils.sendRequest(t, evn, context, testCaseLog);
			logger.info(respResult.toString());
			// excel单元格最多字符32767
			if (respResult.getContent().length() > 32767) {
				testCaseLog.setResponseResult(respResult.getContent().substring(0, 32767));
			} else {
				testCaseLog.setResponseResult(respResult.getContent());
			}
			// 保存输出变量
			Map<String, String> out_parame = t.toMap(t.getOutParam());// {"data.0.children.0.label":"var_label2",}
			if (out_parame != null) {
				for (Map.Entry<String, String> op : out_parame.entrySet()) {
					try {
						context.put(op.getValue(),
								JsonPath.parse(respResult.getContent()).read(op.getKey()).toString());
					} catch (Exception e) {
						logger.info(op.getKey() + "没有匹配");
					}

				}
			}

			// 输出校验 断言
			Map<String, String> out_check = t.toMap(t.getOutCheck());
			StringBuffer sb = new StringBuffer();
			if (out_check != null) {
				for (Map.Entry<String, String> oc : out_check.entrySet()) {
					// 期望值
					String expect = "";
					try {
						expect = JsonPath.read(respResult.getContent(), oc.getKey()).toString();
						// 实际值
						String actual = Parse.parse(oc.getValue(), context).get("result");
						
						if (expect.equals(actual)) {
							sb.append("[预期值="+expect+",实际值="+actual+"]、");
							testCaseLog.setCheck(sb.toString().substring(0, sb.length()-1));
							System.out.println(sb.toString());
							testCaseLog.setStatus("success");
						} else {
							testCaseLog.setStatus("fail");
							testCaseLog.setStatusMes("实际值与期望值不相等：预期值=【" + expect + "】" + "实际值=【" + actual+ "】");
						}
					} catch (Exception e) {
						testCaseLog.setStatus("fail");
						testCaseLog.setStatusMes("获取实际值或期望值失败：" + e.getMessage());
						e.printStackTrace();
					}
				}
			}
			testCaseLogsList.add(testCaseLog);
			System.err.println(testCaseLog);
		}
	}

	public static void main(String[] args) {
		InterfaceExcute.run();
//		String s = JsonPath.parse("{\"code\":200,\"data\":[{\"children\":[{\"name\":\"财经\",\"id\":1755,\"parentId\":1747,\"isBottom\":true},{\"name\":\"要闻\",\"id\":1752,\"parentId\":1747,\"isBottom\":true},{\"name\":\"国际\",\"id\":1754,\"parentId\":1747,\"isBottom\":true},{\"name\":\"体育\",\"id\":1756,\"parentId\":1747,\"isBottom\":true},{\"name\":\"国内\",\"id\":1753,\"parentId\":1747,\"isBottom\":true}],\"name\":\"新闻\",\"id\":1747,\"parentId\":null,\"isBottom\":false},{\"children\":[{\"name\":\"时事速递\",\"id\":1884,\"parentId\":1748,\"isBottom\":true},{\"name\":\"热图排行\",\"id\":1885,\"parentId\":1748,\"isBottom\":true}],\"name\":\"图片\",\"id\":1748,\"parentId\":null,\"isBottom\":false},{\"children\":[{\"name\":\"热播排行\",\"id\":1893,\"parentId\":1749,\"isBottom\":true},{\"name\":\"最新视频\",\"id\":1892,\"parentId\":1749,\"isBottom\":true}],\"name\":\"视频\",\"id\":1749,\"parentId\":null,\"isBottom\":false},{\"children\":[{\"name\":\"求职简历\",\"id\":1903,\"parentId\":1750,\"isBottom\":true},{\"name\":\"学习资料\",\"id\":1904,\"parentId\":1750,\"isBottom\":true},{\"name\":\"工作总结\",\"id\":1902,\"parentId\":1750,\"isBottom\":true}],\"name\":\"文库\",\"id\":1750,\"parentId\":null,\"isBottom\":false},{\"children\":[{\"children\":[{\"name\":\"安全\",\"id\":2070,\"parentId\":1907,\"isBottom\":true},{\"name\":\"浏览器\",\"id\":2069,\"parentId\":1907,\"isBottom\":true}],\"name\":\"系统软件\",\"id\":1907,\"parentId\":1751,\"isBottom\":false},{\"children\":[{\"name\":\"学习\",\"id\":2066,\"parentId\":1906,\"isBottom\":true},{\"name\":\"办公\",\"id\":2065,\"parentId\":1906,\"isBottom\":true}],\"name\":\"应用软件\",\"id\":1906,\"parentId\":1751,\"isBottom\":false},{\"children\":[{\"name\":\"社区\",\"id\":2068,\"parentId\":1908,\"isBottom\":true},{\"name\":\"交友\",\"id\":2067,\"parentId\":1908,\"isBottom\":true}],\"name\":\"聊天软件\",\"id\":1908,\"parentId\":1751,\"isBottom\":false},{\"name\":\"下载排行\",\"id\":2063,\"parentId\":1751,\"isBottom\":true},{\"name\":\"下载分类\",\"id\":2062,\"parentId\":1751,\"isBottom\":true}],\"name\":\"下载\",\"id\":1751,\"parentId\":null,\"isBottom\":false},{\"children\":[{\"name\":\"体验后台\",\"id\":1957,\"parentId\":1945,\"isBottom\":true},{\"name\":\"程序下载\",\"id\":1953,\"parentId\":1945,\"isBottom\":true},{\"name\":\"广告服务\",\"id\":1955,\"parentId\":1945,\"isBottom\":true},{\"name\":\"友情链接\",\"id\":1956,\"parentId\":1945,\"isBottom\":true},{\"name\":\"许可协议\",\"id\":1954,\"parentId\":1945,\"isBottom\":true},{\"name\":\"联系我们\",\"id\":1952,\"parentId\":1945,\"isBottom\":true}],\"name\":\"底部导航\",\"id\":1945,\"parentId\":null,\"isBottom\":false},{\"name\":\"投票调查\",\"id\":2122,\"parentId\":null,\"isBottom\":true},{\"name\":\"智能表单\",\"id\":2311,\"parentId\":null,\"isBottom\":true},{\"name\":\"专题\",\"id\":1842,\"parentId\":null,\"isBottom\":true},{\"name\":\"采集栏目\",\"id\":2292,\"parentId\":null,\"isBottom\":true}],\"message\":\"操作成功\",\"redirectUrl\":\"\",\"requestUrl\":\"\",\"timestamp\":\"2020-07-18 09:47:42\",\"token\":\"\"}").read("$.data[0].children[0].id").toString();
//		System.out.println(s);
	}
}
