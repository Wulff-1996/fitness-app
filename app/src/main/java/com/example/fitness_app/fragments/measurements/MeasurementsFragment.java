package com.example.fitness_app.fragments.measurements;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.fitness_app.R;
import com.example.fitness_app.constrants.Globals;
import com.example.fitness_app.entities.EventBustEvent;
import com.example.fitness_app.fragments.buttom_sheet_dialogs.BottomSheetDialog;
import com.example.fitness_app.fragments.buttom_sheet_dialogs.CreateMeasurementDialog;
import com.example.fitness_app.fragments.buttom_sheet_dialogs.EditMeasurementDialog;
import com.example.fitness_app.models.Benchmark;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static com.example.fitness_app.constrants.EventBusEvents.EVENT_BUS_EVENT_MEASUREMENT_ADDED;
import static com.example.fitness_app.constrants.EventBusEvents.EVENT_BUS_EVENT_MEASUREMENT_DELETED;
import static com.example.fitness_app.constrants.EventBusEvents.EVENT_BUS_EVENT_MEASUREMENT_UPDATED;

public class MeasurementsFragment extends Fragment implements BottomSheetDialog.BottomSheetDialogDelegate
{
    private TextView titleText;
    private ListView listView;
    private ArrayAdapter adapter;
    private TreeMap<String, Benchmark> measurements;
    private List benchmarkNames;
    private View view;
    private String category;
    private FloatingActionButton createBenchmark;
    private GraphView graph;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_measurements, container, false);

        init();
        initGraphView();

        return view;
    }

    private void init()
    {
        measurements = new TreeMap<>();
        benchmarkNames = new ArrayList();
        listView = view.findViewById(R.id.listview);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                editMeasurement(position);
            }
        });

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
        Map<String, Benchmark> tmpMap = Globals.userAccount.getBenchmarks();

        for (Map.Entry<String, Benchmark> entry: tmpMap.entrySet())
        {
            if (entry.getValue().getExerciseCategory().equals(category))
            {
                measurements.put(entry.getKey(), entry.getValue());
            }
        }
        for (TreeMap.Entry<String, Benchmark> entry: measurements.entrySet())
        {
            benchmarkNames.add(entry.getKey() + ": " + entry.getValue().getValue());
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

        graph = view.findViewById(R.id.fragment_measurements_graphview);
        // Hides the graph if no data is available
        if (measurements.size() == 0)
        {
            graph.setVisibility(view.GONE);
        }

        // remove grid
        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);

        DataPoint[] dpArray = new DataPoint[measurements.size()];
        int tmpCount = 0;
        for (TreeMap.Entry<String, Benchmark> entry: measurements.entrySet())
        {
            dpArray[tmpCount] = new DataPoint(tmpCount+1, entry.getValue().getValue());
            tmpCount++;
        }
        LineGraphSeries<DataPoint> lines = new LineGraphSeries<>(dpArray);
        PointsGraphSeries<DataPoint> points = new PointsGraphSeries<>(dpArray);

        points.setOnDataPointTapListener((series, dataPoint) -> Toast.makeText(getActivity(), "Value: " + dataPoint.getY(), Toast.LENGTH_SHORT).show());

        graph.addSeries(points);
        graph.addSeries(lines);
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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBustEvent<Object> event){
        switch (event.getEvent()){
            case EVENT_BUS_EVENT_MEASUREMENT_ADDED:
                init();
                initGraphView();
                break;
            case EVENT_BUS_EVENT_MEASUREMENT_UPDATED:
                init();
                initGraphView();
                break;
            case EVENT_BUS_EVENT_MEASUREMENT_DELETED:
                init();
                initGraphView();
                break;
            default:
                break;
        }
    }
}
