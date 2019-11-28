package com.example.fitness_app.fragments.measurements;

import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitness_app.R;
import com.example.fitness_app.entities.BottomSheetTitleEntity;
import com.example.fitness_app.fragments.BaseFragment;
import com.example.fitness_app.fragments.BottomSheetDialog;
import com.example.fitness_app.views.BackArrowView;
import com.example.fitness_app.views.SelectorView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.jjoe64.graphview.series.Series;

import java.util.ArrayList;
import java.util.List;

public class MeasurementFragment extends BaseFragment implements SelectorView.SelectorViewDelegate, BackArrowView.BackArrowViewDelegate, BottomSheetDialog.BottomSheetDialogDelegate {
    private String selectedCategory;
    private SelectorView subCategoriesView;
    private BottomSheetDialog subCategoryBottomSheet;
    private List<String> subCategories = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_measurements, container, false);

        setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(
                android.R.transition.fade
        ));

        initBackArrow(view);
        initView(view);
        initGraphView(view);

        fetchSubCategories();

        return view;
    }

    private void initBackArrow(View view){
        BackArrowView backArrowView = view.findViewById(R.id.fragment_track_back_arrow);
        backArrowView.setDelegate(this);
    }

    // TODO fetch sub categories from database
    private void fetchSubCategories(){
        List<String> subCategories = new ArrayList<>();
        subCategories.add("Max");
        subCategories.add("Three");
        subCategories.add("Five");
        subCategories.add("Ten");
        this.subCategories = subCategories;
        subCategoriesView.setOptions(this.subCategories);
    }

    private void initView(View view){
        TextView categoryHeaderView = view.findViewById(R.id.fragment_track_category_header);
        categoryHeaderView.setText(selectedCategory);
        subCategoriesView = view.findViewById(R.id.fragment_track_sub_category_selector_view);
        subCategoriesView.setDelegate(this);
    }

    private void initGraphView(View view){
        GraphView graph = view.findViewById(R.id.fragment_track_graph_view);

        // remove grid
        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);

        LineGraphSeries<DataPoint> lines = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(1, 35),
                new DataPoint(2, 42),
                new DataPoint(3, 30),
                new DataPoint(4, 60),
                new DataPoint(5, 70)
        });
        PointsGraphSeries<DataPoint> points = new PointsGraphSeries<>(new DataPoint[] {
                new DataPoint(1, 35),
                new DataPoint(2, 42),
                new DataPoint(3, 30),
                new DataPoint(4, 60),
                new DataPoint(5, 70)
        });
        points.setOnDataPointTapListener(new OnDataPointTapListener(){

            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Toast.makeText(getActivity(), "y: " + dataPoint.getY(), Toast.LENGTH_SHORT).show();
            }
        });
        graph.addSeries(lines);
        graph.addSeries(points);
        graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);
    }

    void setCategory(String category) {
        this.selectedCategory = category;
    }

    @Override
    public void onTitleChanged(String title) {

    }

    @Override
    public void onTitleClicked() {
        if (subCategoryBottomSheet == null){
            BottomSheetTitleEntity titleEntity =
                    new BottomSheetTitleEntity(
                            selectedCategory,
                            getString(R.string.fragment_track_sub_categories_sub_title)
                    );
            subCategoryBottomSheet = BottomSheetDialog.newInstance(titleEntity, subCategories);
            subCategoryBottomSheet.setDelegate(this);
            subCategoryBottomSheet.setShowsDialog(true);
            subCategoryBottomSheet.show(getFragmentManager(), "Sub Categories Bottom Sheet Dialog");
        } else if (!subCategoryBottomSheet.getShowsDialog()){
            subCategoryBottomSheet.setDelegate(this);
            subCategoryBottomSheet.setShowsDialog(true);
            subCategoryBottomSheet.show(getFragmentManager(), "Sub Categories Bottom Sheet Dialog");
        }
    }

    @Override
    public void onBackArrowClicked() {
        fragmentNavigation.popFragment();
    }

    @Override
    public void onBottomSheetItemClicked(BottomSheetDialog dialogInstance, String option, int position) {
        subCategoriesView.setTitle(option);
    }

    @Override
    public void onBottomSheetDialogDismissed(BottomSheetDialog dialogInstance) {
        subCategoryBottomSheet.setShowsDialog(false);
    }
}
