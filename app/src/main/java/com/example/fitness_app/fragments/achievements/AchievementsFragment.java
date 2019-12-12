package com.example.fitness_app.fragments.achievements;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.fitness_app.R;
import com.example.fitness_app.adapters.AchievementsAdapter;
import com.example.fitness_app.adapters.AdapterOnItemClickListener;
import com.example.fitness_app.api.FirestoreService;
import com.example.fitness_app.fragments.BaseFragment;
import com.example.fitness_app.interfaces.FirebaseCallback;
import com.example.fitness_app.models.AchievementAccountEntity;
import com.example.fitness_app.models.AchievementEntryEntity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AchievementsFragment extends BaseFragment implements AdapterOnItemClickListener {
    private AchievementsAdapter adapter;
    private List<AchievementAccountEntity> achievements = new ArrayList<>();
    private int selectedIndex;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fetchAllAchievementsFromUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_achievement, container, false);

        setupToolbar();
        setupFab(view);
        setupSwipeToRefresh(view);
        setupAdapter(view);

        return view;
    }

    private void setupToolbar(){
    }

    private void setupFab(View view){
        FloatingActionButton fab = view.findViewById(R.id.fragment_achievements_fab);
        fab.setOnClickListener(view1 -> {

        });
    }

    private void setupSwipeToRefresh(View view){
        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.fragment_achievement_swipe_to_refresh);
        swipeRefreshLayout.setOnRefreshListener(() -> fetchAllAchievementsFromUser());
    }

    private void setupAdapter(View view){
        RecyclerView recyclerView = view.findViewById(R.id.fragment_achievements_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new AchievementsAdapter(getContext(), achievements);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        selectedIndex = position;
        AchievementEntryFragment fragment = new AchievementEntryFragment();
        fragment.setAchievement(achievements.get(position));
        fragmentNavigation.pushFragment(fragment);
    }

    private void fetchAllAchievementsFromUser(){
        showProgressBar(true);
        FirestoreService.fetchAllAchievementsByUser(new FirebaseCallback() {
            @Override
            public void onSuccess(Object object) {
                achievements = (List<AchievementAccountEntity>) object;
                if (adapter != null){
                    adapter.setAchievements(achievements);
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(FirebaseFirestoreException.Code errorCode) {
            }
            @Override
            public void onFinish() {
                showProgressBar(false);
            }
        });
    }

    //TODO to create data
    private void createAchievements(){
        AchievementEntryEntity achievement = new AchievementEntryEntity();
        achievement.setId(UUID.randomUUID().toString());
        achievement.setTitle("Completed a task once");
        achievement.setDescription("To complete this achievement you will have to complete any task once");
        achievement.setAchievementPoints(100L);
        achievement.setType("once");
        achievement.setAmountToCompleteCount(1L);
        achievement.setTotalPlayersCompleted(0L);
        achievement.setWhenToUpdate("tasks");

        AchievementEntryEntity achievement2 = new AchievementEntryEntity();
        achievement2.setId(UUID.randomUUID().toString());
        achievement2.setTitle("Completed a task 3 times");
        achievement2.setDescription("To complete this achievement you will have to complete any task of a total of 3 times");
        achievement2.setAchievementPoints(200L);
        achievement2.setType("total");
        achievement2.setAmountToCompleteCount(3L);
        achievement2.setTotalPlayersCompleted(0L);
        achievement2.setWhenToUpdate("tasks");

        AchievementEntryEntity achievement3 = new AchievementEntryEntity();
        achievement3.setId(UUID.randomUUID().toString());
        achievement3.setTitle("Completed a task 3 times in a row");
        achievement3.setDescription("To complete this achievement you will have to complete any task 3 days in a row, you cannot combine tasks, it have to be the same task");
        achievement3.setAchievementPoints(300L);
        achievement3.setType("streak");
        achievement3.setAmountToCompleteCount(3L);
        achievement3.setTotalPlayersCompleted(0L);
        achievement3.setWhenToUpdate("tasks");

        FirestoreService.addAchievement(achievement, new FirebaseCallback() {
            @Override
            public void onSuccess(Object object) {
                Toast.makeText(getContext(), "completed 1", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(FirebaseFirestoreException.Code errorCode) {

            }

            @Override
            public void onFinish() {

            }
        });

        FirestoreService.addAchievement(achievement2, new FirebaseCallback() {
            @Override
            public void onSuccess(Object object) {
                Toast.makeText(getContext(), "completed 3", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(FirebaseFirestoreException.Code errorCode) {

            }

            @Override
            public void onFinish() {

            }
        });

        FirestoreService.addAchievement(achievement3, new FirebaseCallback() {
            @Override
            public void onSuccess(Object object) {
                Toast.makeText(getContext(), "completed 3", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(FirebaseFirestoreException.Code errorCode) {

            }

            @Override
            public void onFinish() {

            }
        });
    }
}
