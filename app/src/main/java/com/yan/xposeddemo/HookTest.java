package com.yan.xposeddemo;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.SystemClock;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;

import dalvik.system.DexFile;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class HookTest implements IXposedHookLoadPackage {
    private static String PACKAGE_NAME = "tv.danmaku.bili";
//    private static String PACKAGE_NAME = "com.ouer.agent.app";

    private Object object;

    private View view;
    private ViewPager viewPager;

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) {
        if (PACKAGE_NAME.equals(lpparam.packageName)) {
            try {
//                printMethod(lpparam);

                //获取class类
                Class c = XposedHelpers.findClass("tv.danmaku.bili.MainActivityV2", lpparam.classLoader);
                //获取str字段

                //AdapterView<?> adapterView, View view, int i, long j
                XposedHelpers.findAndHookMethod(c, "onResume", new Object[]{new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        Log.e("HookTest", "hook afterHookedMethod");

                        if (param.thisObject instanceof Activity) {
                            Window window = ((Activity) param.thisObject).getWindow();
                            printView(window.getDecorView());
                        }

                        Log.e("printView", "afterHookedMethod   " + view + "   " + view.getParent() + "   " + view.getParent().getParent() + "   " + view.getParent().getParent().getParent() + "   " + view.getParent().getParent().getParent().getParent());

                        view = (View) view.getParent().getParent().getParent().getParent();
                        if (view == null) {
                            return;
                        }
                        Log.e("HookTest", "Method a: 1111111111");
                        Method performClick = getDeclaredMethod(view, "dispatchTouchEvent", MotionEvent.class);
                        performClick.setAccessible(true);
                        performClick.invoke(view, MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, view.getWidth() * 3 / 8, 10, 0));
                        performClick.invoke(view, MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, view.getWidth() * 3 / 8, 10, 0));
                        Log.e("HookTest", "Method a: 222222222");

//                        Log.e("HookTest", "afterHookedMethod   " + viewPager);
//                        if (viewPager == null) {
//                            return;
//                        }
//                        Log.e("HookTest", "Method a: 1111111111");
//                        Method setCurrentItem = getDeclaredMethod(viewPager, "setCurrentItem", int.class);
//                        setCurrentItem.setAccessible(true);
//                        setCurrentItem.invoke(viewPager, 1);
//                        Log.e("HookTest", "Method a: 222222222");
                    }
                }});

//------------------------------------------------------------------------------------------------

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Method getDeclaredMethod(Object object, String methodName, Class<?>... parameterTypes) {
        Method method = null;

        for (Class<?> clazz = object.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                method = clazz.getDeclaredMethod(methodName, parameterTypes);
                return method;
            } catch (Exception e) {
                //这里甚么都不能抛出去。
                //如果这里的异常打印或者往外抛，则就不会进入
            }
        }

        return null;
    }

    private void printView(View decorView) {
        if (decorView instanceof ViewPager) {
            viewPager = (ViewPager) decorView;
        }

        Log.e("printView", "" + decorView);
        if (decorView instanceof ViewGroup) {


            for (int i = 0; i < ((ViewGroup) decorView).getChildCount(); i++) {
                printView(((ViewGroup) decorView).getChildAt(i));
            }
        } else {
            if (decorView instanceof TextView) {
                if ("频道".equals(((TextView) decorView).getText().toString())) {
                    Log.e("printView", "" + decorView + "    " + "频道");

                    view = decorView;
                }
            }
        }
    }

    public void printMethod(XC_LoadPackage.LoadPackageParam loadPackageParam) throws IOException, ClassNotFoundException {
        DexFile dexFile = new DexFile(loadPackageParam.appInfo.sourceDir);
        Enumeration<String> classNames = dexFile.entries();
        while (classNames.hasMoreElements()) {
            String className = classNames.nextElement();

            if (isClassNameValid(loadPackageParam, className)) {
                final Class clazz = Class.forName(className, false, loadPackageParam.classLoader);

                for (Method method : clazz.getDeclaredMethods()) {
                    if (!Modifier.isAbstract(method.getModifiers())) {
                        XposedBridge.hookMethod(method, new XC_MethodHook() {
                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                if ("tv.danmaku.bili.ui.main2.basic.ToolbarCenterTextView".equals(clazz.getName())) {
                                    object = param.thisObject;
                                }


                                Log.e("HOOKED: ", clazz.getName() + "     " + param.thisObject + "     " + param.method.getName() + "    " + Arrays.toString(param.args));
                            }
                        });
                    }
                }
            }
        }
    }

    public boolean isClassNameValid(XC_LoadPackage.LoadPackageParam loadPackageParam, String className) {
        return className.startsWith(loadPackageParam.packageName) && !className.contains("$") && !className.contains("BuildConfig") && !className.equals(loadPackageParam.packageName + ".R");
    }
}
