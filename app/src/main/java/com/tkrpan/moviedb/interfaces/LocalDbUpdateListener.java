package com.tkrpan.moviedb.interfaces;

/**
 * Created by tomislav on 7/28/14.
 */
public interface LocalDbUpdateListener {
    public void localDbIsUpdated();
}

        /*
                Log.e(StaticValue.TOM, "MoviesSyncAdapter localDbUpdateListener: " + MyApplication.currentActivity());

                LocalDbUpdateListener localDbUpdateListener =
                        (((MyActivity)(MyApplication.currentActivity()).getApplicationContext()));

                Log.e(StaticValue.TOM, "MoviesSyncAdapter localDbUpdateListener: " + localDbUpdateListener);
                if(localDbUpdateListener != null){
                    localDbUpdateListener.localDbIsUpdated();
                }
        */
