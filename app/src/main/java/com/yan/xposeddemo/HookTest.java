package com.yan.xposeddemo;

import android.content.Context;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class HookTest implements IXposedHookLoadPackage {
    private static String PACKAGE_NAME = "com.sankuai.youxuan";

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) {
        Log.e("handleLoadPackage11", lpparam.packageName);

        if (PACKAGE_NAME.equals(lpparam.packageName)) {
            Log.e("handleLoadPackage22", lpparam.packageName);
            try {

                hook(lpparam);

                //com.camerasideas.instashot.store.b.l.e

                //获取class类
                //Class c = XposedHelpers.findClass("com.camerasideas.instashot.store.b.l"
                //    , lpparam.classLoader);
                //
                //XposedHelpers.findAndHookMethod(c, "b"
                //    , Context.class
                //    , new XC_MethodHook() {
                //      @Override
                //      protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                //        super.beforeHookedMethod(param);
                //      }
                //
                //      @Override
                //      protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                //        param.setResult(true);
                //        super.afterHookedMethod(param);
                //        Log.e("handleLoadPackage22 c", param.getResult() + "");
                //      }
                //    }
                //);

                //XposedHelpers.findAndHookMethod(c, "b",
                //    Context.class
                //    , new XC_MethodHook() {
                //      @Override
                //      protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                //        super.beforeHookedMethod(param);
                //      }
                //
                //      @Override
                //      protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                //        //if (param != null && param.args[0] instanceof MotionEvent) {
                //        //  // 获取onInterceptTouchEvent的参数
                //        //  MotionEvent me = (MotionEvent) param.args[0];
                //        //  // thisObject就是SwipeBackLayout也就是View，所以这里直接通过getContext获取context
                //        //  Context context = ((View) param.thisObject).getContext();
                //        //
                //        //  if (me.getX() < ViewConfiguration.get(context).getScaledEdgeSlop()) {
                //        //    super.afterHookedMethod(param);
                //        //    return;
                //        //  }
                //        //}
                //
                //        super.afterHookedMethod(param);
                //
                //        if (!((boolean) param.getResult())) {
                //          flag(param);
                //        }
                //        Log.e("handleLoadPackage22 b", param.getResult() + "    ");
                //
                //        //                        Log.e("HookTest", "hook afterHookedMethod");
                //        //
                //        //                        if (param.thisObject instanceof Activity) {
                //        //                            Window window = ((Activity) param.thisObject).getWindow();
                //        //                            printView(window.getDecorView());
                //        //                        }
                //        //
                //        //                        Log.e("printView", "afterHookedMethod   " + view + "   " + view.getParent() + "   " + view.getParent().getParent() + "   " + view.getParent().getParent().getParent() + "   " + view.getParent().getParent().getParent().getParent());
                //        //
                //        //                        view = (View) view.getParent().getParent().getParent().getParent();
                //        //                        if (view == null) {
                //        //                            return;
                //        //                        }
                //        //                        Log.e("HookTest", "Method a: 1111111111");
                //        //                        Method performClick = getDeclaredMethod(view, "dispatchTouchEvent", MotionEvent.class);
                //        //                        performClick.setAccessible(true);
                //        //                        performClick.invoke(view, MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, view.getWidth() * 3 / 8, 10, 0));
                //        //                        performClick.invoke(view, MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, view.getWidth() * 3 / 8, 10, 0));
                //        //                        Log.e("HookTest", "Method a: 222222222");
                //        //
                //        ////                        Log.e("HookTest", "afterHookedMethod   " + viewPager);
                //        ////                        if (viewPager == null) {
                //        ////                            return;
                //        ////                        }
                //        ////                        Log.e("HookTest", "Method a: 1111111111");
                //        ////                        Method setCurrentItem = getDeclaredMethod(viewPager, "setCurrentItem", int.class);
                //        ////                        setCurrentItem.setAccessible(true);
                //        ////                        setCurrentItem.invoke(viewPager, 1);
                //        ////                        Log.e("HookTest", "Method a: 222222222");
                //      }
                //    }
                //);

                //------------------------------------------------------------------------------------------------

            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    private Context ctx;

    public void hook(final XC_LoadPackage.LoadPackageParam loadPackageParam)
            throws IOException, ClassNotFoundException {
//        XposedHelpers.findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                ctx = (Context) param.args[0];
//                Log.e("HOOKED: ", "--- " + ctx.getPackageManager().getClass());
//            }
//        });


//        final Class clazz = Class.forName("android.content.pm.PackageManager", false, loadPackageParam.classLoader);
//        @SuppressLint("PrivateApi") final Class clazz = Class.forName("android.app.ApplicationPackageManager", false, loadPackageParam.classLoader);
//        Log.e("HOOKED: ", clazz.toString());
//        XposedHelpers.findAndHookMethod(clazz, "getPackageInfo", String.class, int.class, new XC_MethodHook() {
//            @RequiresApi(api = Build.VERSION_CODES.O)
//            @Override
//            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                super.beforeHookedMethod(param);
//
//            }
//
//            @RequiresApi(api = Build.VERSION_CODES.O)
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                Log.e("HOOKED: ", "signature param.getResult()222 " + "  " + param.getResult());
//                if (!"com.sankuai.youxuan".equalsIgnoreCase((String) param.args[0])){
//                    super.afterHookedMethod(param);
//                    return;
//                }
//
//                if (param.getResult() == null) {
//                    super.afterHookedMethod(param);
//                    return;
//                }
//                Signature[] signatures = ((PackageInfo) param.getResult()).signatures;
//                if (signatures == null) {
//                    super.afterHookedMethod(param);
//                    return;
//                }
//                for (int i = 0; i < signatures.length; i++) {
//                    Signature signature = signatures[i];
//                    String encoded = Base64.getEncoder().encodeToString(signature.toByteArray());
//                    Log.e("HOOKED: ", "signature " + i + "  " + encoded);
//
//                    if (i == 0) {
//                        Signature fixed = new Signature(Base64.getDecoder().decode("MIICfzCCAeigAwIBAgIETWkbuDANBgkqhkiG9w0BAQUFADCBgjELMAkGA1UEBhMCQ04xEDAOBgNVBAgTB0JlaWppbmcxEDAOBgNVBAcTB0JlaWppbmcxJDAiBgNVBAoTG1Nhbmt1YWkgVGVjaG5vbG9neSBDby4gTHRkLjEUMBIGA1UECxMLbWVpdHVhbi5jb20xEzARBgNVBAMTCkNIRU4gTGlhbmcwIBcNMTEwMjI2MTUyNjQ4WhgPMjExMTAyMDIxNTI2NDhaMIGCMQswCQYDVQQGEwJDTjEQMA4GA1UECBMHQmVpamluZzEQMA4GA1UEBxMHQmVpamluZzEkMCIGA1UEChMbU2Fua3VhaSBUZWNobm9sb2d5IENvLiBMdGQuMRQwEgYDVQQLEwttZWl0dWFuLmNvbTETMBEGA1UEAxMKQ0hFTiBMaWFuZzCBnzANBgkqhkiG9w0BAQEFAAOBjQAwgYkCgYEAugm3LO7BWgTZuR1muiImtQJU5449WfZ+b2HwQsZH8BfryHmZVIokTUBZ0dhyTnn3HO9Fb3GsBuPsEolkdG5vFAt1ojhB+huuNpDc2rDPRvtUteavS2Ghd3Uj+BkBN9GN01cvSdypQPatK1nY58OatihKk3vjG6T5IL+pmzFJbXUCAwEAATANBgkqhkiG9w0BAQUFAAOBgQBIqd+eowe6y/MhQxfQPmplijTVOhTP2qcatcBc6SBBMevtJkAFvMQrwsDIbo+OAFlAmfbvYjlN7gUacSAG/e7f4XJV04KAFY2aG41AVsw9q0nZghudehXB15I3oBEsyA89hvREd5/eOPdDDQ8Ma7X7owfq/B5gHEPAIi/dAK0i+A=="));
//                        signatures[i] = fixed;
//                    }
//                }
//                super.afterHookedMethod(param);
//            }
//        });

//        final Class clazz = Class.forName("android.content.pm.PackageManager", false, loadPackageParam.classLoader);

        final Class clazz = Class.forName("com.meituan.mmp.lib.web.b", false, loadPackageParam.classLoader);
        Log.e("HOOKED: ", clazz.toString());

        for (Method method : clazz.getDeclaredMethods()) {
            XposedBridge.hookMethod(method, new XC_MethodHook() {

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    Log.e("HOOKED", "param  " + param.method.getDeclaringClass().getCanonicalName() + "#" + param.method.getName() + " -  " + Arrays.toString(param.args));
                    super.afterHookedMethod(param);
                }
            });
        }
        final Class clazz2 = Class.forName("com.meituan.mmp.lib.api.web.MTWebViewModule", false, loadPackageParam.classLoader);
        Log.e("HOOKED: ", clazz2.toString());

        for (Method method : clazz2.getDeclaredMethods()) {
            XposedBridge.hookMethod(method, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    Log.e("HOOKED", "param  " + param.method.getDeclaringClass().getCanonicalName() + "#" + param.method.getName() + " -  " + Arrays.toString(param.args));
                    super.afterHookedMethod(param);
                }
            });
        }

        final Class clazz3 = Class.forName("com.sankuai.titans.protocol.jsbridge.impl.KNBInterface", false, loadPackageParam.classLoader);
        Log.e("HOOKED: ", clazz3.toString());
        for (Method method : clazz3.getDeclaredMethods()) {
            XposedBridge.hookMethod(method, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    Log.e("HOOKED", "param  " + param.method.getDeclaringClass().getCanonicalName() + "#" + param.method.getName() + " -  " + Arrays.toString(param.args));
                    super.afterHookedMethod(param);
                }
            });
        }


        final Class clazz9 = Class.forName("com.sankuai.titans.proxy.shark.SharkManager", false, loadPackageParam.classLoader);
        Log.e("HOOKED: ", "SharkManager " + clazz9.toString());
        for (Method method : clazz9.getDeclaredMethods()) {
            XposedBridge.hookMethod(method, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    Log.e("HOOKED", "SharkManager " + "param  " + param.method.getDeclaringClass().getCanonicalName() + "#" + param.method.getName() + " -  " + Arrays.toString(param.args));
                }
            });
        }

        final Class clazz10 = Class.forName("com.dianping.titans.client.TitansWebViewClient", false, loadPackageParam.classLoader);
        Log.e("HOOKED: ", "SharkManager " + clazz10.toString());
        for (Method method : clazz10.getDeclaredMethods()) {
            XposedBridge.hookMethod(method, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    Log.e("HOOKED", "SharkManager " + "param  " + param.method.getDeclaringClass().getCanonicalName() + "#" + param.method.getName() + " -  " + Arrays.toString(param.args));
                }
            });
        }

        final Class clazz11 = Class.forName("com.meituan.mmp.lib.engine.AppPage", false, loadPackageParam.classLoader);
        Log.e("HOOKED: ", "AppPage " + clazz11.toString());
        for (Method method : clazz11.getDeclaredMethods()) {
            XposedBridge.hookMethod(method, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    Log.e("HOOKED", "AppPage " + "param  " + param.method.getDeclaringClass().getCanonicalName() + "#" + param.method.getName() + " -  " + Arrays.toString(param.args));
                }
            });
        }


        final Class clazz5 = Class.forName("com.meituan.mmp.lib.web.d", false, loadPackageParam.classLoader);
        Log.e("HOOKED: ", clazz5.toString());

        for (Method method : clazz5.getDeclaredMethods()) {
            XposedBridge.hookMethod(method, new XC_MethodHook() {

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    Log.e("HOOKED", "param  " + param.method.getDeclaringClass().getCanonicalName() + "#" + param.method.getName() + " -  " + Arrays.toString(param.args));
                    super.afterHookedMethod(param);
                }
            });
        }




        final Class clazz6 = Class.forName("com.dianping.jscore.JSExecutor", false, loadPackageParam.classLoader);
        Log.e("HOOKED: ", "------ " + clazz6.toString());
        for (Method method : clazz6.getDeclaredMethods()) {
            XposedBridge.hookMethod(method, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(final MethodHookParam param) throws Throwable {
                    //addJavaScriptInterface
                    if (param.method.getName().contains("addJavaScriptInterface")) {
                        Log.e("HOOKED", "addJavaScriptInterface ------------- original -- ");
                        final Object original = param.args[1];

                        Class inf = original.getClass();

                        for (Method m : inf.getDeclaredMethods()) {
                            XposedBridge.hookMethod(m, new XC_MethodHook() {
                                @Override
                                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                    Log.e("HOOKED", "addJavaScriptInterface ------------- original -- ");
                                    Log.e("HOOKED", "param  " + param.method.getDeclaringClass().getCanonicalName() + "#" + param.method.getName() + " -  " + Arrays.toString(param.args));
                                }
                            });
                        }


//                        try {
//                            Class clazz = Class.class;
//                            Field accessFlags = clazz.getDeclaredField("accessFlags");
//                            accessFlags.setAccessible(true);
//
//                            //com.dianping.jscore.JavaScriptInterface
//                            Class jsinterface = Class.forName("com.dianping.jscore.JavaScriptInterface", false, loadPackageParam.classLoader);
//
//                            Object accessFlagsOriginal = accessFlags.get(jsinterface);
//                            accessFlags.set(jsinterface, 537396737);
//
//                            Log.e("HOOKED", "param original -- " + original);
//                            Object proxy = Proxy.newProxyInstance(loadPackageParam.classLoader, new Class[]{jsinterface}, new InvocationHandler() {
//                                @Override
//                                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//                                    Log.e("HOOKED", "param original 111 -- " + method.getDeclaringClass().getCanonicalName() + "#" + method.getName() + " -  " + Arrays.toString(args));
//                                    Object invoke = method.invoke(original, args);
//                                    Log.e("HOOKED", "param original 222 -- ");
//                                    return invoke;
//                                }
//                            });
//                            accessFlags.set(jsinterface, accessFlagsOriginal);
//                            param.args[1] = proxy;
//                        } catch (Throwable e) {
//                            Log.e("HOOKED", "param original err -- " );
//                            e.printStackTrace();
//                        }
                    }
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    Log.e("HOOKED", "param  " + param.method.getDeclaringClass().getCanonicalName() + "#" + param.method.getName() + " -  " + Arrays.toString(param.args));
                    super.afterHookedMethod(param);
                }
            });
        }


        //android.webkit.WebView.addJavascriptInterface
        final Class clazz7 = Class.forName("android.webkit.WebView", false, loadPackageParam.classLoader);
        Log.e("HOOKED: ", "------777 " + clazz7.toString());
        XposedHelpers.findAndHookMethod(clazz7, "addJavascriptInterface", Object.class, String.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Log.e("HOOKED: ", "------jsClass  " + Arrays.toString(param.args));
                Class jsClass = (Class) param.args[0].getClass();
                Log.e("HOOKED: ", "------jsClass  " + jsClass);
                for (Method method : jsClass.getDeclaredMethods()) {
                    XposedBridge.hookMethod(method, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            Log.e("HOOKED", "param  " + param.method.getDeclaringClass().getCanonicalName() + "#" + param.method.getName() + " -  " + Arrays.toString(param.args));
                        }
                    });
                }

            }
        });


        //okhttp3.x.a#a()okhttp3.x.a#a()
        final Class clazz8 = Class.forName("okhttp3.x$a", false, loadPackageParam.classLoader);
        Log.e("HOOKED: ", clazz8.toString());
        XposedHelpers.findAndHookMethod(clazz8, "a", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                //okhttp3.x.a#e
                Object build = param.thisObject;
                Field indecenterField = build.getClass().getDeclaredField("e");
                indecenterField.setAccessible(true);
                Object indecenters = indecenterField.get(build);

                Class loggerClass = XposedHelpers.findClass("okhttp3.logging.HttpLoggingInterceptor", loadPackageParam.classLoader);
                Object logger = loggerClass.newInstance();

                Field levelField = loggerClass.getDeclaredField("c");
                levelField.setAccessible(true);
                Class levelClass = XposedHelpers.findClass("okhttp3.logging.HttpLoggingInterceptor.Level", loadPackageParam.classLoader);
                Object level = Enum.valueOf(levelClass, "BODY");
                levelField.set(logger, level);
                Log.e("HOOKED", "indecenter111  " + indecenters.getClass() + "  " + level + "  " + build + "  " + logger);
                ((List) indecenters).add(logger);
                Log.e("HOOKED", "indecenter222  " + indecenters + "  " + level + "  " + build + "  " + logger);

            }
        });
    }

    static class Log {
        static File log;
        static FileWriter fileWriter;

        static {
            log = new File("mnt/sdcard/meituanyouxuanlog.txt");
            if (!log.exists()) {
                try {
                    log.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (log.exists()) {
                try {
                    fileWriter = new FileWriter(log, true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        static void e(String tag, String msg) {
            android.util.Log.e(tag, msg);
            if (fileWriter != null) {
                try {
                    fileWriter.write(tag);
                    fileWriter.write(msg);
                    fileWriter.write("\n");
                    fileWriter.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}


