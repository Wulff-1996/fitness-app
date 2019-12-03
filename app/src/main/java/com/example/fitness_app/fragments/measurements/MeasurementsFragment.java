package com.example.fitness_app.fragments.measurements;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.fitness_app.R;
import com.example.fitness_app.activities.LoginActivity;
import com.example.fitness_app.fragments.buttom_sheet_dialogs.CreateMeasurementDialog;
import com.example.fitness_app.models.Benchmark;
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
import java.util.Map;

public class MeasurementsFragment extends Fragment implements CreateMeasurementDialog.CreateBenchmarkDialogDelegate
{
    private TextView titleText;
    private ListView listView;
    private ArrayAdapter adapter;
    private Map<String, Benchmark> measurements;
    private List benchmarkNames;
    private View view;
    private String category;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_measurements, container, false);
        //initView();
        //initGraphView();
        init();

        return view;
    }

    private void init()
    {
        listView = view.findViewById(R.id.listview);
        benchmarkNames = new ArrayList();

        category = getArguments().getString("CATEGORY");
        titleText = view.findViewById(R.id.titleTextView);
        titleText.setText(category);
        updateListView();
    }

    private void updateListView()
    {
        measurements = LoginActivity.userAccount.getBenchmarks();
        for (Map.Entry<String, Benchmark> entry: measurements.entrySet())
        {
            if (entry.getValue().getExerciseCategory().equals(category))
            {
                benchmarkNames.add(entry.getKey() + ": " + entry.getValue().getValue());
            }
        }
        adapter = new ArrayAdapter(view.getContext(), android.R.layout.simple_list_item_1, benchmarkNames);
        listView.setAdapter(adapter);
    }


    public void addMeasurement(View v)
    {
        // Create popup with input fields for date and value
        CreateMeasurementDialog popup = new CreateMeasurementDialog();
        popup.setDelegate(this);
        popup.show(getFragmentManager(), "Create measurement");

        // Create benchmark based on inputfields

        // Refresh benchmarkNames list
    }


    private void initView(){
        TextView categoryHeaderView = view.findViewById(R.id.fragment_track_category_header);
        categoryHeaderView.setText(category);
    }

    private void initGraphView(){
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

    @Override
    public void onDelete()
    {

    }

    @Override
    public void OnConfirmDeleteDialogDismissed(CreateMeasurementDialog dialogInstance)
    {

    }


}
