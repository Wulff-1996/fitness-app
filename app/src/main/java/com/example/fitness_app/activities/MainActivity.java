package com.example.fitness_app.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.fitness_app.R;
import com.example.fitness_app.constrants.TrackNavigationState;
import com.example.fitness_app.fragments.Track.TrackCategoriesFragment;
import com.example.fitness_app.fragments.Track.TrackFragment;
import com.example.fitness_app.fragments.achievement.AchievementFragment;
import com.example.fitness_app.fragments.profile.ProfileFragment;
import com.example.fitness_app.fragments.quests.QuestFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements MyNavigationDelegate {

    private Fragment selectedFragment;
    private TrackNavigationState trackNavigationState = TrackNavigationState.NOT_SELECTED;
    private MainActivity mInstance;
    private BottomNavigationView bottomNavigationView;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.nav_track:
                    TrackCategoriesFragment frag = new TrackCategoriesFragment();
                    frag.setDelegate(mInstance);
                    selectedFragment = frag;
                    loadFragment(selectedFragment);
                    return true;
                case R.id.nav_quests:
                    selectedFragment = new QuestFragment();
                    loadFragment(selectedFragment);
                    return true;
                case R.id.nav_achievements:
                    selectedFragment = new AchievementFragment();
                    loadFragment(selectedFragment);
                    return true;
                case R.id.nav_profile:
                    selectedFragment = new ProfileFragment();
                    loadFragment(selectedFragment);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mInstance = this;

        bottomNavigationView = findViewById(R.id.activity_main_bottom_navigation);

        TrackCategoriesFragment startFragment = new TrackCategoriesFragment();
        startFragment.setDelegate(mInstance);

        loadFragment(startFragment);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    /**
     * handle navigation state of track
     * used to choose witch fragment of the Track use cases that will be shown
     */
    private void handleTrackNavigation(){
        switch (trackNavigationState){
            case NOT_SELECTED:
                selectedFragment = new TrackCategoriesFragment();
                loadFragment(selectedFragment);
                break;
            case SELECTED:
        }
    }

    /**
     * load the selected fragment into the fragment canvas
     * @param fragment the selected fragment
     */
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.activity_main_fragment_canvas, fragment);
        transaction.commit();
    }

    public void changeFragmentForSameNavigationPoint(Fragment fragment){

    }


    @Override
    public void changeFragment(Fragment fragment) {
        loadFragment(fragment);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {}
}

