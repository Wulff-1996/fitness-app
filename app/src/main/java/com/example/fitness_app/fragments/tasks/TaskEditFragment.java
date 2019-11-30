package com.example.fitness_app.fragments.tasks;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.applandeo.materialcalendarview.CalendarView;
import com.example.fitness_app.R;
import com.example.fitness_app.api.FirestoreService;
import com.example.fitness_app.constrants.ApiConstants;
import com.example.fitness_app.fragments.BaseFragment;
import com.example.fitness_app.models.Account;
import com.example.fitness_app.models.FirebaseCallback;
import com.example.fitness_app.models.Task;
import com.example.fitness_app.models.TaskEntry;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TaskEditFragment extends BaseFragment {
    private CalendarView calendarView;
    private Account account;
    private Task selectedTask;

    public TaskEditFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_task_edit, container, false);

        setupCalendarView(view);

        return view;
    }

    private void setupCalendarView(View view) {
        calendarView = view.findViewById(R.id.fragment_task_edit_calendar_view);
        calendarView.setOnDayClickListener(
                eventDay -> {
                    //
                    if (calendarView.getSelectedDates().contains(eventDay.getCalendar())){
                        deleteEntry(eventDay.getCalendar());
                    }
                    // day is going to be selected
                    else {
                        postEntry();
                    }
                });

        // set selected dates
        List<Calendar> calendars = new ArrayList<>();
        for (TaskEntry entry : selectedTask.getEntries().values()) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.valueOf(entry.getCompletionDate()));
            calendars.add(calendar);
        }
        calendarView.setSelectedDates(calendars);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void setTask(Task task) {
        this.selectedTask = task;
    }

    public void setAccount(Account account){this.account = account;}

    private void postEntry() {

        // create new task entry
        TaskEntry entry = new TaskEntry(
                UUID.randomUUID().toString(),
                String.valueOf(System.currentTimeMillis())
        );

        // add new task entry
        account.getTasks()
                .get(selectedTask.getId())
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

    private void deleteEntry(Calendar calendar) {

        // get entry for the current day
        TaskEntry deleteEntry = null;

       // TODO check if the times are from the same day and get that task entry

        // remove entry
        account.getTasks()
                .get(selectedTask.getId())
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
