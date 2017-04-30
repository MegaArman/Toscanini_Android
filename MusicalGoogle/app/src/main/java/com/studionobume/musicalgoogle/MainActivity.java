package com.studionobume.musicalgoogle;


import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.studionobume.musicalgoogle.Fragments.HomeScreenFragment;
import com.studionobume.musicalgoogle.Fragments.QueryFragment;
import com.studionobume.musicalgoogle.Fragments.SheetFragment;
import com.studionobume.musicalgoogle.Fragments.TaskFragment;
import com.studionobume.musicalgoogle.Interactions.HomeScreenInteraction;
import com.studionobume.musicalgoogle.Interactions.RetainedFragmentInteraction;

public class MainActivity extends AppCompatActivity implements HomeScreenInteraction{

    private Fragment homeScreenFragment,taskFragment, sheetFragment , queryFragment;

    private SharedPreferences prefs;
    private FragmentManager fragmentManager;
    public static final int READ_TIMEOUT_MS = 20000;
    public static final int CONNECT_TIMEOUT_MS = 20000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        if(prefs.getString("push","").equals("True")){
            Log.d("Home","Wants Push Notifications.");
        }

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
            ((RetainedFragmentInteraction)taskFragment).setActiveFragmentTag(QueryFragment.TAG_QUERY_FRAGMENT);
            queryFragment = fragmentManager.findFragmentByTag(QueryFragment.TAG_QUERY_FRAGMENT);
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
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
    /*
        if (id == R.id.action_settings) {

        }
        else if (id == R.id.action_message) {

        }

        else if (id == R.id.action_logout) {

        }
        */

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void changeFragment(String fragment_name) {
        Fragment fragment;
        Class fragmentClass = null;
        if(fragment_name.equals(SheetFragment.TAG_SHEET_FRAGMENT)){
            fragmentClass = SheetFragment.class;
            Log.d("Home", "sheetfragment selected");
        }
        else if(fragment_name.equals(QueryFragment.TAG_QUERY_FRAGMENT)){
            fragmentClass = QueryFragment.class;
            Log.d("HW2", "queryfragment selected");
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
}
