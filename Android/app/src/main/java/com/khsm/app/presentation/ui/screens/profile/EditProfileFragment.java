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
import com.khsm.app.data.entities.Gender;
import com.khsm.app.data.entities.Session;
import com.khsm.app.data.entities.User;
import com.khsm.app.domain.AuthManager;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class EditProfileFragment extends Fragment {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());

    public static EditProfileFragment newInstance() {return new EditProfileFragment();}

    private AuthManager authManager;

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        authManager = new AuthManager(requireContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_profile_layout, container, false);

        avatar_imageView = view.findViewById(R.id.avatar_imageView);

        firstName = view.findViewById(R.id.first_name);
        lastName = view.findViewById(R.id.last_name);
        wcaId = view.findViewById(R.id.wca_id);
        city = view.findViewById(R.id.city);
        birthDate = view.findViewById(R.id.birth_date);
        phoneNumber = view.findViewById(R.id.phone_number);

        male = view.findViewById(R.id.male);
        female = view.findViewById(R.id.female);

        Session session = authManager.getSession();
        User user = session.user;

        firstName.setText(user.firstName);
        lastName.setText(user.lastName);
        wcaId.setText(user.wcaId);
        city.setText(user.city);
        if (user.birthDate != null)
            birthDate.setText(dateFormat.format(user.birthDate));
        phoneNumber.setText(user.phoneNumber);

        if (user.gender.equals(Gender.MALE))
            male.setChecked(true);
        else if (user.gender.equals(Gender.FEMALE))
            female.setChecked(true);

        // TODO: 26.02.2018 fix this temporary implementation
        Glide.with(this)
                .load("http://animals.yakohl.com/pic/schneeeule-2592013.jpg")
                .apply(RequestOptions.circleCropTransform())
                .into(avatar_imageView);

        return view;
    }

}
