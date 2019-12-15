package com.example.fitness_app.fragments.tasks;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.applandeo.materialcalendarview.CalendarView;
import com.example.fitness_app.R;
import com.example.fitness_app.api.FirestoreService;
import com.example.fitness_app.constrants.Api;
import com.example.fitness_app.entities.EventBustEvent;
import com.example.fitness_app.fragments.BaseFragment;
import com.example.fitness_app.fragments.buttom_sheet_dialogs.ConfirmDeleteDialog;
import com.example.fitness_app.interfaces.FirebaseCallback;
import com.example.fitness_app.models.Account;
import com.example.fitness_app.models.TaskEntry;
import com.example.fitness_app.models.UserTask;
import com.example.fitness_app.services.TaskService;
import com.example.fitness_app.util.Dates;
import com.example.fitness_app.views.IconButton;
import com.google.android.material.chip.Chip;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.example.fitness_app.constrants.EventBusEvents.EVENT_BUS_EVENT_TASK_DELETED;
import static com.example.fitness_app.constrants.EventBusEvents.EVENT_BUS_EVENT_TASK_EDITED;
import static com.example.fitness_app.constrants.EventBusEvents.EVENT_BUS_EVENT_TASK_ENTRY_ADDED;
import static com.example.fitness_app.constrants.EventBusEvents.EVENT_BUS_EVENT_TASK_ENTRY_DELETED;

public class TaskEditFragment extends BaseFragment implements NewEditTaskDialog.NewTaskDialogDelegate, ConfirmDeleteDialog.ConfirmDeleteDialogDelegate {
    private CalendarView calendarView;
    private Account account;
    private UserTask selectedUserTask;
    private String selectedTaskId;
    private TaskEntry todaysEntry;
    private Calendar selectedMonth;

