package com.khsm.app.data.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.f2prateek.rx.preferences2.Preference;
import com.f2prateek.rx.preferences2.RxSharedPreferences;
import com.google.gson.Gson;

public class SettingsGlobalStorage implements ISettingsGlobalStorage {
    public static ISettingsGlobalStorage create(Context context) {
        return new SettingsGlobalStorage(PreferenceManager.getDefaultSharedPreferences(context));
    }

    private final RxSharedPreferences sharedPreferences;
    private final Gson gson;

    private SettingsGlobalStorage(SharedPreferences sharedPreferences) {
        this.sharedPreferences = RxSharedPreferences.create(sharedPreferences);
        this.gson = new Gson();
    }

    @Nullable
    @Override
    public <T> T getObject(@NonNull String key, @NonNull Class<T> tClass) {
        return getObject(key, tClass, null);
    }

    @Override
    @Nullable
    public <T> T getObject(@NonNull String key, @NonNull Class<T> tClass, @Nullable T defaultValue) {
        Preference<String> preference = sharedPreferences.getString(key);
        if (!preference.isSet())
            return defaultValue;

        String valueS = preference.get();
        return gson.fromJson(valueS, tClass);
    }

    @Override
    public <T> void setObject(@NonNull String key, @Nullable T value) {
        Preference<String> preference = sharedPreferences.getString(key);

        if (value != null) {
            String valueS = gson.toJson(value);
            preference.set(valueS);
        } else {
            preference.delete();
        }
    }

    @Override
    public void clean() {
        sharedPreferences.clear();
    }
}
