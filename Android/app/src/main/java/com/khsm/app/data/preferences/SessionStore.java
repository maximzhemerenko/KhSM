package com.khsm.app.data.preferences;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.khsm.app.data.entities.Session;
import com.khsm.app.data.entities.User;
import com.khsm.app.domain.AuthManager;

public class SessionStore {
    private static final String KEY_SESSION_INFO = "KEY_SESSION_INFO";
    private static final String KEY_USER = "KEY_USER";

    private final ISettingsGlobalStorage settingsGlobalStorage;

    public SessionStore(@NonNull Context context) {
        settingsGlobalStorage = SettingsGlobalStorage.create(context);
    }

    @SuppressWarnings("unused")
    public boolean isAuthenticated() {
        return getSessionInfo() != null;
    }

    public Session getSession() {
        AuthManager.SessionInfo sessionInfo = getSessionInfo();
        if (sessionInfo == null)
            return null;

        User user = getUser();

        return new Session(sessionInfo.token, user);
    }

    public void setSession(@NonNull Session session) {
        AuthManager.SessionInfo sessionInfo = new AuthManager.SessionInfo(session.token);
        setSessionInfo(sessionInfo);
        setUser(session.user);
    }

    public void clearSession() {
        setSessionInfo(null);
        setUser(null);
    }

    @Nullable
    private AuthManager.SessionInfo getSessionInfo() {
        return settingsGlobalStorage.getObject(KEY_SESSION_INFO, AuthManager.SessionInfo.class);
    }

    private void setSessionInfo(@Nullable AuthManager.SessionInfo sessionInfo) {
        settingsGlobalStorage.setObject(KEY_SESSION_INFO, sessionInfo);
    }

    @Nullable
    private User getUser() {
        return settingsGlobalStorage.getObject(KEY_USER, User.class);
    }

    private void setUser(@Nullable User user) {
        settingsGlobalStorage.setObject(KEY_USER, user);
    }
}
