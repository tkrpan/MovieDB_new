package com.tkrpan.moviedb.fragments;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.tkrpan.moviedb.R;
import com.tkrpan.moviedb.StaticValue;
import com.tkrpan.moviedb.helpers.ActionBarHelper;
import com.tkrpan.moviedb.helpers.CroutonHelper;
import com.tkrpan.moviedb.interfaces.SaveNewMovieListener;

import de.keyboardsurfer.android.widget.crouton.Style;

public class AddFragment extends Fragment implements
        View.OnClickListener,
        AdapterView.OnItemSelectedListener {

    private String mTitle, mGenre, mDescription;

    private EditText editText_title, editText_description;
    private Spinner spinner_genre;

    private SaveNewMovieListener saveNewMovieListener;

    public AddFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        ActionBarHelper.setTitleActionBar(getActivity(), StaticValue.ACTION_BAR_TITLE_ADD_NEW);
        setRetainInstance(true);

        View view = inflater.inflate(R.layout.fragment_add, container, false);

        if(view !=null) {

            setView(view);
        }

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            saveNewMovieListener = (SaveNewMovieListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        saveNewMovieListener = null;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.button_save:

                mTitle = editText_title.getText().toString();
                if(!(mTitle.equals("") || mTitle.isEmpty())){
                    mDescription = editText_description.getText().toString();
                    saveNewMovieListener.saveNewButtonPressed(mTitle, mGenre, mDescription);
                } else {
                    CroutonHelper.showCroutonIntoActivity(getActivity(), R.string.dismiss_title,
                            Style.CONFIRM, 2000);
                }

                break;

            default:
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {


        Resources resources = getResources();
        String [] genre_strings = resources.getStringArray(R.array.string_array_genre);
        mGenre =  genre_strings[position];

        /*
        StaticValue.genre genre = StaticValue.genre.values()[position];

        switch (genre){

            case ACTION:

                break;

            case COMEDY:

                break;

            case THRILLER:

                break;

            case HORROR:

                break;

            default:
                break;
        } */
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Resources resources = getResources();
        String [] genre_strings = resources.getStringArray(R.array.string_array_genre);
        mGenre =  genre_strings[0];
    }

    private void setView(View view){
        editText_title = (EditText)view.findViewById(R.id.editText_title);
        editText_description = (EditText)view.findViewById(R.id.editText_description);

        spinner_genre = (Spinner)view.findViewById(R.id.spinner_genre);
        spinner_genre.setOnItemSelectedListener(this);

        ImageButton button_save = (ImageButton)view.findViewById(R.id.button_save);
        button_save.setOnClickListener(this);
    }
}
