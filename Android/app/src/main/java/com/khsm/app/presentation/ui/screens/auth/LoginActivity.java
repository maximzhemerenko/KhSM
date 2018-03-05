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

import com.khsm.app.R;
import com.khsm.app.data.entities.CreateSessionRequest;
import com.khsm.app.data.entities.CreateUserRequest;
import com.khsm.app.data.entities.Session;
import com.khsm.app.data.entities.User;
import com.khsm.app.domain.UsersManager;
import com.khsm.app.presentation.ui.screens.MainActivity;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {
    public static Intent newIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    private EditText email;
    private EditText password;
    private Button login;

    private UsersManager usersManager;

    private Disposable loginDisposable;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);

        login.setOnClickListener(view -> login());
    }

    private void login() {

        if (email.length() < 1 || password.length() < 1) {
            showErrorMessage(getString(R.string.Register_Error_CheckInputData));
            return;
        }

        CreateSessionRequest createSessionRequest = new CreateSessionRequest(
                email.getText().toString(),
                password.getText().toString()
        );


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
        // progressBar.setVisibility(View.INVISIBLE);

        startActivity(MainActivity.intent(this));
    }

    private void handleError(Throwable throwable) {
        // progressBar.setVisibility(View.INVISIBLE);

        new AlertDialog.Builder(this)
                .setTitle(R.string.Error)
                .setMessage(R.string.Register_Error_UserRegisterError)
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
