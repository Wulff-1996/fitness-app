package com.example.fitness_app.fragments.achievements;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.fitness_app.R;
import com.example.fitness_app.adapters.AchievementsApprovalAdapter;
import com.example.fitness_app.adapters.AdapterOnItemClickListener;
import com.example.fitness_app.api.FirestoreService;
import com.example.fitness_app.fragments.BaseFragment;
import com.example.fitness_app.interfaces.FirebaseCallback;
import com.example.fitness_app.models.AchievementApprovalRequest;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;

public class AchievementsApprovalFragment extends BaseFragment implements AdapterOnItemClickListener {
    private AchievementsApprovalAdapter adapter;
    private List<AchievementApprovalRequest> requests = new ArrayList<>();
    private int selectedIndex;
    private ProgressBar progressBar;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fetchRequests();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_achievement_approval_requests, container, false);

        setupProgressBar(view);
        setupSwipeToRefresh(view);
        setupAdapter(view);

        return view;
    }

    private void setupProgressBar(View view){
        progressBar = view.findViewById(R.id.fragment_achievement_approval_preogress);
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void setupSwipeToRefresh(View view){
        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.fragment_achievement_approval_swipe_to_refresh);
        swipeRefreshLayout.setOnRefreshListener(() -> fetchRequests());
    }

    private void setupAdapter(View view){
        RecyclerView recyclerView = view.findViewById(R.id.fragment_achievement_approval_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new AchievementsApprovalAdapter(getContext(), requests);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    private void fetchRequests(){
        FirestoreService.getAllAchievementApprovalRequests(new FirebaseCallback() {
            @Override
            public void onSuccess(Object object) {
                requests = (List<AchievementApprovalRequest>) object;
                if (requests.size() == 0){
                    showMessage(true);
                }
                adapter.setRequests(requests);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(FirebaseFirestoreException.Code errorCode) {

            }

            @Override
            public void onFinish() {
                showProgress(false);
            }
        });
    }

    private void showProgress(boolean show){
        if (show){
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    private void showMessage(boolean isVisible){
        TextView message = getView().findViewById(R.id.fragment_achievement_approval_message);
        if (isVisible){
            message.setVisibility(View.VISIBLE);
        } else {
            message.setVisibility(View.GONE);
        }
    }
}
