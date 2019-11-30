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
import com.example.fitness_app.models.Task;
import com.example.fitness_app.models.TaskWrapper;

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
        holder. completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemListener.OnTaskMarkedComplete(
                        view,
                        position,
                        taskWrappers.get(position));
                holder.updateButtonBackground(data);
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskWrappers.size();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView subject;
        Button completeButton;

        private TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            subject = itemView.findViewById(R.id.task_item_subject);
            completeButton = itemView.findViewById(R.id.task_item_complete_button);
            itemView.setOnClickListener(this);
        }

        private void populateView(final TaskWrapper data){
            subject.setText(data.getTask().getSubject());
            updateButtonBackground(data);
        }

        void updateButtonBackground(TaskWrapper data){
            if (data.getCompletedToday()){
                completeButton.setBackground(context.getDrawable(R.drawable.task_complete_button_round_checked));
            } else {
                completeButton.setBackground(context.getDrawable(R.drawable.task_complete_button_round_unchecked));
            }
        }

        @Override
        public void onClick(View view) {
            if (mItemListener != null){
                mItemListener.onItemClick(view, taskWrappers.get(getAdapterPosition()).getTask(), getAdapterPosition());
            }
        }
    }

    public interface TasksAdapterListener{
        void onItemClick(View view, Task task, int position);
        void OnTaskMarkedComplete(View view, int position, TaskWrapper taskWrapper);
    }
}
