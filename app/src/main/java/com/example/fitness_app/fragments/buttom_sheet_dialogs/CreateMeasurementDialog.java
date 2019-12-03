package com.example.fitness_app.fragments.buttom_sheet_dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fitness_app.R;
import com.example.fitness_app.activities.LoginActivity;
import com.example.fitness_app.models.Benchmark;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class CreateMeasurementDialog extends BottomSheetDialogFragment {
    private CreateBenchmarkDialogDelegate delegate;
    private  Button createMeasurement;
    private CalendarView calendar;
    private EditText value;
    private String selectedDate;


    public CreateMeasurementDialog() {
        super();
    }

    private CreateMeasurementDialog getInstance(){return this;}
    public void setDelegate(CreateBenchmarkDialogDelegate delegate){this.delegate = delegate;}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_create_benchmark, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        createMeasurement = getView().findViewById(R.id.createBenchmarkBtn);
        calendar = getView().findViewById(R.id.calendarView);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener()
        {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth)
            {
                selectedDate = year + "/" + month + "/" + dayOfMonth;
            }
        });
        value = getView().findViewById(R.id.valueEditText);
    }

    public void createMeasurement(View v)
    {
        Benchmark benchmark = new Benchmark(selectedDate, "TestCategory", Float.parseFloat(value.getText().toString()));
        LoginActivity.userAccount.getBenchmarks().put("testID", benchmark);
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (delegate == null){
            if (context instanceof CreateBenchmarkDialogDelegate){
                delegate = (CreateBenchmarkDialogDelegate) context;
            } else {
                throw new RuntimeException("Context must implement the CreateBenchmarkDialogDelegate interface");
            }
        }
    }

    @Override
    public void onDetach() {
        delegate.OnConfirmDeleteDialogDismissed(this);
        delegate = null;
        super.onDetach();
    }

    public interface CreateBenchmarkDialogDelegate
    {
        void onDelete();
        void OnConfirmDeleteDialogDismissed(CreateMeasurementDialog dialogInstance);
    }
}