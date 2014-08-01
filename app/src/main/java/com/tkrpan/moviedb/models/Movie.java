package com.tkrpan.moviedb.models;

import android.content.ContentValues;
import android.database.Cursor;

import com.tkrpan.moviedb.helpers.DatabaseHelper;

import org.json.JSONObject;

/**
 * Created by tomislav on 7/22/14. model class
 */
public class Movie {

    private int id;
    private String title;
    private String genre;
    private String description;

    public Movie() {
    }

    public Movie(String title, String genre, String description) {
        this.title = title;
        this.genre = genre;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    // Create Movie object from a cursor
    public static Movie fromCursor(Cursor cursor) {
        String title = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_MOVIE_TITLE));
        String genre = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_MOVIE_GENRE));
        String description = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_MOVIE_DESCRIPTION));

        return new Movie(title, genre, description);
    }

    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.KEY_MOVIE_TITLE, title);
        values.put(DatabaseHelper.KEY_MOVIE_GENRE, genre);
        values.put(DatabaseHelper.KEY_MOVIE_DESCRIPTION, description);
        return values;
    }

    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("title", this.title);
            jsonObject.put("genre", this.genre);
            jsonObject.put("description", this.description);
        } catch (Exception e) {
            //Log.e(StaticValue.TOM, "RemoteMovieDb: getRemoteMovies movies: " + movies.toString());
        }
        return jsonObject;
    }

    @Override
    public boolean equals(Object object){ //isEquals by title

        boolean equals;

        if(object.getClass() == Movie.class){
            equals = title.equals(((Movie)object).getTitle());
        } else {
            equals = false;
        }
        return equals;
    }
}
