package com.tkrpan.moviedb.helpers;

import android.app.Activity;

import de.keyboardsurfer.android.widget.crouton.Configuration;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * Created by tomislav on 7/23/14.
 */
public class CroutonHelper {

    public static void showCroutonIntoActivity(Activity activity, int message, Style style,
                                               int duration){

          final Crouton crouton = Crouton.makeText(activity, message, style).setConfiguration
                    (new Configuration.Builder().setDuration(duration).build());
            crouton.show();

    }

    public static void cancelAllCroutons(){
        Crouton.cancelAllCroutons();
    }
}
