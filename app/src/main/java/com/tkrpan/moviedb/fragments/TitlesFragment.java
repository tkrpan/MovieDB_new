package com.tkrpan.moviedb.fragments;

import android.accounts.Account;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.SyncStatusObserver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.tkrpan.moviedb.MyActivity;
import com.tkrpan.moviedb.R;
import com.tkrpan.moviedb.StaticValue;
import com.tkrpan.moviedb.adapters.TitleAdapter;
import com.tkrpan.moviedb.helpers.CroutonHelper;
import com.tkrpan.moviedb.helpers.NetworkStateHelper;
import com.tkrpan.moviedb.interfaces.AddNewMovieButtonListener;
import com.tkrpan.moviedb.interfaces.OnMovieSelected;
import com.tkrpan.moviedb.models.Movie;
import com.tkrpan.moviedb.services.AuthenticatorService;
import com.tkrpan.moviedb.syncadapter.SyncUtils;

import java.util.List;

import de.keyboardsurfer.android.widget.crouton.Style;

public class TitlesFragment extends Fragment implements View.OnClickListener,
        AdapterView.OnItemClickListener {

    private static final String TAG = "TitlesFragment";

    private List<Movie> mMovies;
    private ListView listView_titles;
    private ImageButton button_refresh;

    private OnMovieSelected onMovieSelected;
    private AddNewMovieButtonListener addNewMovieButtonListener;

    private Object mSyncObserverHandle;

    public TitlesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mSyncStatusObserver.onStatusChanged(0);

        // Watch for sync state changes
        final int mask = ContentResolver.SYNC_OBSERVER_TYPE_PENDING |
                ContentResolver.SYNC_OBSERVER_TYPE_ACTIVE;
        mSyncObserverHandle = ContentResolver.addStatusChangeListener(mask, mSyncStatusObserver);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mSyncObserverHandle != null) {
            ContentResolver.removeStatusChangeListener(mSyncObserverHandle);
            mSyncObserverHandle = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setRetainInstance(true);

        mMovies = getMoviesFromDb();

        View view = inflater.inflate(R.layout.fragment_titles, container, false);

        if(view !=null) {
            listView_titles = (ListView)view.findViewById(R.id.listView_titles);
            setView(view);
        }
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        SyncUtils.CreateSyncAccount(activity);

        try {
            onMovieSelected = (OnMovieSelected) activity;
            addNewMovieButtonListener = (AddNewMovieButtonListener)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onMovieSelected = null;
        addNewMovieButtonListener = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.button_add:
                addNewMovieButtonListener.addNewButtonPressed();
                break;

            case R.id.button_refresh:
                if(NetworkStateHelper.isNetworkAvailable()) {
                    SyncUtils.TriggerRefresh();
                } else {
                    CroutonHelper.showCroutonIntoActivity(getActivity(), R.string.dismiss_network,
                            Style.ALERT, 2000);
                }

                break;

            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        if(onMovieSelected != null){
            onMovieSelected.movieSelected(mMovies.get(position));
        }
    }

    private void setView(View view){
        fillListView(listView_titles);

        ImageButton button_add = (ImageButton)view.findViewById(R.id.button_add);
        button_add.setOnClickListener(this);

        button_refresh = (ImageButton)view.findViewById(R.id.button_refresh);
        button_refresh.setOnClickListener(this);
    }

    private List<Movie> getMoviesFromDb(){
        List<Movie> movies;
        movies = ((MyActivity)getActivity()).getMovies();
        return movies;
    }

    private void fillListView(ListView listView){

        TitleAdapter titleAdapter = new TitleAdapter(getActivity(), mMovies);

        listView.setAdapter(titleAdapter);
        listView.setOnItemClickListener(this);
    }

    private SyncStatusObserver mSyncStatusObserver = new SyncStatusObserver() {
        /** Callback invoked with the sync adapter status changes. */
        @Override
        public void onStatusChanged(int which) {
            getActivity().runOnUiThread(new Runnable() {
                /**
                 * The SyncAdapter runs on a background thread. To update the UI, onStatusChanged()
                 * runs on the UI thread.
                 */
                @Override
                public void run() {
                    // Create a handle to the account that was created by
                    // SyncService.CreateSyncAccount(). This will be used to query the system to
                    // see how the sync status has changed.
                    Account account = AuthenticatorService.GetAccount();
                    if (account == null) {
                        // GetAccount() returned an invalid value. This shouldn't happen, but
                        // we'll set the status to "not refreshing".
                        setRefreshActionButtonState(false);
                        return;
                    }

                    // Test the ContentResolver to see if the sync adapter is active or pending.
                    // Set the state of the refresh button accordingly.
                    boolean syncActive = ContentResolver.isSyncActive(
                            account, StaticValue.AUTHORITY);

                    boolean syncPending = ContentResolver.isSyncPending(
                            account, StaticValue.AUTHORITY);

                    Log.i(StaticValue.TOM, TAG +" onStatusChanged account: " +account);

                    setRefreshActionButtonState(syncActive || syncPending);
                    List<Movie> newMovies = getMoviesFromDb();
                    if(newMovies.size() != mMovies.size()){
                        mMovies = newMovies;
                        fillListView(listView_titles);
                    }
                }
            });
        }
    };

    public void setRefreshActionButtonState(boolean refresh){

        Log.i(StaticValue.TOM, TAG +" setRefreshActionButtonState refresh: " + refresh);

        if(refresh == true){
            button_refresh.setOnClickListener(null);
            button_refresh.setVisibility(View.GONE);
        }else {
            button_refresh.setOnClickListener(this);
            button_refresh.setVisibility(View.VISIBLE);
        }
    }
}
