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
import com.example.fitness_app.models.Task;
import com.example.fitness_app.models.TaskEntry;
import com.example.fitness_app.models.TaskWrapper;
import com.example.fitness_app.services.TaskService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class TasksFragment extends BaseFragment implements TasksAdapter.TasksAdapterListener, NewTaskBottomSheetDialog.NewTaskDialogDelegate {
    private TasksAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<TaskWrapper> taskWrappers = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        adapter = new TasksAdapter(getContext(), new ArrayList<TaskWrapper>());
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

    private void fetchFromApi(){

    }

    private void fetch(){
        // TODO mock data
        List<TaskEntry> taskEntries = new ArrayList<>();
        taskEntries.add(new TaskEntry("1574970720000"));
        taskEntries.add(new TaskEntry("1575057240000"));
        taskEntries.add(new TaskEntry("1575143640000"));
        taskWrappers.add(new TaskWrapper(
                new Task(
                    "1",
                    "This is my first task",
                    taskEntries),
                TaskService.setIsCompletedToday(taskEntries)));
        taskWrappers.add(new TaskWrapper(
                new Task(
                    "2",
                    "Drink four glasses of water a day.",
                    taskEntries),
                TaskService.setIsCompletedToday(taskEntries)));
        taskWrappers.add(new TaskWrapper(
                new Task(
                    "3",
                    "Go to the gym",
                    taskEntries),
                TaskService.setIsCompletedToday(taskEntries))
        );
        taskWrappers.add(new TaskWrapper(
                new Task(
                    "4",
                        "Take the kids to the playgound",
                        taskEntries),
                TaskService.setIsCompletedToday(taskEntries)
        ));
        taskWrappers.add(new TaskWrapper(
                new Task(
                    "5",
                    "Make dinner for the wife",
                    taskEntries),
                TaskService.setIsCompletedToday(taskEntries)
        ));


        adapter.setTaskWrappers(taskWrappers);
        adapter.notifyDataSetChanged();


        // end loading
        swipeRefreshLayout.setRefreshing(false);

    }

    @Override
    public void onItemClick(View view, int position) {
        //TODO open new details fragment of the selected task
    }

    @Override
    public void OnTaskMarkedComplete(View view, int position, TaskWrapper taskWrapper) {
        // remove entry for the current day if task was marked complete before clicked
        if (taskWrapper.getCompletedToday()){
            taskWrappers.get(position).getTask().getEntries().remove(TaskService.getEntryForCurrentDay(taskWrapper));
            taskWrapper.setCompletedToday(false);
            //TODO post delete to api
        } else {
            // set date for
            taskWrappers.get(position).getTask().getEntries().add(new TaskEntry(String.valueOf(System.currentTimeMillis())));
            taskWrapper.setCompletedToday(true);
            // TODO post new entity to api
        }
    }

    @Override
    public void onTaskSaved(Task task) {
        taskWrappers.add(new TaskWrapper(task, false));
        taskWrappers.size();
        adapter.getTaskWrappers().size();
        // TODO post new entity to api
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onTaskDialogDismissed(NewTaskBottomSheetDialog dialogInstance) {

    }
}
