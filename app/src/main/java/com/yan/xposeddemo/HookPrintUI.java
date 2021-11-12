package com.yan.xposeddemo;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import com.yan.xposeddemo.extra.ExtraLoad;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

import dalvik.system.DexClassLoader;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import me.weishu.reflection.Reflection;

public class HookPrintUI implements IXposedHookLoadPackage {
    private static String[] WHITE_PACKAGE_NAMES = new String[]{
//            "com.tencent.mm"
            "com.smile.gifmaker",
            "com.component.x5webviewdemo",
            "com.example.jetnews",
            "com.yan.d6webviewtest"
    };

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) {
        String packageName = lpparam.packageName;

        Log.e("handleLoadPackage11", packageName);
        XposedBridge.log("HOOKED" + "handleLoadPackage11 " + packageName);

        if (packageName == null) return;
        for (String tpm : WHITE_PACKAGE_NAMES) {
            if (packageName.startsWith(tpm)) {
                Log.e("handleLoadPackage22", packageName);
                XposedBridge.log("HOOKED" + "handleLoadPackage22 " + packageName);

                try {
                    hookInner(lpparam);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void hookInner(final XC_LoadPackage.LoadPackageParam lpparam) throws ClassNotFoundException {
        Class classApp = Class.forName("android.app.Application");
        XposedBridge.log("HOOKED" + "attachBaseContext attachBaseContext " + classApp);
        XposedHelpers.findAndHookMethod(classApp, "attach", Context.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                if (param == null || param.args == null || param.args.length <= 0) return;
                XposedBridge.log("HOOKED" + "attachBaseContext attachBaseContext " + "  " + param.getResult() + Arrays.toString(param.args));
                if (param.args[0] instanceof Context) {
                    final Context ctx = (Context) param.args[0];
                    Reflection.unseal(ctx);
                    ExtraLoad.init(ctx);

                    try {
                        dynamicHook3(ctx);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }

                    try {
                        dynamicHook2(ctx);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }

                    try {
                        dynamicHook4(ctx);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }

                    try {
                        hookView(ctx);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }

//                    soPrinter(ctx);
//                    dexClassHook(ctx);
                }
            }
        });

        ///Users/didi/Library/Android/sdk/platforms/android-28/android.jar!/android/app/Activity.class
        final Class clazz = Class.forName("android.app.Activity", false, lpparam.classLoader);
        Log.e("HOOKED", clazz.toString());
        XposedBridge.log("HOOKED" + clazz.toString());
        XposedBridge.hookAllConstructors(DexClassLoader.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Log.e("HOOKED", "hookAllConstructors" + param.thisObject + "  " + param.getResult() + " " + Arrays.toString(param.args));
                hookXwalkcore((ClassLoader) param.thisObject);
            }
        });

        XposedHelpers.findAndHookMethod(clazz, "onResume", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                if (param.thisObject instanceof Activity) {
                    final Activity activity = (Activity) param.thisObject;
                    if (activity.getWindow() == null
                            || activity.getWindow().getDecorView() == null) return;
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            UIPrinter.printUI(activity);
                        }
                    });
                }
            }
        });

    }

    private void hookView(Context ctx) {
//        for (final Method method : android.view.View.class.getDeclaredMethods()) {
//            XposedBridge.hookMethod(method, new XC_MethodHook() {
//                        @Override
//                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                            Log.e("HOOKED", "android.view.View " + method.getName() + "  " + param.getResult() + "  " + Arrays.toString(param.args));
//                        }
//                    }
//            );
//        }
//        for (final Method method : android.graphics.Canvas.class.getDeclaredMethods()) {
//            XposedBridge.hookMethod(method, new XC_MethodHook() {
//                        @Override
//                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                            Log.e("HOOKED", " android.graphics.Canvas " + method.getName() + "  " + param.getResult() + "  " + Arrays.toString(param.args));
//                        }
//                    }
//            );
//        }
        try {
            Class kwsdk = Class.forName("com.kuaishou.webkit.extension.KwSdk", false, ctx.getClassLoader());

            for (final Method method : kwsdk.getDeclaredMethods()) {
                XposedBridge.hookMethod(method, new XC_MethodHook() {
                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                Log.e("HOOKED", "com.kuaishou.webkit.extension.KwSdk " + method.getName() + "  " + param.getResult() + "  " + Arrays.toString(param.args));
                            }
                        }
                );
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        try {
            Class kwsdk = Class.forName("com.eclipsesource.v8.V8", false, ctx.getClassLoader());
            Log.e("HOOKED", "com.eclipsesource.v8.V8 " + kwsdk);
            for (final Method method : kwsdk.getDeclaredMethods()) {
                XposedBridge.hookMethod(method, new XC_MethodHook() {
                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                Log.e("HOOKED", "com.eclipsesource.v8.V8 " + method.getName() + "  " + param.getResult() + "  " + Arrays.toString(param.args));
                            }
                        }
                );
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            Class kwsdk = Class.forName("com.kuaishou.webkit.extension.jscore.JsV8Object", false, ctx.getClassLoader());
            XposedBridge.hookAllConstructors(kwsdk, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    Log.e("HOOKED", "JsV8Object " + param.method.getName() + "  " + param.getResult() + "  " + Arrays.toString(param.args));

                    if (param.method.getName().contains("com.kuaishou.webkit.extension.jscore.JsV8Object")){
//                        ExtraLoad.logStack("Stack", Log.ERROR);
                    }
                }
            });

            for (final Method method : kwsdk.getDeclaredMethods()) {
                XposedBridge.hookMethod(method, new XC_MethodHook() {
                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                Log.e("HOOKED", "JsV8Object " + method.getName() + "  " + param.getResult() + "  " + Arrays.toString(param.args));
                            }
                        }
                );
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void dynamicHook4(Context ctx) throws ClassNotFoundException {
        final Class sdky = Class.forName("com.tencent.smtt.sdk.y", false, ctx.getClassLoader());
        for (final Method method : sdky.getDeclaredMethods()) {
            Log.e("HOOKED", "com.tencent.smtt.sdk.y " + method);
            method.setAccessible(true);
            XposedBridge.hookMethod(method, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            Log.e("HOOKED", "hook com.tencent.smtt.sdk.y " + method.getName() + "  " + param.getResult() + "  " + Arrays.toString(param.args));

                            try {
                                hookae(param.thisObject);
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                        }
                    }
            );
        }


        final Class class45 = Class.forName("com.tencent.smtt.sdk.WebView", false, ctx.getClassLoader());
        for (final Method method : class45.getDeclaredMethods()) {
            Log.e("HOOKED", "com.tencent.smtt.sdk.WebView " + method);
            method.setAccessible(true);
            XposedBridge.hookMethod(method, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            Log.e("HOOKED", "hookMethod com.tencent.smtt.sdk.WebView " + method.getName() + "  " + param.getResult() + "  " + Arrays.toString(param.args));
                        }
                    }
            );
        }

    }

    static boolean isHook = false;

    private synchronized void hookae(Object thisObject) throws NoSuchFieldException, IllegalAccessException, ClassNotFoundException {
        if (isHook) return;
        isHook = true;

        Class clazz = thisObject.getClass();
        Field fileda = clazz.getDeclaredField("a");
        fileda.setAccessible(true);
        Object dexLoader = fileda.get(thisObject);
        Class dexLoaderClass = dexLoader.getClass();
        Field mClassLoader = dexLoaderClass.getDeclaredField("mClassLoader");
        mClassLoader.setAccessible(true);
        ClassLoader classLoader = (ClassLoader) mClassLoader.get(dexLoader);

        final Class ae = Class.forName("android.webview.chromium.ae", false, classLoader);
        XposedBridge.hookAllConstructors(ae, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Log.e("HOOKED", "android.webview.chromium.ae init " + param.method.getName() + "  " + param.getResult() + "  " + Arrays.toString(param.args));
            }
        });

        for (final Method method : ae.getDeclaredMethods()) {
            Log.e("HOOKED", "android.webview.chromium.ae " + method);
            method.setAccessible(true);
            XposedBridge.hookMethod(method, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            Log.e("HOOKED", "android.webview.chromium.ae " + method.getName() + "  " + param.getResult() + "  " + Arrays.toString(param.args));
                        }
                    }
            );
        }

        Class webCoreProxy = Class.forName("com.tencent.tbs.tbsshell.WebCoreProxy", false, classLoader);
        for (final Method method : webCoreProxy.getDeclaredMethods()) {
            Log.e("HOOKED", "webCoreProxy " + method);
            method.setAccessible(true);
            XposedBridge.hookMethod(method, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            Log.e("HOOKED", "Method webCoreProxy " + method.getName() + "  " + param.getResult() + "  " + Arrays.toString(param.args));
                        }
                    }
            );
        }


        Class webViewFactory = Class.forName("com.tencent.tbs.core.webkit.WebViewFactory", false, classLoader);
        for (final Method method : webViewFactory.getDeclaredMethods()) {
            Log.e("HOOKED", "webViewFactory " + method);
            method.setAccessible(true);
            XposedBridge.hookMethod(method, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            Log.e("HOOKED", "Method webViewFactory " + method.getName() + "  " + param.getResult() + "  " + Arrays.toString(param.args));
                        }
                    }
            );
        }


        final Class AwContents = Class.forName("org.chromium.android_webview.AwContents", false, classLoader);
        for (final Method method : AwContents.getDeclaredMethods()) {
            Log.e("HOOKED", "AwContents " + method);
            method.setAccessible(true);
            XposedBridge.hookMethod(method, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            Log.e("HOOKED", "hook AwContents " + method.getName() + "  " + param.getResult() + "  " + Arrays.toString(param.args));
                        }
                    }
            );
        }


        final Class AwGLFunctor = Class.forName("org.chromium.android_webview.gfx.AwGLFunctor", false, classLoader);
        for (final Method method : AwGLFunctor.getDeclaredMethods()) {
            Log.e("HOOKED", "AwGLFunctor " + method);
            method.setAccessible(true);
            XposedBridge.hookMethod(method, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            Log.e("HOOKED", "hook AwGLFunctor " + method.getName() + "  " + param.getResult() + "  " + Arrays.toString(param.args));
                        }
                    }
            );
        }


        final Class TencentGraphicsUtils = Class.forName("android.webview.chromium.tencent.TencentGraphicsUtils", false, classLoader);
        for (final Method method : TencentGraphicsUtils.getDeclaredMethods()) {
            Log.e("HOOKED", "TencentGraphicsUtils " + method);
            method.setAccessible(true);
            XposedBridge.hookMethod(method, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            Log.e("HOOKED", "hook TencentGraphicsUtils " + method.getName() + "  " + param.getResult() + "  " + Arrays.toString(param.args));
                        }
                    }
            );
        }


        final Class SMTTAdaptation = Class.forName("com.tencent.smtt.os.SMTTAdaptation", false, classLoader);
        for (final Method method : SMTTAdaptation.getDeclaredMethods()) {
            Log.e("HOOKED", "SMTTAdaptation " + method);
            method.setAccessible(true);
            XposedBridge.hookMethod(method, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            Log.e("HOOKED", "hook SMTTAdaptation " + method.getName() + "  " + param.getResult() + "  " + Arrays.toString(param.args));
                        }
                    }
            );
        }

    }


