package com.tkrpan.moviedb.helpers;

import android.app.Activity;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import com.tkrpan.moviedb.StaticValue;

/**
 * Created by tomislav on 7/23/14.
 */

public class SoftKeyboardHelper {

    public static void hideSoftKeyboard(Activity activity) {
/*
        final View myActivityView = activity.findViewById(R.id.my_activity_layout);
        int heightDiff = myActivityView.getRootView().getHeight() - myActivityView.getHeight();

        if (heightDiff > 100) { // keyboard is visible */

            InputMethodManager inputMethodManager = (InputMethodManager)
                    activity.getSystemService(Activity.INPUT_METHOD_SERVICE);

            try{
                inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus()
                        .getWindowToken(), 0);
            }
            catch (NullPointerException e){
                // keyboardIsVisible == false
                Log.e(StaticValue.TOM, "hideSoftKeyboard() " + e);
            }
       // }
    }
}
