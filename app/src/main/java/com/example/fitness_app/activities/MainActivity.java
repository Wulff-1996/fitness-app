package com.example.fitness_app.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import com.example.fitness_app.R;
import com.example.fitness_app.fragments.BaseFragment;
import com.example.fitness_app.fragments.Track.TrackCategoriesFragment;
import com.example.fitness_app.fragments.achievement.AchievementFragment;
import com.example.fitness_app.fragments.profile.ProfileFragment;
import com.example.fitness_app.fragments.quests.QuestFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ncapdevi.fragnav.FragNavController;
import com.ncapdevi.fragnav.FragNavTransactionOptions;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FragNavController.RootFragmentListener, BaseFragment.FragmentNavigation {
    private final int TRACK_INDEX = FragNavController.TAB1;
    private final int QUEST_INDEX = FragNavController.TAB2;
    private final int ACHIEVEMENT_INDEX = FragNavController.TAB3;
    private final int PROFILE_INDEX = FragNavController.TAB4;
    private FragNavController mNavController;
    private BottomBar bottomBar;

    private MainActivity getThis(){return this;}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragNavController.Builder builder = FragNavController.newBuilder(savedInstanceState, getSupportFragmentManager(), R.id.activity_main_fragment_canvas);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new TrackCategoriesFragment());
        fragments.add(new QuestFragment());
        fragments.add(new AchievementFragment());
        fragments.add(new ProfileFragment());

        builder.rootFragments(fragments);
        builder.rootFragmentListener(this, 4);
        mNavController = builder.build();
        bottomBar = findViewById(R.id.activity_main_bottom_navigation);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(int tabId) {
                switch (tabId){
                    case R.id.nav_track:
                        mNavController.switchTab(TRACK_INDEX);
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
            case TRACK_INDEX:
                return new TrackCategoriesFragment();
            case QUEST_INDEX:
                return new QuestFragment();
            case ACHIEVEMENT_INDEX:
                return new AchievementFragment();
            case PROFILE_INDEX:
                return new ProfileFragment();
        }
        throw new IllegalStateException("Need to send an index that we know");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
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
    public void pushFragment(Fragment fragment, List<androidx.core.util.Pair<View, String>> sharedElementList) {
        if (sharedElementList != null){
            FragNavTransactionOptions.Builder options = FragNavTransactionOptions.newBuilder();
            for (Pair<View, String> pair: sharedElementList) {
                options.addSharedElement(pair);
            }
            mNavController.pushFragment(fragment, options.build());
        } else {
            mNavController.pushFragment(fragment);
        }
    }

    @Override
    public void popFragment() {
        mNavController.popFragment();
    }
}

