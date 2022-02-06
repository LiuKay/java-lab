package com.kay.proxy;

import java.lang.reflect.*;

public class JdkProxyTest {

    public static void main(String[] args) {

        HelloServiceImpl service = new HelloServiceImpl();

        HelloService proxyInstance = (HelloService) Proxy.newProxyInstance(HelloService.class.getClassLoader(),
                service.getClass().getInterfaces(),
                new AopHandler(service)
        );

        System.out.println("is proxy class: " + Proxy.isProxyClass(proxyInstance.getClass()));

        proxyInstance.say();
//        proxyInstance.h();
//        String toString = proxyInstance.toString();
//        System.out.println(toString);
    }

    static class AopHandler implements InvocationHandler {

        private final Object target;

        public AopHandler(Object target) {
            this.target = target;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            //before invocation
            System.out.println("before");

            try {
                makeAccessible(method, target);

                Object result = method.invoke(target, args);

                //after invocation
                System.out.println("after");

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
            System.out.println("hello!");
            throw new RuntimeException();
        }

        @Override
        public void h() {
            System.out.println("h");
        }
    }

}
