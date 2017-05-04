package com.studionobume.musicalgoogle.Fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.studionobume.musicalgoogle.Data.Query;
import com.studionobume.musicalgoogle.Database.DBConstants;
import com.studionobume.musicalgoogle.Database.QueryDatabase;
import com.studionobume.musicalgoogle.Interactions.QueryFragmentInteraction;
import com.studionobume.musicalgoogle.MyApplication;
import com.studionobume.musicalgoogle.R;

import java.util.ArrayList;
/**
 * Created by Togame on 4/30/2017.
 */

public class QueryFragment extends Fragment {
    public static final String TAG_QUERY_FRAGMENT = "QUERY";
    private QueryFragmentInteraction activity;
    private ArrayList<Query> queries;
    private QueryArrayAdapter queryListAdapter;


    private class QueryArrayAdapter extends ArrayAdapter<Query> {
        private final Context context;
        private final ArrayList<Query> queries;
        private int id;

        public QueryArrayAdapter(Context context, int id, ArrayList queries) {
            super(context, id, queries);
            this.context = context;
            this.queries = queries;
            this.id = id;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Log.d("Query", "Getting the view");
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.query_list_view_layout, parent, false);
            TextView alias = (TextView) rowView.findViewById(R.id.Alias);
            TextView query = (TextView) rowView.findViewById(R.id.Query);
            QueryDatabase db = new QueryDatabase(getContext());
            if(db.numberOfRows() == 0) {
                alias.setText("No past queries found.");
                query.setText("Please make a query on the home screen before reviewing past queries.");
            }
            else {
                for (int i = 0; i < 10; i++) {
                    Cursor temp = db.getData(i);
                    alias.setText("Query" + temp.getColumnIndex(DBConstants.COLUMN_NAME_ENTRY_ID));
                    query.setText(temp.getColumnIndex(DBConstants.COLUMN_NAME_QUERY));
                }
            }
            return rowView;
        }
    }

    public static QueryFragment newInstance() {
        QueryFragment fragment = new QueryFragment();
        return fragment;
    }

    public QueryFragment() {
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
        if(context instanceof QueryFragmentInteraction) {
            activity = (QueryFragmentInteraction) context;
        }
        else {
            throw new RuntimeException(context.toString() + " must implement QueryFragmentnteraction");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.all_query_layout, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView queryTV = (TextView) view.findViewById(R.id.queryView2);
        queryTV.setText(PreferenceManager.getDefaultSharedPreferences(MyApplication.getAppContext())
                .getString("newFiles", "defaultStringIfNothingFound"));
    }
}
