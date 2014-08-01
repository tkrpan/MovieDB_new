package com.tkrpan.moviedb.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.tkrpan.moviedb.StaticValue;
import com.tkrpan.moviedb.syncadapter.MoviesSyncAdapter;

/**
 * Created by tomislav on 7/25/14. Daemon service
 */

public class MovieDbSyncService extends Service {

    private static final String TAG = "SyncService";

    // Storage for an instance of the sync adapter
    private static MoviesSyncAdapter moviesSyncAdapter = null;
    // Object to use as a thread-safe lock
    private static final Object sSyncAdapterLock = new Object();

    public static boolean isServiceRunning = false;

    @Override
    public void onCreate() {
        super.onCreate();
        isServiceRunning = true;

        Log.d(StaticValue.TOM, TAG + " onCreate isServiceRunning: " + isServiceRunning);

        synchronized (sSyncAdapterLock) {
            if (moviesSyncAdapter == null) {
                moviesSyncAdapter = new MoviesSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public void onDestroy() {

        isServiceRunning = false;
        Log.d(StaticValue.TOM, TAG + " onDestroy isServiceRunning: " + isServiceRunning);
    }

    /**
     * Return an object that allows the system to invoke
     * the sync adapter.
     */

    @Override
    public IBinder onBind(Intent intent) {
        /*
         * Get the object that allows external processes
         * to call onPerformSync(). The object is created
         * in the base class code when the SyncAdapter
         * constructors call super()
         */
        return moviesSyncAdapter.getSyncAdapterBinder();
    }


}
