package com.khsm.app.presentation.ui.screens.profile;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.khsm.app.R;
import com.khsm.app.data.entities.Gender;
import com.khsm.app.data.entities.User;
import com.khsm.app.domain.UserManager;
import com.khsm.app.presentation.ui.screens.MainActivity;
import com.khsm.app.presentation.ui.utils.maskedittext.EditTextMask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class EditProfileFragment extends Fragment implements Toolbar.OnMenuItemClickListener {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

    public static EditProfileFragment newInstance() {return new EditProfileFragment();}

    private UserManager userManager;

    @SuppressWarnings("FieldCanBeLocal")
    private Toolbar toolbar;

    private ImageView avatar_imageView;
    private EditText firstName;
    private EditText lastName;
    private EditText wcaId;
    private EditText city;
    private EditText birthDate;
    private EditText phoneNumber;

    private RadioButton male;
    private RadioButton female;

    @SuppressWarnings("FieldCanBeLocal")
    private Button save;

    @Nullable
    private Disposable updateUserDisposable;

    @Nullable
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userManager = new UserManager(requireContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_profile_layout, container, false);

        toolbar = view.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.edit_profile);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) requireActivity();
                mainActivity.showMenu();
            }
        });

        avatar_imageView = view.findViewById(R.id.avatar_imageView);

        firstName = view.findViewById(R.id.first_name);
        lastName = view.findViewById(R.id.last_name);
        wcaId = view.findViewById(R.id.wca_id);
        city = view.findViewById(R.id.city);
        birthDate = view.findViewById(R.id.birth_date);
        EditTextMask.setup(birthDate, "##-##-####");
        phoneNumber = view.findViewById(R.id.phone_number);
        EditTextMask.setup(phoneNumber, "#### (##) ###-##-##");

        male = view.findViewById(R.id.male);
        female = view.findViewById(R.id.female);

        save = view.findViewById(R.id.saveButton);
        save.setOnClickListener(v -> updateUser());

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        User user = userManager.getUser();
        if (user != null) {
            setUser(user);
        }
    }

    private void updateUser() {
        // validation
        if (firstName.length() < 1
                || lastName.length() < 1
                || (!male.isChecked() && !female.isChecked())) {
            showErrorMessage(getString(R.string.Register_Error_CheckInputData));
            return;
        }

        // init entities
        Gender gender =
                male.isChecked() ? Gender.MALE :
                        female.isChecked() ? Gender.FEMALE :
                                null;

        Date birthDate;
        try {
            birthDate = stringToJavaDate(this.birthDate.getText().toString());
        } catch (ParseException e) {
            showErrorMessage(getString(R.string.Register_Error_CheckInputData));
            return;
        }

        String phoneNumber = unmaskPhoneNumber(this.phoneNumber.getText().toString());

        User user = new User(
                firstName.getText().toString(),
                lastName.getText().toString(),
                gender,
                city.getText().toString(),
                wcaId.getText().toString(),
                phoneNumber,
                birthDate
        );

        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }

        progressDialog = ProgressDialog.show(requireContext(), null, getString(R.string.Please_WaitD3), true, false);

        if (updateUserDisposable != null) {
            updateUserDisposable.dispose();
            updateUserDisposable = null;
        }

        updateUserDisposable = userManager.updateUser(user)
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

        new AlertDialog.Builder(requireContext())
                .setMessage(R.string.EditProfile_SuccessMessageE)
                .setPositiveButton(R.string.OK, null)
                .show();
    }

    private Date stringToJavaDate(@NonNull String dateString) throws ParseException {
        dateString = dateString.trim();

        if (dateString.isEmpty())
            return null;

        return dateFormat.parse(dateString);
    }

    private String unmaskPhoneNumber(String maskedPhoneNumber) {
        return maskedPhoneNumber.trim()
                .replace(" ", "")
                .replace("(", "")
                .replace(")", "")
                .replace("-", "");
    }

    private void handleError(Throwable throwable) {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }

        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.Error)
                .setMessage(throwable.getMessage())
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
        if (user.gender == Gender.FEMALE)
        {
            Glide.with(this)
                    .load(R.drawable.avatar_female)
                    .apply(RequestOptions.circleCropTransform())
                    .into(avatar_imageView);
        } else if (user.gender == Gender.MALE) {
            Glide.with(this)
                    .load(R.drawable.avatar_male)
                    .apply(RequestOptions.circleCropTransform())
                    .into(avatar_imageView);
        } else {
            Glide.with(this)
                    .load(R.drawable.avatar_nobody)
                    .apply(RequestOptions.circleCropTransform())
                    .into(avatar_imageView);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        MainActivity activity = (MainActivity) requireActivity();

        switch (item.getItemId()) {
            case R.id.results:
                activity.replaceFragment(MyResultsFragment.newInstance());
                return true;
            case R.id.records:
                activity.replaceFragment(MyRecordsFragment.newInstance());
                return true;
        }

        return false;
    }
}
