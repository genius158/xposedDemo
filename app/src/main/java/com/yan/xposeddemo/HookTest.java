//package com.yan.xposeddemo;
//
//import android.content.Context;
//import android.util.Log;
//import dalvik.system.DexFile;
//import de.robv.android.xposed.IXposedHookLoadPackage;
//import de.robv.android.xposed.XC_MethodHook;
//import de.robv.android.xposed.XposedBridge;
//import de.robv.android.xposed.XposedHelpers;
//import de.robv.android.xposed.callbacks.XC_LoadPackage;
//import java.io.IOException;
//import java.lang.reflect.Method;
//import java.lang.reflect.Modifier;
//import java.util.Arrays;
//import java.util.Enumeration;
//
//public class HookTest implements IXposedHookLoadPackage {
//  private static String PACKAGE_NAME = "com.quvideo.xiaoying";
//  //private static String PACKAGE_NAME = "com.ouer.agent.app";
//
//  @Override
//  public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) {
//    Log.e("handleLoadPackage11", lpparam.packageName);
//
//    if (PACKAGE_NAME.equals(lpparam.packageName)) {
//      Log.e("handleLoadPackage22", lpparam.packageName);
//      try {
//
//        hook(lpparam);
//
//        //com.camerasideas.instashot.store.b.l.e
//
//        //获取class类
//        //Class c = XposedHelpers.findClass("com.camerasideas.instashot.store.b.l"
//        //    , lpparam.classLoader);
//        //
//        //XposedHelpers.findAndHookMethod(c, "b"
//        //    , Context.class
//        //    , new XC_MethodHook() {
//        //      @Override
//        //      protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//        //        super.beforeHookedMethod(param);
//        //      }
//        //
//        //      @Override
//        //      protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//        //        param.setResult(true);
//        //        super.afterHookedMethod(param);
//        //        Log.e("handleLoadPackage22 c", param.getResult() + "");
//        //      }
//        //    }
//        //);
//
//        //XposedHelpers.findAndHookMethod(c, "b",
//        //    Context.class
//        //    , new XC_MethodHook() {
//        //      @Override
//        //      protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//        //        super.beforeHookedMethod(param);
//        //      }
//        //
//        //      @Override
//        //      protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//        //        //if (param != null && param.args[0] instanceof MotionEvent) {
//        //        //  // 获取onInterceptTouchEvent的参数
//        //        //  MotionEvent me = (MotionEvent) param.args[0];
//        //        //  // thisObject就是SwipeBackLayout也就是View，所以这里直接通过getContext获取context
//        //        //  Context context = ((View) param.thisObject).getContext();
//        //        //
//        //        //  if (me.getX() < ViewConfiguration.get(context).getScaledEdgeSlop()) {
//        //        //    super.afterHookedMethod(param);
//        //        //    return;
//        //        //  }
//        //        //}
//        //
//        //        super.afterHookedMethod(param);
//        //
//        //        if (!((boolean) param.getResult())) {
//        //          flag(param);
//        //        }
//        //        Log.e("handleLoadPackage22 b", param.getResult() + "    ");
//        //
//        //        //                        Log.e("HookTest", "hook afterHookedMethod");
//        //        //
//        //        //                        if (param.thisObject instanceof Activity) {
//        //        //                            Window window = ((Activity) param.thisObject).getWindow();
//        //        //                            printView(window.getDecorView());
//        //        //                        }
//        //        //
//        //        //                        Log.e("printView", "afterHookedMethod   " + view + "   " + view.getParent() + "   " + view.getParent().getParent() + "   " + view.getParent().getParent().getParent() + "   " + view.getParent().getParent().getParent().getParent());
//        //        //
//        //        //                        view = (View) view.getParent().getParent().getParent().getParent();
//        //        //                        if (view == null) {
//        //        //                            return;
//        //        //                        }
//        //        //                        Log.e("HookTest", "Method a: 1111111111");
//        //        //                        Method performClick = getDeclaredMethod(view, "dispatchTouchEvent", MotionEvent.class);
//        //        //                        performClick.setAccessible(true);
//        //        //                        performClick.invoke(view, MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, view.getWidth() * 3 / 8, 10, 0));
//        //        //                        performClick.invoke(view, MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, view.getWidth() * 3 / 8, 10, 0));
//        //        //                        Log.e("HookTest", "Method a: 222222222");
//        //        //
//        //        ////                        Log.e("HookTest", "afterHookedMethod   " + viewPager);
//        //        ////                        if (viewPager == null) {
//        //        ////                            return;
//        //        ////                        }
//        //        ////                        Log.e("HookTest", "Method a: 1111111111");
//        //        ////                        Method setCurrentItem = getDeclaredMethod(viewPager, "setCurrentItem", int.class);
//        //        ////                        setCurrentItem.setAccessible(true);
//        //        ////                        setCurrentItem.invoke(viewPager, 1);
//        //        ////                        Log.e("HookTest", "Method a: 222222222");
//        //      }
//        //    }
//        //);
//
//        //------------------------------------------------------------------------------------------------
//
//      } catch (Exception e) {
//        e.printStackTrace();
//      }
//    }
//  }
//
//  public void hook(XC_LoadPackage.LoadPackageParam loadPackageParam)
//      throws IOException, ClassNotFoundException {
//    DexFile dexFile = new DexFile(loadPackageParam.appInfo.sourceDir);
//    Enumeration<String> classNames = dexFile.entries();
//    while (classNames.hasMoreElements()) {
//      String className = classNames.nextElement();
//
//      if (isClassNameValid(loadPackageParam, className)) {
//        final Class clazz = Class.forName(className, false, loadPackageParam.classLoader);
//
//        for (Method method : clazz.getDeclaredMethods()) {
//          if (!Modifier.isAbstract(method.getModifiers())) {
//            XposedBridge.hookMethod(method, new XC_MethodHook() {
//              @Override
//              protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                Log.e("HOOKED: ", clazz.getName()
//                    + "     "
//                    + param.thisObject
//                    + "     "
//                    + param.method.getName()
//                    + "    "
//                    + Arrays
//                    .toString(param.args));
//              }
//            });
//          }
//        }
//      }
//    }
//  }
//
//  public boolean isClassNameValid(XC_LoadPackage.LoadPackageParam loadPackageParam,
//      String className) {
//    return className.startsWith(loadPackageParam.packageName)
//        && !className.contains("$")
//        && !className.contains("BuildConfig")
//        && !className.equals(loadPackageParam.packageName + ".R");
//  }
//}
//
//
