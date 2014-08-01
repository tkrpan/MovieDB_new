package com.tkrpan.moviedb;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.tkrpan.moviedb.fragments.AddFragment;
import com.tkrpan.moviedb.fragments.DetailsFragment;
import com.tkrpan.moviedb.fragments.TitlesFragment;
import com.tkrpan.moviedb.helpers.ActionBarHelper;
import com.tkrpan.moviedb.helpers.CroutonHelper;
import com.tkrpan.moviedb.helpers.DatabaseHelper;
import com.tkrpan.moviedb.helpers.NetworkStateHelper;
import com.tkrpan.moviedb.helpers.SoftKeyboardHelper;
import com.tkrpan.moviedb.interfaces.AddNewMovieButtonListener;
import com.tkrpan.moviedb.interfaces.LocalDbUpdateListener;
import com.tkrpan.moviedb.interfaces.OnMovieSelected;
import com.tkrpan.moviedb.interfaces.SaveNewMovieListener;
import com.tkrpan.moviedb.models.Movie;
import com.tkrpan.moviedb.syncadapter.SyncUtils;

import java.util.List;


public class MyActivity extends ActionBarActivity implements
        OnMovieSelected,
        AddNewMovieButtonListener,
        SaveNewMovieListener,
        LocalDbUpdateListener {

    private Fragment mFragment;
    private DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        if (savedInstanceState == null) {
            setupInitialFragment(true);
        } else {
            // rotation
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        CroutonHelper.cancelAllCroutons();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            restoreInitialFragment();
            SoftKeyboardHelper.hideSoftKeyboard(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if(getCurrentVisibleFragment().getTag().equals(AddFragment.class.getSimpleName())){
            Log.i(StaticValue.TOM, "onBackPressed mFragment: " + mFragment);
            restoreInitialFragment();
        } else

        if (getCurrentVisibleFragment().getTag().equals(DetailsFragment.class.getSimpleName())){
            Log.i(StaticValue.TOM, "onBackPressed mFragment: " + mFragment);
            restoreInitialFragment();
        } else

        if(getCurrentVisibleFragment().getTag().equals(TitlesFragment.class.getSimpleName())){
                finish();
        } else {

            super.onBackPressed();
        }
    }

    /**
     * Fragment transition from Titles to Details
     */
    @Override
    public void movieSelected(Movie movie) {

        mFragment = new DetailsFragment();

        mFragment = DetailsFragment.newInstance(movie.getTitle(),
                movie.getGenre(),
                movie.getDescription());

        fragmentTransaction(mFragment, false);
    }

    @Override
    public void addNewButtonPressed() {

        mFragment = new AddFragment();
        fragmentTransaction(mFragment, false);
    }

    @Override
    public void saveNewButtonPressed(String title, String genre, String description) {

        mDatabaseHelper.createMovie(new Movie(title, genre, description));
        mDatabaseHelper.closeDB();

        SoftKeyboardHelper.hideSoftKeyboard(this);
        setupInitialFragment(false);

        //startSync();
    }

    @Override
    public void localDbIsUpdated() {

        Log.e(StaticValue.TOM, "MyActivity localDbIsUpdated: ");
        setupInitialFragment(false);
    }

    private void setupInitialFragment(boolean addToBackStack){

        TitlesFragment titlesFragment = new TitlesFragment();
        fragmentTransaction(titlesFragment, addToBackStack);

        ActionBarHelper.setupInitialActionBar(this);
    }

    private void restoreInitialFragment(){

        FragmentManager fragmentManager = getSupportFragmentManager();
        TitlesFragment titlesFragment = (TitlesFragment)fragmentManager.findFragmentByTag(
                TitlesFragment.class.getSimpleName());

        Log.i(StaticValue.TOM, "restoreInitialFragment backstack: " + getSupportFragmentManager()
                .getBackStackEntryCount());

        fragmentTransaction(titlesFragment, false);
        ActionBarHelper.setupInitialActionBar(this);
    }

    private void fragmentTransaction(Fragment fragment, boolean addToBackStack){

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.my_activity_layout, fragment,
                fragment.getClass().getSimpleName());

        if(addToBackStack){
            fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
        }

        fragmentTransaction.commit();
    }

    public List<Movie> getMovies(){
        mDatabaseHelper = new DatabaseHelper(getApplicationContext());
        List<Movie> movies = mDatabaseHelper.getAllMovies();
        mDatabaseHelper.closeDB();

        return movies;
    }

    private Fragment getCurrentVisibleFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        return fragmentManager.findFragmentById(R.id.my_activity_layout);
    }
}
