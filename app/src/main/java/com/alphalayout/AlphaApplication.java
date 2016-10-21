package com.alphalayout;

import android.app.Application;

import com.umeng.analytics.MobclickAgent;

/**
 * @author glority - lu.meng
 */
public class AlphaApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        MobclickAgent.setDebugMode(BuildConfig.DEBUG);
        MobclickAgent.setCatchUncaughtExceptions(false);
    }
}
