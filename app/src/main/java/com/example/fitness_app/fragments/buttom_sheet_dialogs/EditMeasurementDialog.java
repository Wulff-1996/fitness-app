package com.example.fitness_app.fragments.buttom_sheet_dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.fitness_app.R;
import com.example.fitness_app.api.FirestoreRepository;
import com.example.fitness_app.constrants.Globals;
import com.example.fitness_app.models.Benchmark;
import com.example.fitness_app.models.EventBustEvent;
import com.example.fitness_app.models.TaskEntry;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.example.fitness_app.constrants.EventBusEvents.EVENT_BUS_EVENT_MEASUREMENT_UPDATED;

public class EditMeasurementDialog extends DialogFragment
{
    private EditText value;
    private CalendarView calendar;
    private String date, ID;
    private Benchmark oldBenchmark;


    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
    {
        ID = getArguments().getString("ID");
        oldBenchmark = Globals.userAccount.getBenchmarks().get(ID);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_create_benchmark, null);

        builder.setView(view)
                .setPositiveButton("Save changes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if (!value.getText().toString().isEmpty())
                        {
                            Globals.userAccount.getBenchmarks().remove(ID);
                            Benchmark benchmark = new Benchmark(date, oldBenchmark.getExerciseCategory(), Float.parseFloat(value.getText().toString()));
                            Globals.userAccount.getBenchmarks().put(date + " - " + oldBenchmark.getExerciseCategory(), benchmark);
                            updateEventBus();
                        }
                    }
                }).setNegativeButton("Delete measurement", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Globals.userAccount.getBenchmarks().remove(ID);
                updateEventBus();
            }
        });

        value = view.findViewById(R.id.dialog_create_benchmark_value);
        calendar = view.findViewById(R.id.calendarView);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener()
        {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth)
            {
                String realMonth = String.valueOf(month+1);
                String realDay = String.valueOf(dayOfMonth);
                if (month + 1 < 10)
                {
                    realMonth = "0" + realMonth;
                }
                if (dayOfMonth < 10)
                {
                    realDay = "0" + realDay;
                }
                date = year + "/" + realMonth + "/" + realDay;
            }
        });

        // Set the displayed values equals to the old values as a default point
        value.setText(oldBenchmark.getValue().toString());

        String[] parts = oldBenchmark.getDate().split("/");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int day = Integer.parseInt(parts[2]);

        // Create a calendar time
        Calendar tmpCalendar = Calendar.getInstance();
        tmpCalendar.set(year, month-1, day);
        // Get milliseconds of the newly stated date
        long millisec = tmpCalendar.getTimeInMillis();
        // Set the displayed calendar to the milliseconds calculated
        calendar.setDate(millisec, true, true);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        date = sdf.format(calendar.getDate());
        System.out.println("current date: " + date);
        return builder.create();
    }

    private void updateEventBus()
    {
        FirestoreRepository.postCurrentAccount();
        EventBustEvent<TaskEntry> eventBustEvent = new EventBustEvent<>(EVENT_BUS_EVENT_MEASUREMENT_UPDATED, null);
        EventBus.getDefault().post(eventBustEvent);
    }
}