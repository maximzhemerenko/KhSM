package com.khsm.app;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LoadUsersAsyncTask loadUsersAsyncTask = new LoadUsersAsyncTask();
        loadUsersAsyncTask.execute();
    }

    private class LoadUsersAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                // подключить через gradle retrofit
                // описать метод нашего бекенда в интерфейсе и указать его ретрофиту
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://10.0.2.2:5000/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                Link service = retrofit.create(Link.class);

                List<User> users = service.getUsers().execute().body();
                users.toString();
                // сделать запрос на сервер с помощью ретрофита и получить результат
                // в режиме отладки убедиться что приход\т правильные данные
                return null;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
