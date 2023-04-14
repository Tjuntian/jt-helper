package com.asphyxia.common;

public class BaseContext {

    private static ThreadLocal threadLocal = new ThreadLocal();


    public static String get() {
        String username = (String) threadLocal.get();
        return username;
    }


    public static void set(String value) {
        threadLocal.set(value);
    }
}
