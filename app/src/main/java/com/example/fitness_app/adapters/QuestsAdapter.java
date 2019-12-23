package com.example.fitness_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitness_app.R;
import com.example.fitness_app.constrants.QuestStatusTypes;
import com.example.fitness_app.models.QuestAccountEntity;
import com.example.fitness_app.storage.StorageManager;
import com.example.fitness_app.util.Dates;
import com.example.fitness_app.views.IconView;

import java.util.List;

public class QuestsAdapter extends RecyclerView.Adapter<QuestsAdapter.QuestViewHolder> {
    public Context context;
    private List<QuestAccountEntity> quests;
    private LayoutInflater mInflater;
    private AdapterOnItemClickListener mClickListener;

    public QuestsAdapter(Context context, List<QuestAccountEntity> quests) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.quests = quests;
    }

    public void setClickListener(AdapterOnItemClickListener listener) {
        this.mClickListener = listener;
    }

    public List<QuestAccountEntity> getQuests() {
        return quests;
    }

    public void setQuests(List<QuestAccountEntity> quests) {
        this.quests = quests;
    }

    @NonNull
    @Override
    public QuestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.quest_item, parent, false);
        return new QuestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestViewHolder holder, int position) {
        holder.populateView(quests.get(position));
    }

    @Override
    public int getItemCount() {
        return quests.size();
    }

    public class QuestViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private IconView statusIconView = itemView.findViewById(R.id.quest_item_status_icon);
        private TextView titleView = itemView.findViewById(R.id.quest_item_title);
        private TextView experiencePointsValueView = itemView.findViewById(R.id.quest_item_experience_points_value);
        private TextView levelValueView = itemView.findViewById(R.id.quest_item_level_value);
        private TextView statusMessage = itemView.findViewById(R.id.quest_item_invalid_status_message);
        private IconView experiencePointsIcon = itemView.findViewById(R.id.quest_item_experience_points_icon);
        private IconView levelIcon = itemView.findViewById(R.id.quest_item_level_icon);


        private QuestViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        private void populateView(QuestAccountEntity data) {
            titleView.setText(data.getTitle());
            experiencePointsValueView.setText(String.valueOf(data.getExperiencePoints()));
            levelValueView.setText(String.valueOf(data.getLevelRequirement()));

            if (data.getIsCompleted()) {
                statusIconView.setText(context.getString(R.string.icon_checked_circle));
                statusIconView.setTextColor(context.getColor(R.color.accent));
                experiencePointsIcon.setTextColor(context.getColor(R.color.accent));
                levelIcon.setTextColor(context.getColor(R.color.accent));
            } else if (data.getExpireDate() != null &&
                        Dates.isDeadlineReached(data.getExpireDate())){
                statusIconView.setText(context.getString(R.string.icon_clock));
                statusIconView.setTextColor(context.getColor(R.color.icon_color_muted));
                titleView.setTextColor(context.getColor(R.color.mutedText));
                statusMessage.setVisibility(View.VISIBLE);
                statusMessage.setText("Expired");
            }
            else if (data.getLevelRequirement() > StorageManager.getInstance(context).getAccount().retrieveLevelInformation().get(0)) {
                statusIconView.setText(context.getString(R.string.icon_lock));
                titleView.setTextColor(context.getColor(R.color.mutedText));
                statusIconView.setTextColor(context.getColor(R.color.icon_color_muted));
                statusMessage.setVisibility(View.VISIBLE);
                statusMessage.setText("Level to High");
                statusMessage.setTextColor(context.getColor(R.color.warning));
            } else {
                statusIconView.setText(context.getString(R.string.icon_unchecked_circle));
                experiencePointsIcon.setTextColor(context.getColor(R.color.icon_color));
                levelIcon.setTextColor(context.getColor(R.color.icon_color));
                updateStatus(data.getStatus());
            }
        }

        private void updateStatus(String status){
            switch (status){
                case QuestStatusTypes.NOT_REQUESTED:
                    statusMessage.setVisibility(View.VISIBLE);
                    statusMessage.setText("Not requested");
                    statusMessage.setTextColor(context.getColor(R.color.mutedText));
                    break;

                case QuestStatusTypes.PENDING_APPROVAL:
                    statusMessage.setVisibility(View.VISIBLE);
                    statusMessage.setText("Pending Approval");
                    statusMessage.setTextColor(context.getColor(R.color.warning));
                    break;

                case QuestStatusTypes.DECLINED:
                    statusMessage.setVisibility(View.VISIBLE);
                    statusMessage.setText("Declined");
                    statusMessage.setTextColor(context.getColor(R.color.cancel));
                    break;
            }
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }
}
