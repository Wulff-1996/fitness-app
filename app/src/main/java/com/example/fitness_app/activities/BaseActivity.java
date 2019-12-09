package com.example.fitness_app.activities;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fitness_app.fragments.LoadingFragment;

public abstract class BaseActivity extends AppCompatActivity {
    private LoadingFragment loadingFragment;
    private Toast toast;

    public void showProgressBar(boolean isShowing) {
        if (isShowing) {
            loadingFragment = LoadingFragment.newInstance();
            assert getFragmentManager() != null;
            loadingFragment.show(this.getSupportFragmentManager(), "progress dialog");
        } else {
            loadingFragment.dismiss();
        }
    }

    public void showToast(String message){
        hideToast();
        toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void hideToast(){
        if (toast != null){
            toast.cancel();
        }
    }
}
