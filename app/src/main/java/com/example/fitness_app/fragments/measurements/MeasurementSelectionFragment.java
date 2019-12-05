package com.example.fitness_app.fragments.measurements;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fitness_app.R;
import com.example.fitness_app.activities.MainActivity;
import com.example.fitness_app.fragments.BaseFragment;
import com.example.fitness_app.models.BenchmarkCategories;
import com.example.fitness_app.models.FirebaseCallback;
import com.example.fitness_app.services.Firestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;

public class MeasurementSelectionFragment extends BaseFragment {
    private ListView listView;
    private ArrayAdapter adapter;
    private List benchmarkCategories;
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_measurement_selection, container, false);

        init();

        return view;
    }

    private void init()
    {
        listView = view.findViewById(R.id.listview);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Bundle bundle = new Bundle();
                bundle.putString("CATEGORY", adapter.getItem(position).toString());
                MeasurementsFragment measurement = new MeasurementsFragment();
                measurement.setArguments(bundle);
                MainActivity.mNavController.pushFragment(measurement);
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
                adapter = new ArrayAdapter(view.getContext(), android.R.layout.simple_list_item_1, benchmarkCategories);
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
    }
}
