package com.example.fitness_app.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.example.fitness_app.constrants.SubCategoryType;
import com.example.fitness_app.entities.BottomSheetActionItemEntity;
import com.example.fitness_app.entities.BottomSheetTitleEntity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fitness_app.R;

import java.util.List;

/**
 * Class for displaying a modal from the bottom of the screen
 */
public class BottomSheetDialog extends BottomSheetDialogFragment {

    private BottomSheetTitleEntity title;
    private List<BottomSheetActionItemEntity> items;
    private BottomSheetDialogDelegate delegate;
    private BottomSheetDialog mInstance;

    private BottomSheetDialog(BottomSheetTitleEntity title, List<BottomSheetActionItemEntity> items) {
        this.title = title;
        this.items = items;
        mInstance = this;
    }

    public void setDelegate(BottomSheetDialogDelegate delegate){
        this.delegate = delegate;
    }

    public List<BottomSheetActionItemEntity> getItems() {
        return this.items;
    }

    public static BottomSheetDialog newInstance(BottomSheetTitleEntity title, List<BottomSheetActionItemEntity> items) {
        return new BottomSheetDialog(title, items);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bottom_sheet_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        final RecyclerView recyclerView = view.findViewById(R.id.fragment_bottom_sheet_dialog_action_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new BottomSheetAdapter());

        TextView headlineTextView = view.findViewById(R.id.fragment_bottom_sheet_dialog_header);
        TextView sublineTextView = view.findViewById(R.id.fragment_bottom_sheet_dialog_subline);
        TextView cancelTextView = view.findViewById(R.id.fragment_bottom_sheet_dialog_cancel);

        headlineTextView.setText(title.getHeadline());
        sublineTextView.setText(title.getSubline());
        cancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delegate.onBottomSheetDialogDismissed(mInstance);
                dismiss();
            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        final Fragment parent = getParentFragment();
        if (parent != null) {
            delegate = (BottomSheetDialogDelegate) parent;
        } else {
            delegate = (BottomSheetDialogDelegate) context;
        }
    }

    @Override
    public void onDetach() {
        delegate = null;
        super.onDetach();
    }

    public interface BottomSheetDialogDelegate {
        void onBottomSheetItemClicked(BottomSheetDialog dialogInstance, BottomSheetActionItemEntity actionInstance, int position);

        void onBottomSheetDialogDismissed(BottomSheetDialog dialogInstance);
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        final TextView titleView;

        ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.fragment_bottom_sheet_dialog_item, parent, false));
            titleView = itemView.findViewById(R.id.fragment_bottom_sheet_dialog_item_title);
            titleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (delegate != null) {
                        delegate.onBottomSheetItemClicked(mInstance, items.get(getAdapterPosition()), getAdapterPosition());
                        dismiss();
                    }
                }
            });
        }

    }

    private class BottomSheetAdapter extends RecyclerView.Adapter<ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.titleView.setText(items.get(position).getTitle());
            if (items.get(position).getColor() != null) {
                holder.titleView.setTextColor(items.get(position).getColor());
            }
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

    }

}
