package com.khsm.app.presentation.ui.screens.auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.khsm.app.R;
import com.khsm.app.data.entities.CreateSessionRequest;
import com.khsm.app.data.entities.Session;
import com.khsm.app.domain.UsersManager;
import com.khsm.app.presentation.ui.screens.MainActivity;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

    // AppCompatActivity этот класс нужно наследовать, что работать с API
public class LoginActivity extends AppCompatActivity {
    // Intent класс нужен здесь, чтобы создать новое Активити и запустить именно его
    public static Intent newIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    private EditText email;
    private EditText password;
    private Button login;
    private ProgressBar progressBar;

    private UsersManager usersManager;

    // Одноразовый интерфейс - переменная для его использования
    private Disposable loginDisposable;

    // onCreate запускает активность (участвует при создании новой активности)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // метод, который устанавливает соедрижимое из layout-файла
        setContentView(R.layout.login_activity);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(View.INVISIBLE);

        // метод, благодаря которому кнопка реагирует на нажатие
        login.setOnClickListener(view -> login());
    }

    private void login() {

        if (email.length() < 1 || password.length() < 1) {
            showErrorMessage(getString(R.string.Login_Error_CheckInputData));
            return;
        }

        // Ссылка, которая получает данные, введенные в EditText
        CreateSessionRequest createSessionRequest = new CreateSessionRequest(
                email.getText().toString(),
                password.getText().toString()
        );

        progressBar.setVisibility(View.VISIBLE);

        if (loginDisposable != null) {
            loginDisposable.dispose();
            loginDisposable = null;
        }

        loginDisposable = usersManager.login(createSessionRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::loginCompleted,
                        this::handleError
                );

    }

    private void loginCompleted(Session session) {
        progressBar.setVisibility(View.INVISIBLE);

        startActivity(MainActivity.intent(this));
    }

    private void handleError(Throwable throwable) {
        progressBar.setVisibility(View.INVISIBLE);

        new AlertDialog.Builder(this)
                .setTitle(R.string.Error)
                .setMessage(R.string.Login_Error_Authentication)
                .setPositiveButton(R.string.OK, null)
                .show();
    }

    private void showErrorMessage(String errorMessage) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.Error)
                .setMessage(errorMessage)
                .setPositiveButton(R.string.OK, null)
                .show();
    }

}
