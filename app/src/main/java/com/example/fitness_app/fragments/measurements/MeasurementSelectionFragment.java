package com.example.fitness_app.fragments.measurements;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.fitness_app.R;
import com.example.fitness_app.adapters.AdapterOnItemClickListener;
import com.example.fitness_app.adapters.MeasurementSelectionAdapter;
import com.example.fitness_app.api.FirestoreService;
import com.example.fitness_app.fragments.BaseFragment;
import com.example.fitness_app.interfaces.FirebaseCallback;
import com.example.fitness_app.models.BenchmarkCategories;

import java.util.ArrayList;
import java.util.List;

public class MeasurementSelectionFragment extends BaseFragment implements AdapterOnItemClickListener {
    private MeasurementSelectionAdapter adapter;
    private List<String> benchmarkCategories = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fetchCategories();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_measurement_selection, container, false);

        setupProgressBar(view);
        setupSwipeToRefresh(view);
        setupAdapter(view);

        return view;
    }

    private void setupProgressBar(View view){
        ProgressBar progressBar = view.findViewById(R.id.fragment_measurement_selection_progress_bar);
        progressBar.setIndeterminate(true);
        setProgressBar(progressBar);
        if (!hasShownInitialLoading()){
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    private void setupSwipeToRefresh(View view){
        swipeRefreshLayout = view.findViewById(R.id.fragment_measurement_selection_swipe_to_refresh);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            fetchCategories();
        });
    }

    private void setupAdapter(View view){
        RecyclerView recyclerView = view.findViewById(R.id.fragment_measurement_selection_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new MeasurementSelectionAdapter(getContext(), benchmarkCategories);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    private void fetchCategories()
    {
        FirestoreService.getBenchmarkCategories(new FirebaseCallback()
        {
            @Override
            public void onSuccess(Object object)
            {
                BenchmarkCategories bmc = (BenchmarkCategories) object;
                benchmarkCategories = bmc.getCategories();
                if (adapter != null){
                    adapter.setMeasurementCategories(benchmarkCategories);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Exception errorCode)
            {

            }

            @Override
            public void onFinish()
            {
                swipeRefreshLayout.setRefreshing(false);
                setFetching(false);
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        MeasurementsFragment fragment = new MeasurementsFragment();
        fragment.setCategory(benchmarkCategories.get(position));
        fragmentNavigation.pushFragment(fragment);
    }
}
