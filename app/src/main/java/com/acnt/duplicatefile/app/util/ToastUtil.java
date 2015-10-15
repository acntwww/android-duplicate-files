package com.acnt.duplicatefile.app.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast工具类，让开发者在使用Toast时少打几个字符
 *
 * @author liushuai
 *
 */
public class ToastUtil {
    /**
     * 显示长时间的Toast
     *
     * @param context
     * @param msg
     */
    public static void showLongToast(Context context, CharSequence msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    /**
     * 显示短时间的Toast
     *
     * @param context
     * @param msg
     */
    public static void showShortToast(Context context, CharSequence msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}