package com.example.fitness_app.fragments.achievements;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.fitness_app.R;
import com.example.fitness_app.adapters.AchievementsManagementAdapter;
import com.example.fitness_app.adapters.AdapterOnItemClickListener;
import com.example.fitness_app.api.FirestoreService;
import com.example.fitness_app.fragments.BaseFragment;
import com.example.fitness_app.interfaces.FirebaseCallback;
import com.example.fitness_app.models.AchievementEntryEntity;
import com.example.fitness_app.models.EventBustEvent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import static com.example.fitness_app.constrants.AchievementManagementMode.ACHIEVEMENT_DETAILS;
import static com.example.fitness_app.constrants.AchievementManagementMode.ADDING_NEW_ACHIEVEMENT;
import static com.example.fitness_app.constrants.EventBusEvents.EVENT_BUS_EVENT_ACHIEVEMENT_ADDED;
import static com.example.fitness_app.constrants.EventBusEvents.EVENT_BUS_EVENT_ACHIEVEMENT_DELETED;

public class AchievementManagementFragment extends BaseFragment implements AdapterOnItemClickListener {
    private AchievementsManagementAdapter adapter;
    private List<AchievementEntryEntity> achievements = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private int selectedPosition;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fetchAllAchievements();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_achievement_management, container, false);

        setupProgressBar(view);
        setupSwipeToRefresh(view);
        setupFab(view);
        setupAdapter(view);

        return view;
    }

    private void setupProgressBar(View view) {
        ProgressBar progressBar = view.findViewById(R.id.fragment_achievements_management_progressbar);
        progressBar.setIndeterminate(true);
        setProgressBar(progressBar);
        if (!hasShownInitialLoading()) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    private void setupSwipeToRefresh(View view) {
        swipeRefreshLayout = view.findViewById(R.id.fragment_achievements_management_swipe_to_refresh);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            fetchAllAchievements();
        });
    }

    private void setupFab(View view) {
        FloatingActionButton floatingActionButton = view.findViewById(R.id.fragment_achievements_management_fab);
        floatingActionButton.setOnClickListener(view1 -> {
            handleAddAchievement();
        });
    }

    private void setupAdapter(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.fragment_achievements_management_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new AchievementsManagementAdapter(getContext(), achievements);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    private void fetchAllAchievements() {
        FirestoreService.getAllAchievements(new FirebaseCallback() {
            @Override
            public void onSuccess(Object object) {
                achievements = (List<AchievementEntryEntity>) object;
                adapter.setAchievements(achievements);
                adapter.notifyDataSetChanged();
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

    private void handleAddAchievement() {
        AchievementManagementEntryFragment fragment = new AchievementManagementEntryFragment();
        fragment.setMode(ADDING_NEW_ACHIEVEMENT);
        fragmentNavigation.pushFragment(fragment);
    }


    @Override
    public void onItemClick(View view, int position) {
        selectedPosition = position;
        AchievementManagementEntryFragment fragment = new AchievementManagementEntryFragment();
        fragment.setMode(ACHIEVEMENT_DETAILS);
        fragment.setAchievement(achievements.get(position));
        fragmentNavigation.pushFragment(fragment);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBustEvent<Object> event) {
        AchievementEntryEntity achievement = (AchievementEntryEntity) event.getData();

        switch (event.getEvent()) {
            case EVENT_BUS_EVENT_ACHIEVEMENT_ADDED:
                achievements.add(achievement);
                adapter.notifyItemInserted(adapter.getItemCount());
                break;
            case EVENT_BUS_EVENT_ACHIEVEMENT_DELETED:
                achievements.remove(achievement);
                adapter.notifyItemRemoved(selectedPosition);
                break;
        }
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }
}
