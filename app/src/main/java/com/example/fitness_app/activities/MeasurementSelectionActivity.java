package com.example.fitness_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fitness_app.R;
import com.example.fitness_app.models.BenchmarkCategories;
import com.example.fitness_app.models.FirebaseCallback;
import com.example.fitness_app.services.Firestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;

public class MeasurementSelectionActivity extends AppCompatActivity
{
    private ListView listView;
    private ArrayAdapter adapter;
    private List benchmarkCategories;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measurement_selection);
        init();
    }

    private void init()
    {
        listView = findViewById(R.id.listview);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(view.getContext(), MeasurementsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("CATEGORY", adapter.getItem(position).toString());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        benchmarkCategories = new ArrayList();
        updateListView();
    }

    private void updateListView()
    {
        Firestore.fetchObject("benchmark_categories", "benchmark_categories", BenchmarkCategories.class, new FirebaseCallback()
        {
            @Override
            public void onSuccess(Object object)
            {
                BenchmarkCategories bmc = (BenchmarkCategories) object;
                benchmarkCategories = bmc.getCategories();
                adapter = new ArrayAdapter(MeasurementSelectionActivity.this, android.R.layout.simple_list_item_1, benchmarkCategories);
                listView.setAdapter(adapter);
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

    @Override
    protected void onStart()
    {
        super.onStart();
    }
}
