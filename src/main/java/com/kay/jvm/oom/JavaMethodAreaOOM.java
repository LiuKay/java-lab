package com.kay.jvm.oom;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;


/**
 * JDK7: VMArgs: -XX:PermSize=10M -XX:MaxPermSize=10M
 * <p>
 * JDK8 or higher: No Perm Area
 */
public class JavaMethodAreaOOM {

		static class ObjectOOM {

		}

		public static void main(String[] args) {
				while (true) {
						Enhancer enhancer = new Enhancer();
						enhancer.setSuperclass(ObjectOOM.class);
						enhancer.setUseCache(false);
						enhancer
								.setCallback((MethodInterceptor) (o, method, objects, methodProxy) -> methodProxy
										.invokeSuper(o, objects));
						enhancer.create();
				}
		}
}
