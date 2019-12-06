package com.example.fitness_app.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.fitness_app.R;
import com.example.fitness_app.fragments.BaseFragment;
import com.example.fitness_app.fragments.achievements.AchievementsFragment;
import com.example.fitness_app.fragments.measurements.MeasurementSelectionFragment;
import com.example.fitness_app.fragments.profile.ProfileFragment;
import com.example.fitness_app.fragments.quests.QuestFragment;
import com.example.fitness_app.fragments.tasks.TasksFragment;
import com.google.firebase.FirebaseApp;
import com.ncapdevi.fragnav.FragNavController;
import com.roughike.bottombar.BottomBar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FragNavController.RootFragmentListener, BaseFragment.FragmentNavigation {
    private final int TASKS_INDEX = FragNavController.TAB1;
    private final int MEASURE_INDEX = FragNavController.TAB2;
    private final int QUEST_INDEX = FragNavController.TAB3;
    private final int ACHIEVEMENT_INDEX = FragNavController.TAB4;
    private final int PROFILE_INDEX = FragNavController.TAB5;
    public static FragNavController mNavController;
    private BottomBar bottomBar;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //  register this app to firebase
        FirebaseApp.initializeApp(this);

        setupToolbar();

        FragNavController.Builder builder = FragNavController.newBuilder(savedInstanceState, getSupportFragmentManager(), R.id.activity_main_fragment_canvas);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new TasksFragment());
        fragments.add(new MeasurementSelectionFragment());
        fragments.add(new QuestFragment());
        fragments.add(new AchievementsFragment());
        fragments.add(new ProfileFragment());

        builder.rootFragments(fragments);
        builder.rootFragmentListener(this, 5);
        mNavController = builder.build();
        bottomBar = findViewById(R.id.activity_main_bottom_navigation);
        bottomBar.setOnTabSelectListener(tabId -> {
            switch (tabId){
                case R.id.nav_tasks:
                    if (mNavController.isRootFragment()){
                        setToolbarTitle(getString(R.string.title_tasks));
                    }
                    mNavController.switchTab(TASKS_INDEX);
                    break;
                case R.id.nav_measure:
                    if (mNavController.isRootFragment()){
                        setToolbarTitle(getString(R.string.title_measurement));
                    }
                    mNavController.switchTab(MEASURE_INDEX);
                    break;
                case R.id.nav_quests:
                    if (mNavController.isRootFragment()){
                        setToolbarTitle(getString(R.string.title_quests));
                    }
                    mNavController.switchTab(QUEST_INDEX);
                    break;
                case R.id.nav_achievements:
                    if (mNavController.isRootFragment()){
                        setToolbarTitle(getString(R.string.title_achievements));
                    }
                    mNavController.switchTab(ACHIEVEMENT_INDEX);
                    break;
                case R.id.nav_profile:
                    if (mNavController.isRootFragment()){
                        setToolbarTitle(getString(R.string.title_profile));
                    }
                    mNavController.switchTab(PROFILE_INDEX);
                    break;
            }
        });
        bottomBar.setOnTabReselectListener(tabId -> mNavController.clearStack());
    }

    @Override
    public Fragment getRootFragment(int index) {
        switch (index) {
            case TASKS_INDEX:
                return new TasksFragment();
            case MEASURE_INDEX:
                return new MeasurementSelectionFragment();
            case QUEST_INDEX:
                return new QuestFragment();
            case ACHIEVEMENT_INDEX:
                return new AchievementsFragment();
            case PROFILE_INDEX:
                return new ProfileFragment();
        }
        throw new IllegalStateException("Need to send an index that we know");
    }

    private void setupToolbar(){
        toolbar = findViewById(R.id.application_toolbar_toolbar);
        setSupportActionBar(toolbar);
        showBackArrowOnToolBar(false);
    }

    public void setToolbarTitle(String title){
        TextView titleView = toolbar.findViewById(R.id.application_toolbar_header);
        titleView.setText(title);
    }

    public void showBackArrowOnToolBar(boolean isVisible){
        if (isVisible){
            toolbar.findViewById(R.id.application_toolbar_back_arrow).setVisibility(View.VISIBLE);
            toolbar.findViewById(R.id.application_toolbar_back_arrow).setOnClickListener(view -> {
                popFragment();
                if (mNavController.isRootFragment()){
                    toolbar.findViewById(R.id.application_toolbar_back_arrow).setVisibility(View.INVISIBLE);
                }
            });
        } else {
            toolbar.findViewById(R.id.application_toolbar_back_arrow).setVisibility(View.INVISIBLE);
        }
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

