package com.tkrpan.moviedb.syncadapter;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import com.tkrpan.moviedb.StaticValue;
import com.tkrpan.moviedb.models.Movie;
import com.tkrpan.moviedb.network_routine.RemoteMovieDb;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tomislav on 7/25/14. mySyncAdapter
 */

public class MoviesSyncAdapter extends AbstractThreadedSyncAdapter {

    public MoviesSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle bundle, String s,
                              ContentProviderClient contentProviderClient,
                              SyncResult syncResult) {

        Log.e(StaticValue.TOM, "******** onPerformSync MoviesSyncAdapter onPerformSync **********");

        try {
            // Get remote movies
            RemoteMovieDb remoteMovieDb = new RemoteMovieDb();
            List<Movie> remoteMovies = remoteMovieDb.getRemoteMovies(StaticValue.movies_url);
            for(Movie movie :remoteMovies){
                Log.e(StaticValue.TOM, "onPerformSync remoteMovies: " + movie.getTitle());
            }

            // Get movies from local
            ArrayList<Movie> localMovies = new ArrayList<Movie>();
            Cursor cursorMovies = contentProviderClient.query(StaticValue.CONTENT_URI, null,
                    null, null, null);

            if (cursorMovies != null) {
                while (cursorMovies.moveToNext()) {
                    localMovies.add(Movie.fromCursor(cursorMovies));
                }
                cursorMovies.close();
            }
            for(Movie movie :localMovies){
                Log.i(StaticValue.TOM, "onPerformSync localMovies: " + movie.getTitle());
            }

            // missing on Remote
            ArrayList<Movie> moviesToRemote = new ArrayList<Movie>();
            for (Movie localMovie : localMovies) {
                if(!remoteMovies.contains(localMovie)){
                    moviesToRemote.add(localMovie);
                }
            }
            for(Movie movie : moviesToRemote){
                Log.e(StaticValue.TOM, "onPerformSync moviesToRemote: " + movie.getTitle());
            }

            // missing on Local
            ArrayList<Movie> moviesToLocal = new ArrayList<Movie>();
            for (Movie remoteMovie : remoteMovies) {
                if (!localMovies.contains(remoteMovie)) {
                    moviesToLocal.add(remoteMovie);
                }
            }
            for(Movie movie : moviesToLocal){
                Log.i(StaticValue.TOM, "onPerformSync moviesToLocal: " + movie.getTitle());
            }


            if (moviesToRemote.size() == 0) {
                Log.d(StaticValue.TOM, "MoviesSyncAdapter onPerformSync " +
                        "No local changes to update server");
            } else {
                Log.d(StaticValue.TOM, "MoviesSyncAdapter onPerformSync " +
                        "Updating remote server with local changes");

                // Updating remote movies
                remoteMovieDb.postMoviesToRemoteDb(StaticValue.movies_url, moviesToRemote);
            }

            if (moviesToLocal.size() == 0) {
                Log.d(StaticValue.TOM, "No server changes to update local database");
            } else {
                Log.d(StaticValue.TOM, "Updating local database with remote changes");

                // Updating local movies
                int i = 0;
                ContentValues moviesToLocalValues[] = new ContentValues[moviesToLocal.size()];
                for (Movie localMovie : moviesToLocal) {
                    Log.d(StaticValue.TOM, "Remote -> Local: " + localMovie.getTitle());
                    moviesToLocalValues[i++] = localMovie.getContentValues();
                }
                contentProviderClient.bulkInsert(StaticValue.CONTENT_URI, moviesToLocalValues);
            }
        } catch (Exception e) {
        Log.e(StaticValue.TOM, "MoviesSyncAdapter error: " + e);
        }
    }
}
