package com.autotest.utils;

import java.util.List;
import java.util.Map;

import com.autotest.beans.ObjectLib;

public class ObjectLibUtils {
	// 通过对象名称返回对象库对象
		public static ObjectLib getObjectLib(String objectName, List<Map<String, ObjectLib>> objectLibs) {
			ObjectLib objectLib = null;
			for (Map<String, ObjectLib> m : objectLibs) {
				try {
					objectLib = m.get(objectName);
					if (objectLib != null) {
						break;
					} else {
						continue;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return objectLib;
		}
}
