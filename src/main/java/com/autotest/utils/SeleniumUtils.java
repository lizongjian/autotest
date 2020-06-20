package com.autotest.utils;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.autotest.beans.Evn;
import com.autotest.beans.ObjectLib;
import com.autotest.beans.UITestCase;
import com.autotest.beans.UITestCaseLog;
import com.sun.org.apache.xml.internal.serializer.ElemDesc;

public class SeleniumUtils {
	static WebDriver driver = null;
	static String result = null;
	
	public static String execute(UITestCase uiTestCase,Evn evn,List<Map<String,ObjectLib>> objectLibs,Map<String,String> context,UITestCaseLog uiTestCaseLog) {
		String module = uiTestCase.getModule();
		String keyword = uiTestCase.getKeyword();
		String inParam = uiTestCase.getInParam();
		String objectName = uiTestCase.getObject();
		//根据对象名称获取对应对象库对象 并解析对象路径的带参数
		ObjectLib objectLib = getObject(objectName,objectLibs,inParam);
		//根据对象获取元素
		WebElement element = getElement(objectLib);
		
		switch (keyword) {
		case "open":
			result = open(inParam,evn.getIp());
			break;
		case "click":
			result = click(element,inParam);
			break;
		case "input":
			result = input(element,inParam);
			break;
		default:
			result = "当前关键字为:"+keyword+",没有匹配的关键字，请检查!";
			break;
		}
		return result;
	}
	
	//通过对象名称返回对象库对象
	public static ObjectLib getObject(String objectName,List<Map<String,ObjectLib>> objectLibs,String inParam) {
		ObjectLib objectLib = null; 
		for(Map<String,ObjectLib> m : objectLibs) {
			try {
				objectLib = m.get(objectName);
				if(objectLib!=null) {
					break;
				}else {
					continue;
				}
			} catch (Exception e) {
				e.printStackTrace();
				result = e.getStackTrace().toString();
			}
		}
		return objectLib;
	}
	
	//通过定位方式和定位值获取对象
	public static WebElement getElement(ObjectLib o) {
		WebElement element = null;
		if(o!=null) {
			String locateType = o.getLocateType();
			String locateValue = o.getLocateValue();		
			switch (locateType) {
			case "xpath":
				try {
					element = driver.findElement(By.xpath(locateValue));
				} catch (Exception e) {
					e.printStackTrace();
					result = e.getStackTrace().toString();
				}
				break;
			case "id":
				try {
					element = driver.findElement(By.id(locateValue));
				} catch (Exception e) {
					e.printStackTrace();
					result = e.getStackTrace().toString();
				}
				break;
			default:
				result = "";
				break;
			}
		}
		return element;
	}
	
	public static String open(String browser,String url) {
		String result = null;
		switch (browser) {
		case "chrome":
			try {
				System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+File.separator+"driver"+File.separator+"chromedriver.exe");
				driver = new ChromeDriver();
				driver.manage().window().maximize();
				driver.get(url);
			} catch (Exception e) {
				e.printStackTrace();
				result = e.getStackTrace().toString();
			}
			break;
		case "firefox":
			try {
				driver = new FirefoxDriver();
				driver.manage().window().maximize();
				driver.get(url);
			} catch (Exception e) {
				e.printStackTrace();
				result = e.getStackTrace().toString();
			}
			break;
		default:
			result = "当前浏览器为:"+browser+",没有匹配的浏览器驱动,请检查!";
			break;
		}
		return result;
	}
	
	public static String click(WebElement element,String inParam) {
		String result = null;
		try {
			element.click();
		} catch (Exception e) {
			e.printStackTrace();
			result = e.getStackTrace().toString();
		}
		return result;
		
	}
	
	public static String input(WebElement element,String inParam) {
		String result = null;
		try {
			element.sendKeys(inParam);
		} catch (Exception e) {
			e.printStackTrace();
			result = e.getStackTrace().toString();
		}
		return result;
	}
	
}
