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
import com.example.fitness_app.fragments.BaseFragment;
import com.example.fitness_app.fragments.LoadingFragment;
import com.example.fitness_app.models.Account;
import com.example.fitness_app.models.Task;
import com.example.fitness_app.models.TaskEntry;
import com.example.fitness_app.models.TaskWrapper;
import com.example.fitness_app.services.Firestore;
import com.example.fitness_app.services.TaskService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class TasksFragment extends BaseFragment implements TasksAdapter.TasksAdapterListener, NewTaskBottomSheetDialog.NewTaskDialogDelegate, LoadingFragment.LoadingFragmentDelegate {
    private TasksAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<TaskWrapper> taskWrappers = new ArrayList<>();
    private Account account;
    private LoadingFragment loadingFragment;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fetch();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return  inflater.inflate(R.layout.fragment_tasks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRefreshView(view);
        setupAdapter(view);
        setupNewTaskButton(view);
    }

    private void showProgressBar(boolean isShowing){
        if (isShowing){
            loadingFragment = LoadingFragment.newInstance();
            loadingFragment.setDelegate(this);
            assert getFragmentManager() != null;
            loadingFragment.show(getFragmentManager(), "progress dialog");
        } else {
            loadingFragment.dismiss();
        }
    }

    private void setupRefreshView(View view){
        swipeRefreshLayout = view.findViewById(R.id.fragment_tasks_swipe_to_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetch();
            }
        });
    }

    private void setupAdapter(View view){
        RecyclerView recyclerView = view.findViewById(R.id.fragment_tasks_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new TasksAdapter(getContext(), taskWrappers);
        adapter.setmItemListener(this);
        recyclerView.setAdapter(adapter);
    }

    private void setupNewTaskButton(View view){
        FloatingActionButton newTaskBtn = view.findViewById(R.id.fragment_tasks_add_new_task_button);
        newTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNewTaskDialog();
            }
        });
    }

    private void showNewTaskDialog(){
        NewTaskBottomSheetDialog newTaskBottomSheetDialog = new NewTaskBottomSheetDialog();
        newTaskBottomSheetDialog.setDelegate(this);
        assert getFragmentManager() != null;
        newTaskBottomSheetDialog.show(getFragmentManager(), "New Task Dialog");
    }

    private void fetch(){
       Firestore.getInstance()
               .collection("accounts")
               .document("wulffjakob@gmail.com")
               .get()
               .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                   @Override
                   public void onComplete(@NonNull com.google.android.gms.tasks.Task<DocumentSnapshot> task) {
                       if (task.isSuccessful()){
                           taskWrappers.clear();
                           account = Objects.requireNonNull(task.getResult()).toObject(Account.class);

                           assert account != null;
                           for (Map.Entry taskEntrySet: account.getTasks().entrySet()) {
                               Task task1 = (Task) taskEntrySet.getValue();
                               task1.setId(taskEntrySet.getKey().toString());
                               taskWrappers.add(
                                       new TaskWrapper(
                                               task1,
                                               TaskService.setIsCompletedToday(task1.getEntries())));
                               Map<String, TaskEntry> taskEntryMap = new HashMap<>(task1.getEntries());
                               task1.getEntries().clear();
                               for (Map.Entry<String, TaskEntry> taskEntry: taskEntryMap.entrySet()) {
                                   TaskEntry taskEntry1 = taskEntry.getValue();
                                   taskEntry1.setId(taskEntry.getKey());
                                   task1.getEntries().put(taskEntry.getKey(), taskEntry1);
                               }
                           }

                           adapter.setTaskWrappers(taskWrappers);
                           adapter.notifyDataSetChanged();

                           swipeRefreshLayout.setRefreshing(false);
                       } else {
                           String bvvv = "";
                       }
                   }
               });
    }

    @Override
    public void onItemClick(View view, int position) {
        fragmentNavigation.pushFragment(new TaskEditFragment());
    }

    @Override
    public void OnTaskMarkedComplete(View view, int position, TaskWrapper taskWrapper) {
        // remove entry for the current day if task was marked complete before clicked
        if (taskWrapper.getCompletedToday()){
            deleteEntry(taskWrapper, position);

        } else {
            // set date for
            postEntry(taskWrapper, position);
        }
    }

    private void deleteEntry(final TaskWrapper taskWrapper, final int position) {
        DocumentReference docRef =
                Firestore.
                        getInstance()
                        .collection("accounts")
                        .document("wulffjakob@gmail.com");

        final TaskEntry deleteEntry = TaskService.getEntryForCurrentDay(taskWrapper);

        Objects.requireNonNull(account.getTasks()
                .get(taskWrapper.getTask().getId()))
                .getEntries()
                .remove(
                        deleteEntry.getId());

        Map<String, Object> updates = new HashMap<>();
        updates.put("tasks", account.getTasks());

        showProgressBar(true);
        docRef.update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                showProgressBar(false);
                if (task.isSuccessful()){
                    taskWrappers.get(position).getTask().getEntries().remove(deleteEntry.getId());
                    taskWrapper.setCompletedToday(false);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void postEntry(final TaskWrapper taskWrapper, final int position){
        DocumentReference docRef =
                Firestore.
                        getInstance()
                        .collection("accounts")
                        .document("wulffjakob@gmail.com");

        final String id = UUID.randomUUID().toString();
        final TaskEntry entryToBeAdded = new TaskEntry();
        entryToBeAdded.setCompletionDate(String.valueOf(System.currentTimeMillis()));
        entryToBeAdded.setId(id);
        Objects.requireNonNull(account.getTasks().get(taskWrapper.getTask().getId())).getEntries().put(id, entryToBeAdded);
        Map<String, Object> updates = new HashMap<>();
        updates.put("tasks", account.getTasks());

        showProgressBar(true);
        docRef.update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                showProgressBar(false);
                taskWrappers.get(position).getTask().getEntries().put(id, entryToBeAdded);
                taskWrapper.setCompletedToday(true);
                adapter.getTaskWrappers().get(position).getTask().getEntries().put(id, entryToBeAdded);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onTaskSaved(Task task) {
        postTaskToApi(task);
    }

    private void postTaskToApi(final Task task){
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> tasks = new HashMap<>();
        String id = UUID.randomUUID().toString();
        task.setId(id);
        tasks.put(id, task);
        data.put("tasks", tasks);
        Firestore.getInstance().collection("accounts").document("wulffjakob@gmail.com")
                .set(data, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                fetch();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        }).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                showProgressBar(false);
            }
        });
    }

    @Override
    public void onTaskDialogDismissed(NewTaskBottomSheetDialog dialogInstance) {

    }

    @Override
    public void onLoadingFragmentDismissed() {

    }
}
