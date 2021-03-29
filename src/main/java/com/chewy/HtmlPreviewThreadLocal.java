package com.chewy;//package example;

public class HtmlPreviewThreadLocal {
    public static final ThreadLocal<Context> ridThreadLocal = new ThreadLocal();

    public static void set(Context context) {
        ridThreadLocal.set(context);
    }

    public static void unset() {
        ridThreadLocal.remove();
    }

    public static Context get() {
        return ridThreadLocal.get();
    }
}
