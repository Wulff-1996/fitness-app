package com.example.fitness_app.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fitness_app.R;
import com.example.fitness_app.models.Account;
import com.example.fitness_app.models.Benchmark;
import com.example.fitness_app.models.FirebaseCallback;
import com.example.fitness_app.services.Firestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MeasurementsActivity extends AppCompatActivity
{
    private TextView titleText;
    private ListView listView;
    private ArrayAdapter adapter;
    private Map<String, Benchmark> measurements;
    private List benchmarkNames;
    private Account userAccount;
    String category;

    private RelativeLayout popupRL;
    private PopupWindow popupWindow;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measurements);
        init();
    }

    private void init()
    { ;






        listView = findViewById(R.id.listview);
        benchmarkNames = new ArrayList();
        category = getIntent().getExtras().getString("CATEGORY");
        titleText = findViewById(R.id.titleTextView);
        titleText.setText(category);



        Firestore.fetchObject("accounts", Firestore.getCurrentUser().getEmail(), Account.class, new FirebaseCallback()
        {
            @Override
            public void onSuccess(Object object)
            {
                userAccount = (Account) object;
                updateListView();
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

    private void updateListView()
    {
        measurements = userAccount.getBenchmarks();
        for (Map.Entry<String, Benchmark> entry: measurements.entrySet())
        {
            if (entry.getValue().getExerciseCategory().equals(category))
            {
                benchmarkNames.add(entry.getKey() + ": " + entry.getValue().getValue());
            }
        }
        adapter = new ArrayAdapter(MeasurementsActivity.this, android.R.layout.simple_list_item_1, benchmarkNames);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }

    public void addMeasurement(View v)
    {
        // Create popup with input fields for date and value

        // Create benchmark based on inputfields

        // Refresh benchmarkNames list
    }
}