//    private void dexClassHook(Context ctx) {
//        XposedBridge.hookAllConstructors(DexClassLoader.class, new XC_MethodHook() {
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                Log.e("hook", "DexClassLoader " + param.thisObject + "   " + Arrays.toString(param.args) + "  ");
////                try {
////                    final Class ae = Class.forName("android.webview.chromium.ae", false, (ClassLoader) param.thisObject);
////                    for (final Method method : ae.getDeclaredMethods()) {
////                        Log.e("HOOKED", "android.webview.chromium.ae " + method);
////                        method.setAccessible(true);
////                        XposedBridge.hookMethod(method, new XC_MethodHook() {
////                                    @Override
////                                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
////                                        Log.e("HOOKED", "android.webview.chromium.ae " + method.getName() + "  " + Arrays.toString(param.args));
////                                    }
////                                }
////                        );
////                    }
////                } catch (Throwable e) {
////                    e.printStackTrace();
////                }
//            }
//        });
//
//        for (Method method : ClassLoader.class.getDeclaredMethods()) {
//            XposedBridge.hookMethod(method, new XC_MethodHook() {
//                @Override
//                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                    Log.e("hook", "ClassLoader " + Arrays.toString(param.args) + "  " + param.thisObject + "   ");
//                }
//            });
//        }
//    }

    private void hookXwalkcore(ClassLoader thisObject) {
        printAwContents(thisObject);
    }

    private void printAwContents(ClassLoader classLoader) {
        try {
            if (!classLoader.toString().contains("xwalk")) return;
            Class clazz = Class.forName("com.tencent.xweb.Awcontents", false, classLoader);
            for (Method method : clazz.getDeclaredMethods()) {
                Log.e("HOOKED", "printAwContents  " + method);

            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void dynamicHook3(final Context context) throws ClassNotFoundException {
        final Class class45 = Class.forName("com.xiaoju.webkit.WebView", false, context.getClassLoader());
        for (final Method method : class45.getDeclaredMethods()) {
            Log.e("HOOKED", "com.xiaoju.webkit.WebView " + method);
            method.setAccessible(true);
            XposedBridge.hookMethod(method, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            Log.e("HOOKED", "com.xiaoju.webkit.WebView " + method.getName() + "  " + param.getResult() + "  " + Arrays.toString(param.args));

                            if ("ensureProviderCreated".equals(method.getName())) {
                                hookChromium(param.thisObject, class45);
                            }
                        }
                    }
            );
        }
    }

    private void dynamicHook2(final Context context) throws ClassNotFoundException {
        final Class class45 = Class.forName("com.kuaishou.webkit.WebView", false, context.getClassLoader());
        for (final Method method : class45.getDeclaredMethods()) {
            Log.e("HOOKED", "com.kuaishou.webkit.WebView " + method);
            method.setAccessible(true);
            XposedBridge.hookMethod(method, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            Log.e("HOOKED", "com.kuaishou.webkit.WebView " + method.getName() + "  " + param.getResult() + "  " + Arrays.toString(param.args));

                            if ("ensureProviderCreated".equals(method.getName())) {
                                hookChromium(param.thisObject, class45);
                            }
                        }
                    }
            );
        }

        final Class WebViewFactory = Class.forName("com.kuaishou.webkit.WebViewFactory", false, context.getClassLoader());
        for (final Method method : WebViewFactory.getDeclaredMethods()) {
            Log.e("HOOKED", "com.kuaishou.webkit.WebViewFactory " + method);
            method.setAccessible(true);
            XposedBridge.hookMethod(method, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            Log.e("HOOKED", "com.kuaishou.webkit.WebViewFactory " + method.getName() + "  " + param.getResult() + "  " + Arrays.toString(param.args));
                        }
                    }
            );
        }


        //com.kuaishou.webkit.internal.KsWebViewUtils#useSysWebView
        final Class class1 = Class.forName("com.kuaishou.webkit.internal.KsWebViewUtils", false, context.getClassLoader());
        XposedHelpers.findAndHookMethod(class1, "useSysWebView", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Log.e("HOOKED", "KsWebViewUtils useSysWebView " + param.getResult());
            }
        });
    }

    int tag = 0x324;

    private void hookChromium(Object thisObject, Class class45) throws NoSuchFieldException, IllegalAccessException, ClassNotFoundException {
        if (thisObject instanceof View) {

            View v = (View) thisObject;
            if (v.getTag() != null) return;
            v.setTag(true);
            Field mProvider = class45.getDeclaredField("mProvider");
            mProvider.setAccessible(true);
            Object provider = mProvider.get(thisObject);
            if (provider == null) {
                v.setTag(null);
                return;
            }

            ClassLoader classLoader = provider.getClass().getClassLoader();
            Log.e("HOOKED", "classLoader  " + classLoader);

            Class classDeviceFormFactor = Class.forName("c.c.b.a.m1_123_p", false, classLoader);
            for (final Method method : classDeviceFormFactor.getDeclaredMethods()) {
                Log.e("HOOKED", "c.c.b.a.m1_123_p " + method);
                method.setAccessible(true);
                XposedBridge.hookMethod(method, new XC_MethodHook() {
                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                Log.e("HOOKED", "c.c.b.a.m1_123_p " + method.getName() + "  " + param.getResult() + "  " + Arrays.toString(param.args));
                            }
                        }
                );
            }
        }
    }

    private void dynamicHook(final Context context) throws ClassNotFoundException, NoSuchMethodException {
        final Class class7 = Class.forName("com.tencent.mm.plugin.appbrand.jsapi.n", false, context.getClassLoader());
        Log.e("HOOKED", "PrintUI jsapi " + class7);
        for (final Method method : class7.getDeclaredMethods()) {
            Log.e("HOOKED", "PrintUI jsapi " + method);
            method.setAccessible(true);
            XposedBridge.hookMethod(method, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            Log.e("HOOKED", "jsChannel jsapi " + method.getName() + "  " + param.getResult() + "  " + Arrays.toString(param.args));
                        }
                    }
            );
        }

        hookLog(context);

        ////com.tencent.mm.plugin.appbrand.performance.AppBrandPerformanceManager.a(com.tencent.luggage.sdk.e.d, int, java.lang.String)
        final Class clazz9 = Class.forName("com.tencent.mm.plugin.appbrand.performance.AppBrandPerformanceManager", false, context.getClassLoader());
        final Class p1Class = Class.forName("com.tencent.luggage.sdk.e.d", false, context.getClassLoader());
        XposedHelpers.findAndHookMethod(clazz9, "a", p1Class, int.class, String.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Log.e("HOOKED", "jsChannel printParam " + Arrays.toString(param.args));
            }
        });

        //com.tencent.mm.plugin.appbrand.ui.u.aZ
        final Class clazz11 = Class.forName("com.tencent.mm.plugin.appbrand.appstorage.v", false, context.getClassLoader());
        XposedHelpers.findAndHookMethod(clazz11, "c", int.class, String.class, String.class, String.class, new XC_MethodHook() {

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Log.e("HOOKED", "jsChannel com.tencent.mm.plugin.appbrand.appstorage.v#c " + Arrays.toString(param.args));
                Log.e("HOOKED", "jsChannel com.tencent.mm.plugin.appbrand.appstorage.v#c " + param.getResult());
            }
        });


        //com.tencent.luggage.sdk.b.a.c.a$a
        final Class clazz18 = Class.forName("com.tencent.luggage.sdk.b.a.c.a$a", false, context.getClassLoader());
        Log.e("HOOKED", "jsChannel a$a nativeInvokeHandler  clazz18 " + clazz18);
        XposedHelpers.findAndHookMethod(clazz18, "nativeInvokeHandler", String.class, String.class, int.class, boolean.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Log.e("HOOKED", "jsChannel a$a nativeInvokeHandler " + Arrays.toString(param.args));
            }
        });

        //com.tencent.mm.plugin.appbrand.ui.u.aZ
        final Class clazz10 = Class.forName("com.tencent.mm.plugin.appbrand.ui.u", false, context.getClassLoader());
        XposedHelpers.findAndHookMethod(clazz10, "aZ", int.class, String.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Log.e("HOOKED", "jsChannel aZaZaZ " + Arrays.toString(param.args));
            }
        });

        //com.tencent.mm.appbrand.commonjni.AppBrandCommonBindingJni.nativeInvokeHandler(String str, String str2, int i2, boolean z)
        final Class clazz5 = Class.forName("com.tencent.mm.appbrand.commonjni.AppBrandCommonBindingJni", false, context.getClassLoader());

        XposedHelpers.findAndHookMethod(clazz5, "nativeInvokeHandler", String.class, String.class, int.class, boolean.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Log.e("HOOKED", "jsChannel nativeInvokeHandler " + Arrays.toString(param.args));
            }
        });


        //com.tencent.mm.plugin.appbrand.appstorage.g.g(int, java.lang.String, int)
        final Class clazz15 = Class.forName("com.tencent.mm.plugin.appbrand.appstorage.g", false, context.getClassLoader());
        for (final Method method : clazz15.getDeclaredMethods()) {
            XposedBridge.hookMethod(method, new XC_MethodHook() {

                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            Log.e("HOOKED", "PrintUI_MMKV appstorage.g " + method.getName() + "  " + Arrays.toString(param.args) + " res: " + param.getResult());

                        }
                    }
            );

        }

        //com.tencent.mm.plugin.appbrand.appstorage.g.g(int, java.lang.String, int)
