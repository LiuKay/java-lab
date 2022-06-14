package com.kay.proxy;

import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.*;
import java.security.AccessController;
import java.security.PrivilegedAction;

@Log4j2
public class JdkProxyTest {

    public static void main(String[] args) throws Exception {

        HelloServiceImpl service = new HelloServiceImpl();

        HelloService proxyInstance = (HelloService) Proxy.newProxyInstance(HelloService.class.getClassLoader(),
                service.getClass().getInterfaces(),
                new AopHandler(service)
        );

        proxyInstance.say();
        proxyInstance.h();

//output the proxy to file
//        outputProxyClassToFile(service);
    }

    private static void outputProxyClassToFile(HelloServiceImpl service) throws Exception {
        String name = "java.lang.reflect.ProxyGenerator";
        ClassLoader classLoader = JdkProxyTest.class.getClassLoader();
        Class<?> aClass = classLoader.loadClass(name);
        Method method = aClass.getDeclaredMethod("generateProxyClass", String.class, Class[].class);
        AccessController.doPrivileged((PrivilegedAction<Void>) () -> {
            method.setAccessible(true);
            return null;
        });
        byte[] bytes = (byte[]) method.invoke(null, "com.kay.proxy.$Proxy0", service.getClass().getInterfaces());
        outputClass(bytes, "$Proxy0");
    }

    public static void outputClass(byte[] bytes, String className) {
        final String currentPath = System.getProperty("user.dir");
        String path = currentPath + File.separator + className + ".class";
        try (FileOutputStream out = new FileOutputStream(path)) {
            System.out.println("output proxy class to path:" + path);
            out.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class AopHandler implements InvocationHandler {

        private final Object target;

        public AopHandler(Object target) {
            this.target = target;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            //before invocation
            log.info("before");

            try {
                makeAccessible(method, target);

                Object result = method.invoke(target, args);

                //after invocation
                log.info("after");

                return result;
            } catch (InvocationTargetException exception) {
                throw exception.getTargetException();
            } catch (IllegalAccessException ex) {
                throw new RuntimeException("Could not access method [" + method + "]", ex);
            }
        }
    }

    public static void makeAccessible(Method method, Object object) {
        if ((!Modifier.isPublic(method.getModifiers()) ||
                !Modifier.isPublic(method.getDeclaringClass().getModifiers())) && !method.canAccess(object)) {
            method.setAccessible(true);
        }
    }

    interface HelloService {
        void say();

        void h();
    }

    static class HelloServiceImpl implements HelloService {
        @Override
        public void say() {
            log.info("hello!");
            throw new RuntimeException();
        }

        @Override
        public void h() {
            log.info("h");
        }
    }

}
