package com.yan.xposeddemo.extra;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class LibSoLoader {
    private static final String TAG = LibSoLoader.class.getSimpleName();
    private static File lastSoDir = null;


    /**
     * 清除so路径，实际上是设置一个无效的path，用户测试无so库的情况
     *
     * @param classLoader
     */
    public static void clearSoPath(ClassLoader classLoader) {
        try {
            final String testDirNoSo = Environment.getExternalStorageDirectory().getAbsolutePath() + "/xxx/";
            new File(testDirNoSo).mkdirs();
            installNativeLibraryPath(classLoader, testDirNoSo);
        } catch (Throwable throwable) {
            Log.e(TAG, "dq clear path error" + throwable.toString());
            throwable.printStackTrace();
        }
    }

    public static synchronized boolean installNativeLibraryPath(ClassLoader classLoader, String folderPath) throws Throwable {
        return installNativeLibraryPath(classLoader, new File(folderPath));
    }

    public static synchronized boolean installNativeLibraryPath(ClassLoader classLoader, File folder)
            throws Throwable {
        if (classLoader == null || folder == null || !folder.exists()) {
            Log.e(TAG, "classLoader or folder is illegal " + folder);
            return false;
        }
        final int sdkInt = Build.VERSION.SDK_INT;
        final boolean aboveM = (sdkInt == 25 && getPreviousSdkInt() != 0) || sdkInt > 25;
        if (aboveM) {
            try {
                V25.install(classLoader, folder);
            } catch (Throwable throwable) {
                try {
                    V23.install(classLoader, folder);
                } catch (Throwable throwable1) {
                    V14.install(classLoader, folder);
                }
            }
        } else if (sdkInt >= 23) {
            try {
                V23.install(classLoader, folder);
            } catch (Throwable throwable) {
                V14.install(classLoader, folder);
            }
        } else if (sdkInt >= 14) {
            V14.install(classLoader, folder);
        }
        lastSoDir = folder;
        return true;
    }

    private static final class V23 {
        private static void install(ClassLoader classLoader, File folder) throws Throwable {
            Field pathListField = ReflectUtil.findField(classLoader, "pathList");
            Object dexPathList = pathListField.get(classLoader);

            Field nativeLibraryDirectories = ReflectUtil.findField(dexPathList, "nativeLibraryDirectories");
            List<File> libDirs = (List<File>) nativeLibraryDirectories.get(dexPathList);

            //去重
            if (libDirs == null) {
                libDirs = new ArrayList<>(2);
            }
            final Iterator<File> libDirIt = libDirs.iterator();
            while (libDirIt.hasNext()) {
                final File libDir = libDirIt.next();
                if (folder.equals(libDir) || folder.equals(lastSoDir)) {
                    libDirIt.remove();
                    Log.d(TAG, "dq libDirIt.remove() " + folder.getAbsolutePath());
                    break;
                }
            }

            libDirs.add(0, folder);
            Field systemNativeLibraryDirectories =
                    ReflectUtil.findField(dexPathList, "systemNativeLibraryDirectories");
            List<File> systemLibDirs = (List<File>) systemNativeLibraryDirectories.get(dexPathList);

            //判空
            if (systemLibDirs == null) {
                systemLibDirs = new ArrayList<>(2);
            }
            Log.d(TAG, "dq systemLibDirs,size=" + systemLibDirs.size());

            Method makePathElements = ReflectUtil.findMethod(dexPathList, "makePathElements", List.class, File.class, List.class);
            ArrayList<IOException> suppressedExceptions = new ArrayList<>();
            libDirs.addAll(systemLibDirs);

            Object[] elements = (Object[]) makePathElements.invoke(dexPathList, libDirs, null, suppressedExceptions);
            Field nativeLibraryPathElements = ReflectUtil.findField(dexPathList, "nativeLibraryPathElements");
            nativeLibraryPathElements.setAccessible(true);
            nativeLibraryPathElements.set(dexPathList, elements);
        }
    }

    /**
     * 把自定义的native库path插入nativeLibraryDirectories最前面，即使安装包libs目录里面有同名的so，也优先加载指定路径的外部so
     */
    private static final class V25 {
        private static void install(ClassLoader classLoader, File folder) throws Throwable {
            Field pathListField = ReflectUtil.findField(classLoader, "pathList");
            Object dexPathList = pathListField.get(classLoader);
            Field nativeLibraryDirectories = ReflectUtil.findField(dexPathList, "nativeLibraryDirectories");

            List<File> libDirs = (List<File>) nativeLibraryDirectories.get(dexPathList);
            //去重
            if (libDirs == null) {
                libDirs = new ArrayList<>(2);
            }
            final Iterator<File> libDirIt = libDirs.iterator();
            while (libDirIt.hasNext()) {
                final File libDir = libDirIt.next();
                if (folder.equals(libDir) || folder.equals(lastSoDir)) {
                    libDirIt.remove();
                    Log.d(TAG, "dq libDirIt.remove()" + folder.getAbsolutePath());
                    break;
                }
            }

            libDirs.add(0, folder);
            //system/lib
            Field systemNativeLibraryDirectories = ReflectUtil.findField(dexPathList, "systemNativeLibraryDirectories");
            List<File> systemLibDirs = (List<File>) systemNativeLibraryDirectories.get(dexPathList);

            //判空
            if (systemLibDirs == null) {
                systemLibDirs = new ArrayList<>(2);
            }
            Log.d(TAG, "dq systemLibDirs,size=" + systemLibDirs.size());

            Method makePathElements = ReflectUtil.findMethod(dexPathList, "makePathElements", List.class);
            libDirs.addAll(systemLibDirs);

            Object[] elements = (Object[]) makePathElements.invoke(dexPathList, libDirs);
            Field nativeLibraryPathElements = ReflectUtil.findField(dexPathList, "nativeLibraryPathElements");
            nativeLibraryPathElements.setAccessible(true);
            nativeLibraryPathElements.set(dexPathList, elements);
        }
    }


    private static final class V14 {
        private static void install(ClassLoader classLoader, File folder) throws Throwable {
            Field pathListField = ReflectUtil.findField(classLoader, "pathList");
            Object dexPathList = pathListField.get(classLoader);

            ReflectUtil.expandFieldArray(dexPathList, "nativeLibraryDirectories", new File[]{folder});
        }
    }

    /**
     * fuck部分机型删了该成员属性，兼容
     *
     * @return 被厂家删了返回1，否则正常读取
     */
    @TargetApi(Build.VERSION_CODES.M)
    private static int getPreviousSdkInt() {
        try {
            return Build.VERSION.PREVIEW_SDK_INT;
        } catch (Throwable ignore) {
        }
        return 1;
    }

    public static class ReflectUtil {

        public static Field findField(Object instance, String name) throws NoSuchFieldException {
            for (Class<?> clazz = instance.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
                try {
                    Field field = clazz.getDeclaredField(name);
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }
                    return field;
                } catch (NoSuchFieldException e) {
                }
            }

            throw new NoSuchFieldException("Field " + name + " not found in " + instance.getClass());
        }

        public static Field findField(Class<?> originClazz, String name) throws NoSuchFieldException {
            for (Class<?> clazz = originClazz; clazz != null; clazz = clazz.getSuperclass()) {
                try {
                    Field field = clazz.getDeclaredField(name);
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }
                    return field;
                } catch (NoSuchFieldException e) {
                }
            }
            throw new NoSuchFieldException("Field " + name + " not found in " + originClazz);
        }

        public static Method findMethod(Object instance, String name, Class<?>... parameterTypes)
                throws NoSuchMethodException {
            for (Class<?> clazz = instance.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
                try {
                    Method method = clazz.getDeclaredMethod(name, parameterTypes);
                    if (!method.isAccessible()) {
                        method.setAccessible(true);
                    }
                    return method;
                } catch (NoSuchMethodException e) {
                }
            }

            throw new NoSuchMethodException("Method "
                    + name
                    + " with parameters "
                    + Arrays.asList(parameterTypes)
                    + " not found in " + instance.getClass());
        }


        /**
         * 数组替换
         */
        public static void expandFieldArray(Object instance, String fieldName, Object[] extraElements)
                throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
            Field jlrField = findField(instance, fieldName);

            Object[] original = (Object[]) jlrField.get(instance);
            Object[] combined = (Object[]) Array.newInstance(original.getClass().getComponentType(), original.length + extraElements.length);

            // NOTE: changed to copy extraElements first, for patch load first

            System.arraycopy(extraElements, 0, combined, 0, extraElements.length);
            System.arraycopy(original, 0, combined, extraElements.length, original.length);

            jlrField.set(instance, combined);
        }

        public static void reduceFieldArray(Object instance, String fieldName, int reduceSize)
                throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
            if (reduceSize <= 0) {
                return;
            }
            Field jlrField = findField(instance, fieldName);
            Object[] original = (Object[]) jlrField.get(instance);
            int finalLength = original.length - reduceSize;
            if (finalLength <= 0) {
                return;
            }
            Object[] combined = (Object[]) Array.newInstance(original.getClass().getComponentType(), finalLength);
            System.arraycopy(original, reduceSize, combined, 0, finalLength);
            jlrField.set(instance, combined);
        }

        public static Constructor<?> findConstructor(Object instance, Class<?>... parameterTypes)
                throws NoSuchMethodException {
            for (Class<?> clazz = instance.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
                try {
                    Constructor<?> ctor = clazz.getDeclaredConstructor(parameterTypes);
                    if (!ctor.isAccessible()) {
                        ctor.setAccessible(true);
                    }
                    return ctor;
                } catch (NoSuchMethodException e) {
                }
            }
            throw new NoSuchMethodException("Constructor"
                    + " with parameters "
                    + Arrays.asList(parameterTypes)
                    + " not found in " + instance.getClass());
        }

        public static Object getActivityThread(Context context, Class<?> activityThread) {
            try {
                if (activityThread == null) {
                    activityThread = Class.forName("android.app.ActivityThread");
                }
                Method m = activityThread.getMethod("currentActivityThread");
                m.setAccessible(true);
                Object currentActivityThread = m.invoke(null);
                if (currentActivityThread == null && context != null) {
                    Field mLoadedApk = context.getClass().getField("mLoadedApk");
                    mLoadedApk.setAccessible(true);
                    Object apk = mLoadedApk.get(context);
                    Field mActivityThreadField = apk.getClass().getDeclaredField("mActivityThread");
                    mActivityThreadField.setAccessible(true);
                    currentActivityThread = mActivityThreadField.get(apk);
                }
                return currentActivityThread;
            } catch (Throwable ignore) {
                return null;
            }
        }

        public static int getValueOfStaticIntField(Class<?> clazz, String fieldName, int defVal) {
            try {
                final Field field = findField(clazz, fieldName);
                return field.getInt(null);
            } catch (Throwable thr) {
                return defVal;
            }
        }

    }

}