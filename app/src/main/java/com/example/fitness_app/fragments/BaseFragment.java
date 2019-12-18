package com.example.fitness_app.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.fitness_app.activities.BaseActivity;

import icepick.Icepick;

public abstract class BaseFragment extends Fragment {
    public FragmentNavigation fragmentNavigation;
    private ProgressBar progressBar;
    private boolean hasShownInitialLoading = false;
    private boolean isFetching = false;

    public void showProgress(boolean show) {
        if (progressBar != null){
            if (show){
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // to register bundle for saving state
        Icepick.restoreInstanceState(this, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        hasShownInitialLoading = true;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // register for saving state
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof FragmentNavigation){
            fragmentNavigation = (FragmentNavigation) context;
        }
    }

    public void showToast(String message){
        ((BaseActivity)getActivity()).showToast(message);
    }

    public void hideToast(){
        ((BaseActivity)getActivity()).hideToast();
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public boolean isFetching() {
        return isFetching;
    }

    public void setFetching(boolean fetching) {
        isFetching = fetching;
        if (progressBar != null){
            if (fetching){
                showProgress(true);
            } else {
                showProgress(false);
            }
        }
    }

    public interface FragmentNavigation {
        void pushFragment(Fragment fragment);
        void popFragment();
    }

    public boolean isHasShownInitialLoading() {
        return hasShownInitialLoading;
    }
}