    public TaskEditFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectedMonth = Calendar.getInstance();
        selectedMonth.setTimeInMillis(System.currentTimeMillis());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_task_edit, container, false);

        setupToolbar(view);
        setupCalendarView(view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        populateView(view);
    }

    private void populateView(View view){
        IconButton deleteButton = view.findViewById(R.id.fragment_task_edit_delete_button);
        IconButton editButton = view.findViewById(R.id.fragment_task_edit_edit_button);

        setTaskTitle(selectedUserTask.getTitle());

        if (todaysEntry != null){
            setIsCompletedTodayChipState(true);
        } else {
            setIsCompletedTodayChipState(false);
        }

        deleteButton.setOnClickListener(itemView -> handleDeleteTask());
        editButton.setOnClickListener(itemView -> handleEditTask());
    }

    private void setIsCompletedTodayChipState(boolean isCompleted){
        if (getView() == null) return;
        Chip isCompletedTodayChip = getView().findViewById(R.id.fragment_task_edit_is_completed_today_chip);
        if (isCompleted){
            isCompletedTodayChip.setChipBackgroundColorResource(R.color.success);
        } else {
            isCompletedTodayChip.setChipBackgroundColorResource(R.color.cancel);
        }
    }

    private void setTaskTitle(String title){
        if (getView() == null) return;
        TextView taskTitleView = getView().findViewById(R.id.fragment_task_edit_task_title);
        taskTitleView.setText(title);
    }

    private void setupToolbar(View view){
        ConstraintLayout toolbar = view.findViewById(R.id.fragment_task_edit_toolbar);
        IconButton backArrow = toolbar.findViewById(R.id.application_toolbar_back_arrow);
        backArrow.setOnClickListener(view1 -> {
            fragmentNavigation.popFragment();
        });
    }

    private void setupCalendarView(View view) {
        calendarView = view.findViewById(R.id.fragment_task_edit_calendar_view);
        calendarView.setOnDayClickListener(
                eventDay -> {
                    // check if selected date is in the same month
                    if (eventDay.getCalendar().get(Calendar.YEAR) == selectedMonth.get(Calendar.YEAR) &&
                        eventDay.getCalendar().get(Calendar.MONTH) == selectedMonth.get(Calendar.MONTH)){

                        // same month
                        Calendar currentDay = Calendar.getInstance();
                        currentDay.setTimeInMillis(System.currentTimeMillis());

                        // check if selected day is not after the current date
                        if (eventDay.getCalendar().get(Calendar.DAY_OF_MONTH) <= currentDay.get(Calendar.DAY_OF_MONTH)){
                            if (calendarView.getSelectedDates().contains(eventDay.getCalendar())){
                                // date is going to be deselected
                                deleteEntry(eventDay.getCalendar());
                            }
                            // day is going to be selected
                            else {
                                postEntry(eventDay.getCalendar());
                            }
                        } else {
                            // show toast future dates cant be mark completed
                            List<Calendar> disabledDays = new ArrayList<>();
                            disabledDays.add(eventDay.getCalendar());
                            calendarView.setDisabledDays(disabledDays);
                            showToast(getContext().getString(R.string.fragment_task_edit_future_dates_not_acceptable));
                        }

                    } else {
                        // different month, change month on calendarView
                        try{
                            calendarView.setDate(eventDay.getCalendar());
                            selectedMonth = eventDay.getCalendar();
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });

        // set selected dates
        List<Calendar> calendars = new ArrayList<>();
        for (TaskEntry entry : selectedUserTask.getEntries().values()) {
            if (Dates.isToday(Long.valueOf(entry.getCompletionDate()))){
                todaysEntry = entry;
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.valueOf(entry.getCompletionDate()));
            calendars.add(calendar);
        }
        calendarView.setSelectedDates(calendars);
    }

    private void handleDeleteTask(){
        ConfirmDeleteDialog dialog = new ConfirmDeleteDialog();
        dialog.setDelegate(this);
        dialog.show(getFragmentManager(), "confirm delete dialog");
    }

    private void handleEditTask(){
        NewEditTaskDialog dialog = new NewEditTaskDialog();
        dialog.setDelegate(this);
        dialog.setHeadline(getString(R.string.fragment_task_edit_edit_task));
        dialog.setIcon(getString(R.string.icon_edit));
        dialog.setUserTask(selectedUserTask);
        dialog.show(getFragmentManager(), "new or edit fragment dialog");
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void setTask(UserTask userTask) {
        this.selectedUserTask = userTask;
    }

    public void setAccount(Account account){this.account = account;}
    public void setSelectedTaskId(String taksId){this.selectedTaskId = taksId;}

    private void postEntry(Calendar calendar) {

        // create new task entry
        TaskEntry entry = new TaskEntry(
                UUID.randomUUID().toString(),
                String.valueOf(calendar.getTimeInMillis())
        );

        // add new task entry
        account.getTasks()
                .get(selectedUserTask.getId())
                .getEntries()
                .put(entry.getId(), entry);

        // create update entity
        Map<String, Object> updates = new HashMap<>();
        updates.put(
                Api.TASKS_FIELD_NAME,
                account.getTasks());

        postUpdates(entry, updates, EVENT_BUS_EVENT_TASK_ENTRY_ADDED);
    }

    private void deleteEntry(Calendar calendar) {

        // get entry for the current day
        TaskEntry deleteEntry = TaskService.getTaskEntry(calendar, selectedUserTask.getEntries().values());

        // remove entry
        account.getTasks()
                .get(selectedUserTask.getId())
                .getEntries()
                .remove(
                        deleteEntry.getId());

        Map<String, Object> updates = new HashMap<>();
        updates.put(
                Api.TASKS_FIELD_NAME,
                account.getTasks());

        postUpdates(deleteEntry, updates, EVENT_BUS_EVENT_TASK_ENTRY_DELETED);
    }

    @Override
    public void onDone(UserTask userTask) {
        account.getTasks().get(selectedUserTask.getId()).setTitle(userTask.getTitle());

        Map<String, Object> updates = new HashMap<>();
        updates.put(
                Api.TASKS_FIELD_NAME,
                account.getTasks());

        postUpdates(userTask, updates, EVENT_BUS_EVENT_TASK_EDITED);
    }

    @Override
    public void onTaskDialogDismissed(NewEditTaskDialog dialogInstance) {

    }

    @Override
    public void onDelete() {
        account.getTasks().remove(selectedUserTask.getId());

        Map<String, Object> updates = new HashMap<>();
        updates.put(
                Api.TASKS_FIELD_NAME,
                account.getTasks());

        postUpdates(null, updates, EVENT_BUS_EVENT_TASK_DELETED);
    }

    @Override
    public void OnConfirmDeleteDialogDismissed(ConfirmDeleteDialog dialogInstance) {

    }

    private void postUpdates(Object data, Map<String, Object> updates, String updateType){
        showProgressBar(true);
        FirestoreService.updateTaskEntry(updates, new FirebaseCallback() {
            @Override
            public void onSuccess(Object object) {
                switch (updateType){
                    case EVENT_BUS_EVENT_TASK_ENTRY_ADDED:
                        // post message to Tasksfragment if the added task entry is the current day
                        if (Dates.isToday(Long.valueOf(((TaskEntry) data).getCompletionDate()))){
                            EventBustEvent<TaskEntry> eventBustEvent = new EventBustEvent<>(EVENT_BUS_EVENT_TASK_ENTRY_ADDED, null);
                            EventBus.getDefault().post(eventBustEvent);
                            setIsCompletedTodayChipState(true);
                        }
                        break;
                    case EVENT_BUS_EVENT_TASK_ENTRY_DELETED:
                        // post message to Tasksfragment if the removed task entry is the current day
                        if (Dates.isToday(Long.valueOf(((TaskEntry)data).getCompletionDate()))){
                            EventBustEvent<TaskEntry> eventBustEvent = new EventBustEvent<>(EVENT_BUS_EVENT_TASK_ENTRY_DELETED, null);
                            EventBus.getDefault().post(eventBustEvent);
                            setIsCompletedTodayChipState(false);
                        }
                        break;

                    case EVENT_BUS_EVENT_TASK_DELETED:
                        EventBustEvent<Object> eventDelete = new EventBustEvent<>(EVENT_BUS_EVENT_TASK_DELETED, null);
                        EventBus.getDefault().post(eventDelete);
                        fragmentNavigation.popFragment();
                        break;
                    case EVENT_BUS_EVENT_TASK_EDITED:
                        EventBustEvent<UserTask> eventUpdateTask = new EventBustEvent<>(EVENT_BUS_EVENT_TASK_EDITED, ((UserTask) data));
                        EventBus.getDefault().post(eventUpdateTask);
                        setTaskTitle(((UserTask) data).getTitle());
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
}
