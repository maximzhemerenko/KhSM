package com.khsm.app.data.api;

import android.content.Context;
import android.support.annotation.NonNull;

import com.khsm.app.BuildConfig;
import com.khsm.app.data.api.base.ApiBase;
import com.khsm.app.data.api.core.ApiBuilderHelper;
import com.khsm.app.data.entities.Session;
import com.khsm.app.data.preferences.SessionStore;

import okhttp3.logging.HttpLoggingInterceptor;

public abstract class ApiFactory {
    public static Api createApi(@NonNull Context context) {
        ApiBuilderHelper apiBuilderHelper = new ApiBuilderHelper();

        ApiBase.AuthInterceptor authInterceptor = new ApiBase.AuthInterceptor();

        apiBuilderHelper.addInterceptor(authInterceptor);

        if (BuildConfig.DEBUG) {
            apiBuilderHelper.addLoggingInterceptor(HttpLoggingInterceptor.Level.BODY);
        }

        String serviceUrl = "http://10.0.2.2:5000/api/";

        RestApi restApi = apiBuilderHelper.createRetrofitApi(serviceUrl, RestApi.class);

        Api api = new Api(restApi, authInterceptor);

        SessionStore sessionStore = new SessionStore(context);

        Session session = sessionStore.getSession();
        if (session != null) {
            api.setSessionToken(session.token);
        }

        return api;
    }
}
