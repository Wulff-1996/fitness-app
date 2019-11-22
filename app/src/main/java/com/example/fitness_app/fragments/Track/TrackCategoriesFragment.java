package com.example.fitness_app.fragments.Track;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.fitness_app.R;
import com.example.fitness_app.activities.MyNavigationDelegate;
import com.example.fitness_app.adapters.TrackCategoriesAdapter;

import java.util.ArrayList;
import java.util.List;

public class TrackCategoriesFragment extends Fragment implements TrackCategoriesAdapter.ItemClickListener {

    private TrackCategoriesAdapter adapter;
    private MyNavigationDelegate delegate;

    public void setDelegate(MyNavigationDelegate delegate){
        this.delegate = delegate;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_track_categories, container, false);

        setupSwipeToRefresh(view);

        setupAdapter(view);

        fetch();

        return view;
    }

    private void setupSwipeToRefresh(View view){
        SwipeRefreshLayout swipeRefreshLayout =  view.findViewById(R.id.fragment_track_category_swipe_to_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetch();
            }
        });
    }

    private void setupAdapter(View view){
        RecyclerView recyclerView = view.findViewById(R.id.fragment_track_category_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new TrackCategoriesAdapter(getContext(), new ArrayList<String>());
        adapter.setItemClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    /**
     * get data from the database
     * //TODO implement
     */
    private void fetch(){
        // TODO remove list and get from database
        List<String> categories = new ArrayList<>();
        categories.add("Benchpress");
        categories.add("Shoulderpress");
        categories.add("Squat");
        categories.add("Deadlift");
        adapter.setCategories(categories);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(View view, int position) {
        startFragment(adapter.getCategories().get(position));
    }

    private void startFragment(String category){
        TrackFragment fragment = new TrackFragment();
        fragment.setCategory(category);
        delegate.changeFragment(fragment);
    }
}
