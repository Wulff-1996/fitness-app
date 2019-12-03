package com.example.fitness_app.fragments.tasks;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fitness_app.R;
import com.example.fitness_app.models.Task;
import com.example.fitness_app.views.IconView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class NewEditTaskDialog extends BottomSheetDialogFragment {
    private NewTaskDialogDelegate delegate;
    private  Button newButton;
    private EditText taskEditText;
    private String icon;
    private String headline;
    private Task task;

    public NewEditTaskDialog() {
        super();
    }

    private NewEditTaskDialog getInstance(){return this;}
    void setDelegate(NewTaskDialogDelegate delegate){this.delegate = delegate;}
    void setIcon(String icon){this.icon = icon;}
    void setHeadline(String headline){this.headline = headline;}
    void setTask(Task task){this.task = task;}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_task_bottom_sheet_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        taskEditText = view.findViewById(R.id.fragment_new_task_bottom_sheet_dialog_task);
        Button cancelButton = view.findViewById(R.id.fragment_new_task_bottom_sheet_dialog_cancel_button);
        newButton = view.findViewById(R.id.fragment_new_task_bottom_sheet_dialog_save_button);
        IconView iconView = view.findViewById(R.id.fragment_new_task_bottom_sheet_dialog_task_icon);
        TextView headlineView = view.findViewById(R.id.fragment_new_task_bottom_sheet_dialog_title);

        iconView.setText(icon);
        headlineView.setText(headline);
        if (task != null){
            taskEditText.setText(task.getTitle());
        }


        taskEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // only enable save button if there is inputted text for a task
                enableSaveButtonIfNeeded(count);
            }
        });

        cancelButton.setOnClickListener(view1 -> getInstance().dismiss());
        newButton.setOnClickListener(view12 -> handleSave());
        newButton.setEnabled(false);

    }

    // enable save button if there is any text for the task
    private void enableSaveButtonIfNeeded(int count){
        newButton.setEnabled(count > 0);
    }

    // handle save of new task
    private void handleSave(){
        Task task = new Task();
        task.setTitle(taskEditText.getText().toString());
        delegate.onDone(task);
        this.dismiss();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (delegate == null){
            if (context instanceof NewTaskDialogDelegate){
                delegate = (NewTaskDialogDelegate) context;
            } else {
                throw new RuntimeException("Context must implement the BottomSheetDialogDelegate interface");
            }
        }
    }

    @Override
    public void onDetach() {
        delegate.onTaskDialogDismissed(this);
        delegate = null;
        super.onDetach();
    }

    public interface NewTaskDialogDelegate {
        void onDone(Task task);
        void onTaskDialogDismissed(NewEditTaskDialog dialogInstance);
    }
}
