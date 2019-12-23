package com.example.fitness_app.fragments.quests;

import android.os.Bundle;
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
import com.example.fitness_app.constrants.QuestStatusTypes;
import com.example.fitness_app.fragments.BaseFragment;
import com.example.fitness_app.interfaces.FirebaseCallback;
import com.example.fitness_app.models.QuestAccountEntity;
import com.example.fitness_app.models.QuestApprovalRequestEntity;
import com.example.fitness_app.storage.StorageManager;
import com.example.fitness_app.util.Dates;
import com.example.fitness_app.views.IconButton;
import com.example.fitness_app.views.IconView;

import java.util.UUID;

public class QuestEntryFragment extends BaseFragment implements View.OnClickListener {
    private QuestAccountEntity quest;
    private String userDescription = "";
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quest_entry, container, false);

        setupToolbar(view);
        setupProgressBar(view);
        populateView(view);

        return view;
    }

    private void setupToolbar(View view){
        ConstraintLayout toolbar = view.findViewById(R.id.fragment_quest_entry_toolbar);
        IconButton backArrow = toolbar.findViewById(R.id.application_toolbar_back_arrow);
        backArrow.setOnClickListener(view1 -> fragmentNavigation.popFragment());
    }

    private void setupProgressBar(View view){
        progressBar = view.findViewById(R.id.fragment_quest_entry_progress_bar);
        progressBar.setIndeterminate(true);
        setProgressBar(progressBar);
    }



    private void populateView(View view) {
        // bind data
        bindData(view);

        // handle different states
        if (quest.getExpireDate() != null && Dates.isDeadlineReached(quest.getExpireDate())) {
            // quest is expired
            populateExpiredState(view);

        } else if (quest.getLevelRequirement() > StorageManager.getInstance(getContext()).getAccount().getLevelInformation()[0]) {
            // quest has to high of a rank
            populateLevelToHighState(view);

        } else {
            // quest can be completed
            populateNormalState(view);
        }
    }

    @Override
    public void onClick(View view) {
        // disable button so not to have multiple requests
        disableRequestButton(true);

        QuestApprovalRequestEntity approvalRequest = new QuestApprovalRequestEntity();
        approvalRequest.setId(UUID.randomUUID().toString());
        approvalRequest.setQuestId(quest.getId());
        approvalRequest.setQuestTitle(quest.getTitle());
        approvalRequest.setQuestDescription(quest.getDescription());
        approvalRequest.setExperiencePoints(quest.getExperiencePoints());
        approvalRequest.setUserEmail(FirestoreRepository.getCurrentUser().getEmail());
        approvalRequest.setUsername("unknown username");
        approvalRequest.setStatus(QuestStatusTypes.PENDING_APPROVAL);
        approvalRequest.setUserDescription(userDescription);
        approvalRequest.setRequestDate(System.currentTimeMillis());

        postApprovalRequest(approvalRequest);
    }

    private void postApprovalRequest(QuestApprovalRequestEntity request){
        showProgress(true);
        FirestoreService.addQuestApprovalRequest(request, new FirebaseCallback() {
            @Override
            public void onSuccess(Object object) {
                updateQuest(request);
                updateStatusView(request.getStatus(), getView());
                updateRequestButtonVisibility(false, getView());
                setRequestedDateValueView(Dates.formatDate(request.getRequestDate()), getView());
                updateRequestedDateVisibility(true, getView());
                enableUserDescriptionView(false, getView());
            }

            @Override
            public void onFailure(Exception e) {

            }

            @Override
            public void onFinish() {
                showProgress(false);
                disableRequestButton(false);
            }
        });
    }

    private void populateExpiredState(View view){
        muteViews(view);
        showExpiredText(view);
        removeRequestButton(view);
        disableUserNote(view);
    }

    private void populateLevelToHighState(View view){
        muteViews(view);
        showLevelToHighText(view);
        removeRequestButton(view);
        disableUserNote(view);
    }

    private void removeRequestButton(View view){
        Button requestButton = view.findViewById(R.id.fragment_quest_entry_request_button);
        requestButton.setVisibility(View.GONE);
    }

    private void disableUserNote(View view){
        EditText userNote = view.findViewById(R.id.fragment_quest_entry_user_description_value);
        userNote.setEnabled(false);
    }

    private void showExpiredText(View view){
        TextView overdue = view.findViewById(R.id.fragment_quest_entry_expire_date_overdue);
        overdue.setVisibility(View.VISIBLE);
    }

    private void showLevelToHighText(View view){
        TextView levelToHigh = view.findViewById(R.id.fragment_quest_entry_required_level_to_high);
        levelToHigh.setVisibility(View.VISIBLE);
    }

    private void populateNormalState(View view){
        // update view by status
        if (quest.getStatus().equals(QuestStatusTypes.ACCEPTED) ||
                quest.getStatus().equals(QuestStatusTypes.PENDING_APPROVAL)){
            // disable
            enableUserDescriptionView(false, view);
            updateRequestButtonVisibility(false, view);
        } else {
            // enable
            enableUserDescriptionView(true, view);
            updateRequestButtonVisibility(true, view);
        }
    }

    private void muteViews(View view){
        IconView questIcon = view.findViewById(R.id.fragment_quest_entry_quest_icon);
        TextView titleView = view.findViewById(R.id.fragment_quest_entry_title_value);
        IconView experiencePointsIcon = view.findViewById(R.id.fragment_quest_entry_experience_points_icon);
        IconView levelIcon = view.findViewById(R.id.fragment_quest_entry_required_level_icon);
        IconView descriptionIcon = view.findViewById(R.id.fragment_quest_entry_description_icon);
        IconView statusIcon = view.findViewById(R.id.fragment_quest_entry_status_icon);
        TextView statusValue = view.findViewById(R.id.fragment_quest_entry_status_value);
        IconView requestedDateIcon = view.findViewById(R.id.fragment_quest_entry_requested_date_icon);
        TextView requestedDateValue = view.findViewById(R.id.fragment_quest_entry_requested_date_value);
        IconView userDescriptionIcon = view.findViewById(R.id.fragment_quest_entry_user_description_icon);
        EditText userDesctiptionValue = view.findViewById(R.id.fragment_quest_entry_user_description_value);
        TextView requiredLevelView = view.findViewById(R.id.fragment_quest_entry_required_level_value);
        TextView experiencePoints = view.findViewById(R.id.fragment_quest_entry_experience_points_value);
        TextView descriptionView = view.findViewById(R.id.fragment_quest_entry_description_value);

        // mute views
        questIcon.setTextColor(getContext().getColor(R.color.mutedText));
        titleView.setTextColor(getContext().getColor(R.color.mutedText));
        levelIcon.setTextColor(getContext().getColor(R.color.mutedText));
        requiredLevelView.setTextColor(getContext().getColor(R.color.mutedText));
        experiencePointsIcon.setTextColor(getContext().getColor(R.color.mutedText));
        experiencePoints.setTextColor(getContext().getColor(R.color.mutedText));
        descriptionIcon.setTextColor(getContext().getColor(R.color.mutedText));
        descriptionView.setTextColor(getContext().getColor(R.color.mutedText));
        statusIcon.setTextColor(getContext().getColor(R.color.mutedText));
        statusValue.setTextColor(getContext().getColor(R.color.mutedText));
        requestedDateIcon.setTextColor(getContext().getColor(R.color.mutedText));
        requestedDateValue.setTextColor(getContext().getColor(R.color.mutedText));
        userDescriptionIcon.setTextColor(getContext().getColor(R.color.mutedText));
        userDesctiptionValue.setTextColor(getContext().getColor(R.color.mutedText));
    }

    private void bindData(View view){
        IconView completedIcon = view.findViewById(R.id.fragment_quest_entry_completed_icon);
        TextView completedValue = view.findViewById(R.id.fragment_quest_entry_completed_value);
        TextView titleView = view.findViewById(R.id.fragment_quest_entry_title_value);
        TextView requiredLevelView = view.findViewById(R.id.fragment_quest_entry_required_level_value);
        TextView expireDateView = view.findViewById(R.id.fragment_quest_entry_expire_date_value);
        TextView experiencePointsView = view.findViewById(R.id.fragment_quest_entry_experience_points_value);
        TextView desctioptionView = view.findViewById(R.id.fragment_quest_entry_description_value);
        TextView requestedDateView = view.findViewById(R.id.fragment_quest_entry_requested_date_value);
        EditText userDescriptionView = view.findViewById(R.id.fragment_quest_entry_user_description_value);
        Button requestButton = view.findViewById(R.id.fragment_quest_entry_request_button);


        // remove completed views if not completed
        if (!quest.getIsCompleted()){
            completedIcon.setVisibility(View.GONE);
            completedValue.setVisibility(View.GONE);
        }

        titleView.setText(quest.getTitle());
        requiredLevelView.setText(String.valueOf(quest.getLevelRequirement()));

        if (quest.getExpireDate() == null){
            expireDateView.setText("Undefined");
        } else {
            expireDateView.setText(Dates.formatDate(quest.getExpireDate()));
        }

        experiencePointsView.setText(String.valueOf(quest.getExperiencePoints()));

        if (quest.getDescription() != null){
            desctioptionView.setText(quest.getDescription());
        } else {
            desctioptionView.setText(getContext().getString(R.string.no_description));
        }

        updateStatusView(quest.getStatus(), view);

        if (quest.getHasRequested()){
            requestedDateView.setText(Dates.formatDate(quest.getRequestedDate()));
            updateRequestedDateVisibility(true, view);
        } else {
            updateRequestedDateVisibility(false, view);
        }

        if (quest.getUserDescription() != null){
            userDescriptionView.setText(quest.getUserDescription());
        }

        requestButton.setOnClickListener(this);
    }

    private void setRequestedDateValueView(String value, View view){
        TextView requestedDateValueView = view.findViewById(R.id.fragment_quest_entry_requested_date_value);
        requestedDateValueView.setText(value);
    }

    private void disableRequestButton(boolean isDisabled){
        if (getView() == null) return;
        Button requestApprovalButton = getView().findViewById(R.id.fragment_quest_entry_request_button);
        if (isDisabled){
            requestApprovalButton.setEnabled(false);
        } else {
            requestApprovalButton.setEnabled(true);
        }
    }

    private void enableUserDescriptionView(boolean isEnabled, View view){
        if (view == null) return;
        EditText userDescriptionView = view.findViewById(R.id.fragment_quest_entry_user_description_value);
        if (isEnabled){
            userDescriptionView.setEnabled(true);
        } else {
            userDescriptionView.setEnabled(false);
        }
    }

    private void updateQuest(QuestApprovalRequestEntity request){
        quest.setStatus(request.getStatus());
        quest.setHasRequested(true);
        quest.setUserDescription(request.getUserDescription());
        quest.setRequestedDate(request.getRequestDate());
    }

    private void updateRequestButtonVisibility(boolean isVisible, View view){
        if (view == null) return;
        Button requestApprovalButton = view.findViewById(R.id.fragment_quest_entry_request_button);
        if (isVisible){
            requestApprovalButton.setVisibility(View.VISIBLE);
        } else {
            requestApprovalButton.setVisibility(View.GONE);
        }
    }

    private void updateStatusView(String status, View view){
        if (view == null) return;
        TextView statusValueView = view.findViewById(R.id.fragment_quest_entry_status_value);

        // update status for manual
        switch (status){
            case QuestStatusTypes.ACCEPTED:
                statusValueView.setText(getString(R.string.quest_status_accepted));
                statusValueView.setTextColor(getContext().getColor(R.color.success));
                break;

            case QuestStatusTypes.PENDING_APPROVAL:
                statusValueView.setText(getString(R.string.quest_status_pending));
                statusValueView.setTextColor(getContext().getColor(R.color.warning));
                break;

            case QuestStatusTypes.DECLINED:
                statusValueView.setText(getString(R.string.quest_status_declined));
                statusValueView.setTextColor(getContext().getColor(R.color.cancel));
                break;

            case QuestStatusTypes.NOT_REQUESTED:
                statusValueView.setText(getString(R.string.quest_status_not_requested));
                statusValueView.setTextColor(getContext().getColor(R.color.mutedText));
                break;
        }
    }

    private void updateRequestedDateVisibility(boolean isVisible, View view){
        if (view == null) return;
        IconView dateIcon = view.findViewById(R.id.fragment_quest_entry_requested_date_icon);
        TextView dateTitle = view.findViewById(R.id.fragment_quest_entry_requested_date_title);
        TextView dateValue = view.findViewById(R.id.fragment_quest_entry_requested_date_value);

        if (isVisible){
            dateIcon.setVisibility(View.VISIBLE);
            dateTitle.setVisibility(View.VISIBLE);
            dateValue.setVisibility(View.VISIBLE);
        } else {
            dateIcon.setVisibility(View.GONE);
            dateTitle.setVisibility(View.GONE);
            dateValue.setVisibility(View.GONE);
        }
    }

    public void setQuest(QuestAccountEntity quest){this.quest = quest;}
}