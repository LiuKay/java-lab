package com.kay.utils;

public class FileUtils {

		public static String getClassPathFilePath(String path) {
				return Thread.currentThread().getContextClassLoader().getResource(path).getFile();
		}

}
