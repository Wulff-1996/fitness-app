package com.example.fitness_app.activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.fitness_app.R;
import com.example.fitness_app.fragments.BaseFragment;
import com.example.fitness_app.fragments.achievements.AchievementsFragment;
import com.example.fitness_app.fragments.measurements.MeasurementsFragment;
import com.example.fitness_app.fragments.profile.ProfileFragment;
import com.example.fitness_app.fragments.quests.QuestFragment;
import com.example.fitness_app.fragments.tasks.TasksFragment;
import com.google.firebase.FirebaseApp;
import com.ncapdevi.fragnav.FragNavController;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FragNavController.RootFragmentListener, BaseFragment.FragmentNavigation {
    private final int TASKS_INDEX = FragNavController.TAB1;
    private final int MEASURE_INDEX = FragNavController.TAB2;
    private final int QUEST_INDEX = FragNavController.TAB3;
    private final int ACHIEVEMENT_INDEX = FragNavController.TAB4;
    private final int PROFILE_INDEX = FragNavController.TAB5;
    private FragNavController mNavController;
    private BottomBar bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //  register this app to firebase
        FirebaseApp.initializeApp(this);

        FragNavController.Builder builder = FragNavController.newBuilder(savedInstanceState, getSupportFragmentManager(), R.id.activity_main_fragment_canvas);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new TasksFragment());
        fragments.add(new MeasurementsFragment());
        fragments.add(new QuestFragment());
        fragments.add(new AchievementsFragment());
        fragments.add(new ProfileFragment());

        builder.rootFragments(fragments);
        builder.rootFragmentListener(this, 5);
        mNavController = builder.build();
        bottomBar = findViewById(R.id.activity_main_bottom_navigation);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(int tabId) {
                switch (tabId){
                    case R.id.nav_tasks:
                        mNavController.switchTab(TASKS_INDEX);
                        break;
                    case R.id.nav_measure:
                        mNavController.switchTab(MEASURE_INDEX);
                        break;
                    case R.id.nav_quests:
                        mNavController.switchTab(QUEST_INDEX);
                        break;
                    case R.id.nav_achievements:
                        mNavController.switchTab(ACHIEVEMENT_INDEX);
                        break;
                    case R.id.nav_profile:
                        mNavController.switchTab(PROFILE_INDEX);
                        break;
                }
            }
        });
        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(int tabId) {
                mNavController.clearStack();
            }
        });
    }

    @Override
    public Fragment getRootFragment(int index) {
        switch (index) {
            case TASKS_INDEX:
                return new TasksFragment();
            case MEASURE_INDEX:
                return new MeasurementsFragment();
            case QUEST_INDEX:
                return new QuestFragment();
            case ACHIEVEMENT_INDEX:
                return new AchievementsFragment();
            case PROFILE_INDEX:
                return new ProfileFragment();
        }
        throw new IllegalStateException("Need to send an index that we know");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mNavController != null) {
            mNavController.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onBackPressed() {
        if (mNavController.isRootFragment()) {
            super.onBackPressed();
        } else {
            mNavController.popFragment();
        }
    }

    @Override
    public void pushFragment(Fragment fragment) {
        mNavController.pushFragment(fragment);
    }

    @Override
    public void popFragment() {
        mNavController.popFragment();
    }
}

