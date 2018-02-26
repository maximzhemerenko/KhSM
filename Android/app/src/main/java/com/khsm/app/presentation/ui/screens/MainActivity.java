package com.khsm.app.presentation.ui.screens;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.khsm.app.R;
import com.khsm.app.presentation.ui.screens.disciplines.DisciplineListFragment;
import com.khsm.app.presentation.ui.screens.meetings.MeetingListFragment;
import com.khsm.app.presentation.ui.screens.meetings.MeetingResultsFragment;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    @SuppressWarnings("FieldCanBeLocal")
    private ImageView avatar_imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

        avatar_imageView = headerView.findViewById(R.id.avatar_imageView);

        navigationView.setNavigationItemSelectedListener(onNavigationItemSelectedListener);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content, MeetingResultsFragment.newInstance(null))
                    .commit();
        }

        // TODO: 26.02.2018 fix this temporary implementation
        Glide.with(this)
                .load("http://animals.yakohl.com/pic/schneeeule-2592013.jpg")
                .apply(RequestOptions.circleCropTransform())
                .into(avatar_imageView);
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
            }

            drawerLayout.closeDrawers();

            return true;
        }
    };

    public void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                .replace(R.id.content, fragment)
                .addToBackStack(null)
                .commit();
    }
}
