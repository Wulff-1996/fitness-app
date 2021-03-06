package com.example.fitness_app.fragments.achievements;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.fitness_app.R;
import com.example.fitness_app.api.FirestoreService;
import com.example.fitness_app.fragments.BaseFragment;
import com.example.fitness_app.interfaces.FirebaseCallback;
import com.example.fitness_app.models.AchievementApprovalRequest;
import com.example.fitness_app.models.EventBustEvent;
import com.example.fitness_app.util.Dates;
import com.example.fitness_app.views.IconButton;

import org.greenrobot.eventbus.EventBus;

import static com.example.fitness_app.constrants.EventBusEvents.EVENT_BUS_EVENT_ACHIEVEMENT_REQUEST_APPROVED;
import static com.example.fitness_app.constrants.EventBusEvents.EVENT_BUS_EVENT_ACHIEVEMENT_REQUEST_DECLINED;

public class AchievementApprovalEntryFragment extends BaseFragment {
    private AchievementApprovalRequest request;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_achievement_approval_entry, container, false);

        setupProgressBar(view);
        setupToolbar(view);
        populateView(view);

        return view;
    }

    private void setupProgressBar(View view){
        ProgressBar progressBar = view.findViewById(R.id.fragment_achievement_approval_entry_progress_bar);
        progressBar.setIndeterminate(true);
        setProgressBar(progressBar);
    }

    private void setupToolbar(View view){
        ConstraintLayout toolbar = view.findViewById(R.id.fragment_achievement_approval_entry_toolbar);
        IconButton backArrow = toolbar.findViewById(R.id.application_toolbar_back_arrow);
        backArrow.setOnClickListener(view1 -> fragmentNavigation.popFragment());
    }

    private void populateView(View view){
        TextView achievementTitleView = view.findViewById(R.id.fragment_achievement_approval_entry_title_value);
        TextView achievementDescrView = view.findViewById(R.id.fragment_achievement_approval_entry_description_value);
        TextView achievementPointsView = view.findViewById(R.id.fragment_achievement_approval_entry_achievement_points_value);
        TextView usernameView = view.findViewById(R.id.fragment_achievement_approval_entry_user_value);
        TextView requestedDateView = view.findViewById(R.id.fragment_achievement_approval_entry_requested_date_value);
        TextView userDescrView = view.findViewById(R.id.fragment_achievement_approval_entry_user_description_value);
        Button declineButton = view.findViewById(R.id.fragment_achievement_approval_entry_decline_button);
        Button approveButton = view.findViewById(R.id.fragment_achievement_approval_entry_approve_button);

        achievementTitleView.setText(request.getAchievementTitle());
        achievementDescrView.setText(request.getAchievementDescription());
        achievementPointsView.setText(String.valueOf(request.getAchievementPoints()));
        usernameView.setText(request.getUsername());
        requestedDateView.setText(Dates.formatDate(request.getRequestDate()));
        userDescrView.setText(request.getUserDescription());
        declineButton.setOnClickListener(view1 -> handleDecline());
        approveButton.setOnClickListener(view1 -> handleApprove());
    }

    private void handleDecline(){
        showProgress(true);
        FirestoreService.declineAchievementRequest(request, new FirebaseCallback() {
            @Override
            public void onSuccess(Object object) {
                EventBus.getDefault().post(new EventBustEvent<>(EVENT_BUS_EVENT_ACHIEVEMENT_REQUEST_DECLINED, request));
                fragmentNavigation.popFragment();
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

    private void handleApprove(){
        setFetching(true);
        FirestoreService.approveAchievementRequest(request, new FirebaseCallback() {
            @Override
            public void onSuccess(Object object) {
                EventBus.getDefault().post(new EventBustEvent<>(EVENT_BUS_EVENT_ACHIEVEMENT_REQUEST_APPROVED, request));
                fragmentNavigation.popFragment();
            }

            @Override
            public void onFailure(Exception e) {
            }

            @Override
            public void onFinish() {
                setFetching(false);
            }
        });
    }

    public AchievementApprovalRequest getRequest() {
        return request;
    }

    public void setRequest(AchievementApprovalRequest request) {
        this.request = request;
    }
}