//        webViewChange2System(context);
        //
//        final Class clazz14 = Class.forName("com.eclipsesource.mmv8.V8", false, context.getClassLoader());
//        for (final Method method : clazz14.getDeclaredMethods()) {
//            XposedBridge.hookMethod(method, new XC_MethodHook() {
//                @Override
//                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                    Log.e("HOOKED", "V8V8 " + method.getName() + "   " + Arrays.toString(param.args));
//                }
//            });
//        }


        final Class clazz12 = Class.forName("com.tencent.mmkv.MMKV", false, context.getClassLoader());
        Log.e("HOOKED", "jsChannel clazz12 " + clazz12);
        XposedHelpers.findAndHookMethod(clazz12, "putString", String.class, String.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Log.e("HOOKED", "PrintUI_MMKV putString " + Arrays.toString(param.args));
            }
        });

        Log.e("HOOKED", "jsChannel clazz12 " + clazz12);
        XposedHelpers.findAndHookMethod(clazz12, "getString", String.class, String.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Log.e("HOOKED", "PrintUI_MMKV " + Arrays.toString(param.args));
                Log.e("HOOKED", "PrintUI_MMKV " + param.getResult());
            }
        });

        //com.tencent.mm.plugin.appbrand.jsapi.f#a(java.lang.String, java.lang.String, int, boolean, com.tencent.mm.plugin.appbrand.n.o)
        final Class clazz20 = Class.forName("com.tencent.mm.plugin.appbrand.jsapi.f", false, context.getClassLoader());
        Log.e("HOOKED", "jsChannel clazz20 " + clazz20);
        Class paramO = Class.forName("com.tencent.mm.plugin.appbrand.n.o", false, context.getClassLoader());
        XposedHelpers.findAndHookMethod(clazz20, "a", String.class, String.class, int.class, boolean.class, paramO, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Log.e("HOOKED", "jsapi.f" + Arrays.toString(param.args) + "  " + param.getResult());
            }
        });

        final Class clazz4 = Class.forName("com.tencent.mm.sdk.platformtools.MultiProcessMMKV", false, context.getClassLoader());
        Log.e("HOOKED", "jsChannel clazz12 " + clazz4);
        XposedHelpers.findAndHookMethod(clazz4, "getString", String.class, String.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Log.e("HOOKED", "PrintUI_MultiProcessMMKV " + Arrays.toString(param.args) + "  " + param.getResult());
            }
        });


        //com.tencent.mm.vfs.v#fP
        final Class clazz44 = Class.forName("com.tencent.mm.vfs.v", false, context.getClassLoader());
        Log.e("HOOKED", "jsChannel clazz12 " + clazz44);
        XposedHelpers.findAndHookMethod(clazz44, "fP", String.class, boolean.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Log.e("HOOKED", "com.tencent.mm.vfs.v#fP " + Arrays.toString(param.args) + "  " + param.getResultOrThrowable());
            }
        });

        //com.tencent.xweb.x5.sdk.d#setWebContentsDebuggingEnabled
        final Class clazz24 = Class.forName("com.tencent.xweb.x5.sdk.d", false, context.getClassLoader());
        Log.e("HOOKED", "jsChannel clazz12 " + clazz24);
        XposedHelpers.findAndHookMethod(clazz24, "setWebContentsDebuggingEnabled", boolean.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Log.e("HOOKED", "setWebContentsDebuggingEnabled " + Arrays.toString(param.args) + "  " + param.getResult());
            }
        });


        for (final Method method : clazz24.getDeclaredMethods()) {
            XposedBridge.hookMethod(method, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
                    Log.e("HOOKED", "com.tencent.xweb.x5.sdk.d " + method.getName() + "  " + Arrays.toString(param.args) + "  " + param.getResult());
                    if ("a".equals(method.getName())) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Class class30 = Class.forName("com.tencent.smtt.sdk.WebView", false, context.getClassLoader());
                                    XposedHelpers.callStaticMethod(class30, "setWebContentsDebuggingEnabled", true);

                                } catch (ClassNotFoundException e) {
                                    e.printStackTrace();
                                }


                                XposedHelpers.callStaticMethod(clazz24, "setWebContentsDebuggingEnabled", true);
                                Log.e("HOOKED", "setWebContentsDebuggingEnabled 111 " + Arrays.toString(param.args) + "  " + param.getResult());
                            }
                        });
                    }
                }
            });
        }

        //com.tencent.xweb.WebView
        final Class clazz30 = Class.forName("com.tencent.xweb.WebView", false, context.getClassLoader());

        XposedBridge.hookAllConstructors(clazz30, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
            }
        });
        //com.tencent.xweb.WebView
        Log.e("HOOKED", "jsChannel clazz30 " + clazz30);
        for (final Method method : clazz30.getDeclaredMethods()) {
            XposedBridge.hookMethod(method, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    if ("onShow".equals(method.getName())) {
                        XposedHelpers.callStaticMethod(clazz30, "setWebContentsDebuggingEnabled", true);
                    }

                    Log.e("HOOKED", "WebView " + param.method.getName() + "  " + Arrays.toString(param.args) + "  " + param.getResult());
                    LogTxt.e("WebView ", param.method.getName() + "  " + Arrays.toString(param.args) + "  " + param.getResult());
                }
            });
        }

    }

    /**
     * webview 转换到 系统的
     *
     * @param context
     * @throws ClassNotFoundException
     */
    private void webViewChange2System(final Context context) throws ClassNotFoundException {
        final Class clazz55 = Class.forName("com.tencent.xweb.x", false, context.getClassLoader());
        for (final Method method : clazz55.getDeclaredMethods()) {
            XposedBridge.hookMethod(method, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            Log.e("HOOKED", "com.tencent.xweb.x " + method.getName() + "  " + Arrays.toString(param.args) + " res: " + param.getResult());
                            if ("cBU".equals(method.getName())
                                    || "fZ".equals(method.getName())
                            ) {
                                //com.tencent.xweb.WebView.c#WV_KIND_SYS
                                Class clazzC = Class.forName("com.tencent.xweb.WebView$c", false, context.getClassLoader());
                                Object level = Enum.valueOf(clazzC, "WV_KIND_SYS");
                                param.setResult(level);
                            }
                        }
                    }
            );
        }
    }

