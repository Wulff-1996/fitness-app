package com.example.fitness_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitness_app.R;
import com.example.fitness_app.models.TaskWrapper;
import com.example.fitness_app.models.UserTask;
import com.google.android.material.chip.Chip;

import java.util.List;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TaskViewHolder> {
    public Context context;
    private List<TaskWrapper> taskWrappers;
    private LayoutInflater mInflater;
    private TasksAdapterListener mItemListener;

    public TasksAdapter(Context context, List<TaskWrapper> tasksWrappers) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.taskWrappers = tasksWrappers;
    }

    public void setmItemListener(TasksAdapterListener listener){this.mItemListener = listener;}
    public void setTaskWrappers(List<TaskWrapper> taskWrappers){this.taskWrappers = taskWrappers;}
    public List<TaskWrapper> getTaskWrappers(){return this.taskWrappers;}

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TaskViewHolder holder, final int position) {
        final TaskWrapper data = taskWrappers.get(position);
        holder.populateView(data);
        holder. completeButton.setOnClickListener(view -> {
            mItemListener.OnTaskMarkedComplete(
                    view,
                    position,
                    taskWrappers.get(position));
            holder.updateStatus(data);
        });
    }

    @Override
    public int getItemCount() {
        return taskWrappers.size();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView subject;
        Button completeButton;
        Chip isCompletedChip;

        private TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            subject = itemView.findViewById(R.id.task_item_task_title_value);
            completeButton = itemView.findViewById(R.id.task_item_mark_complete_button);
            isCompletedChip = itemView.findViewById(R.id.task_item_is_completed_chip);
            itemView.setOnClickListener(this);
        }

        private void populateView(final TaskWrapper data){
            subject.setText(data.getUserTask().getTitle());
            updateStatus(data);
        }

        void updateStatus(TaskWrapper data){
            if (data.getCompletedToday()){
                isCompletedChip.setChipBackgroundColorResource(R.color.success);
                completeButton.setText(context.getString(R.string.task_item_mark_uncomplete_today));
            } else {
                isCompletedChip.setChipBackgroundColorResource(R.color.cancel);
                completeButton.setText(context.getString(R.string.task_item_mark_complete_today));}
        }

        @Override
        public void onClick(View view) {
            if (mItemListener != null){
                mItemListener.onItemClick(view, taskWrappers.get(getAdapterPosition()).getUserTask(), getAdapterPosition());
            }
        }
    }

    public interface TasksAdapterListener{
        void onItemClick(View view, UserTask userTask, int position);
        void OnTaskMarkedComplete(View view, int position, TaskWrapper taskWrapper);
    }
}
