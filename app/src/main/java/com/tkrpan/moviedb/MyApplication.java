package com.tkrpan.moviedb;

import android.app.Application;
import android.content.Context;

/**
 * Created by tomislav on 7/26/14. Custom application class
 */

public class MyApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public static Context getContext()
    {
        return mContext;
    }

}
