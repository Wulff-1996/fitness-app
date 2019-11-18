package com.example.fitness_app.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.fitness_app.R;
import com.example.fitness_app.fragments.Track.TrackFragment;
import com.example.fitness_app.fragments.achievement.AchievementFragment;
import com.example.fitness_app.fragments.profile.ProfileFragment;
import com.example.fitness_app.fragments.quests.QuestFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;

            switch (item.getItemId()) {
                case R.id.nav_track:
                    fragment = new TrackFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.nav_quests:
                    fragment = new QuestFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.nav_achievements:
                    fragment = new AchievementFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.nav_profile:
                    fragment = new ProfileFragment();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.activity_main_bottom_navigation);
        loadFragment(new TrackFragment());
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.activity_main_fragment_canvas, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
