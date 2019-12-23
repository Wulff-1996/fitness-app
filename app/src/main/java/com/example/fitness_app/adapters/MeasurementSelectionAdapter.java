package com.example.fitness_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitness_app.R;

import java.util.List;

public class MeasurementSelectionAdapter extends RecyclerView.Adapter<MeasurementSelectionAdapter.MeasurementSelectionViewHolder> {
    public Context context;
    private List<String> measurementCategories;
    private LayoutInflater mInflater;
    private AdapterOnItemClickListener mClickListener;

    public MeasurementSelectionAdapter(Context context, List<String> measurementCategories) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.measurementCategories = measurementCategories;
    }

    public void setClickListener(AdapterOnItemClickListener listener) {
        this.mClickListener = listener;
    }

    public List<String> getMeasurementCategories() {
        return measurementCategories;
    }

    public void setMeasurementCategories(List<String> measurementCategories) {
        this.measurementCategories = measurementCategories;
    }

    @NonNull
    @Override
    public MeasurementSelectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.measurement_selection_item, parent, false);
        return new MeasurementSelectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MeasurementSelectionViewHolder holder, int position) {
        holder.populateView(measurementCategories.get(position));
    }

    @Override
    public int getItemCount() {
        return measurementCategories.size();
    }

    public class MeasurementSelectionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView title = itemView.findViewById(R.id.measurement_selection_item_title);


        private MeasurementSelectionViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        private void populateView(String data) {
            title.setText(data);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }
}
