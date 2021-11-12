package com.yan.xposeddemo;

import android.util.Log;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;

public class DumpHook implements IXposedHookLoadPackage {
  private static final String TAG = "DumpHook";
  Class Dex;
  Method Dex_getBytes;
  Method getDex;
  String packagename;

  public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
    packagename = "com.wujie.chengxin";
    XposedBridge.log("设定包名：" + packagename);
    if ((!lpparam.packageName.equals(packagename)) || packagename == null) {
      XposedBridge.log("当前程序包名与设定不一致或者包名为空");
      return;
    }
    initRefect();
    XposedBridge.log("目标包名：" + lpparam.packageName);
    String str = "java.lang.ClassLoader";
    String str2 = "loadClass";
    Log.e(TAG, "目标包名：" + lpparam.packageName);

    XposedHelpers.findAndHookMethod(str, lpparam.classLoader, str2, String.class, Boolean.TYPE,
        new XC_MethodHook() {
          protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            super.afterHookedMethod(param);
            Class cls = (Class) param.getResult();
            if (cls == null) {
              //XposedBridge.log("cls == null");
              return;
            }
            String name = cls.getName();
            XposedBridge.log("当前类名：" + name);
            Log.e(TAG, "当前类名：" + name);

            byte[] bArr =
                (byte[]) Dex_getBytes.invoke(getDex.invoke(cls, new Object[0]), new Object[0]);
            if (bArr == null) {
              Log.e(TAG, "数据为空：返回");
              XposedBridge.log("数据为空：返回");
              return;
            }
            Log.e(TAG, "开始写数据");

            XposedBridge.log("开始写数据");
            String dex_path =
                "/data/data/" + packagename + "/" + packagename + "_" + bArr.length + ".dex";
            XposedBridge.log(dex_path);
            File file = new File(dex_path);
            if (file.exists()) return;
            writeByte(bArr, file.getAbsolutePath());
          }
        });
  }

  public void initRefect() {
    try {
      Dex = Class.forName("com.android.dex.Dex");
      Dex_getBytes = Dex.getDeclaredMethod("getBytes", new Class[0]);
      getDex = Class.forName("java.lang.Class").getDeclaredMethod("getDex", new Class[0]);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    }
  }

  public void writeByte(byte[] bArr, String str) {
    try {
      OutputStream outputStream = new FileOutputStream(str);
      outputStream.write(bArr);
      outputStream.close();
    } catch (IOException e) {
      e.printStackTrace();
      Log.e(TAG, "文件写出失败");
      XposedBridge.log("文件写出失败");
    }
  }
}