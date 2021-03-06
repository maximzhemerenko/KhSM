package com.khsm.app.presentation.ui.screens;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.khsm.app.R;
import com.khsm.app.data.entities.Gender;
import com.khsm.app.data.entities.Session;
import com.khsm.app.data.entities.User;
import com.khsm.app.domain.AuthManager;
import com.khsm.app.presentation.ui.screens.auth.LoginActivity;
import com.khsm.app.presentation.ui.screens.disciplines.DisciplineListFragment;
import com.khsm.app.presentation.ui.screens.meetings.MeetingListFragment;
import com.khsm.app.presentation.ui.screens.meetings.MeetingResultsFragment;
import com.khsm.app.presentation.ui.screens.news.NewsFragment;
import com.khsm.app.presentation.ui.screens.profile.EditProfileFragment;
import com.khsm.app.presentation.ui.screens.rankings.RankingsFragment;

public class MainActivity extends AppCompatActivity {
    public static Intent newIntent(Context context, boolean clearTask) {
        Intent intent = new Intent(context, MainActivity.class);
        if (clearTask) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        return intent;
    }

    private AuthManager authManager;

    private DrawerLayout drawerLayout;

    @SuppressWarnings("FieldCanBeLocal")
    private ImageView avatar_imageView;
    private TextView userName_textView;
    private MenuItem myProfileMenuItem;
    @SuppressWarnings("FieldCanBeLocal")
    private MenuItem loginMenuItem;
    @SuppressWarnings("FieldCanBeLocal")
    private MenuItem logoutMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        authManager = new AuthManager(this);

        drawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

        Menu navigationViewMenu = navigationView.getMenu();

        myProfileMenuItem = navigationViewMenu.findItem(R.id.my_profile);
        loginMenuItem = navigationViewMenu.findItem(R.id.login);
        logoutMenuItem = navigationViewMenu.findItem(R.id.logout);

        avatar_imageView = headerView.findViewById(R.id.avatar_imageView);
        userName_textView = headerView.findViewById(R.id.userName);

        navigationView.setNavigationItemSelectedListener(onNavigationItemSelectedListener);

        Session session = authManager.getSession();

        if (session == null) {
            userName_textView.setText(R.string.Main_NoAccount);
            myProfileMenuItem.setVisible(false);
            loginMenuItem.setVisible(true);
            logoutMenuItem.setVisible(false);
        } else {
            User user = session.user;
            myProfileMenuItem.setVisible(true);
            userName_textView.setText(user.firstName + " " + user.lastName);
            loginMenuItem.setVisible(false);
            logoutMenuItem.setVisible(true);
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content, MeetingResultsFragment.newInstance(null))
                    .commit();
        }

        // TODO: 26.02.2018 fix this temporary implementation
        if (session != null && session.user.gender == Gender.FEMALE)
        {
            Glide.with(this)
                    .load(R.drawable.avatar_female)
                    .apply(RequestOptions.circleCropTransform())
                    .into(avatar_imageView);
        } else if (session != null && session.user.gender == Gender.MALE) {
            Glide.with(this)
                    .load(R.drawable.avatar_male)
                    .apply(RequestOptions.circleCropTransform())
                    .into(avatar_imageView);
        } else if (session == null) {
            Glide.with(this)
                    .load(R.drawable.avatar_nobody)
                    .apply(RequestOptions.circleCropTransform())
                    .into(avatar_imageView);
        }
    }

    private NavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem menuItem) {
            menuItem.setChecked(true);

            if (menuItem.getItemId() == R.id.last_meeting) {
                replaceFragment(MeetingResultsFragment.newInstance(null));
            } else if (menuItem.getItemId() == R.id.meetings) {
                replaceFragment(MeetingListFragment.newInstance());
            } else if (menuItem.getItemId() == R.id.disciplines) {
                replaceFragment(DisciplineListFragment.newInstance());
            } else if (menuItem.getItemId() == R.id.login) {
                startActivity(LoginActivity.newIntent(MainActivity.this));
            } else if (menuItem.getItemId() == R.id.my_profile) {
                replaceFragment(EditProfileFragment.newInstance());
            } else if (menuItem.getItemId() == R.id.Rankings) {
                replaceFragment(RankingsFragment.newInstance());
            } else if (menuItem.getItemId() == R.id.news) {
                replaceFragment(NewsFragment.newInstance());
            } else if (menuItem.getItemId() == R.id.logout) {
                authManager.logout().subscribe();
                startActivity(MainActivity.newIntent(MainActivity.this, true));
            }

            drawerLayout.closeDrawers();

            return true;
        }
    };

    public void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, fragment)
                .addToBackStack(null)
                .commit();
    }

    public void showMenu() {
        drawerLayout.openDrawer(Gravity.LEFT, true);
    }
}
