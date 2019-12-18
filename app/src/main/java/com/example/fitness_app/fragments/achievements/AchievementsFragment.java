package com.example.fitness_app.fragments.achievements;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.fitness_app.R;
import com.example.fitness_app.adapters.AchievementsAdapter;
import com.example.fitness_app.adapters.AdapterOnItemClickListener;
import com.example.fitness_app.api.FirestoreService;
import com.example.fitness_app.constrants.AchievementTypes;
import com.example.fitness_app.constrants.AchievementUpdateEvents;
import com.example.fitness_app.fragments.BaseFragment;
import com.example.fitness_app.interfaces.FirebaseCallback;
import com.example.fitness_app.models.AchievementAccountEntity;
import com.example.fitness_app.models.AchievementEntryEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AchievementsFragment extends BaseFragment implements AdapterOnItemClickListener {
    private AchievementsAdapter adapter;
    private List<AchievementAccountEntity> achievements = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fetchAllAchievementsFromUser();
        //createAchievements(); //TODO remove
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_achievement, container, false);

        setupProgressBar(view);
        setupSwipeToRefresh(view);
        setupAdapter(view);

        return view;
    }

    private void setupProgressBar(View view){
        ProgressBar progressBar = view.findViewById(R.id.fragment_achievement_progress_bar);
        progressBar.setIndeterminate(true);
        setProgressBar(progressBar);
        if (!isHasShownInitialLoading()){
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    private void setupSwipeToRefresh(View view){
        swipeRefreshLayout = view.findViewById(R.id.fragment_achievements_swipe_to_refresh);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            fetchAllAchievementsFromUser();
        });
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
        AchievementEntryFragment fragment = new AchievementEntryFragment();
        fragment.setAchievement(achievements.get(position));
        fragmentNavigation.pushFragment(fragment);
    }

    private void fetchAllAchievementsFromUser(){
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
            public void onFailure(Exception e) {
            }
            @Override
            public void onFinish() {
                swipeRefreshLayout.setRefreshing(false);
                setFetching(false);
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
        achievement.setType(AchievementTypes.AUTOMATIC);
        achievement.setAmountToCompleteCount(1L);
        achievement.setTotalPlayersCompleted(0L);
        List<String> updates = new ArrayList<>();
        updates.add(AchievementUpdateEvents.TASKS);
        updates.add(AchievementUpdateEvents.ONCE);
        updates.add(AchievementUpdateEvents.COMPLETE);
        achievement.setWhenToUpdate(updates);
        achievement.setDateCreated(System.currentTimeMillis());

        FirestoreService.addAchievement(achievement, new FirebaseCallback() {
            @Override
            public void onSuccess(Object object) {
                Toast.makeText(getContext(), "completed 1", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Exception e) {

            }

            @Override
            public void onFinish() {

            }
        });

        AchievementEntryEntity achievement2 = new AchievementEntryEntity();
        achievement2.setId(UUID.randomUUID().toString());
        achievement2.setTitle("Completed a task 3 times");
        achievement2.setDescription("To complete this achievement you will have to complete any task of a total of 3 times");
        achievement2.setAchievementPoints(200L);
        achievement2.setType(AchievementTypes.AUTOMATIC);
        achievement2.setAmountToCompleteCount(3L);
        achievement2.setTotalPlayersCompleted(0L);
        List<String> updates2 = new ArrayList<>();
        updates2.add(AchievementUpdateEvents.TASKS);
        updates2.add(AchievementUpdateEvents.TOTAL);
        updates2.add(AchievementUpdateEvents.COMPLETE);
        achievement2.setWhenToUpdate(updates2);
        achievement2.setDateCreated(System.currentTimeMillis());

        FirestoreService.addAchievement(achievement2, new FirebaseCallback() {
            @Override
            public void onSuccess(Object object) {
                Toast.makeText(getContext(), "completed 2", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Exception e) {

            }

            @Override
            public void onFinish() {

            }
        });

        AchievementEntryEntity achievement3 = new AchievementEntryEntity();
        achievement3.setId(UUID.randomUUID().toString());
        achievement3.setTitle("Like our facebook page");
        achievement3.setDescription("To complete this achievement you will have to visit out facebook page 'some facebook page', and place a like.");
        achievement3.setAchievementPoints(500L);
        achievement3.setType(AchievementTypes.MANUAL);
        achievement3.setTotalPlayersCompleted(0L);
        List<String> updates3 = new ArrayList<>();
        updates3.add(AchievementUpdateEvents.MANUAL);
        achievement3.setWhenToUpdate(updates3);
        achievement3.setDateCreated(System.currentTimeMillis());

        FirestoreService.addAchievement(achievement3, new FirebaseCallback() {
            @Override
            public void onSuccess(Object object) {
                Toast.makeText(getContext(), "completed 3", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Exception e) {

            }

            @Override
            public void onFinish() {

            }
        });

        AchievementEntryEntity achievement4 = new AchievementEntryEntity();
        achievement4.setId(UUID.randomUUID().toString());
        achievement4.setTitle("Complete a task 3 days in a row");
        achievement4.setDescription("To complete this achievement you will have to complete any task for a total of three days in a row.");
        achievement4.setAchievementPoints(400L);
        achievement4.setType(AchievementTypes.AUTOMATIC);
        achievement4.setTotalPlayersCompleted(0L);
        achievement4.setAmountToCompleteCount(4L);
        List<String> updates4 = new ArrayList<>();
        updates4.add(AchievementUpdateEvents.TASKS);
        updates4.add(AchievementUpdateEvents.STREAK);
        updates4.add(AchievementUpdateEvents.COMPLETE);
        achievement4.setWhenToUpdate(updates4);
        achievement4.setDateCreated(System.currentTimeMillis());

        FirestoreService.addAchievement(achievement4, new FirebaseCallback() {
            @Override
            public void onSuccess(Object object) {
                Toast.makeText(getContext(), "completed 4", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Exception e) {

            }

            @Override
            public void onFinish() {

            }
        });

        AchievementEntryEntity achievement5 = new AchievementEntryEntity();
        achievement5.setId(UUID.randomUUID().toString());
        achievement5.setTitle("Invite a friend to this application");
        achievement5.setDescription("To complete this achievement invite a friend to join this app. Write the email of the friend you have added. This is so that we cant approve your request.");
        achievement5.setAchievementPoints(800L);
        achievement5.setType(AchievementTypes.MANUAL);
        achievement5.setTotalPlayersCompleted(0L);
        achievement5.setAmountToCompleteCount(null);
        List<String> updates5 = new ArrayList<>();
        updates5.add(AchievementUpdateEvents.MANUAL);
        achievement5.setWhenToUpdate(updates5);
        achievement5.setDateCreated(System.currentTimeMillis());

        FirestoreService.addAchievement(achievement5, new FirebaseCallback() {
            @Override
            public void onSuccess(Object object) {
                Toast.makeText(getContext(), "completed 5", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Exception e) {

            }

            @Override
            public void onFinish() {

            }
        });
    }
}
