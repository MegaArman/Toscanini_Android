package com.studionobume.musicalgoogle.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.studionobume.musicalgoogle.Constants.Constants;
import com.studionobume.musicalgoogle.Interactions.SheetFragmentInteraction;
import com.studionobume.musicalgoogle.MyApplication;
import com.studionobume.musicalgoogle.Networking.NetWorker;
import com.studionobume.musicalgoogle.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import static com.studionobume.musicalgoogle.Constants.Constants.url;

/**
 * Created by Togame on 4/30/2017.
 */

public class SheetFragment extends Fragment implements ListView.OnItemClickListener{
    public static final String TAG_SHEET_FRAGMENT = "SHEET";
    private SheetFragmentInteraction activity;
    private ArrayList<String> urls;
    private ListView urlList;
    private TextView textView;
    private URLArrayAdapter urlArrayAdapter;
    private NetWorker netWorker;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    private class URLArrayAdapter extends ArrayAdapter<String> {
        private final Context context;
        private final ArrayList<String> urls;
        private int id;

        public URLArrayAdapter(Context context, int id, ArrayList urls){
            super(context, id, urls);
            this.context = context;
            this.urls = urls;
            this.id= id;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.sheet_layout, parent, false);
            TextView urlbox = (TextView) rowView.findViewById(R.id.urlBox);
            urlbox.setText(urls.get(position));
            return rowView;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof SheetFragmentInteraction) {
            activity = (SheetFragmentInteraction) context;
        }
        else {
            throw new RuntimeException(context.toString() + " must implement SheetFragmentnteraction");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View completeView = inflater.inflate(R.layout.all_sheet_layout, container, false);
        TextView queryTV = (TextView) completeView.findViewById(R.id.queryView);

        String temp = activity.getActivityData();

        //TODO:
        if(temp == null) {
            queryTV.setText("No Query Entered.");
        }
        else {
            queryTV.setText("Showing results for " + temp);
        }
        return completeView;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        urls = new ArrayList<String>();

        //TODO: network request goes her
        Map<String, String> params = new HashMap<String, String>();
        params.put("composer", "");


        String enteredQuery = "";
        String completeURL = "";

        if (activity.getActivityData() != null) {
            enteredQuery = activity.getActivityData().toString();
        }

        if (!enteredQuery.isEmpty()) {
            completeURL = Constants.url + enteredQuery.replace(" ", "_");
        }
        else {
            Toast.makeText(MyApplication.getAppContext(), "please enter a search", Toast.LENGTH_LONG);
        }
        netWorker = netWorker.getSInstance(); //singleton


        NetWorker nw = NetWorker.getSInstance();
        nw.get(completeURL, new NetWorker.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                Log.d("success", result);
                if (result.contains("error")) {
                    Toast.makeText(MyApplication.getAppContext(), "Toscanini says: " + result, Toast.LENGTH_LONG).show();
                    return;
                }

                Gson gson = new Gson();
                Type listType = new TypeToken<List<String>>() {}.getType();

                urls = gson.fromJson(result, listType); //TODO, if error???
                urlList = (ListView)view.findViewById(R.id.all_urls);

                urlArrayAdapter= new URLArrayAdapter(getActivity(), R.layout.sheet_layout, urls);
                urlList.setAdapter(urlArrayAdapter);
                urlList.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                        String item = urlList.getItemAtPosition(position).toString();
                        Log.d("String", item);
                        activity.setUrl(item);
                        String url = Constants.url.replace("/?=", "") +"/pdf_scores/" + activity.getUrl();
                        Log.d("String", url);
                        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(launchBrowser);
                    }
                });
            }

            @Override
            public void onFailure(String result) {
                Log.d("failure", result);
            }
        });
    }


}
