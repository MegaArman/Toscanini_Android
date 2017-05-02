package com.studionobume.musicalgoogle.Data;

/**
 * Created by Togame on 5/1/2017.
 */

public class Query {
    private String query;
    private String date;
    private String alias;

    public Query(String query1, String date1, String alias1) {
        query = query1;
        date = date1;
        alias = alias1;
    }

    public String getQuery(){
        return query;
    }

    public String getDate(){
        return date;
    }

    public String getAlias() {
        return alias;
    }

}
