package com.example.fitness_app.fragments.achievements;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.fitness_app.R;
import com.example.fitness_app.fragments.BaseFragment;
import com.example.fitness_app.models.AchievementAccountEntity;
import com.example.fitness_app.views.IconButton;
import com.example.fitness_app.views.IconView;

public class AchievementEntryFragment extends BaseFragment {
    private AchievementAccountEntity achievement;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_achievement_entry, container, false);

        setupToolbar(view);
        populateView(view);

        return view;
    }

    private void setupToolbar(View view){
        ConstraintLayout toolbar = view.findViewById(R.id.fragment_achievement_entry_toolbar);
        IconButton backArrow = toolbar.findViewById(R.id.application_toolbar_back_arrow);
        backArrow.setOnClickListener(view1 -> {
            fragmentNavigation.popFragment();
        });
    }

    private void populateView(View view){
        TextView titleView = view.findViewById(R.id.fragment_achievement_entry_title_value);
        IconView completedIconView = view.findViewById(R.id.fragment_achievement_entry_completed_icon);
        TextView completedTextView = view.findViewById(R.id.fragment_achievement_entry_completed_value);
        TextView playersCompletedView = view.findViewById(R.id.fragment_achievement_entry_players_completed_value);
        TextView typeView = view.findViewById(R.id.fragment_achievement_entry_achievement_type_value);
        TextView achievementPointsView = view.findViewById(R.id.fragment_achievement_entry_achievement_points_value);
        TextView progressValueView = view.findViewById(R.id.fragment_achievement_entry_progress_value);
        ProgressBar progressBarView = view.findViewById(R.id.fragment_achievement_entry_progress_progress_bar);
        TextView descriptionView = view.findViewById(R.id.fragment_achievement_entry_description_value);

        titleView.setText(achievement.getTitle());
        playersCompletedView.setText(String.valueOf(achievement.getTotalPlayersCompleted()));
        typeView.setText(achievement.getType());
        achievementPointsView.setText(String.valueOf(achievement.getAchievementPoints()));
        progressValueView.setText(getString(R.string.completedOutOfTotal, achievement.getCompletedCount(), achievement.getAmountToCompleteCount()));
        float result =  ((float)achievement.getCompletedCount()/(float)achievement.getAmountToCompleteCount()) * (float) 100L;
        int intResult = (int) (float) result;
        progressBarView.setProgress(intResult);

        descriptionView.setText(achievement.getDescription());


        if (!achievement.getIsCompleted()){
            completedIconView.setVisibility(View.GONE);
            completedTextView.setVisibility(View.GONE);
        }



    }

    public void setAchievement(AchievementAccountEntity achievement){this.achievement = achievement;}
}
