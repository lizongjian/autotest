package com.autotest.utils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.autotest.beans.Evn;
import com.autotest.beans.ObjectLib;
import com.autotest.beans.UITestCase;
import com.autotest.beans.UITestCaseLog;
import com.autotest.common.Parse;

public class SeleniumUtils {

	static WebDriver driver = null;
	static Map<String, String> result = null;

	public static Map<String, String> execute(UITestCase uiTestCase, Evn evn, List<Map<String, ObjectLib>> objectLibs,
			Map<String, String> context, UITestCaseLog uiTestCaseLog) {
		String keyword = uiTestCase.getKeyword();
		String inParam = Parse.parse(uiTestCase.getInParam(), context);
		String outParam = uiTestCase.getOutParam();
		String objectName = uiTestCase.getObject();
		// 根据对象名称获取对应对象库对象
		ObjectLib objectLib = ObjectLibUtils.getObjectLib(objectName, objectLibs);
		// 根据对象获取元素 并解析路径参数 $<>
		WebElement element = getElement(objectLib, inParam);
		switch (keyword) {
		case "open":
			result = open(inParam, evn.getIp());
			break;
		case "click":
			result = click(element, inParam);
			break;
		case "input":
			result = input(element, inParam);
			break;
		case "getAttr":
			result = getAttr(element, inParam, outParam);
			break;
		case "wait":
			result = wait(inParam);
			break;
		default:
			result.put("error", "当前关键字为:" + keyword + ",没有匹配的关键字，请检查!");
			break;
		}
		return result;
	}

	private static Map<String, String> wait(String inParam) {
		Map<String, String> result = new HashMap<String, String>();
		try {
			long time = Long.valueOf(inParam)*1000;
			Thread.sleep(time);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("error",e.getStackTrace().toString());
		}
		return result;
	}

	

	// 通过定位方式和定位值获取对象
	public static WebElement getElement(ObjectLib o, String inParam) {
		WebElement element = null;
		if (o != null) {
			String locateType = o.getLocateType();
			// 解析
			String locateValue = Parse.parse(o.getLocateValue(), inParam);
			switch (locateType) {
			case "xpath":
				try {
					element = driver.findElement(By.xpath(locateValue));
				} catch (Exception e) {
					e.printStackTrace();
					result.put("error", e.getStackTrace().toString());
				}
				break;
			case "id":
				try {
					element = driver.findElement(By.id(locateValue));
				} catch (Exception e) {
					e.printStackTrace();
					result.put("error", e.getStackTrace().toString());
				}
				break;
			default:
				result.put("error", "没有匹配的对象");
				break;
			}
		}
		return element;
	}

	public static Map<String, String> open(String browser, String url) {
		Map<String, String> result = new HashMap<String, String>();
		switch (browser) {
		case "chrome":
			try {
				System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + File.separator + "driver"
						+ File.separator + "chromedriver.exe");
				driver = new ChromeDriver();
				//隐式等待
				driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
				driver.manage().window().maximize();
				driver.get(url);
			} catch (Exception e) {
				e.printStackTrace();
				result.put("error", e.getStackTrace().toString());
			}
			break;
		case "firefox":
			try {
				driver = new FirefoxDriver();
				driver.manage().window().maximize();
				driver.get(url);
			} catch (Exception e) {
				e.printStackTrace();
				result.put("error", e.getStackTrace().toString());
			}
			break;
		default:
			result.put("error", "当前浏览器为:" + browser + ",没有匹配的浏览器驱动,请检查!");
			break;
		}
		return result;
	}

	public static Map<String, String> click(WebElement element, String inParam) {
		Map<String, String> result = new HashMap<String, String>();
		try {
			element.click();
		} catch (Exception e) {
			e.printStackTrace();
			result.put("error", e.getStackTrace().toString());
		}
		return result;

	}

	public static Map<String, String> input(WebElement element, String inParam) {
		Map<String, String> result = new HashMap<String, String>();
		try {
			element.sendKeys(inParam);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("error", e.getStackTrace().toString());
		}
		return result;
	}

	public static Map<String, String> getAttr(WebElement element, String inParam, String outParam) {
		Map<String, String> result = new HashMap<String, String>();
		try {

			String value = element.getAttribute(inParam);
			result.put(outParam, value);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("error", e.getStackTrace().toString());
		}
		return result;
	}
	
	public static void closeDriver() {
		if(driver!=null) {
			driver.quit();
		}
	}
	
	public static void main(String[] args) {
		String a = Parse.parse("//*[@id=\"$<搜索按钮>\"]", "su");
		System.out.println(a);
		
		Map<String,String> m = null;
		m.put("1", "1");
		System.out.println(m.get(1));
	}

	
}
