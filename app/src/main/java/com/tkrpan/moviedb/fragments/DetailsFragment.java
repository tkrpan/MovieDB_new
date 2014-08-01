package com.tkrpan.moviedb.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tkrpan.moviedb.R;
import com.tkrpan.moviedb.StaticValue;
import com.tkrpan.moviedb.helpers.ActionBarHelper;

public class DetailsFragment extends Fragment {

    private String mTitle, mGenre, mDescription;

    public static DetailsFragment newInstance(String title_of_movie, String genre_of_movie,
                                              String description_of_movie) {

        DetailsFragment fragment = new DetailsFragment();

        Bundle args = new Bundle();

        args.putString(StaticValue.key_title_of_movie, title_of_movie);
        args.putString(StaticValue.key_genre_of_movie, genre_of_movie);
        args.putString(StaticValue.key_description_of_movie, description_of_movie);
        fragment.setArguments(args);

        return fragment;
    }
    public DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitle = getArguments().getString(StaticValue.key_title_of_movie);
            mGenre = getArguments().getString(StaticValue.key_genre_of_movie);
            mDescription = getArguments().getString(StaticValue.key_description_of_movie);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        ActionBarHelper.setTitleActionBar(getActivity(), StaticValue.ACTION_BAR_TITLE_DETAILS);
        setRetainInstance(true);

        View view = inflater.inflate(R.layout.fragment_details, container, false);

        if(view !=null) {

            setView(view);
        }

        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void setView(View view){
        TextView textView_title = (TextView)view.findViewById(R.id.textView_title);
        TextView textView_genre = (TextView)view.findViewById(R.id.textView_genre);
        TextView textView_description = (TextView)view.findViewById(R.id.textView_description);

        textView_title.setText(mTitle);
        textView_genre.setText(mGenre);
        textView_description.setText(mDescription);
    }
}
