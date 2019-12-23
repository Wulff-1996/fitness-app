package com.example.fitness_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitness_app.R;
import com.example.fitness_app.models.QuestEntity;
import com.example.fitness_app.util.Dates;

import java.util.List;

public class QuestManagementAdapter extends RecyclerView.Adapter<QuestManagementAdapter.QuestManagementViewHolder> {
    public Context context;
    private List<QuestEntity> quests;
    private LayoutInflater mInflater;
    private AdapterOnItemClickListener mClickListener;

    public QuestManagementAdapter(Context context, List<QuestEntity> quests) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.quests = quests;
    }

    public void setClickListener(AdapterOnItemClickListener listener) {
        this.mClickListener = listener;
    }

    public List<QuestEntity> getQuests() {
        return quests;
    }

    public void setQuests(List<QuestEntity> quests) {
        this.quests = quests;
    }

    @NonNull
    @Override
    public QuestManagementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.quest_management_item, parent, false);
        return new QuestManagementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestManagementViewHolder holder, final int position) {
        holder.populateView(quests.get(position));
    }

    @Override
    public int getItemCount() {
        return quests.size();
    }

    public class QuestManagementViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView titleView = itemView.findViewById(R.id.quests_management_item_title);
        private TextView experiencePointsValueView = itemView.findViewById(R.id.quests_management_item_experience_points);
        private TextView levelValueView = itemView.findViewById(R.id.quests_management_item_level_value);
        private TextView createdDateValueView = itemView.findViewById(R.id.quests_management_item_date_created_value);


        private QuestManagementViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        private void populateView(QuestEntity data) {
            titleView.setText(data.getTitle());
            experiencePointsValueView.setText(String.valueOf(data.getExperiencePoints()));
            levelValueView.setText(String.valueOf(data.getLevelRequirement()));
            createdDateValueView.setText(Dates.formatDate(data.getDateCreated()));
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }
}