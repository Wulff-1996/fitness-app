package com.example.fitness_app.fragments.measurements;

import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fitness_app.R;
import com.example.fitness_app.fragments.BaseFragment;

public class MeasurementFragment extends BaseFragment {

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

        return view;
    }
}
