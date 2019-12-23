package com.example.fitness_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitness_app.R;
import com.example.fitness_app.models.QuestApprovalRequestEntity;
import com.example.fitness_app.util.Dates;

import java.util.List;

public class QuestsApprovalAdapter extends RecyclerView.Adapter<QuestsApprovalAdapter.QuestApprovalViewHolder> {
    public Context context;
    private List<QuestApprovalRequestEntity> requests;
    private LayoutInflater mInflater;
    private AdapterOnItemClickListener mClickListener;

    public QuestsApprovalAdapter(Context context, List<QuestApprovalRequestEntity> requests) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.requests = requests;
    }

    public void setClickListener(AdapterOnItemClickListener listener) {
        this.mClickListener = listener;
    }

    public void setRequests(List<QuestApprovalRequestEntity> requests) {
        this.requests = requests;
    }

    public List<QuestApprovalRequestEntity> getRequests() {
        return this.requests;
    }

    @NonNull
    @Override
    public QuestApprovalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.quest_approval_item, parent, false);
        return new QuestApprovalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestApprovalViewHolder holder, final int position) {
        holder.populateView(requests.get(position));
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    public class QuestApprovalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView questTitleView = itemView.findViewById(R.id.quest_approval_item_quest_title);
        private TextView userName = itemView.findViewById(R.id.quest_approval_item_user_value);
        private TextView dateValue = itemView.findViewById(R.id.quest_approval_item_date_value);


        private QuestApprovalViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        private void populateView(QuestApprovalRequestEntity data) {
            questTitleView.setText(data.getQuestTitle());
            userName.setText(data.getUsername());
            dateValue.setText(Dates.formatDate(data.getRequestDate()));
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }
}