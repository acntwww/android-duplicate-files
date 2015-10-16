package com.acnt.duplicatefile.app;

import android.app.Application;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * 应用全局变量
 * @author NKY
 * @date 15/10/15 23:42
 */
public class MyApp extends Application {

    /**
     * 全局的上下文环境
     */
    private static Application sGlobalContext;


    @Override
    public void onCreate() {
        super.onCreate();
        sGlobalContext = this;
        CrashReport.initCrashReport(this, "900010095", false);

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        sGlobalContext = null;
    }


    public static Application getGlobalContext() {
        return sGlobalContext;
    }
}
