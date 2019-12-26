package com.example.fitness_app.fragments.quests;

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
import com.example.fitness_app.adapters.AdapterOnItemClickListener;
import com.example.fitness_app.adapters.QuestsApprovalAdapter;
import com.example.fitness_app.api.FirestoreService;
import com.example.fitness_app.fragments.BaseFragment;
import com.example.fitness_app.interfaces.FirebaseCallback;
import com.example.fitness_app.models.EventBustEvent;
import com.example.fitness_app.models.QuestApprovalRequestEntity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import static com.example.fitness_app.constrants.EventBusEvents.EVENT_BUS_EVENT_QUEST_REQUEST_APPROVED;
import static com.example.fitness_app.constrants.EventBusEvents.EVENT_BUS_EVENT_QUEST_REQUEST_DECLINED;

public class QuestsApprovalFragment extends BaseFragment implements AdapterOnItemClickListener {
    private QuestsApprovalAdapter adapter;
    private List<QuestApprovalRequestEntity> requests = new ArrayList<>();
    private int selectedIndex;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fetchRequests();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_quest_approval_requests, container, false);

        setupProgressBar(view);
        setupSwipeToRefresh(view);
        setupAdapter(view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showMessage(requests.size() == 0, view);
    }

    private void setupProgressBar(View view){
        ProgressBar progressBar = view.findViewById(R.id.fragment_quest_approval_progress);
        progressBar.setIndeterminate(true);
        setProgressBar(progressBar);
        if (!hasShownInitialLoading()){
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    private void setupSwipeToRefresh(View view){
        swipeRefreshLayout = view.findViewById(R.id.fragment_quest_approval_swipe_to_refresh);
        swipeRefreshLayout.setOnRefreshListener(() -> fetchRequests());
    }

    private void setupAdapter(View view){
        RecyclerView recyclerView = view.findViewById(R.id.fragment_quest_approval_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new QuestsApprovalAdapter(getContext(), requests);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    private void fetchRequests(){
        FirestoreService.getAllQuestApprovalRequests(new FirebaseCallback() {
            @Override
            public void onSuccess(Object object) {
                requests = (List<QuestApprovalRequestEntity>) object;
                adapter.setRequests(requests);
                adapter.notifyDataSetChanged();
                showMessage(requests.size() == 0, getView());
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
        selectedIndex = position;
        QuestApprovalEntryFragment fragment = new QuestApprovalEntryFragment();
        fragment.setRequest(adapter.getRequests().get(position));
        fragmentNavigation.pushFragment(fragment);
    }

    private void showMessage(boolean isVisible, View view){
        if (view == null) return;
        TextView message = view.findViewById(R.id.fragment_quest_approval_message);
        if (isVisible){
            message.setVisibility(View.VISIBLE);
        } else {
            message.setVisibility(View.GONE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBustEvent<Object> event){
        switch (event.getEvent()){
            case EVENT_BUS_EVENT_QUEST_REQUEST_APPROVED:
            case EVENT_BUS_EVENT_QUEST_REQUEST_DECLINED:
                QuestApprovalRequestEntity request = (QuestApprovalRequestEntity) event.getData();
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
