package com.example.fitness_app.fragments.tasks;

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
import com.example.fitness_app.fragments.BaseFragment;
import com.example.fitness_app.models.Account;
import com.example.fitness_app.models.FirebaseCallback;
import com.example.fitness_app.models.Task;
import com.example.fitness_app.models.TaskEntry;
import com.example.fitness_app.models.TaskWrapper;
import com.example.fitness_app.services.TaskService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class TasksFragment extends BaseFragment implements TasksAdapter.TasksAdapterListener, NewTaskBottomSheetDialog.NewTaskDialogDelegate {
    private TasksAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<TaskWrapper> taskWrappers = new ArrayList<>();
    private Account account;
    private FirestoreService taskRepository = new FirestoreService();


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
        NewTaskBottomSheetDialog newTaskBottomSheetDialog = new NewTaskBottomSheetDialog();
        newTaskBottomSheetDialog.setDelegate(this);
        assert getFragmentManager() != null;
        newTaskBottomSheetDialog.show(getFragmentManager(), "New Task Dialog");
    }


    @Override
    public void onItemClick(View view, Task task, int position) {
        TaskEditFragment fragment = new TaskEditFragment();
        fragment.setTask(task);
        fragment.setAccount(account);
        fragmentNavigation.pushFragment(fragment);
    }

    @Override
    public void OnTaskMarkedComplete(View view, int position, TaskWrapper taskWrapper) {
        // remove entry for the current day if task was marked complete before clicked
        if (taskWrapper.getCompletedToday()) {
            deleteEntry(taskWrapper, position);

        } else {
            // set date for
            postEntry(taskWrapper, position);
        }
    }

    @Override
    public void onTaskSaved(Task task) {
        postTaskToApi(task);
    }

    @Override
    public void onTaskDialogDismissed(NewTaskBottomSheetDialog dialogInstance) {

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

        showProgressBar(true);
        FirestoreService.updateTaskEntry(updates, new FirebaseCallback() {
            @Override
            public void onSuccess(Object object) {
                taskWrapper.setCompletedToday(true);
                adapter.notifyDataSetChanged();
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

        showProgressBar(true);
        FirestoreService.updateTaskEntry(updates, new FirebaseCallback() {
            @Override
            public void onSuccess(Object object) {
                taskWrapper.setCompletedToday(false);
                adapter.notifyDataSetChanged();
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
}
