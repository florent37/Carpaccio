package com.github.florent37.carpaccio;

/**
 * Created by florentchampigny on 24/07/15.
 */
public class CarpaccioLogger {
    public static boolean ENABLE_LOG = false;

    public static void i(String tag, String string) {
        if (ENABLE_LOG) android.util.Log.i(tag, string);
    }

    public static void e(String tag, String string) {
        if (ENABLE_LOG) android.util.Log.e(tag, string);
    }

    public static void e(String tag, String string, Exception e) {
        if (ENABLE_LOG) android.util.Log.e(tag, string, e);
    }

    public static void e(String tag, String string, Throwable t) {
        if (ENABLE_LOG) android.util.Log.e(tag, string, t);
    }

    public static void d(String tag, String string) {
        if (ENABLE_LOG) android.util.Log.d(tag, string);
    }

    public static void v(String tag, String string) {
        if (ENABLE_LOG) android.util.Log.v(tag, string);
    }

    public static void w(String tag, String string) {
        if (ENABLE_LOG) android.util.Log.w(tag, string);
    }

    public static void w(String tag, Exception e) {
        if (ENABLE_LOG) android.util.Log.w(tag, e);
    }
}
