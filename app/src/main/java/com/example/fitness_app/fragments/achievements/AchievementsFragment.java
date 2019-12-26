package com.example.fitness_app.fragments.achievements;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.List;

public class AchievementsFragment extends BaseFragment implements AdapterOnItemClickListener {
    private AchievementsAdapter adapter;
    private List<AchievementAccountEntity> achievements = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fetchAllAchievementsFromUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_achievement, container, false);

        setupProgressBar(view);
        setupSwipeToRefresh(view);
        setupAdapter(view);
        showMessage(achievements.size() == 0, view);

        return view;
    }

    private void setupProgressBar(View view){
        ProgressBar progressBar = view.findViewById(R.id.fragment_achievement_progress_bar);
        progressBar.setIndeterminate(true);
        setProgressBar(progressBar);
        if (!hasShownInitialLoading()){
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
                showMessage(achievements.size() == 0, getView());
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

    private void showMessage(boolean isVisible, View view){
        if (view == null) return;
        TextView message = view.findViewById(R.id.fragment_achievement_message);
        if (isVisible){
            message.setVisibility(View.VISIBLE);
        } else {
            message.setVisibility(View.GONE);
        }
    }
}
