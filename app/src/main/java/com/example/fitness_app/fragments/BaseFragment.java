package com.example.fitness_app.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import icepick.Icepick;

public abstract class BaseFragment extends Fragment {
    public FragmentNavigation fragmentNavigation;
    private LoadingFragment loadingFragment;

    public void showProgressBar(boolean isShowing) {
        if (isShowing) {
            loadingFragment = LoadingFragment.newInstance();
            assert getFragmentManager() != null;
            loadingFragment.show(getFragmentManager(), "progress dialog");
        } else {
            loadingFragment.dismiss();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // to register bundle for saving state
        Icepick.restoreInstanceState(this, savedInstanceState);
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

    public interface FragmentNavigation {
        void pushFragment(Fragment fragment);
        void popFragment();
    }
}
