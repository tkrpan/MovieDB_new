package com.tkrpan.moviedb.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tkrpan.moviedb.R;
import com.tkrpan.moviedb.models.Movie;

import java.util.List;

/**
 * Created by tomislav on 7/22/14. adapter for list of titles
 */
public class TitleAdapter extends BaseAdapter {

    private Context mContext;
    private List<Movie> mMovies;

    public TitleAdapter(Context context, List<Movie> movies) {
        this.mContext = context;
        this.mMovies = movies;
    }

    @Override
    public int getCount() {
        return mMovies.size();
    }

    @Override
    public Object getItem(int position) {
        return mMovies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mMovies.indexOf(mMovies.get(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.adapter_title, parent, false);
        }

        if (convertView != null) {

            TextView title_textView = (TextView)convertView.findViewById(R.id.textView_title);
            String title = mMovies.get(position).getTitle();

            title_textView.setText(title);
        }
        return convertView;
    }
}