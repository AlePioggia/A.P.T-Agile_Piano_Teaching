package com.example.apt_agile_piano_teaching.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Settings {

    private final SharedPreferences sharedPreferences;
    private final Preference preference;

    public Settings(final Context context, final Preference preference) {
        this.sharedPreferences = context.getSharedPreferences(preference.toString(), Context.MODE_PRIVATE);
        this.preference = preference;
    }

    public boolean getPreference() {
        return sharedPreferences.getBoolean(this.preference.toString(), false);
    }

    public void setTrue() {
        setBoolean(true);
    }

    public void setFalse() {
        setBoolean(false);
    }

    private void setBoolean(boolean bool) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(this.preference.toString(), bool);
        editor.apply();
    }

}
