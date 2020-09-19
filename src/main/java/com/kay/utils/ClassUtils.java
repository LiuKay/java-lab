package com.kay.utils;

public class ClassUtils {

		private static final char PACKAGE_SEPARATOR = '.';

		private static final char PATH_SEPARATOR = '/';

		public static ClassLoader getDefaultClassLoader() {
				ClassLoader cl = null;
				try {
						cl = Thread.currentThread().getContextClassLoader();
				} catch (Throwable ex) {
						//can not access current thread context class loader
				}
				if (cl == null) {
						//use current thread class loader
						cl = ClassUtils.class.getClassLoader();
						if (cl == null) {
								// -> bootstrap ClassLoader
								try {
										cl = ClassLoader.getSystemClassLoader();
								} catch (Throwable ex) {
										//ignored
								}
						}
				}
				return cl;
		}

		public static String classPackageAsResourcePath(Class<?> clazz) {
				if (clazz == null) {
						return "";
				}
				String className = clazz.getName();
				int packageEndIndex = className.lastIndexOf(PACKAGE_SEPARATOR);
				if (packageEndIndex == -1) {
						return "";
				}
				String packageName = className.substring(0, packageEndIndex);
				return packageName.replace(PACKAGE_SEPARATOR, PATH_SEPARATOR);
		}
}
