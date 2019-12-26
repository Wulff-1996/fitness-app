package com.example.fitness_app.fragments.quests;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.fitness_app.R;
import com.example.fitness_app.adapters.AdapterOnItemClickListener;
import com.example.fitness_app.adapters.QuestManagementAdapter;
import com.example.fitness_app.api.FirestoreService;
import com.example.fitness_app.constrants.QuestManagementEntryModes;
import com.example.fitness_app.fragments.BaseFragment;
import com.example.fitness_app.interfaces.FirebaseCallback;
import com.example.fitness_app.models.EventBustEvent;
import com.example.fitness_app.models.QuestEntity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import static com.example.fitness_app.constrants.EventBusEvents.EVENT_BUS_EVENT_QUEST_ADDED;
import static com.example.fitness_app.constrants.EventBusEvents.EVENT_BUS_EVENT_QUEST_DELETED;

public class QuestManagementFragment extends BaseFragment implements AdapterOnItemClickListener {
    private List<QuestEntity> quests = new ArrayList<>();
    private QuestManagementAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int selectedPosition;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fetchQuests();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_quest_management, container, false);

        setupProgressBar(view);
        setupSwipeToRefresh(view);
        setupFab(view);
        setupAdapter(view);

        showMessage(quests.size() == 0, view);

        return view;
    }

    private void setupProgressBar(View view) {
        ProgressBar progressBar = view.findViewById(R.id.fragment_quests_management_progressbar);
        progressBar.setIndeterminate(true);
        setProgressBar(progressBar);
        if (!hasShownInitialLoading()){
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    private void setupSwipeToRefresh(View view) {
        swipeRefreshLayout = view.findViewById(R.id.fragment_quests_management_swipe_to_refresh);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            fetchQuests();
        });
    }

    private void setupFab(View view) {
        FloatingActionButton floatingActionButton = view.findViewById(R.id.fragment_quests_management_fab);
        floatingActionButton.setOnClickListener(view1 -> {
            handleAddQuest();
        });
    }

    private void setupAdapter(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.fragment_quests_management_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new QuestManagementAdapter(getContext(), quests);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    private void fetchQuests() {
        FirestoreService.getAllQuests(new FirebaseCallback() {
            @Override
            public void onSuccess(Object object) {
                quests = (List<QuestEntity>) object;
                if (adapter != null){
                    adapter.setQuests(quests);
                    adapter.notifyDataSetChanged();
                    showMessage(quests.size() == 0, getView());
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

    private void showMessage(boolean isVisible, View view){
        if (view == null) return;
        TextView message = view.findViewById(R.id.fragment_quest_management_message);
        if (isVisible){
            message.setVisibility(View.VISIBLE);
        } else {
            message.setVisibility(View.GONE);
        }
    }

    private void handleAddQuest(){
        QuestManagementEntryFragment fragment = new QuestManagementEntryFragment();
        fragment.setMode(QuestManagementEntryModes.ADDING);
        fragmentNavigation.pushFragment(fragment);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBustEvent<Object> event) {
        QuestEntity quest = (QuestEntity) event.getData();
        switch (event.getEvent()) {
            case EVENT_BUS_EVENT_QUEST_ADDED:
                quests.add(quest);
                adapter.notifyItemInserted(adapter.getItemCount());
                break;
            case EVENT_BUS_EVENT_QUEST_DELETED:
                quests.remove(quest);
                adapter.notifyItemRemoved(selectedPosition);
                break;
        }
    }



    @Override
    public void onItemClick(View view, int position) {
        selectedPosition = position;
        QuestManagementEntryFragment fragment = new QuestManagementEntryFragment();
        fragment.setQuest(quests.get(position));
        fragment.setMode(QuestManagementEntryModes.DETAILS);
        fragmentNavigation.pushFragment(fragment);
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
