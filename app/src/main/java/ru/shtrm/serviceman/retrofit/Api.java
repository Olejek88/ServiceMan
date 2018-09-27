package ru.shtrm.serviceman.retrofit;

import android.support.v7.preference.PreferenceManager;

public class Api {

    // Base API
    //public static final String API_BASE = "http://sman-api.local.net";
    public static final String API_BASE = "http://api.serviceman.tehnosber.ru";
    // Get status of a specific number
    public static final String EQUIPMENT_STATE = API_BASE + "/query";
}
