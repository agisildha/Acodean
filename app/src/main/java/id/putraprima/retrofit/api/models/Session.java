package id.putraprima.retrofit.api.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class Session {
    private static final String APP_KEY = "key_app";
    private static final String VERSION_KEY = "version_app";

    private SharedPreferences preferences;

    public Session(Context context){
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public String getApp(){
        return preferences.getString(APP_KEY, null);
    }

    public void setApp(String app){
        preferences.edit().putString(APP_KEY, app).apply();
    }

    public String getVersion(){
        return preferences.getString(VERSION_KEY, null);
    }

    public void setVersion(String version){
        preferences.edit().putString(VERSION_KEY, version).apply();
    }
}
