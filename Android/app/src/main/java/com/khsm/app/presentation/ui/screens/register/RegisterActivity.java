package com.khsm.app.presentation.ui.screens.register;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.khsm.app.R;

public class RegisterActivity extends AppCompatActivity {

    public static Intent newIntent(Context context) {
        return new Intent(context, RegisterActivity.class);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
    }

}
