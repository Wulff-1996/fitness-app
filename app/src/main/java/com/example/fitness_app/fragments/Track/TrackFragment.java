package com.example.fitness_app.fragments.Track;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.fitness_app.R;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.jjoe64.graphview.series.Series;

import java.util.Calendar;
import java.util.Date;

public class TrackFragment extends Fragment {
    private String category;
    private String subCategory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_track, container, false);

        initGraphView(view);

        return view;
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
        this.category = category;
    }

    void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }
}
