package com.example.fitness_app.fragments.achievements;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.fitness_app.R;
import com.example.fitness_app.api.FirestoreRepository;
import com.example.fitness_app.api.FirestoreService;
import com.example.fitness_app.constrants.AchievementStatusTypes;
import com.example.fitness_app.constrants.AchievementTypes;
import com.example.fitness_app.fragments.BaseFragment;
import com.example.fitness_app.interfaces.FirebaseCallback;
import com.example.fitness_app.models.AchievementAccountAutomaticEntity;
import com.example.fitness_app.models.AchievementAccountEntity;
import com.example.fitness_app.models.AchievementAccountManualEntity;
import com.example.fitness_app.models.AchievementApprovalRequest;
import com.example.fitness_app.util.Dates;
import com.example.fitness_app.views.IconButton;
import com.example.fitness_app.views.IconView;

import java.util.UUID;

public class AchievementEntryFragment extends BaseFragment implements View.OnClickListener {
    private AchievementAccountEntity achievement;
    private String userDescription = "";
    private ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_achievement_entry, container, false);

        setupToolbar(view);
        setupProgressBar(view);
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

    private void setupProgressBar(View view){
        progressBar = view.findViewById(R.id.fragment_achievement_entry_progress_bar);
        progressBar.setIndeterminate(true);
        setProgressBar(progressBar);
    }

    private void populateView(View view){
        TextView titleView = view.findViewById(R.id.fragment_achievement_entry_title_value);
        IconView completedIconView = view.findViewById(R.id.fragment_achievement_entry_completed_icon);
        TextView completedTextView = view.findViewById(R.id.fragment_achievement_entry_completed_value);
        TextView playersCompletedView = view.findViewById(R.id.fragment_achievement_entry_players_completed_value);
        TextView achievementPointsView = view.findViewById(R.id.fragment_achievement_entry_achievement_points_value);
        TextView progressValueView = view.findViewById(R.id.fragment_achievement_entry_progress_value);
        ProgressBar progressBarView = view.findViewById(R.id.fragment_achievement_entry_progress_progress_bar);
        IconView progressIcon = view.findViewById(R.id.fragment_achievement_entry_progress_icon);
        TextView progressTitle = view.findViewById(R.id.fragment_achievement_entry_progress_title);
        TextView descriptionView = view.findViewById(R.id.fragment_achievement_entry_description_value);
        IconView statusIcon = view.findViewById(R.id.fragment_achievement_entry_status_icon);
        TextView statusTitleView = view.findViewById(R.id.fragment_achievement_entry_status_title);
        TextView statusValueView = view.findViewById(R.id.fragment_achievement_entry_status_value);
        Button requestApprovalButton = view.findViewById(R.id.fragment_achievement_entry_request_button);
        EditText userDescriptionView = view.findViewById(R.id.fragment_achievement_entry_user_description_value);
        IconView userDescriptionIcon = view.findViewById(R.id.fragment_achievement_entry_user_description_icon);
        TextView userDescriptionTitle = view.findViewById(R.id.fragment_achievement_entry_user_description_title);

        // populate view
        titleView.setText(achievement.getTitle());
        playersCompletedView.setText(String.valueOf(achievement.getTotalPlayersCompleted()));
        achievementPointsView.setText(String.valueOf(achievement.getAchievementPoints()));
        descriptionView.setText(achievement.getDescription());

        // remove completed icon and title if not completed
        if (!achievement.getIsCompleted()){
            completedIconView.setVisibility(View.GONE);
            completedTextView.setVisibility(View.GONE);
        }

        // check if achievement is an automatic one or manual
        if (achievement.getType().equals(AchievementTypes.AUTOMATIC)){
            // remove views only for manual
            statusIcon.setVisibility(View.GONE);
            statusTitleView.setVisibility(View.GONE);
            statusValueView.setVisibility(View.GONE);
            requestApprovalButton.setVisibility(View.GONE);
            userDescriptionView.setVisibility(View.GONE);
            userDescriptionIcon.setVisibility(View.GONE);
            userDescriptionTitle.setVisibility(View.GONE);
            updateRequestedDateVisibility(false, view, null);

            // setup progress for automatic entity
            AchievementAccountAutomaticEntity automaticEntity = (AchievementAccountAutomaticEntity) achievement;

            // set progress bar
            progressValueView.setText(getString(R.string.completedOutOfTotal, automaticEntity.getCompletedCount(), automaticEntity.getAmountToCompleteCount()));
            float result =  ((float)automaticEntity.getCompletedCount()/(float)automaticEntity.getAmountToCompleteCount()) * (float) 100L;
            int intResult = (int) (float) result;
            progressBarView.setProgress(intResult);

        } else {
            // manual achievement

            // remove progress bar, cant track progress on manual approval achievements
            progressBarView.setVisibility(View.GONE);
            progressIcon.setVisibility(View.GONE);
            progressTitle.setVisibility(View.GONE);
            progressValueView.setVisibility(View.GONE);

            // populate manual view
            AchievementAccountManualEntity manual = (AchievementAccountManualEntity) achievement;
            requestApprovalButton.setOnClickListener(this);
            updateStatusView(manual.getStatus(), view);

            // enable user description view or not
            if (manual.getStatus().equals(AchievementStatusTypes.ACCEPTED) ||
                    manual.getStatus().equals(AchievementStatusTypes.PENDING_APPROVAL)){
                enableUserDescriptionView(false, view);
            } else {
                enableUserDescriptionView(true, view);
            }

            // populate user description if any
            if (manual.getUserDescription() != null){
                userDescriptionView.setText(manual.getUserDescription());
            }

            // set text watcher to get the value from the view
            userDescriptionView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    userDescription = String.valueOf(charSequence);
                }
                @Override
                public void afterTextChanged(Editable editable) {}
            });

            // only show request button if status is declined or not requested
            if (manual.getStatus().equals(AchievementStatusTypes.ACCEPTED) ||
                manual.getStatus().equals(AchievementStatusTypes.PENDING_APPROVAL)){
                updateRequestButtonVisibility(false, view);
            }

            // show requested date if status not is not requested
            if (!manual.getStatus().equals(AchievementStatusTypes.NOT_REQUESTED)){
                updateRequestedDateVisibility(true, view, Dates.formatDate(manual.getRequestedDate()));
            } else {
                updateRequestedDateVisibility(false, view, null);
            }
        }
    }

    @Override
    public void onClick(View view) {
        AchievementApprovalRequest approvalRequest = new AchievementApprovalRequest();
        approvalRequest.setId(UUID.randomUUID().toString());
        approvalRequest.setAchievementId(achievement.getId());
        approvalRequest.setAchievementTitle(achievement.getTitle());
        approvalRequest.setAchievementDescription(achievement.getDescription());
        approvalRequest.setAchievementPoints(achievement.getAchievementPoints());
        approvalRequest.setUserEmail(FirestoreRepository.getCurrentUser().getEmail());
        approvalRequest.setUsername("unknown username");
        approvalRequest.setStatus(AchievementStatusTypes.PENDING_APPROVAL);
        approvalRequest.setUserDescription(userDescription);
        approvalRequest.setRequestDate(System.currentTimeMillis());

        postApprovalRequest(approvalRequest);
    }

    private void postApprovalRequest(AchievementApprovalRequest request){
        showProgress(true);
        FirestoreService.addAchievementApprovalRequest(request, new FirebaseCallback() {
            @Override
            public void onSuccess(Object object) {
                updateAchievement(request);
                updateStatusView(request.getStatus(), getView());
                updateRequestButtonVisibility(false, getView());
                updateRequestedDateVisibility(true, getView(), Dates.formatDate(request.getRequestDate()));
                enableUserDescriptionView(false, getView());
            }

            @Override
            public void onFailure(Exception e) {

            }

            @Override
            public void onFinish() {
                showProgress(false);
            }
        });
    }

    private void enableUserDescriptionView(boolean isEnabled, View view){
        if (view == null) return;
        EditText userDescriptionView = view.findViewById(R.id.fragment_achievement_entry_user_description_value);
        if (isEnabled){
            userDescriptionView.setEnabled(true);
        } else {
            userDescriptionView.setEnabled(false);
        }
    }

    private void updateAchievement(AchievementApprovalRequest request){
        AchievementAccountManualEntity manual = (AchievementAccountManualEntity) achievement;
        manual.setStatus(request.getStatus());
        manual.setHasRequested(true);
        manual.setUserDescription(request.getUserDescription());
        manual.setRequestedDate(request.getRequestDate());
    }

    private void updateRequestButtonVisibility(boolean isVisible, View view){
        if (view == null) return;
        Button requestApprovalButton = view.findViewById(R.id.fragment_achievement_entry_request_button);
        if (isVisible){
            requestApprovalButton.setVisibility(View.VISIBLE);
        } else {
            requestApprovalButton.setVisibility(View.GONE);
        }
    }

    private void updateStatusView(String status, View view){
        if (view == null) return;
        TextView statusValueView = view.findViewById(R.id.fragment_achievement_entry_status_value);

        // update status for manual
        switch (status){
            case AchievementStatusTypes.ACCEPTED:
                statusValueView.setText(getString(R.string.achievement_status_accepted));
                statusValueView.setTextColor(getContext().getColor(R.color.success));
                break;

            case AchievementStatusTypes.PENDING_APPROVAL:
                statusValueView.setText(getString(R.string.achievement_status_pending));
                statusValueView.setTextColor(getContext().getColor(R.color.warning));
                break;

            case AchievementStatusTypes.DECLINED:
                statusValueView.setText(getString(R.string.achievement_status_declined));
                statusValueView.setTextColor(getContext().getColor(R.color.cancel));
                break;

            case AchievementStatusTypes.NOT_REQUESTED:
                statusValueView.setText(getString(R.string.achievement_status_not_requested));
                statusValueView.setTextColor(getContext().getColor(R.color.mutedText));
                break;
        }
    }

    private void updateRequestedDateVisibility(boolean isVisible, View view, String date){
        if (view == null) return;
        IconView dateIcon = view.findViewById(R.id.fragment_achievement_entry_requested_date_icon);
        TextView dateTitle = view.findViewById(R.id.fragment_achievement_entry_requested_date_title);
        TextView dateValue = view.findViewById(R.id.fragment_achievement_entry_requested_date_value);

        if (isVisible){
            dateIcon.setVisibility(View.VISIBLE);
            dateTitle.setVisibility(View.VISIBLE);
            dateValue.setVisibility(View.VISIBLE);
        } else {
            dateIcon.setVisibility(View.GONE);
            dateTitle.setVisibility(View.GONE);
            dateValue.setVisibility(View.GONE);
        }

        if (date != null){
            dateValue.setText(date);
        }
    }

    public void setAchievement(AchievementAccountEntity achievement){this.achievement = achievement;}
}
