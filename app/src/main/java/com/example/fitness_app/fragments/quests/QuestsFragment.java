package com.example.fitness_app.fragments.quests;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.fitness_app.R;
import com.example.fitness_app.adapters.AdapterOnItemClickListener;
import com.example.fitness_app.adapters.QuestsAdapter;
import com.example.fitness_app.api.FirestoreService;
import com.example.fitness_app.fragments.BaseFragment;
import com.example.fitness_app.interfaces.FirebaseCallback;
import com.example.fitness_app.models.QuestAccountEntity;

import java.util.ArrayList;
import java.util.List;

public class QuestsFragment extends BaseFragment implements AdapterOnItemClickListener {
    private List<QuestAccountEntity> quests = new ArrayList<>();
    private QuestsAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fetchQuests();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quests, container, false);

        setupProgressBar(view);
        setupSwipeToRefresh(view);
        setupAdapter(view);

        return view;
    }

    private void setupProgressBar(View view){
        ProgressBar progressBar = view.findViewById(R.id.fragment_quests_progress_bar);
        progressBar.setIndeterminate(true);
        setProgressBar(progressBar);
        if (!hasShownInitialLoading()){
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    private void setupSwipeToRefresh(View view){
        swipeRefreshLayout = view.findViewById(R.id.fragment_quests_swipe_to_refresh);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            fetchQuests();
        });
    }

    private void setupAdapter(View view){
        RecyclerView recyclerView = view.findViewById(R.id.fragment_quests_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new QuestsAdapter(getContext(), quests);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    private void fetchQuests(){
        FirestoreService.getAllAccountQuests(new FirebaseCallback() {
            @Override
            public void onSuccess(Object object) {
                quests = (List<QuestAccountEntity>) object;
                if (adapter != null){
                    adapter.setQuests(quests);
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

    @Override
    public void onItemClick(View view, int position) {
        QuestEntryFragment fragment = new QuestEntryFragment();
        fragment.setQuest(quests.get(position));
        fragmentNavigation.pushFragment(fragment);
    }
}
