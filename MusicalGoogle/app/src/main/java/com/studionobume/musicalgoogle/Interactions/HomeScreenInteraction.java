package com.studionobume.musicalgoogle.Interactions;

/**
 * Created by Togame on 4/30/2017.
 */

public interface HomeScreenInteraction {
    void changeFragment(String fragment_name);
    String getActivityData();
    void setActivityData(String actual);
}
