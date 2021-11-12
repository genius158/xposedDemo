package com.yan.xposeddemo;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

public class UIPrinter {
    public static final String TAG = "PrintUI";

    private static LinkedList<View> views = new LinkedList<>();

    private static StringBuilder spaceSB = new StringBuilder();

    public static void printUI(Activity activity) {
        View root = activity.getWindow().getDecorView();
        views.offer(root);
        Log.e(TAG, spaceSB + "------------- Activity:" + activity + " ---------------");

        while (!views.isEmpty()) {
            int size = views.size();
            for (int i = 0; i < size; i++) {
                View view = views.poll();
                if (view == null) continue;

                Class viewClass = view.getClass();
                String viewIdInfo = getName(root.getContext(), view.getId());
                Log.e(TAG, spaceSB + "< " + view);
                Log.e(TAG, spaceSB + "  " + "id: " + viewIdInfo);
                if (view.getParent() != null && view.getParent() instanceof View) {
                    Log.e(TAG, spaceSB + "  " + "parent: " + getName(root.getContext(), ((View) view.getParent()).getId()) + ":" + view.getParent());
                }
                Log.e(TAG, spaceSB + "  " + "size: " + view.getWidth() + "  " + view.getHeight());
                Log.e(TAG, spaceSB + "  " + "xy: " + view.getX() + "  " + view.getY());
                Log.e(TAG, spaceSB + "  " + "translation: " + view.getTranslationX() + "  " + view.getTranslationY());
                if (view instanceof ViewGroup) {
                    ViewGroup parent = (ViewGroup) view;
                    for (int j = 0; j < parent.getChildCount(); j++) {
                        views.add(parent.getChildAt(j));
                    }
                } else if (view instanceof TextView) {
                    Log.e(TAG, spaceSB + "  " + "text: " + ((TextView) view).getText());
                }
                Log.e(TAG, spaceSB + "  " + viewClass.getCanonicalName() + " />");
            }
            spaceSB.append("    ");
        }
        spaceSB.delete(0, spaceSB.length());
    }

    public static String getName(Context context, int id) {
        if (id == -1) return "";
        try {
            Resources res = context.getResources();
//           return res.getResourceEntryName(id);//得到的是 name
            return res.getResourceName(id);//得到的是 包/type/name
        } catch (Throwable e) {
            return "";
        }
    }

    static class Log {
        static File log;
        static FileWriter fileWriter;

        static {
            log = new File("mnt/sdcard/meituanyouxuanlog.txt");
            if (!log.exists()) {
                try {
                    log.createNewFile();
                } catch (Throwable e) {
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

            if (!log.exists() || !log.canWrite()) return;
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
