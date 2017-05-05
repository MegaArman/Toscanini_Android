package com.studionobume.musicalgoogle.Interactions;

/**
 * Created by Togame on 4/29/2017.
 */

public interface RetainedFragmentInteraction {
    String getActiveFragmentTag();
    void setActiveFragmentTag(String s);
    void startBackgroundServiceNeeded();
}
