package com.cocode.focora.internal;

import android.content.Context;
import android.content.SharedPreferences;

public final class FocoraPrefs {
    private static final String PREFS_NAME = "focora_discovery_prefs";

    private FocoraPrefs() {}

    public static boolean hasCompleted(Context context, String key) {
        return getPrefs(context).getBoolean(key, false);
    }

    public static void markCompleted(Context context, String key) {
        getPrefs(context).edit().putBoolean(key, true).apply();
    }

    public static void reset(Context context, String key) {
        getPrefs(context).edit().remove(key).apply();
    }

    private static SharedPreferences getPrefs(Context context) {
        return context.getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }
}