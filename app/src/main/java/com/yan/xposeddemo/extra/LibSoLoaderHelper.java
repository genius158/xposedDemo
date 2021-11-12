package com.yan.xposeddemo.extra;

import android.content.Context;

import java.io.File;

 class LibSoLoaderHelper {

    public static boolean load(Context context) {
        File fileDir = context.getFilesDir();

        File libFile = new File(fileDir, "extra");

        if (!libFile.exists()) return  false;
        File[] files = libFile.listFiles();
        if (files == null || files.length == 0) return  false;

        try {
            LibSoLoader.installNativeLibraryPath(context.getClassLoader(), libFile);
            return  true;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return  false;
    }

}
