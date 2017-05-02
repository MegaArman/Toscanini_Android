package com.studionobume.musicalgoogle.Interactions;

/**
 * Created by Togame on 5/1/2017.
 */

public interface SheetFragmentInteraction {
    void changeFragment(String fragment_name);
    String getActivityData();
    void setUrl(String url);
    String getUrl();
}
