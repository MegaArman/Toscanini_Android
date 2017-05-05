package com.studionobume.musicalgoogle.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.studionobume.musicalgoogle.Data.Query;
import com.studionobume.musicalgoogle.Interactions.AliasFragmentInteraction;
import com.studionobume.musicalgoogle.R;

import java.util.ArrayList;

/**
 * Created by a on 5/4/17.
 */

public class AliasFragment extends Fragment {
    public static final String TAG__FRAGMENT_ALIAS = "ALIAS";
    private AliasFragmentInteraction activity;
    private ArrayList<Query> allqueries;
    private ListView queryList;
    private AliasFragment.AliasArrayAdapter aliasArrayAdapter;

    private class AliasArrayAdapter extends ArrayAdapter<Query> {
        private final Context context;
        private final ArrayList<Query> queries;
        private int id;

        public AliasArrayAdapter(Context context, int id, ArrayList queries){
            super(context, id, allqueries);
            this.context = context;
            this.queries = queries;
            this.id= id;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.query_list_view_layout, parent, false);
            TextView aliasbox = (TextView) rowView.findViewById(R.id.Alias);
            aliasbox.setText(queries.get(position).getAlias());
            TextView querybox = (TextView) rowView.findViewById(R.id.Query);
            querybox.setText(queries.get(position).getQuery());
            return rowView;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof AliasFragmentInteraction) {
            activity = (AliasFragmentInteraction) context;
        }
        else {
            throw new RuntimeException(context.toString() + " must implement AliasFragmentnteraction");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View completeView = inflater.inflate(R.layout.alias_layout, container, false);
        queryList = (ListView) completeView.findViewById(R.id.queyAll);
        queryList.setAdapter(aliasArrayAdapter);
        return completeView;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
}
