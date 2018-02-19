package com.khsm.app.presentation.ui.screens;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.khsm.app.R;
import com.khsm.app.presentation.ui.screens.meetings.MeetingListFragment;

public class MainActivity extends AppCompatActivity {
    /*private CompositeDisposable disposable;

    private UsersManager usersManager;

    private RecyclerView recyclerView;
    private ProgressBar progressBar;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content, MeetingListFragment.newInstance())
                    .commit();
        }

        /*disposable = new CompositeDisposable();

        usersManager = new UsersManager();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(ProgressBar.INVISIBLE);

        loadUsers();*/
    }

    /*
    @Override
    protected void onDestroy() {
        super.onDestroy();

        disposable.clear();
    }
    */

    /*
    private void loadUsers() {
        progressBar.setVisibility(ProgressBar.VISIBLE);

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
        progressBar.setVisibility(ProgressBar.INVISIBLE);

        UserListAdapter adapter = new UserListAdapter(this, users);
        recyclerView.setAdapter(adapter);
    }

    private void handleError(Throwable throwable) {
        progressBar.setVisibility(ProgressBar.INVISIBLE);

        throwable.printStackTrace();

        new AlertDialog.Builder(this)
                .setTitle(R.string.Error)
                .setMessage(throwable.getMessage())
                .setPositiveButton(R.string.OK, null)
                .show();
    }
    */
}
