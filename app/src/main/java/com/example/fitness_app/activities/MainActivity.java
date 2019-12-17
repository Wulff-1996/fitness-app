package com.example.fitness_app.activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.fitness_app.R;
import com.example.fitness_app.constrants.ApplicationMode;
import com.example.fitness_app.constrants.IntentKeys;
import com.example.fitness_app.fragments.BaseFragment;
import com.example.fitness_app.fragments.achievements.AchievementsFragment;
import com.example.fitness_app.fragments.achievements.AchievementsManagementViewPager;
import com.example.fitness_app.fragments.measurements.MeasurementSelectionFragment;
import com.example.fitness_app.fragments.profile.ProfileFragment;
import com.example.fitness_app.fragments.quests.QuestFragment;
import com.example.fitness_app.fragments.quests.QuestManagementFragment;
import com.example.fitness_app.fragments.tasks.TasksFragment;
import com.example.fitness_app.models.Account;
import com.google.firebase.FirebaseApp;
import com.ncapdevi.fragnav.FragNavController;
import com.roughike.bottombar.BottomBar;

import java.util.ArrayList;
import java.util.List;

import static com.example.fitness_app.constrants.BottomBarIndexes.ACHIEVEMENT_INDEX;
import static com.example.fitness_app.constrants.BottomBarIndexes.MEASURE_INDEX;
import static com.example.fitness_app.constrants.BottomBarIndexes.PROFILE_INDEX;
import static com.example.fitness_app.constrants.BottomBarIndexes.QUEST_INDEX;
import static com.example.fitness_app.constrants.BottomBarIndexes.SUPER_USER_ACHIEVEMENT_REQUESTS_INDEX;
import static com.example.fitness_app.constrants.BottomBarIndexes.SUPER_USER_PROFILE_INDEX;
import static com.example.fitness_app.constrants.BottomBarIndexes.SUPER_USER_QUESTS_INDEX;
import static com.example.fitness_app.constrants.BottomBarIndexes.TASKS_INDEX;

public class MainActivity extends BaseActivity implements FragNavController.RootFragmentListener, BaseFragment.FragmentNavigation {
    public static FragNavController mNavController;
    private BottomBar bottomBar;
    private ApplicationMode applicationMode;
    private Account account;
    private FragNavController.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //  register this app to firebase
        FirebaseApp.initializeApp(this);
        builder = FragNavController.newBuilder(savedInstanceState, getSupportFragmentManager(), R.id.activity_main_fragment_canvas);
        bottomBar = findViewById(R.id.activity_main_bottom_navigation);
        applicationMode = (ApplicationMode) getIntent().getSerializableExtra(IntentKeys.INTENT_KEY_APPLICATION_MODE);

        if (applicationMode == ApplicationMode.APPLICATION_USERS){
            handleUserApp();
        } else {
            handleSuperUserApp();
        }
    }

    private void handleUserApp(){
        bottomBar.setItems(R.xml.menu_bottom_bar);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new TasksFragment());
        fragments.add(new MeasurementSelectionFragment());
        fragments.add(new QuestFragment());
        fragments.add(new AchievementsFragment());
        fragments.add(new ProfileFragment());

        builder.rootFragments(fragments);
        builder.rootFragmentListener(this, 5);
        mNavController = builder.build();

        bottomBar.setOnTabSelectListener(tabId -> {
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
        });
        bottomBar.setOnTabReselectListener(tabId -> mNavController.clearStack());
    }

    private void handleSuperUserApp(){
        bottomBar.setItems(R.xml.app_super_user_bottom_bar_menu);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new QuestManagementFragment());
        fragments.add(new AchievementsManagementViewPager());
        fragments.add(new ProfileFragment());

        builder.rootFragments(fragments);
        builder.rootFragmentListener(this, 3);
        mNavController = builder.build();

        bottomBar.setOnTabSelectListener(tabId -> {
            switch (tabId){
                case R.id.nav_super_user_quests:
                    mNavController.switchTab(SUPER_USER_QUESTS_INDEX);
                    break;
                case R.id.nav_super_user_achievements:
                    mNavController.switchTab(SUPER_USER_ACHIEVEMENT_REQUESTS_INDEX);
                    break;
                case R.id.nav_super_user_profile:
                    mNavController.switchTab(SUPER_USER_PROFILE_INDEX);
                    break;
            }
        });
        bottomBar.setOnTabReselectListener(tabId -> mNavController.clearStack());
    }

    @Override
    public Fragment getRootFragment(int index) {
        if (applicationMode == ApplicationMode.APPLICATION_USERS){
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
        } else {
            switch (index){
                case SUPER_USER_QUESTS_INDEX:
                    return new QuestManagementFragment();
                case SUPER_USER_ACHIEVEMENT_REQUESTS_INDEX:
                    return new AchievementsManagementViewPager();
                case SUPER_USER_PROFILE_INDEX:
                    return new ProfileFragment();
            }
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

