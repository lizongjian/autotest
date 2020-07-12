package com.autotest.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionToStringUtils {
	public static String get(Throwable e) {
		if (e == null) {
			return "";
		}
		StringWriter stringWriter = new StringWriter();
		e.printStackTrace(new PrintWriter(stringWriter));
		return stringWriter.toString();
	}
}
