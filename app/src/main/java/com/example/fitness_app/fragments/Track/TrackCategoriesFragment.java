package com.example.fitness_app.fragments.Track;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.fitness_app.R;
import com.example.fitness_app.activities.MyNavigationDelegate;
import com.example.fitness_app.adapters.TrackCategoriesAdapter;
import com.example.fitness_app.entities.BottomSheetActionItemEntity;
import com.example.fitness_app.entities.BottomSheetTitleEntity;
import com.example.fitness_app.fragments.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

import static com.example.fitness_app.constrants.SubCategoryType.FIVE;
import static com.example.fitness_app.constrants.SubCategoryType.MAX;
import static com.example.fitness_app.constrants.SubCategoryType.TEN;
import static com.example.fitness_app.constrants.SubCategoryType.THREE;

public class TrackCategoriesFragment extends Fragment implements TrackCategoriesAdapter.ItemClickListener, BottomSheetDialog.BottomSheetDialogDelegate {

    private TrackCategoriesAdapter adapter;
    private BottomSheetDialog subCategoryBottomSheetDialog;
    private BottomSheetActionItemEntity maxItem;
    private BottomSheetActionItemEntity threeItem;
    private BottomSheetActionItemEntity fiveItem;
    private BottomSheetActionItemEntity tenItem;
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

        // TODO remove list and get from database
        List<String> categories = new ArrayList<>();
        categories.add("Benchpress");
        categories.add("Shoulderpress");
        categories.add("Squat");
        categories.add("Deadlift");

        adapter = new TrackCategoriesAdapter(getContext(), categories);
        adapter.setItemClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    /**
     * get data from the database
     * //TODO implement
     */
    private void fetch(){}

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(getContext(), "hello", Toast.LENGTH_SHORT).show();
        showBottomSheetDialog(adapter.getCategories().get(position));
    }

    private void showBottomSheetDialog(String headline){

        BottomSheetTitleEntity title = new BottomSheetTitleEntity(headline, "Select a sub category");
        maxItem = new BottomSheetActionItemEntity(MAX.name());
        threeItem = new BottomSheetActionItemEntity(THREE.name());
        fiveItem = new BottomSheetActionItemEntity(FIVE.name());
        tenItem = new BottomSheetActionItemEntity(TEN.name());

        List<BottomSheetActionItemEntity> items = new ArrayList<>();
        items.add(maxItem);
        items.add(threeItem);
        items.add(fiveItem);
        items.add(tenItem);


        subCategoryBottomSheetDialog = BottomSheetDialog.newInstance(title, items);
        subCategoryBottomSheetDialog.setDelegate(this);
        subCategoryBottomSheetDialog.show(getChildFragmentManager(), "SubCategoriesDialog");
    }

    private void startFragment(String category, String subCategory){
        TrackFragment fragment = new TrackFragment();
        fragment.setCategory(category);
        fragment.setSubCategory(subCategory);
        delegate.changeFragment(fragment);
    }

    @Override
    public void onBottomSheetItemClicked(BottomSheetDialog dialogInstance, BottomSheetActionItemEntity actionInstance, int position) {
        if (dialogInstance == subCategoryBottomSheetDialog){

            if (actionInstance == maxItem){

            } else if (actionInstance == threeItem){

            } else if (actionInstance == fiveItem) {

            } else if (actionInstance == tenItem){

            }
        }
    }

    @Override
    public void onBottomSheetDialogDismissed(BottomSheetDialog dialogInstance) {

    }
}
