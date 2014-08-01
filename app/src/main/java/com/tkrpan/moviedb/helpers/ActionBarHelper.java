package com.tkrpan.moviedb.helpers;

import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

import com.tkrpan.moviedb.StaticValue;

/**
 * Created by tomislav on 7/23/14.  ActionBAr helper class
 */
public class ActionBarHelper {

    public static void setTitleActionBar(Activity activity, String title){

        ActionBar actionBar = ((ActionBarActivity)activity).getSupportActionBar();

        actionBar.setTitle(title);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public static void setupInitialActionBar(Activity activity){
        ActionBar actionBar = ((ActionBarActivity)activity).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(StaticValue.ACTION_BAR_MOVIE_DB);
        actionBar.setHomeButtonEnabled(false);
    }
}
