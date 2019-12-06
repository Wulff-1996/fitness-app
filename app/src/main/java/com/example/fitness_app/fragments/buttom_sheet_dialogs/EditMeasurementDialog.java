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
import com.example.fitness_app.activities.LoginActivity;
import com.example.fitness_app.models.Benchmark;
import com.example.fitness_app.services.Firestore;

import java.util.Calendar;

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
        oldBenchmark = LoginActivity.userAccount.getBenchmarks().get(ID);

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
                            LoginActivity.userAccount.getBenchmarks().remove(ID);
                            Benchmark benchmark = new Benchmark(date, oldBenchmark.getExerciseCategory(), Float.parseFloat(value.getText().toString()));
                            LoginActivity.userAccount.getBenchmarks().put(date + " - " + oldBenchmark.getExerciseCategory(), benchmark);
                            Firestore.postCurrentAccount();
                        }
                    }
                }).setNegativeButton("Delete measurement", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                LoginActivity.userAccount.getBenchmarks().remove(ID);
                Firestore.postCurrentAccount();
            }
        });

        value = view.findViewById(R.id.valueEditText);
        calendar = view.findViewById(R.id.calendarView);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener()
        {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth)
            {
                date = year + "/" + month + "/" + dayOfMonth;
            }
        });

        // Set the displayed values equals to the old values as a default point
        value.setText(oldBenchmark.getValue().toString());

        String parts[] = oldBenchmark.getDate().split("/");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int day = Integer.parseInt(parts[2]);

        // Create a calendar time
        Calendar tmpCalendar = Calendar.getInstance();
        tmpCalendar.set(Calendar.YEAR, year);
        tmpCalendar.set(Calendar.MONTH, month);
        tmpCalendar.set(Calendar.DAY_OF_MONTH, day);
        // Get milliseconds of the newly stated date
        long millisec = tmpCalendar.getTimeInMillis();
        // Set the displayed calendar to the milliseconds calculated
        calendar.setDate(millisec, true, true);
        return builder.create();
    }
}