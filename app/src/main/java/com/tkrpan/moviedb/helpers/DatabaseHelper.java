package com.tkrpan.moviedb.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.tkrpan.moviedb.StaticValue;
import com.tkrpan.moviedb.models.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tomislav on 7/22/14. Class with CRU methods
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "movieDatabase";

    // Table Name
    public static final String TABLE_MOVIE = "movies";

    // Column names
    public static final String KEY_ID = "id";
    public static final String KEY_MOVIE_TITLE = "movie_title";
    public static final String KEY_MOVIE_GENRE = "movie_genre";
    public static final String KEY_MOVIE_DESCRIPTION = "movie_description";

    /*** Table Create Statement***/
    private static final String CREATE_TABLE_MOVIE = "CREATE TABLE "
            + TABLE_MOVIE + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_MOVIE_TITLE + " TEXT,"
            + KEY_MOVIE_GENRE + " TEXT, "
            + KEY_MOVIE_DESCRIPTION + " TEXT" + ")";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // creating table
        sqLiteDatabase.execSQL(CREATE_TABLE_MOVIE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        // on upgrade drop older table
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIE);

        // create new tables
        onCreate(sqLiteDatabase);
    }

    /**
     * Creating Movie
     */
    public long createMovie(Movie movie) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MOVIE_TITLE, movie.getTitle());
        values.put(KEY_MOVIE_GENRE, movie.getGenre());
        values.put(KEY_MOVIE_DESCRIPTION, movie.getDescription());

        // insert row
        long movie_id = db.insert(TABLE_MOVIE, null, values);
        return movie_id;
    }

    /**
     * getting all Movies
     */
    public List<Movie> getAllMovies() {
        List<Movie> movies = new ArrayList<Movie>();
        String selectQuery = "SELECT  * FROM " + TABLE_MOVIE;

        Log.e(StaticValue.TOM, "getAllMovies() " + selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Movie movie = new Movie();
                movie.setId(cursor.getInt((cursor.getColumnIndex(KEY_ID))));
                movie.setTitle(cursor.getString(cursor.getColumnIndex(KEY_MOVIE_TITLE)));
                movie.setGenre(cursor.getString(cursor.getColumnIndex(KEY_MOVIE_GENRE)));
                movie.setDescription(cursor.getString(cursor.getColumnIndex(KEY_MOVIE_DESCRIPTION)));

                // adding movies list
                movies.add(movie);
            } while (cursor.moveToNext());
        }
        return movies;
    }

    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}
