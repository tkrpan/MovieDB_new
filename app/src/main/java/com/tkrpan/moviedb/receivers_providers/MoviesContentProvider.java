package com.tkrpan.moviedb.receivers_providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.tkrpan.moviedb.StaticValue;
import com.tkrpan.moviedb.helpers.DatabaseHelper;

/**
 * Created by tomislav on 7/27/14. Content provider
 */

public class MoviesContentProvider extends ContentProvider {

    public static final UriMatcher URI_MATCHER = buildUriMatcher();

    public static final String PATH = "movies";
    public static final int PATH_TOKEN = 100;
    public static final String PATH_FOR_ID = "movies/*";
    public static final int PATH_FOR_ID_TOKEN = 200;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = StaticValue.AUTHORITY;
        matcher.addURI(authority, PATH, PATH_TOKEN);
        matcher.addURI(authority, PATH_FOR_ID, PATH_FOR_ID_TOKEN);
        return matcher;
    }

    private DatabaseHelper databaseHelper;

    @Override
    public boolean onCreate() {

        Context context = getContext();
        databaseHelper = new DatabaseHelper(context);

        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        final int match = URI_MATCHER.match(uri);

        switch (match) {
            // retrieve movies list
            case PATH_TOKEN: {
                SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
                builder.setTables(DatabaseHelper.TABLE_MOVIE);
                return builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
            }

            case PATH_FOR_ID_TOKEN: {
                int movieId = (int) ContentUris.parseId(uri);
                SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
                builder.setTables(DatabaseHelper.TABLE_MOVIE);
                builder.appendWhere(DatabaseHelper.KEY_ID + "=" + movieId);
                return builder.query(db, projection, selection,selectionArgs, null, null, sortOrder);
            }

            default:
                return null;
        }
    }

    @Override
    public String getType(Uri uri) {

        final int match = URI_MATCHER.match(uri);

        switch (match) {

            case PATH_TOKEN:
                return StaticValue.CONTENT_TYPE_DIR;

            case PATH_FOR_ID_TOKEN:
                return StaticValue.CONTENT_ITEM_TYPE;

            default:
                throw new UnsupportedOperationException("URI " + uri + " is not supported.");
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        int token = URI_MATCHER.match(uri);

        switch (token) {

            case PATH_TOKEN: {
                long id = database.insert(DatabaseHelper.TABLE_MOVIE, null, contentValues);
                if (id != -1) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return StaticValue.CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build();
            }

            default: {
                throw new UnsupportedOperationException("URI: " + uri + " not supported.");
            }
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int token = URI_MATCHER.match(uri);
        int rowsDeleted = -1;

        switch (token) {

            case (PATH_TOKEN):
                rowsDeleted = db.delete(DatabaseHelper.TABLE_MOVIE, selection, selectionArgs);
                break;

            case (PATH_FOR_ID_TOKEN):
                String tvShowIdWhereClause = DatabaseHelper.KEY_ID + "=" + uri.getLastPathSegment();

                if (!TextUtils.isEmpty(selection)) {
                    tvShowIdWhereClause += " AND " + selection;
                }
                rowsDeleted = db.delete(DatabaseHelper.TABLE_MOVIE, tvShowIdWhereClause, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        // Notifying the changes
        if (rowsDeleted != -1) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
