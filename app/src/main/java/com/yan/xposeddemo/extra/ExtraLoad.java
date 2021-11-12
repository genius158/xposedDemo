package com.yan.xposeddemo.extra;

import android.annotation.SuppressLint;
import android.content.Context;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;
import io.hexhacking.xunwind.XUnwind;

public class ExtraLoad {

    public static ClassLoader extraClassloader;
    @SuppressLint("StaticFieldLeak")
    public static Context ctx;

    private static Class<?> XwindClass;

    public static void init(Context ctx) {
        ExtraLoad.ctx = ctx;
        LibSoLoaderHelper.load(ctx);

        File apkFile = new File(new File(ctx.getFilesDir(), "extraapk"), "app-debug.apk");
        if (!apkFile.exists()) return;
        File soFile = new File(new File(ctx.getFilesDir(), "extra"), "armeabi-v7a");
        if (!soFile.exists()) return;

        extraClassloader = new DexClassLoader(apkFile.getAbsolutePath(),
                null, soFile.getAbsolutePath(), ctx.getClassLoader().getParent());
        try {
            XwindClass = Class.forName(XUnwind.class.getName(), false, extraClassloader);

            initXunwind();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    private static void initXunwind() {
//        public static synchronized void init() throws SecurityException, UnsatisfiedLinkError {
//            init(null);
//        }

        try {
            Method initMethod = XwindClass.getMethod("init");
            initMethod.invoke(XwindClass);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void logStack(String logtag, int priority) {
        XUnwind.logLocalCurrentThread(logtag, priority);
        try {
            Method logLocalCurrentThreadMethod = XwindClass.getMethod("logLocalCurrentThread", String.class, int.class);
            logLocalCurrentThreadMethod.invoke(XwindClass, logtag, priority);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
