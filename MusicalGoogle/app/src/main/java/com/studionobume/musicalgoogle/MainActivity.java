package com.studionobume.musicalgoogle;


import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.studionobume.musicalgoogle.Constants.Constants;
import com.studionobume.musicalgoogle.Fragments.AliasFragment;
import com.studionobume.musicalgoogle.Fragments.HomeScreenFragment;
import com.studionobume.musicalgoogle.Fragments.QueryFragment;
import com.studionobume.musicalgoogle.Fragments.SheetFragment;
import com.studionobume.musicalgoogle.Fragments.TaskFragment;
import com.studionobume.musicalgoogle.Interactions.AliasFragmentInteraction;
import com.studionobume.musicalgoogle.Interactions.HomeScreenInteraction;
import com.studionobume.musicalgoogle.Interactions.QueryFragmentInteraction;
import com.studionobume.musicalgoogle.Interactions.RetainedFragmentInteraction;
import com.studionobume.musicalgoogle.Interactions.SheetFragmentInteraction;

public class MainActivity extends AppCompatActivity implements AliasFragmentInteraction, HomeScreenInteraction, QueryFragmentInteraction, SheetFragmentInteraction {

    private Fragment homeScreenFragment,taskFragment, sheetFragment , queryFragment;
    private FragmentManager fragmentManager;
    private String query;
    private String actual_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();

        //Check if server is up and running otherwise inform user
        Log.d("Home", "Create the taskFragment.");
        taskFragment = (TaskFragment) fragmentManager.findFragmentByTag(TaskFragment.TAG_TASK_FRAGMENT);

        if (taskFragment == null) {
            Log.d("Home", "No taskFragment can be found; Creating new one.");
            taskFragment = new TaskFragment();
            fragmentManager.beginTransaction().add(taskFragment, TaskFragment.TAG_TASK_FRAGMENT).commit();
        }

        if (savedInstanceState == null) {
            Log.d("Home", "No Previous State Found; Creating Home Fragment");
            homeScreenFragment = new HomeScreenFragment();
            // Set dashboard fragment to be the default fragment shown
            ((RetainedFragmentInteraction)taskFragment).setActiveFragmentTag(HomeScreenFragment.TAG_HOME_FRAGMENT);
            fragmentManager.beginTransaction().replace(R.id.frame, homeScreenFragment ).commit();
        } else {
            // Get referencecs to the fragments if they existed, null otherwise
            sheetFragment = fragmentManager.findFragmentByTag(SheetFragment.TAG_SHEET_FRAGMENT);
            queryFragment = fragmentManager.findFragmentByTag(QueryFragment.TAG_QUERY_FRAGMENT);
            ((RetainedFragmentInteraction)taskFragment).setActiveFragmentTag(QueryFragment.TAG_QUERY_FRAGMENT);
        }

        //If a notification being clicked got us here
        if(getIntent().getAction().equals(Constants.NEW_SCORE_ACTION)) {
            this.changeFragment(QueryFragment.TAG_QUERY_FRAGMENT);
        }
        else {
            Log.d("the intent", "was "  + getIntent().getAction());
        }
    }

    // inside on resume you need to tell the retained fragment to start the service
    @Override
    public void onResume(){
        super.onResume();
        ((RetainedFragmentInteraction)taskFragment).startBackgroundServiceNeeded();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
            setActivityData(query);
            sendBroadcast(intent);
            Log.d("Queries", "The voice gave me: " + query);
            FragmentTransaction fragTrans = fragmentManager.beginTransaction();
            fragTrans.replace(R.id.frame, new SheetFragment(),
                    ((RetainedFragmentInteraction)taskFragment).getActiveFragmentTag());
            fragTrans.addToBackStack(null);
            fragTrans.commitAllowingStateLoss();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.past_Queries) {
            this.changeFragment(QueryFragment.TAG_QUERY_FRAGMENT);
            Log.d("Home", "Switching to QueryFragment");
        }
        if(id == R.id.past_Sheets) {
            this.changeFragment(SheetFragment.TAG_SHEET_FRAGMENT);
            Log.d("Home", "Switching to SheetFragment");
        }
        if(id == R.id.home) {
            this.changeFragment(HomeScreenFragment.TAG_HOME_FRAGMENT);
            Log.d("Home", "Switching to HomeFragment");
        }
        if(id == R.id.Instructions) {
            Intent launchBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.INSTRUCTION_URL));
            startActivity(launchBrowser);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void changeFragment(String fragment_name) {
        Fragment fragment;
        Class fragmentClass = null;
        if(fragment_name.equals(SheetFragment.TAG_SHEET_FRAGMENT)){
            fragmentClass = SheetFragment.class;
            Log.d("Fragment", "sheetfragment selected");
        }
        else if(fragment_name.equals(QueryFragment.TAG_QUERY_FRAGMENT)){
            fragmentClass = QueryFragment.class;
            Log.d("Fragment", "queryfragment selected");
        }
        else if(fragment_name.equals(HomeScreenFragment.TAG_HOME_FRAGMENT)) {
            fragmentClass = HomeScreenFragment.class;
            Log.d("Fragment", "homeFragment selected");
        }

        try {
            if (fragmentClass != null) {
                fragment = (Fragment) fragmentClass.newInstance();
                FragmentTransaction ft= fragmentManager.beginTransaction();
                ft.replace(R.id.frame, fragment,
                        ((RetainedFragmentInteraction)taskFragment).getActiveFragmentTag());
                ft.addToBackStack(null);
                ft.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getUrl() {
        return actual_url;
    }

    public void setActivityData(String actual) {
        query = actual;
    }

    @Override
    public void setUrl(String url) {
        actual_url = url;
    }

    public String getActivityData() {
        return query;
    }
}
