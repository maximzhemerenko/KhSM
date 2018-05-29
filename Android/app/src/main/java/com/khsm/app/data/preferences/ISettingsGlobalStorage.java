package com.khsm.app.data.preferences;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

@SuppressWarnings("unused")
public interface ISettingsGlobalStorage {
    @Nullable
    <T> T getObject(@NonNull String key, @NonNull Class<T> tClass);

    @Nullable
    <T> T getObject(@NonNull String key, @NonNull Class<T> tClass, @SuppressWarnings("SameParameterValue") @Nullable T defaultValue);

    <T> void setObject(@NonNull String key, @Nullable T value);

    void clean();
}
