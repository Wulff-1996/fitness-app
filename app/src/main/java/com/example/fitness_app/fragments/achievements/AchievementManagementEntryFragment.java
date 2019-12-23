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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.fitness_app.R;
import com.example.fitness_app.api.FirestoreService;
import com.example.fitness_app.constrants.AchievementManagementMode;
import com.example.fitness_app.constrants.AchievementTypes;
import com.example.fitness_app.constrants.AchievementUpdateEvents;
import com.example.fitness_app.entities.EventBustEvent;
import com.example.fitness_app.fragments.BaseFragment;
import com.example.fitness_app.interfaces.FirebaseCallback;
import com.example.fitness_app.models.AchievementEntryEntity;
import com.example.fitness_app.util.Dates;
import com.example.fitness_app.views.IconButton;
import com.example.fitness_app.views.IconView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.example.fitness_app.constrants.EventBusEvents.EVENT_BUS_EVENT_ACHIEVEMENT_ADDED;
import static com.example.fitness_app.constrants.EventBusEvents.EVENT_BUS_EVENT_ACHIEVEMENT_DELETED;

public class AchievementManagementEntryFragment extends BaseFragment implements View.OnClickListener {
    private AchievementManagementMode mode;
    private AchievementEntryEntity achievement;
    private Button actionButton;
    private String inputAchievementPoints;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mode == AchievementManagementMode.ADDING_NEW_ACHIEVEMENT){
            setupNewAchievement();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_achievement_management_entry, container, false);

        setupToolbar(view);
        setupActionButton(view);
        setupProgressBar(view);
        populateView(view);

        return view;
    }

    private void setupNewAchievement(){
        achievement = new AchievementEntryEntity();
        achievement.setType(AchievementTypes.MANUAL);
        achievement.setTotalPlayersCompleted(0L);
        achievement.setId(UUID.randomUUID().toString());
        List<String> updates = new ArrayList<>();
        updates.add(AchievementUpdateEvents.MANUAL);
        achievement.setWhenToUpdate(updates);
    }

    private void setupProgressBar(View view){
        ProgressBar progressBar = view.findViewById(R.id.fragment_achievement_management_entry_progress_bar);
        progressBar.setIndeterminate(true);
        setProgressBar(progressBar);
    }

    private void setupToolbar(View view){
        ConstraintLayout toolbar = view.findViewById(R.id.fragment_achievement_management_entry_toolbar);
        IconButton backArrow = toolbar.findViewById(R.id.application_toolbar_back_arrow);
        backArrow.setOnClickListener(view1 -> fragmentNavigation.popFragment());
    }

    private void populateView(View view){
        TextView modeHeader = view.findViewById(R.id.fragment_achievement_management_entry_mode_header);
        EditText titleValue = view.findViewById(R.id.fragment_achievement_management_entry_title_value);
        EditText descriptionValue = view.findViewById(R.id.fragment_achievement_management_entry_description_value);
        EditText achievementPointsValue = view.findViewById(R.id.fragment_achievement_management_entry_achievement_points_value);

        // details views (gone when adding)
        // date created
        IconView dateCreatedIcon = view.findViewById(R.id.fragment_achievement_entry_date_created_icon);
        TextView dateCreatedTitle = view.findViewById(R.id.fragment_achievement_entry_date_created_title);
        TextView dateCreatedValue = view.findViewById(R.id.fragment_achievement_entry_date_created_value);
        View dateCreatedDivider = view.findViewById(R.id.fragment_achievement_management_entry_date_created_divider);

        // total players completed
        IconView totalPlayersCompletedIcon = view.findViewById(R.id.fragment_achievement_management_entry_players_completed_icon);
        TextView totalPlayersCompletedTitle = view.findViewById(R.id.fragment_achievement_management_entry_players_completed_title);
        TextView totalPlayersCompletedValue = view.findViewById(R.id.fragment_achievement_management_entry_players_completed_value);
        View totalPlayersCompletedDivider = view.findViewById(R.id.fragment_achievement_management_entry_players_completed_divider);

        // achievement type
        IconView typeIcon = view.findViewById(R.id.fragment_achievement_management_entry_type_icon);
        TextView typeTitle = view.findViewById(R.id.fragment_achievement_management_entry_type_title);
        TextView typeValue = view.findViewById(R.id.fragment_achievement_management_entry_type_value);
        View typeDivider = view.findViewById(R.id.fragment_achievement_management_entry_type_divider);


        if (mode == AchievementManagementMode.ADDING_NEW_ACHIEVEMENT){
            modeHeader.setText("Add New Achievement");
            // setup text watcher for listening for input
            titleValue.addTextChangedListener(getTitleWatcher());
            descriptionValue.addTextChangedListener(getDescriptionWatcher());
            achievementPointsValue.addTextChangedListener(getAchievementsWatcher());

            // remove views associated with details mode
            dateCreatedIcon.setVisibility(View.GONE);
            dateCreatedTitle.setVisibility(View.GONE);
            dateCreatedValue.setVisibility(View.GONE);
            dateCreatedDivider.setVisibility(View.GONE);
            totalPlayersCompletedIcon.setVisibility(View.GONE);
            totalPlayersCompletedTitle.setVisibility(View.GONE);
            totalPlayersCompletedValue.setVisibility(View.GONE);
            totalPlayersCompletedDivider.setVisibility(View.GONE);
            typeIcon.setVisibility(View.GONE);
            typeTitle.setVisibility(View.GONE);
            typeValue.setVisibility(View.GONE);
            typeDivider.setVisibility(View.GONE);
        } else {
            modeHeader.setText("Achievement Details");
            // disable input views
            titleValue.setFocusable(false);
            titleValue.setEnabled(false);
            descriptionValue.setFocusable(false);
            descriptionValue.setEnabled(false);
            achievementPointsValue.setFocusable(false);
            achievementPointsValue.setEnabled(false);

            // populate details views
            titleValue.setText(achievement.getTitle());
            descriptionValue.setText(achievement.getDescription());
            achievementPointsValue.setText(String.valueOf(achievement.getAchievementPoints()));
            dateCreatedValue.setText(Dates.formatDate(achievement.getDateCreated()));
            totalPlayersCompletedValue.setText(String.valueOf(achievement.getTotalPlayersCompleted()));
            typeValue.setText(achievement.getType());
        }
    }

    private void setupActionButton(View view){
        actionButton = view.findViewById(R.id.fragment_achievement_management_entry_action_button);
        if (mode == AchievementManagementMode.ADDING_NEW_ACHIEVEMENT){
            // setup save action
            actionButton.setText("Save");
            actionButton.setTextColor(getContext().getColor(R.color.accent));
            actionButton.setEnabled(false);
        } else {
            // setup delete action
            actionButton.setText("Delete");
            actionButton.setTextColor(getContext().getColor(R.color.cancel));
            actionButton.setEnabled(true);
        }
        actionButton.setOnClickListener(this);
    }

    private void disableActionButton(boolean isDisable){
        if (getView() == null) return;
        if (isDisable){
            actionButton.setEnabled(false);
        } else {
            actionButton.setEnabled(true);
        }
    }

    private void handleSave(){
        setFetching(true);
        disableActionButton(true);
        FirestoreService.addAchievement(achievement, new FirebaseCallback() {
            @Override
            public void onSuccess(Object object) {
                EventBus.getDefault().post(new EventBustEvent<>(EVENT_BUS_EVENT_ACHIEVEMENT_ADDED, achievement));
                Toast.makeText(getContext(), "Achievement added", Toast.LENGTH_LONG).show();
                fragmentNavigation.popFragment();
            }

            @Override
            public void onFailure(Exception e) {

            }

            @Override
            public void onFinish() {
                disableActionButton(false);
                setFetching(false);
            }
        });
    }

    private void handleDelete(){
        setFetching(true);
        disableActionButton(true);
        FirestoreService.deleteAchievement(achievement, new FirebaseCallback() {
            @Override
            public void onSuccess(Object object) {
                EventBus.getDefault().post(new EventBustEvent<>(EVENT_BUS_EVENT_ACHIEVEMENT_DELETED, achievement));
                Toast.makeText(getContext(), "Achievement deleted", Toast.LENGTH_LONG).show();
                fragmentNavigation.popFragment();
            }

            @Override
            public void onFailure(Exception e) {

            }

            @Override
            public void onFinish() {
                disableActionButton(false);
                setFetching(false);
            }
        });
    }

    private void enableActionButtonIfValid(){
        if (mode == AchievementManagementMode.ACHIEVEMENT_DETAILS){
            actionButton.setEnabled(true);
        } else {
            if (achievement.getTitle() != null && !achievement.getTitle().isEmpty() &&
                    achievement.getDescription() != null && !achievement.getDescription().isEmpty() &&
                    isValidAchievementPoints(inputAchievementPoints)){
                actionButton.setEnabled(true);
            } else {
                actionButton.setEnabled(false);
            }
        }
    }

    private TextWatcher getTitleWatcher(){
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                achievement.setTitle(charSequence.toString());
                enableActionButtonIfValid();
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        };
    }

    private TextWatcher getDescriptionWatcher(){
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                achievement.setDescription(charSequence.toString());
                enableActionButtonIfValid();
            }
        };
    }

    private TextWatcher getAchievementsWatcher(){
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int lengthBefore, int lengthAfter) {
                inputAchievementPoints = charSequence.toString(); // string to validate on
                if (charSequence.length() <= 0){
                    hideToast();
                    showToast("Must not be empty");
                } else if (charSequence.toString().contains("[a-zA-Z]+")) {
                    hideToast();
                    showToast("Only numbers allowed");
                } else {
                    // valid now set points
                    achievement.setAchievementPoints(Long.valueOf(charSequence.toString()));
                }
                enableActionButtonIfValid();
            }
        };
    }

    private boolean isValidAchievementPoints(String input){
        if (input == null) return false;
        if (input.length() == 0) return false;
        return !input.contains("[a-zA-Z]+");
    }

    public void setMode(AchievementManagementMode mode) {
        this.mode = mode;
    }

    public AchievementEntryEntity getAchievement() {
        return achievement;
    }

    public void setAchievement(AchievementEntryEntity achievement) {
        this.achievement = achievement;
    }

    @Override
    public void onClick(View view) {
        if (mode == AchievementManagementMode.ADDING_NEW_ACHIEVEMENT){
            // set achievement date created
            achievement.setDateCreated(System.currentTimeMillis());
            handleSave();
        } else {
            handleDelete();
        }
    }
}
