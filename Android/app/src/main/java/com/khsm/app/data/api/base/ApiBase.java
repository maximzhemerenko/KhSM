package com.khsm.app.data.api.base;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.IOException;

import io.reactivex.Completable;
import io.reactivex.Single;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class ApiBase {
    private final AuthInterceptor authInterceptor;

    protected ApiBase(AuthInterceptor authInterceptor) {
        this.authInterceptor = authInterceptor;
    }

    public void setSessionToken(@Nullable String sessionToken) {
        authInterceptor.sessionToken = sessionToken;
    }

    protected Completable processResponse(Completable upstream) {
        return upstream; // TODO: 14.02.2018 add generic error handling
    }

    protected <TResponse> Single<TResponse> processResponse(Single<TResponse> upstream) {
        return upstream; // TODO: 14.02.2018 add generic error handling
    }

    //region Interceptor
    public static class AuthInterceptor implements Interceptor {
        @Nullable
        private String sessionToken;

        private static final String HEADER_AUTHORIZATION = "Authorization";

        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            Request.Builder builder = chain.request().newBuilder();

            addHeaders(builder);

            return chain.proceed(builder.build());
        }

        private void addHeaders(@NonNull Request.Builder builder) {
            String authentication = sessionToken;
            if (authentication != null) {
                builder.addHeader(HEADER_AUTHORIZATION, authentication);
            }
        }
    }
    //endregion
}
