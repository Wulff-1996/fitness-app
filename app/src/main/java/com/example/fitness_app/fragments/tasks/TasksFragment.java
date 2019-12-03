package com.example.fitness_app.fragments.tasks;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.fitness_app.R;
import com.example.fitness_app.adapters.TasksAdapter;
import com.example.fitness_app.api.FirestoreService;
import com.example.fitness_app.constrants.ApiConstants;
import com.example.fitness_app.entities.EventBustEvent;
import com.example.fitness_app.fragments.BaseFragment;
import com.example.fitness_app.models.Account;
import com.example.fitness_app.models.FirebaseCallback;
import com.example.fitness_app.models.Task;
import com.example.fitness_app.models.TaskEntry;
import com.example.fitness_app.models.TaskWrapper;
import com.example.fitness_app.services.TaskService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static com.example.fitness_app.constrants.EventBusEvents.EVENT_BUS_EVENT_TASK_DELETED;
import static com.example.fitness_app.constrants.EventBusEvents.EVENT_BUS_EVENT_TASK_EDITED;
import static com.example.fitness_app.constrants.EventBusEvents.EVENT_BUS_EVENT_TASK_ENTRY_ADDED;
import static com.example.fitness_app.constrants.EventBusEvents.EVENT_BUS_EVENT_TASK_ENTRY_ADDED_CURRENT_DAY;
import static com.example.fitness_app.constrants.EventBusEvents.EVENT_BUS_EVENT_TASK_ENTRY_DELETED;

