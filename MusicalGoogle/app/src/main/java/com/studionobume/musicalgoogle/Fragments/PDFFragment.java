package com.studionobume.musicalgoogle.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.studionobume.musicalgoogle.Interactions.PDFFragmentInteraction;
import com.studionobume.musicalgoogle.R;

/**
 * Created by Togame on 5/1/2017.
 */

public class PDFFragment extends Fragment implements View.OnClickListener {

    private PDFFragmentInteraction activity;

    public static PDFFragment newInstance() {
        PDFFragment fragment = new PDFFragment();
        return fragment;
    }

    public PDFFragment() {
        //Required empty pulic constructor
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() { super.onResume();}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof PDFFragmentInteraction) {
            activity = (PDFFragmentInteraction) context;
        }
        else {
            throw new RuntimeException(context.toString() + " must implement PDFFragmentnteraction");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pdf_layout, container, false);
        try {
            WebView mWebView = (WebView) view.findViewById(R.id.webView);
            String url = getArguments().getString("theUrl");
            mWebView.loadUrl(url);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {

    }
}
