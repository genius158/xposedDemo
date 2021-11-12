package com.yan.xposeddemo;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class HookRoot implements IXposedHookLoadPackage {
    final String[] strArr = {"/sbin/", "/system/bin/", "/system/xbin/", "/data/local/xbin/", "/data/local/bin/", "/system/sd/xbin/", "/system/bin/failsafe/", "/data/local/"};

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) {
        Class fileClazz = XposedHelpers.findClass("java.io.File", lpparam.classLoader);

        XposedHelpers.findAndHookConstructor(fileClazz, String.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                if (param.args == null || param.args.length <= 0) return;
                String name = (String) param.args[0];
                for (String n : strArr) {
                    if (name.startsWith(n)) {
                        param.args[0] = "test";
                    }
                }
            }
        });
    }
}
