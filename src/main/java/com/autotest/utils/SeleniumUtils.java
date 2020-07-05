package com.autotest.utils;

import java.io.File;
import java.time.Duration;
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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.autotest.beans.Evn;
import com.autotest.beans.ObjectLib;
import com.autotest.beans.UITestCase;
import com.autotest.beans.UITestCaseLog;
import com.autotest.common.Parse;

public class SeleniumUtils {

	static WebDriver driver = null;
	//执行结果返回值
	static Map<String, String> result = null;

	public static Map<String, String> execute(UITestCase uiTestCase, Evn evn, ObjectLib object,
			Map<String, String> context, UITestCaseLog uiTestCaseLog) {
		String keyword = uiTestCase.getKeyword();
		String inParam = uiTestCase.getInParam();
		String outParam = uiTestCase.getOutParam();
		WebElement element = getElement(object, inParam);
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
		case "getText":
			result = getText(element, inParam, outParam);
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
			long time = Long.valueOf(inParam) * 1000;
			Thread.sleep(time);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("error", e.getStackTrace().toString());
		}
		return result;
	}

	// 通过定位方式和定位值获取对象
	public static WebElement getElement(ObjectLib o, String inParam) {
		Map<String,Object> result = new HashMap<String, Object>();
		WebElement element = null;
		if (o != null) {
			String locateType = o.getLocateType();
			// 解析
			String locateValue = Parse.parse(o.getLocateValue(), inParam);
			// 显示等待，元素存在 则返回 4.0 新的 旧的两个参数的过期了
			// WebDriverWait wait =new WebDriverWait(driver, 20) @Deprecated
			// Duration 时间间隔
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
			switch (locateType) {
			case "xpath":
				try {
					wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(locateValue)));
					element = driver.findElement(By.xpath(locateValue));
				} catch (Exception e) {
					e.printStackTrace();
					result.put("error", e.getStackTrace().toString());
				}
				break;
			case "id":
				try {
					wait.until(ExpectedConditions.presenceOfElementLocated(By.id(locateValue)));
					element = driver.findElement(By.id(locateValue));
				} catch (Exception e) {
					e.printStackTrace();
					result.put("error", e.getStackTrace().toString());
				}
				break;
			case "css":
				try {
					wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(locateValue)));
					element = driver.findElement(By.cssSelector(locateValue));
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
				// 隐式等待 30s内 找元素的超时时间
				driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
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
	
	public static Map<String, String> getText(WebElement element, String inParam, String outParam) {
		Map<String, String> result = new HashMap<String, String>();
		try {
			String value = element.getText();
			result.put(outParam, value);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("error", e.getStackTrace().toString());
		}
		return result;
	}

	public static void closeDriver() {
		if (driver != null) {
			driver.quit();
		}
	}

	public static void main(String[] args) {
		String a = Parse.parse("//*[@id=\"$<搜索按钮>\"]", "su");
		System.out.println(a);

		Map<String, String> m = null;
		m.put("1", "1");
		System.out.println(m.get(1));
	}

}
