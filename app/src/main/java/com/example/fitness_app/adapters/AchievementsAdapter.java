package com.example.fitness_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitness_app.R;
import com.example.fitness_app.models.AchievementEntryEntity;
import com.example.fitness_app.views.IconView;

import java.util.List;

public class AchievementsAdapter extends RecyclerView.Adapter<AchievementsAdapter.AchievementViewHolder> {
    public Context context;
    private List<AchievementEntryEntity> achievementEntryEntities;
    private LayoutInflater mInflater;
    private AdapterOnItemClickListener mClickListener;

    public AchievementsAdapter(Context context, List<AchievementEntryEntity> achievementEntryEntities) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.achievementEntryEntities = achievementEntryEntities;
    }

    public void setClickListener(AdapterOnItemClickListener listener){this.mClickListener = listener;}
    public void setAchievementEntryEntities(List<AchievementEntryEntity> achievementEntryEntities){this.achievementEntryEntities = achievementEntryEntities;}
    public List<AchievementEntryEntity> getAchievementEntryEntities(){return this.achievementEntryEntities;}

    @NonNull
    @Override
    public AchievementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.achievement_item, parent, false);
        return new AchievementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AchievementViewHolder holder, final int position) {
        holder.populateView(achievementEntryEntities.get(position));
    }

    @Override
    public int getItemCount() {
        return achievementEntryEntities.size();
    }

    public class AchievementViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private IconView completedIconView = itemView.findViewById(R.id.achievement_item_completed_icon);
        private TextView titleView = itemView.findViewById(R.id.achievement_item_title);
        private IconView achievementPointsIconView = itemView.findViewById(R.id.achievement_item_achievement_points_icon);
        private TextView achievementsPointsView = itemView.findViewById(R.id.achievement_item_achievement_points);
        private TextView totalPlayersCompleted = itemView.findViewById(R.id.achievement_item_total_players_completed);


        private AchievementViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        private void populateView(final AchievementEntryEntity data){
            titleView.setText(data.getTitle());
            completedIconView.setText(context.getString(R.string.icon_unchecked_circle));
            completedIconView.setTextColor(context.getColor(R.color.icon_color_muted));
            achievementPointsIconView.setTextColor(context.getColor(R.color.icon_color_muted));
            achievementsPointsView.setText(String.valueOf(data.getAchievementPoints()));
            totalPlayersCompleted.setText(String.valueOf(data.getTotalPlayersCompleted()));

        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null){
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }
}
