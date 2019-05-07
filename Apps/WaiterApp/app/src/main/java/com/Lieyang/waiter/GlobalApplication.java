/**
 * Created by Taras Tysovskyi. Edited by Hongpeng Zhang
 * This class serves to instantiate some global objects and provide global Context from anywhere with getAppContext() method
 */
package com.Lieyang.waiter;

import android.app.Application;
import android.content.Context;

public class GlobalApplication extends Application {

    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
    }

    public static Context getAppContext() {
        return appContext;
    }
}