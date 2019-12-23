package com.example.fitness_app.activities;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {
    private Toast toast;
    private ProgressBar progressBar;
    private boolean hasShownInitialLoading = false;
    private boolean isFetching = false;

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public void showProgress(boolean show) {
        if (progressBar != null){
            if (show){
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.GONE);
            }
        }
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
    public boolean hasShownInitialLoading() {
        return hasShownInitialLoading;
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        hasShownInitialLoading = true;
    }
}
