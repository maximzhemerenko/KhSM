package com.khsm.app.domain;

import android.content.Context;
import android.support.annotation.NonNull;

import com.khsm.app.data.api.Api;
import com.khsm.app.data.api.ApiFactory;
import com.khsm.app.data.entities.CreateSessionRequest;
import com.khsm.app.data.entities.CreateUserRequest;
import com.khsm.app.data.entities.Session;
import com.khsm.app.data.preferences.SessionStore;

import io.reactivex.Completable;

public class AuthManager {
    private final Api api;
    private final SessionStore sessionStore;

    public AuthManager(@NonNull Context context) {
        api = ApiFactory.createApi(context);
        sessionStore = new SessionStore(context);
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
        return Completable.create(emitter -> sessionStore.clearSession());
    }

    public Session getSession() {
        return sessionStore.getSession();
    }

    private void authenticate(@NonNull Session session) {
        sessionStore.setSession(session);
    }

    @SuppressWarnings("WeakerAccess")
    public static class SessionInfo {
        public final String token;

        public SessionInfo(String token) {
            this.token = token;
        }
    }
}
