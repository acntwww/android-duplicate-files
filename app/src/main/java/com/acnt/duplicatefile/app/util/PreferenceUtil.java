package com.acnt.duplicatefile.app.util;

import android.content.Context;
import android.content.SharedPreferences;
import com.acnt.duplicatefile.app.MyApp;

import java.util.List;

/**
 * 存储的工具类
 * @author NKY
 * @date 15/10/15 23:41
 */
public class PreferenceUtil {


    private static final String PREFERENCE_FILE = "config.xml";

    private static SharedPreferences getSharedPreferences() {
        return MyApp.getGlobalContext().getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE);
    }

    private static SharedPreferences.Editor getEditor() {
        return getSharedPreferences().edit();
    }


    //public static List<String> get

}
