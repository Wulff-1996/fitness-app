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
import com.example.fitness_app.api.FirestoreRepository;
import com.example.fitness_app.models.Benchmark;
import com.example.fitness_app.models.FirebaseCallback;
import com.google.firebase.firestore.FirebaseFirestoreException;

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
                .setPositiveButton("Create", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if (!value.getText().toString().isEmpty())
                        {
                            Benchmark benchmark = new Benchmark(date, category, Float.parseFloat(value.getText().toString()));
                            LoginActivity.userAccount.getBenchmarks().put(date + " - " + category, benchmark);
                            FirestoreRepository.postObject("accounts", FirestoreRepository.getCurrentUser().getEmail(), LoginActivity.userAccount, new FirebaseCallback()
                            {
                                @Override
                                public void onSuccess(Object object)
                                {
                                }

                                @Override
                                public void onFailure(FirebaseFirestoreException.Code errorCode)
                                {
                                }

                                @Override
                                public void onFinish()
                                {
                                }
                        });
                        }
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
        return builder.create();
    }
}