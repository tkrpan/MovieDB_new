package com.tkrpan.moviedb;

import android.net.Uri;

/**
 * Created by tomislav on 7/22/14. Constants
 */
public final class StaticValue {

    public static String TOM = "TOM";

    //ActionBar titles
    public static String ACTION_BAR_MOVIE_DB = "MovieDB";
    public static String ACTION_BAR_TITLE_ADD_NEW = "Add new";
    public static String ACTION_BAR_TITLE_DETAILS = "Details";

    //Key values for new instance of DetailsFragment
    public static final String key_title_of_movie = "title";
    public static final String key_genre_of_movie = "genre";
    public static final String key_description_of_movie = "description";

    //public static enum genre {ACTION, COMEDY, THRILLER, HORROR}
    public static final String movies_url = "http://moviedbkrpan.herokuapp.com/movies";

    //Content provider
    public static final String AUTHORITY = "com.tkrpan.moviedb";

    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.tkrpan.movie";
    public static final String CONTENT_TYPE_DIR = "vnd.android.cursor.dir/vnd.tkrpan.movie";


    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/movies");
}
