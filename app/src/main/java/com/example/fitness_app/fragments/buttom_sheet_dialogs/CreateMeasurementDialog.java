package com.example.fitness_app.fragments.buttom_sheet_dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
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

import static com.example.fitness_app.constrants.EventBusEvents.EVENT_BUS_EVENT_MEASUREMENT_ADDED;

public class CreateMeasurementDialog extends DialogFragment
{
    private EditText value;
    private CalendarView calendar;
    private String category;
    private String date;


    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
    {
        category = getArguments().getString("CATEGORY");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_create_benchmark, null);


        builder.setView(view)
                .setPositiveButton("Create", (dialog, which) -> {
                    if (!value.getText().toString().isEmpty())
                    {
                        Benchmark benchmark = new Benchmark(date, category, Float.parseFloat(value.getText().toString()));
                        Globals.userAccount.getBenchmarks().put(date + " - " + category, benchmark);
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
                String realMonth = String.valueOf(month + 1);
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        date = sdf.format(calendar.getDate());
        return builder.create();
    }

    private void updateEventBus()
    {
        FirestoreRepository.postCurrentAccount();
        EventBustEvent<TaskEntry> eventBustEvent = new EventBustEvent<>(EVENT_BUS_EVENT_MEASUREMENT_ADDED, null);
        EventBus.getDefault().post(eventBustEvent);
    }
}