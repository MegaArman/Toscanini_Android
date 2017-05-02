package com.studionobume.musicalgoogle.Fragments;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.studionobume.musicalgoogle.Interactions.HomeScreenInteraction;
import com.studionobume.musicalgoogle.R;

import static android.support.v4.media.session.MediaButtonReceiver.handleIntent;

/**
 * Created by Togame on 4/30/2017.
 */

public class HomeScreenFragment extends Fragment implements SearchView.OnQueryTextListener {

    public static final String TAG_HOME_FRAGMENT = "HOME";

    private SearchView search;
    private HomeScreenInteraction activity;
    public static HomeScreenFragment newInstance() {
        HomeScreenFragment fragment = new HomeScreenFragment();
        return fragment;
    }

    public HomeScreenFragment() {
        //Required empty pulic constructor
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);//Make sure you have this line of code.
    }

    @Override
    public void onResume() { super.onResume();}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof HomeScreenInteraction) {
            activity = (HomeScreenInteraction) context;
        }
        else {
            throw new RuntimeException(context.toString() + " must implement HomeScreenInteraction");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Home", "View has been created");
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        SearchView search = (SearchView) view.findViewById(R.id.search);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        search.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        search.setIconifiedByDefault(false);
        search.setQueryHint("Flute D4 C#");
        return view;
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.d("Home", "Search Bar Clicked.");
        activity.changeFragment(SheetFragment.TAG_SHEET_FRAGMENT);
        Log.d("Home", "Switching to SheetFragment");
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

}