public class TasksFragment extends BaseFragment implements TasksAdapter.TasksAdapterListener, NewEditTaskDialog.NewTaskDialogDelegate {
    private TasksAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<TaskWrapper> taskWrappers = new ArrayList<>();
    private Account account;
    private int selectedIndex;
    private boolean hasBeenCreated = false; // avoid loading every time fragment is visible again

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fetch();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tasks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!hasBeenCreated){
            showProgressBar(true);
            hasBeenCreated = true;
        }
        setupRefreshView(view);
        setupAdapter(view);
        setupNewTaskButton(view);
    }

    private void setupRefreshView(View view) {
        swipeRefreshLayout = view.findViewById(R.id.fragment_tasks_swipe_to_refresh);
        swipeRefreshLayout.setOnRefreshListener(() -> fetch());
    }

    private void setupAdapter(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.fragment_tasks_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new TasksAdapter(getContext(), taskWrappers);
        adapter.setmItemListener(this);
        recyclerView.setAdapter(adapter);
    }

    private void setupNewTaskButton(View view) {
        FloatingActionButton newTaskBtn = view.findViewById(R.id.fragment_tasks_add_new_task_button);
        newTaskBtn.setOnClickListener(view1 -> showNewTaskDialog());
    }

    private void showNewTaskDialog() {
        NewEditTaskDialog newEditTaskDialog = new NewEditTaskDialog();
        newEditTaskDialog.setDelegate(this);
        newEditTaskDialog.setIcon(getString(R.string.icon_new_item));
        newEditTaskDialog.setHeadline(getString(R.string.fragment_tasks_new_task));
        assert getFragmentManager() != null;
        newEditTaskDialog.show(getFragmentManager(), "New Task Dialog");
    }


    @Override
    public void onItemClick(View view, Task task, int position) {
        selectedIndex = position;
        TaskEditFragment fragment = new TaskEditFragment();
        fragment.setTask(task);
        fragment.setAccount(account);
        fragmentNavigation.pushFragment(fragment);
    }

    @Override
    public void OnTaskMarkedComplete(View view, int position, TaskWrapper taskWrapper) {
        selectedIndex = position;

        // remove entry for the current day if task was marked complete before clicked
        if (taskWrapper.getCompletedToday()) {
            deleteEntry(taskWrapper, position);

        } else {
            // set date for
            postEntry(taskWrapper, position);
        }
    }

    @Override
    public void onDone(Task task) {
        postTaskToApi(task);
    }

    @Override
    public void onTaskDialogDismissed(NewEditTaskDialog dialogInstance) {

    }

    ////////////////////////  API CALLS /////////////////////////////////////////////
    private void fetch() {
        FirestoreService.getAllTasks(new FirebaseCallback() {
            @Override
            public void onSuccess(Object object) {
                taskWrappers.clear();
                account = (Account) object;

                // map to taskWrapper to register if task is completed today
                for (Task task : account.getTasks().values()) {
                    taskWrappers.add(new TaskWrapper(
                            task,
                            TaskService.setIsCompletedToday(task.getEntries())
                    ));
                }
                if (adapter != null){
                    adapter.setTaskWrappers(taskWrappers);
                    adapter.notifyDataSetChanged();
                    showProgressBar(false);
                }
            }

            @Override
            public void onFailure(FirebaseFirestoreException.Code errorCode) {
            }

            @Override
            public void onFinish() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void postTaskToApi(final Task task) {
        showProgressBar(true);
        FirestoreService.postNewTask(task, new FirebaseCallback() {
            @Override
            public void onSuccess(Object object) {
                fetch();
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

    private void postEntry(final TaskWrapper taskWrapper, final int position) {

        // create new task entry
        TaskEntry entry = new TaskEntry(
                UUID.randomUUID().toString(),
                String.valueOf(System.currentTimeMillis())
        );

        // add new task entry
        account.getTasks()
                .get(taskWrapper.getTask().getId())
                .getEntries()
                .put(entry.getId(), entry);

        // create update entity
        Map<String, Object> updates = new HashMap<>();
        updates.put(
                ApiConstants.TASKS_FIELD_NAME,
                account.getTasks());

        postUpdates(taskWrapper, updates, EVENT_BUS_EVENT_TASK_ENTRY_ADDED_CURRENT_DAY);
    }

    private void deleteEntry(final TaskWrapper taskWrapper, final int position) {

        // get entry for the current day
        final TaskEntry deleteEntry = TaskService.getEntryForCurrentDay(taskWrapper);

        // remove entry
        Objects.requireNonNull(account.getTasks()
                .get(taskWrapper.getTask().getId()))
                .getEntries()
                .remove(
                        deleteEntry.getId());

        Map<String, Object> updates = new HashMap<>();
        updates.put(
                ApiConstants.TASKS_FIELD_NAME,
                account.getTasks());
        postUpdates(taskWrapper, updates, EVENT_BUS_EVENT_TASK_ENTRY_DELETED);
    }

    private void postUpdates(Object data, Map<String, Object> updates, String updateType){
        showProgressBar(true);
        FirestoreService.updateTaskEntry(updates, new FirebaseCallback() {
            @Override
            public void onSuccess(Object object) {
                switch (updateType) {

                    case EVENT_BUS_EVENT_TASK_ENTRY_ADDED_CURRENT_DAY:
                        ((TaskWrapper) data).setCompletedToday(true);
                        adapter.notifyItemChanged(selectedIndex);
                        break;
                    case EVENT_BUS_EVENT_TASK_ENTRY_DELETED:
                        ((TaskWrapper) data).setCompletedToday(false);
                        adapter.notifyItemChanged(selectedIndex);
                        break;
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBustEvent<Object> event){
        switch (event.getEvent()){
            case EVENT_BUS_EVENT_TASK_ENTRY_DELETED:
                taskWrappers.get(selectedIndex).setCompletedToday(false);
                adapter.notifyItemChanged(selectedIndex);
                break;
            case EVENT_BUS_EVENT_TASK_ENTRY_ADDED:
                taskWrappers.get(selectedIndex).setCompletedToday(true);
                adapter.notifyItemChanged(selectedIndex);
                break;
            case EVENT_BUS_EVENT_TASK_EDITED:
                Task task = (Task) event.getData();
                taskWrappers.get(selectedIndex).getTask().setTitle(task.getTitle());
                adapter.notifyItemChanged(selectedIndex);
                break;
            case EVENT_BUS_EVENT_TASK_DELETED:
                taskWrappers.remove(selectedIndex);
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
