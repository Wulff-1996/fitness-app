package com.example.fitness_app.fragments.measurements;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.fitness_app.R;
import com.example.fitness_app.activities.LoginActivity;
import com.example.fitness_app.fragments.buttom_sheet_dialogs.BottomSheetDialog;
import com.example.fitness_app.fragments.buttom_sheet_dialogs.CreateMeasurementDialog;
import com.example.fitness_app.fragments.buttom_sheet_dialogs.EditMeasurementDialog;
import com.example.fitness_app.models.Benchmark;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

public class MeasurementsFragment extends Fragment implements BottomSheetDialog.BottomSheetDialogDelegate
{
    private TextView titleText;
    private ListView listView;
    private ArrayAdapter adapter;
    private Map<String, Benchmark> measurements;
    private List benchmarkNames;
    private View view;
    private String category;
    private FloatingActionButton createBenchmark;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_measurements, container, false);

        //initGraphView();
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
                editMeasurement(position);
            }
        });
        benchmarkNames = new ArrayList();

        category = getArguments().getString("CATEGORY");
        titleText = view.findViewById(R.id.titleTextView);
        titleText.setText(category);
        createBenchmark = view.findViewById(R.id.activity_measurements_addBtn);
        createBenchmark.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                addMeasurement();
            }
        });
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


    private void addMeasurement()
    {
        Bundle bundle = new Bundle();
        bundle.putString("CATEGORY", category);

        // Create popup with input fields for date/value
        CreateMeasurementDialog createMeasurementDialog = new CreateMeasurementDialog();
        createMeasurementDialog.setArguments(bundle);
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev =  fm.findFragmentByTag("dialog");
        if(prev != null)
        {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        createMeasurementDialog.show(ft, "dialog");
    }

    private void editMeasurement(int position)
    {
        String itemID = adapter.getItem(position).toString().split(":")[0];
        Bundle bundle = new Bundle();
        bundle.putString("ID", itemID);

        // Create popup with input fields for date/value
        EditMeasurementDialog editMeasurementDialog = new EditMeasurementDialog();
        editMeasurementDialog.setArguments(bundle);
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev =  fm.findFragmentByTag("dialog");
        if(prev != null)
        {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        editMeasurementDialog.show(ft, "dialog");
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
    public void onBottomSheetItemClicked(BottomSheetDialog dialogInstance, String option, int position)
    {

    }

    @Override
    public void onBottomSheetDialogDismissed(BottomSheetDialog dialogInstance)
    {

    }
}
