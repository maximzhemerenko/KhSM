package com.khsm.app.presentation.ui.screens.profile;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.khsm.app.R;

public class EditProfileFragment extends Fragment {
    public static EditProfileFragment newInstance() {return new EditProfileFragment();}

    private ImageView avatar_imageView;
    private EditText firstName;
    private EditText lastName;
    private EditText wcaId;
    private EditText city;
    private EditText birthDate;
    private EditText phoneNumber;

    private RadioButton male;
    private RadioButton female;

    private Toolbar toolbar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_profile_layout, container, false);

        avatar_imageView = view.findViewById(R.id.avatar_imageView);

        // TODO: 26.02.2018 fix this temporary implementation
        Glide.with(this)
                .load("http://animals.yakohl.com/pic/schneeeule-2592013.jpg")
                .apply(RequestOptions.circleCropTransform())
                .into(avatar_imageView);

        return view;
    }
}
