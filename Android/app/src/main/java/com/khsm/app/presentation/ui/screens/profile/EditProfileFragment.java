package com.khsm.app.presentation.ui.screens.profile;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.khsm.app.R;
import com.khsm.app.data.entities.Gender;
import com.khsm.app.data.entities.Session;
import com.khsm.app.data.entities.User;
import com.khsm.app.domain.AuthManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class EditProfileFragment extends Fragment {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

    public static EditProfileFragment newInstance() {return new EditProfileFragment();}

    private AuthManager authManager;

    @SuppressWarnings("FieldCanBeLocal")
    private Button save;

    private ImageView avatar_imageView;
    private EditText firstName;
    private EditText lastName;
    private EditText wcaId;
    private EditText city;
    private EditText birthDate;
    private EditText phoneNumber;

    private RadioButton male;
    private RadioButton female;

    @SuppressWarnings("unused")
    private Toolbar toolbar;

    @Nullable
    private Disposable registerDisposable;

    @Nullable
    private ProgressDialog progressDialog;

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

        save = view.findViewById(R.id.saveButton);
        save.setOnClickListener(v -> updateUser());

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        Session session = authManager.getSession();
        setUser(session.user);
    }

    private void updateUser() {
        // validation
        if (firstName.length() < 1
                || lastName.length() < 1
                || (!male.isChecked() && !female.isChecked())) {
            showErrorMessage(getString(R.string.Register_Error_CheckInputData));
            return;
        }

        Date birthDate;

        try {
            birthDate = stringToJavaDate(this.birthDate.getText().toString());
        } catch (ParseException e) {
            showErrorMessage(getString(R.string.Register_Error_CheckInputData));
            return;
        }

        // init entities
        Gender gender =
                male.isChecked() ? Gender.MALE :
                        female.isChecked() ? Gender.FEMALE :
                                null;

        User user = new User(
                firstName.getText().toString(),
                lastName.getText().toString(),
                gender,
                city.getText().toString(),
                wcaId.getText().toString(),
                phoneNumber.getText().toString(),
                birthDate
        );

        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }

        progressDialog = ProgressDialog.show(requireContext(), null, getString(R.string.Please_WaitD3), true, false);

        if (registerDisposable != null) {
            registerDisposable.dispose();
            registerDisposable = null;
        }

        registerDisposable = authManager.updateUser(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::updateUserCompleted,
                        this::handleError
                );
    }

    private void updateUserCompleted(User user){
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }

        setUser(user);

        Toast.makeText(requireContext(), R.string.UpdateUser_SuccessMessage, Toast.LENGTH_LONG).show();
    }

    private Date stringToJavaDate(@NonNull String dateString) throws ParseException {
        dateString = dateString.trim();

        if (dateString.isEmpty())
            return null;

        return dateFormat.parse(dateString);
    }

    private void handleError(Throwable throwable) {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }

        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.Error)
                .setMessage(R.string.Register_Error_UserRegisterError)
                .setPositiveButton(R.string.OK, null)
                .show();
    }

    private void showErrorMessage(String errorMessage) {
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.Error)
                .setMessage(errorMessage)
                .setPositiveButton(R.string.OK, null)
                .show();
    }

    private void setUser(User user) {
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
    }
}