//    private void soPrinter(Context context) {
//        try {
//            Class clazz64 = Class.forName("dalvik.system.DexPathList", false, context.getClassLoader());
//
//            final Field nativeLibraryPathElementsField = clazz64.getDeclaredField("nativeLibraryPathElements");
//            nativeLibraryPathElementsField.setAccessible(true);
//
//            Log.e("HOOKED", "DexPathList " + clazz64);
//            XposedHelpers.findAndHookMethod(clazz64, "addNativePath", Collection.class, new XC_MethodHook() {
//                @Override
//                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                    Log.e("HOOKED", "DexPathList " + param.method.getName() + "  " + Arrays.toString(param.args) + "  " + param.getResult());
//
//                    Object obj = nativeLibraryPathElementsField.get(param.thisObject);
//                    Log.e("HOOKED", "DexPathList " + Arrays.toString((Object[]) obj));
//                }
//            });
//
//        } catch (Throwable e) {
//            e.printStackTrace();
//        }
//
//
//    }

    static class LogTxt {
        static File log;
        static FileWriter fileWriter;

        static {
            log = new File("mnt/sdcard/WechatWebView.txt");
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


    private void hookLog(Context context) throws ClassNotFoundException {
        final Class class7 = Class.forName("com.tencent.mm.sdk.platformtools.Log", false, context.getClassLoader());

        // public static void setLevel(int i2, boolean z) {

        Log.e("HOOKED", "PrintUI target hookLog setLevel   " + class7);
        XposedHelpers.findAndHookMethod(class7, "setLevel", int.class, boolean.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Log.e("HOOKED", "hookLog setLevel");
                param.args[0] = 0;
            }
        });

        try {
            Log.e("HOOKED", "hookLog 1111111  ");
            Field levelField = class7.getDeclaredField("level");
            levelField.setAccessible(true);
            levelField.setInt(class7, 0);
            Log.e("HOOKED", "hookLog 222222  ");
        } catch (Throwable e) {
            Log.e("HOOKED", "hookLog error");
            e.printStackTrace();
        }
    }

}


