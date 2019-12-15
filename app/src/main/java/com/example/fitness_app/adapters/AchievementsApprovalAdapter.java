package com.example.fitness_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitness_app.R;
import com.example.fitness_app.models.AchievementApprovalRequest;

import java.util.List;

public class AchievementsApprovalAdapter extends RecyclerView.Adapter<AchievementsApprovalAdapter.AchievementApprovalViewHolder> {
    public Context context;
    private List<AchievementApprovalRequest> requests;
    private LayoutInflater mInflater;
    private AdapterOnItemClickListener mClickListener;

    public AchievementsApprovalAdapter(Context context, List<AchievementApprovalRequest> requests) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.requests = requests;
    }

    public void setClickListener(AdapterOnItemClickListener listener){this.mClickListener = listener;}
    public void setRequests(List<AchievementApprovalRequest> requests){this.requests = requests;}
    public List<AchievementApprovalRequest> getRequests(){return this.requests;}

    @NonNull
    @Override
    public AchievementApprovalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.achievement_approval_item, parent, false);
        return new AchievementApprovalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AchievementApprovalViewHolder holder, final int position) {
        holder.populateView(requests.get(position));
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    public class AchievementApprovalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView achievementTitleView = itemView.findViewById(R.id.achievement_approval_item_achievement_title);
        private TextView userName = itemView.findViewById(R.id.achievement_approval_item_user_value);
        private TextView dateValue = itemView.findViewById(R.id.achievement_approval_item_date_value);


        private AchievementApprovalViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        private void populateView(final AchievementApprovalRequest data){
            achievementTitleView.setText(data.getAchievementTitle());
            userName.setText(data.getUserNickname());
            dateValue.setText(data.getRequestDate());

        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null){
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }
}
