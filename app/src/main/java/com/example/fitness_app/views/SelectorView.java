package com.example.fitness_app.views;

import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.fitness_app.R;

import java.util.ArrayList;
import java.util.List;

import icepick.Icepick;
import icepick.State;

public class SelectorView extends ConstraintLayout {

    private List<String> options = new ArrayList<>();
    private TextView titleTextView;
    private ImageButton leftArrowImageButton;
    private ImageButton rightArrowImageButton;
    private SelectorViewDelegate delegate;
    @State
    int selectedIndex = 0;

    public SelectorView(Context context) {
        super(context);
        initView(context);
    }

    public SelectorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SelectorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context){
        inflate(getContext(), R.layout.selector_view, this);
        titleTextView = findViewById(R.id.selector_view_title);
        leftArrowImageButton = findViewById(R.id.selector_view_left_arrow);
        rightArrowImageButton = findViewById(R.id.selector_view_right_arrow);

        titleTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (options == null || options.isEmpty()) return;
                delegate.onTitleClicked();
            }
        });

        leftArrowImageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (options == null || options.isEmpty()) return;
                updateSelectedIndexLeftArrow();
                updateTitle();
                delegate.onTitleChanged(titleTextView.getText().toString());
            }
        });

        rightArrowImageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (options == null || options.isEmpty()) return;
                updateSelectedIndexRightArrow();
                updateTitle();
                delegate.onTitleChanged(titleTextView.getText().toString());
            }
        });
    }

    public void setDelegate(SelectorViewDelegate delegate){this.delegate = delegate;}
    public void setOptions(List<String> options){
        this.options = options;
        updateTitle();
    }
    public void setTitle(String title){
        updateSelectedIndexFromTitle();
        titleTextView.setText(title);
    }

    private void updateSelectedIndexFromTitle() {
        int index = 0;
        for (String title: options) {
            if (titleTextView.getText().equals(title)){
                selectedIndex = index;
            }
            index++;
        }
    }

    private void updateTitle(){
        titleTextView.setText(options.get(selectedIndex));
    }

    private void updateSelectedIndexLeftArrow(){
        if (selectedIndex == 0) {
            selectedIndex = options.size() -1;
        } else {
            selectedIndex--;
        }
    }

    private void updateSelectedIndexRightArrow(){
        if (selectedIndex == options.size() -1){
            selectedIndex = 0;
        } else {
            selectedIndex++;
        }
    }

    @Override
    public Parcelable onSaveInstanceState() {
        return Icepick.saveInstanceState(this, super.onSaveInstanceState());
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(Icepick.restoreInstanceState(this, state));
        if (selectedIndex != 0){
            updateTitle();
        }
    }

    public interface SelectorViewDelegate{
        void onTitleChanged(String title);
        void onTitleClicked();
    }
}


