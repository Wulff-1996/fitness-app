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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fitness_app.R;
import com.example.fitness_app.models.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class NewTaskBottomSheetDialog extends BottomSheetDialogFragment {
    private NewTaskDialogDelegate delegate;
    private  Button newButton;
    private EditText taskEditText;

    public NewTaskBottomSheetDialog() {
        super();
    }

    private NewTaskBottomSheetDialog getInstance(){return this;}
    void setDelegate(NewTaskDialogDelegate delegate){this.delegate = delegate;}

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

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getInstance().dismiss();
            }
        });
        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleSave();
            }
        });
        newButton.setEnabled(false);

    }

    // enable save button if there is any text for the task
    private void enableSaveButtonIfNeeded(int count){
        newButton.setEnabled(count > 0);
    }

    // handle save of new task
    private void handleSave(){
        Task task = new Task();
        task.setSubject(taskEditText.getText().toString());
        delegate.onTaskSaved(task);
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
        void onTaskSaved(Task task);
        void onTaskDialogDismissed(NewTaskBottomSheetDialog dialogInstance);
    }
}
