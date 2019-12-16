package com.example.fitness_app.fragments.achievements;

import android.content.Context;
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
import com.example.fitness_app.entities.EventBustEvent;
import com.example.fitness_app.fragments.BaseFragment;
import com.example.fitness_app.interfaces.FirebaseCallback;
import com.example.fitness_app.models.AchievementApprovalRequest;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import static com.example.fitness_app.constrants.EventBusEvents.EVENT_BUS_EVENT_ACHIEVEMENT_REQUEST_APPROVED;
import static com.example.fitness_app.constrants.EventBusEvents.EVENT_BUS_EVENT_ACHIEVEMENT_REQUEST_DECLINED;

public class AchievementsApprovalFragment extends BaseFragment implements AdapterOnItemClickListener {
    private AchievementsApprovalAdapter adapter;
    private List<AchievementApprovalRequest> requests = new ArrayList<>();
    private int selectedIndex;
    private boolean isFetching = false;


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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isFetching){
            showMessage(requests.size() == 0, view);
        }
    }

    private void setupProgressBar(View view){
        ProgressBar progressBar = view.findViewById(R.id.fragment_achievement_approval_preogress);
        progressBar.setIndeterminate(true);
        setProgressBar(progressBar);
    }

    private void setupSwipeToRefresh(View view){
        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.fragment_achievement_approval_swipe_to_refresh);
        swipeRefreshLayout.setOnRefreshListener(() -> fetchRequests());
    }

    private void endRefreshing(){
        if (getView() == null) return;
        SwipeRefreshLayout swipeRefreshLayout = getView().findViewById(R.id.fragment_achievement_approval_swipe_to_refresh);
        swipeRefreshLayout.setRefreshing(false);
    }

    private void setupAdapter(View view){
        RecyclerView recyclerView = view.findViewById(R.id.fragment_achievement_approval_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new AchievementsApprovalAdapter(getContext(), requests);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    private void fetchRequests(){
        setFetching(true);
        FirestoreService.getAllAchievementApprovalRequests(new FirebaseCallback() {
            @Override
            public void onSuccess(Object object) {
                requests = (List<AchievementApprovalRequest>) object;
                adapter.setRequests(requests);
                adapter.notifyDataSetChanged();
                showMessage(requests.size() == 0, getView());
            }

            @Override
            public void onFailure(Exception e) {

            }

            @Override
            public void onFinish() {
                setFetching(false);
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        selectedIndex = position;
        AchievementApprovalEntryFragment fragment = new AchievementApprovalEntryFragment();
        fragment.setRequest(adapter.getRequests().get(position));
        fragmentNavigation.pushFragment(fragment);
    }

    private void showMessage(boolean isVisible, View view){
        if (view == null) return;
        TextView message = view.findViewById(R.id.fragment_achievement_approval_message);
        if (isVisible){
            message.setVisibility(View.VISIBLE);
        } else {
            message.setVisibility(View.GONE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBustEvent<Object> event){
        switch (event.getEvent()){
            case EVENT_BUS_EVENT_ACHIEVEMENT_REQUEST_APPROVED:
            case EVENT_BUS_EVENT_ACHIEVEMENT_REQUEST_DECLINED:
                AchievementApprovalRequest request = (AchievementApprovalRequest) event.getData();
                requests.remove(request);
                adapter.notifyItemRemoved(selectedIndex);
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
