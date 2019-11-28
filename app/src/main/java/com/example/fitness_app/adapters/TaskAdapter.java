package com.example.fitness_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitness_app.R;
import com.example.fitness_app.entities.TasksEntity;
import com.example.fitness_app.util.ProcentCalculator;
import com.google.android.material.chip.Chip;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TrackCategoriesAdapter.CategoriesViewHolder> {
    public Context context;
    private List<TasksEntity> todoEntities;
    private LayoutInflater mInflater;
    private ItemClickListener mItemClickListener;

    public TaskAdapter(Context context, List<TasksEntity> todoEntities) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.todoEntities = todoEntities;
    }

    public TaskAdapter getInstance(){return this;}

    @NonNull
    @Override
    public TrackCategoriesAdapter.CategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull TrackCategoriesAdapter.CategoriesViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    private class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Chip durationChip;
        TextView subject;
        ProgressBar progressBar;
        TextView counter;
        ImageButton rightArrow;
        ImageButton leftArrow;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            durationChip = itemView.findViewById(R.id.todo_item_duration_type);
            subject = itemView.findViewById(R.id.todo_item_subject);
            progressBar = itemView.findViewById(R.id.todo_item_progressBar);
            counter = itemView.findViewById(R.id.todo_item_counter);
            rightArrow = itemView.findViewById(R.id.todo_item_right_arrow);
            leftArrow = itemView.findViewById(R.id.todo_item_left_arrow);
            populateView(todoEntities.get(getAdapterPosition()));
        }

        private void populateView(TasksEntity data){
            switch (data.getDurationType()){
                case "day":
                    durationChip.setChipIcon(context.getDrawable(R.drawable.ic_calendar_day_solid));
                    durationChip.setText("Day");
                    break;
                case "week":
                    durationChip.setChipIcon(context.getDrawable(R.drawable.ic_calendar_week_solid));
                    durationChip.setText("Week");
                    break;
                case "month":
                    durationChip.setChipIcon(context.getDrawable(R.drawable.ic_calendar_month_solid));
                    durationChip.setText("Month");
                    break;
            }

            subject.setText(data.getSubject());
            progressBar.setProgress(ProcentCalculator.getProcent(data.getTaskAmount(), data.getCompletedTasks()));
            counter.setText("" + data.getCompletedTasks() + "/" + data.getTaskAmount());
            rightArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            leftArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }


        @Override
        public void onClick(View view) {
            if (mItemClickListener != null){
                mItemClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    public interface ItemClickListener{
        void onItemClick(View view, int position);
    }
}
