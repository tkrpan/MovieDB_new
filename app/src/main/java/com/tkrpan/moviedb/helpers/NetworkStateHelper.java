package com.tkrpan.moviedb.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.tkrpan.moviedb.MyApplication;

/**
 * Created by tomislav on 7/26/14. Check internet connection
 */
public class NetworkStateHelper {

        public static boolean isNetworkAvailable() {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) MyApplication.getContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
}
