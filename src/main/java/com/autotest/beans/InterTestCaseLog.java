package com.autotest.beans;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ContentLoopMerge;
import com.alibaba.excel.write.merge.LoopMergeStrategy;

import lombok.Data;

@Data
public class InterTestCaseLog {
	@ExcelProperty("模块")
	public String module;
	@ExcelProperty("接口名称")
	public String interName;
	@ExcelProperty("请求方法")
	public String requestMethod;
	@ExcelProperty("请求头")
	public String requestHeader;
	@ExcelProperty("请求路径")
	public String requestPath;
	@ExcelProperty("请求参数")
	public String defaultParame;
	@ExcelProperty("响应结果")
	public String responseResult;
	@ExcelProperty("断言信息")
	public String check;
	@ExcelProperty("状态")
	public String status;

	public static void main(String[] args) {
		List<InterTestCaseLog> l = new ArrayList<InterTestCaseLog>();
		InterTestCaseLog t = new InterTestCaseLog();
		t.setModule("查询全宗");
		t.setInterName("登录");
		t.setRequestHeader("{'a':'b'}");
		t.setRequestMethod("get");
		t.setRequestPath("http://wwww.baidu.com");
		l.add(t);
		String fileName = "C:\\Users\\zonja\\Desktop\\123.xlsx";
		EasyExcel.write(fileName, InterTestCaseLog.class).sheet("测试报告").doWrite(l);
	}
}
