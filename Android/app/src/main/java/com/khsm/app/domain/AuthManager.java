package com.khsm.app.domain;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.khsm.app.data.api.Api;
import com.khsm.app.data.api.ApiFactory;
import com.khsm.app.data.entities.CreateSessionRequest;
import com.khsm.app.data.entities.CreateUserRequest;
import com.khsm.app.data.entities.Session;
import com.khsm.app.data.entities.User;
import com.khsm.app.data.preferences.ISettingsGlobalStorage;
import com.khsm.app.data.preferences.SettingsGlobalStorage;

import io.reactivex.Completable;

public class AuthManager {
    private static final String KEY_SESSION_INFO = "KEY_SESSION_INFO";
    private static final String KEY_USER = "KEY_USER";

    private final Api api;
    private final ISettingsGlobalStorage settingsGlobalStorage;

    public AuthManager(@NonNull Context context) {
        api = ApiFactory.createApi();
        settingsGlobalStorage = SettingsGlobalStorage.create(context);
    }

    public Completable register(CreateUserRequest createUserRequest) {
        return api.register(createUserRequest)
                .doOnSuccess(this::authenticate)
                .toCompletable();
    }

    public Completable login(CreateSessionRequest createSessionRequest) {
        return api.login(createSessionRequest)
                .doOnSuccess(this::authenticate)
                .toCompletable();
    }

    public Completable logout() {
        return Completable.create(emitter -> {
            setSessionInfo(null);
            setUser(null);
        });
    }

    @SuppressWarnings("unused")
    public boolean isAuthenticated() {
        return getSessionInfo() != null;
    }

    public Session getSession() {
        SessionInfo sessionInfo = getSessionInfo();
        if (sessionInfo == null)
            return null;

        User user = getUser();

        return new Session(sessionInfo.token, user);
    }

    private void authenticate(@NonNull Session session) {
        SessionInfo sessionInfo = new SessionInfo(session.token);
        setSessionInfo(sessionInfo);
        setUser(session.user);
    }

    @Nullable
    private SessionInfo getSessionInfo() {
        return settingsGlobalStorage.getObject(KEY_SESSION_INFO, SessionInfo.class);
    }

    private void setSessionInfo(@Nullable SessionInfo sessionInfo) {
        settingsGlobalStorage.setObject(KEY_SESSION_INFO, sessionInfo);
    }

    @Nullable
    private User getUser() {
        return settingsGlobalStorage.getObject(KEY_USER, User.class);
    }

    private void setUser(@Nullable User user) {
        settingsGlobalStorage.setObject(KEY_USER, user);
    }

    @SuppressWarnings("WeakerAccess")
    public static class SessionInfo {
        public final String token;

        public SessionInfo(String token) {
            this.token = token;
        }
    }
}
