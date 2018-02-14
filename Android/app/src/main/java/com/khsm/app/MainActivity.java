package com.khsm.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.khsm.app.data.api.entities.User;
import com.khsm.app.domain.UsersManager;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private CompositeDisposable disposable;

    private UsersManager usersManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        disposable = new CompositeDisposable();

        usersManager = new UsersManager();

        loadUsers();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        disposable.clear();
    }

    private void loadUsers() {
        Disposable disposable = usersManager.getUsers() // получить операцию по загрузке пользователей из сети
                .subscribeOn(Schedulers.io()) // установить чтобы запрос выполнятся не в главном потоке а асинхронно
                .observeOn(AndroidSchedulers.mainThread()) // установить чтобы ответ вернулся в главном потоке
                .subscribe( // обработать ответ
                        this::setUsers, // ссылка на метод, метод будет вызван при успешном ответе сервера
                        this::handleError // ссылка на метод для обработки ошибки
                );
        this.disposable.add(disposable); // добавление операции в массив, все операции будут отмененты при закрытии окна
    }

    private void setUsers(List<User> users) {
        users.toString();
    }

    private void handleError(Throwable throwable) {
        throwable.printStackTrace();
    }
}
