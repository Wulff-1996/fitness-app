package com.example.fitness_app.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.example.fitness_app.R;

import java.util.Objects;

public class LoadingFragment extends androidx.fragment.app.DialogFragment {
    private LoadingFragmentDelegate delegate;

    private LoadingFragment() {
        // Required empty public constructor
    }

    public void setDelegate(LoadingFragmentDelegate delegate){this.delegate = delegate;}

    public static LoadingFragment newInstance() {
        return new LoadingFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Objects.requireNonNull(getDialog()).setCanceledOnTouchOutside(false);
        Objects.requireNonNull(getDialog().getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_loading, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (delegate == null){
            if (context instanceof LoadingFragmentDelegate) {
                delegate = (LoadingFragmentDelegate) context;
            } else {
                throw new RuntimeException(context.toString()
                        + " must implement LoadingFragmentDelegate");
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        delegate.onLoadingFragmentDismissed();
        delegate = null;
    }

    public interface LoadingFragmentDelegate {
        void onLoadingFragmentDismissed();
    }
}
