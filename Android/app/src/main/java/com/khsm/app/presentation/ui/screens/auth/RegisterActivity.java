package com.khsm.app.presentation.ui.screens.auth;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.khsm.app.R;
import com.khsm.app.data.entities.CreateUserRequest;
import com.khsm.app.data.entities.Gender;
import com.khsm.app.data.entities.User;
import com.khsm.app.domain.AuthManager;
import com.khsm.app.presentation.ui.screens.MainActivity;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RegisterActivity extends AppCompatActivity {
    @SuppressWarnings("unused")
    public static Intent newIntent(Context context) {
        return new Intent(context, RegisterActivity.class);
    }

    @SuppressWarnings("FieldCanBeLocal")
    private Button registerButton;
    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText password;
    private EditText confirmPassword;
    private RadioButton male;
    private RadioButton female;

    private Toolbar toolbar;

    @Nullable
    private Disposable registerDisposable;

    private AuthManager authManager;

    @Nullable
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        toolbar = findViewById(R.id.toolbar);

        Menu menu = toolbar.getMenu();

        authManager = new AuthManager(this);

        registerButton = findViewById(R.id.registerButton);
        firstName = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirm_password);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);

        registerButton.setOnClickListener(view -> register());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    private void register() {
        if (firstName.length() < 1
                || lastName.length() < 1
                || email.length() < 1
                || password.length() < 1
                || confirmPassword.length() < 1
                || (!male.isChecked() && !female.isChecked())) {
            showErrorMessage(getString(R.string.Register_Error_CheckInputData));
            return;
        }

        Gender gender =
                male.isChecked() ? Gender.MALE :
                        female.isChecked() ? Gender.FEMALE :
                                null;

        CreateUserRequest createUserRequest = new CreateUserRequest(
                new User(
                        firstName.getText().toString(),
                        lastName.getText().toString(),
                        gender,
                        email.getText().toString()
                ),
                password.getText().toString()
        );

        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        progressDialog = ProgressDialog.show(this, null, getString(R.string.Please_WaitD3), true, false);

        if (registerDisposable != null) {
            registerDisposable.dispose();
            registerDisposable = null;
        }

        registerDisposable = authManager.register(createUserRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::registrationCompleted,
                        this::handleError
                );

    }

    private void registrationCompleted() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }

        startActivity(MainActivity.newIntent(this, true));
    }

    private void handleError(Throwable throwable) {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }

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

    private void showLoginActivity() {
        startActivity(LoginActivity.newIntent(this));
    }
}
