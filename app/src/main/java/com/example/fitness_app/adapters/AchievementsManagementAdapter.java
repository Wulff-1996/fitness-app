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
import com.example.fitness_app.util.Dates;

import java.util.List;

public class AchievementsManagementAdapter extends RecyclerView.Adapter<AchievementsManagementAdapter.AchievementManagementViewHolder> {
    public Context context;
    private List<AchievementEntryEntity> achievements;
    private LayoutInflater mInflater;
    private AdapterOnItemClickListener mClickListener;

    public AchievementsManagementAdapter(Context context, List<AchievementEntryEntity> achievements) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.achievements = achievements;
    }

    public void setClickListener(AdapterOnItemClickListener listener) {
        this.mClickListener = listener;
    }

    public void setAchievements(List<AchievementEntryEntity> achievements) {
        this.achievements = achievements;
    }

    public List<AchievementEntryEntity> getAchievements() {
        return this.achievements;
    }

    @NonNull
    @Override
    public AchievementManagementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.achievement_management_item, parent, false);
        return new AchievementManagementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AchievementManagementViewHolder holder, final int position) {
        holder.populateView(achievements.get(position));
    }

    @Override
    public int getItemCount() {
        return achievements.size();
    }

    public class AchievementManagementViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView titleView = itemView.findViewById(R.id.achievement_management_item_title);
        private TextView achievementsPointsView = itemView.findViewById(R.id.achievement_management_item_achievement_points);
        private TextView totalPlayersCompletedView = itemView.findViewById(R.id.achievement_management_item_total_players_completed);
        private TextView dateCreatedView = itemView.findViewById(R.id.achievement_management_item_date_created_value);


        private AchievementManagementViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        private void populateView(final AchievementEntryEntity data) {
            titleView.setText(data.getTitle());
            achievementsPointsView.setText(String.valueOf(data.getAchievementPoints()));
            totalPlayersCompletedView.setText(String.valueOf(data.getTotalPlayersCompleted()));
            dateCreatedView.setText(Dates.formatDate(data.getDateCreated()));
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }
}