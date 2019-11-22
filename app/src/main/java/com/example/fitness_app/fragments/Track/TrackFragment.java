package com.example.fitness_app.fragments.Track;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import com.example.fitness_app.R;
import com.example.fitness_app.views.SelectorView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.jjoe64.graphview.series.Series;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class TrackFragment extends Fragment implements SelectorView.SelectorViewDelegate {
    private String selectedCategory;
    private List<String> subCategories = new ArrayList<>();
    private TrackFragment mInstance;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mInstance = this;



        //TODO fetch sub categories for specific category
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_track, container, false);

        /*view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_BACK){
                    getActivity().getFragmentManager().popBackStack();
                    return true;
                }
                return false;
            }
        });*/

        fetchSubCategories();
        initView(view);
        initGraphView(view);

        return view;
    }

    // TODO fetch sub categories from database
    private void fetchSubCategories(){
        List<String> subCategories = new ArrayList<>();
        subCategories.add("Max");
        subCategories.add("Three");
        subCategories.add("Five");
        subCategories.add("Ten");
        this.subCategories = subCategories;
    }

    private void initView(View view){
        TextView categoryHeaderView = view.findViewById(R.id.fragment_track_category_header);
        categoryHeaderView.setText(selectedCategory);


        SelectorView subCategoriesView = view.findViewById(R.id.fragment_track_sub_category_selector_view);
        subCategoriesView.setOptions(subCategories);
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

    }


}
