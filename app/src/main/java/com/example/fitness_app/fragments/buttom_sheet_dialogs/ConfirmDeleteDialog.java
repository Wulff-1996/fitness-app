package com.example.fitness_app.fragments.buttom_sheet_dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fitness_app.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ConfirmDeleteDialog extends BottomSheetDialogFragment {
    private ConfirmDeleteDialogDelegate delegate;
    private  Button deleteButton;
    private String headerText;
    private String underHeaderText;

    public ConfirmDeleteDialog() {
        super();
    }

    private ConfirmDeleteDialog getInstance(){return this;}
    public void setDelegate(ConfirmDeleteDialogDelegate delegate){this.delegate = delegate;}
    public void setHeaderText(String headerText){this.headerText = headerText;}
    public void setUnderHeaderText(String underHeaderText){this.underHeaderText = underHeaderText;}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_confirm_delete, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Button cancel = view.findViewById(R.id.dialog_confirm_delete_cancel);
        deleteButton = view.findViewById(R.id.dialog_confirm_delete_delete_button);
        TextView headerView = view.findViewById(R.id.dialog_confirm_delete_header);
        TextView underHeaderView = view.findViewById(R.id.dialog_confirm_delete_under_header);

        if (headerText != null){
            headerView.setText(headerText);
        }
        if (underHeaderText != null){
            underHeaderView.setText(underHeaderText);
        }

        cancel.setOnClickListener(itemView -> getInstance().dismiss());
        deleteButton.setOnClickListener(itemView -> {
            delegate.onDelete();
            dismiss();
        });
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (delegate == null){
            if (context instanceof ConfirmDeleteDialogDelegate){
                delegate = (ConfirmDeleteDialogDelegate) context;
            } else {
                throw new RuntimeException("Context must implement the ConfirmDeleteDialogDelegate interface");
            }
        }
    }

    @Override
    public void onDetach() {
        delegate.OnConfirmDeleteDialogDismissed(this);
        delegate = null;
        super.onDetach();
    }

    public interface ConfirmDeleteDialogDelegate {
        void onDelete();
        void OnConfirmDeleteDialogDismissed(ConfirmDeleteDialog dialogInstance);
    }
}
