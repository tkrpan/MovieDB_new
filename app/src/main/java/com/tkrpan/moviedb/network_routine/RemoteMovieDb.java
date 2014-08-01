package com.tkrpan.moviedb.network_routine;

import android.util.Log;

import com.tkrpan.moviedb.StaticValue;
import com.tkrpan.moviedb.models.Movie;


import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tomislav on 7/26/14. get and set remote movies
 */

public class RemoteMovieDb {

    public RemoteMovieDb() {
    }

    public List <Movie> getRemoteMovies(String uriStr) throws Exception {

        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(uriStr);
        HttpResponse response = httpClient.execute(httpGet);

        String responseString = EntityUtils.toString(response.getEntity());
        JSONArray jsonArray = new JSONArray(responseString);

        List<Movie> movies = this.parse(jsonArray);
        return movies;
    }


    public void postMoviesToRemoteDb(String uriStr, List<Movie> movies) throws Exception {

        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(uriStr);
        httpPost.addHeader("Content-Type", "application/json");

        JSONArray jsonArray = new JSONArray();
        for (Movie movie:movies) {
            jsonArray.put(movie.toJSONObject());
        }

        StringEntity body = new StringEntity(jsonArray.toString(),"UTF-8");
        httpPost.setEntity(body);
        HttpResponse httpResponse = httpClient.execute(httpPost);
    }

    private List<Movie> parse(JSONArray response) {

        List<Movie> movies = new ArrayList<Movie>();
        for (int i = 0; i < response.length(); i++) {
            try {
                JSONObject jsonObject = response.getJSONObject(i);

                String title = jsonObject.getString(StaticValue.key_title_of_movie);
                String genre = jsonObject.getString(StaticValue.key_genre_of_movie);
                String description = jsonObject.getString(StaticValue.key_description_of_movie);

                movies.add(new Movie(title, genre, description));

            } catch (JSONException e) {
                Log.e(StaticValue.TOM, "RemoteMovieDb: parse error: " + e);
            }
        }
        return movies;
    }

    public void delete(String uriStr, Movie movie) throws Exception {

        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpDelete httpDelete = new HttpDelete(uriStr + movie.getTitle());
        HttpResponse response = httpClient.execute(httpDelete);
    }
}
