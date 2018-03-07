package com.khsm.app.presentation.ui.screens.auth;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.khsm.app.R;
import com.khsm.app.data.entities.CreateSessionRequest;
import com.khsm.app.domain.AuthManager;
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
    @SuppressWarnings("FieldCanBeLocal")
    private Button login;

    @Nullable
    private ProgressDialog progressDialog;

    private AuthManager authManager;

    private Disposable loginDisposable;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        authManager = new AuthManager(this);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);

        login.setOnClickListener(view -> login());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    private void login() {
        if (email.length() < 1 || password.length() < 1) {
            showErrorMessage(getString(R.string.Login_Error_CheckInputData));
            return;
        }

        CreateSessionRequest createSessionRequest = new CreateSessionRequest(
                email.getText().toString(),
                password.getText().toString()
        );

        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }

        progressDialog = ProgressDialog.show(this, null, getString(R.string.Please_WaitD3), true, false);

        if (loginDisposable != null) {
            loginDisposable.dispose();
            loginDisposable = null;
        }

        loginDisposable = authManager.login(createSessionRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::loginCompleted,
                        this::handleError
                );

    }

    private void loginCompleted() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }

        startActivity(MainActivity.intent(this, true));
    }

    private void handleError(Throwable throwable) {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }

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
