package com.example.fitness_app.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.fitness_app.R;

public class BackArrowView extends ConstraintLayout {
    private BackArrowViewDelegate delegate;

    public BackArrowView(Context context) {
        super(context);
        initView();
    }
    public BackArrowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }
    public BackArrowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView(){
        inflate(getContext(), R.layout.back_arrow_view, this);
        ImageButton backArrowImageButton = findViewById(R.id.back_arrow_view);
        backArrowImageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                delegate.onBackArrowClicked();
            }
        });

    }

    public void setDelegate(BackArrowViewDelegate delegate){this.delegate = delegate;}

    public interface BackArrowViewDelegate{
        void onBackArrowClicked();
    }
}
