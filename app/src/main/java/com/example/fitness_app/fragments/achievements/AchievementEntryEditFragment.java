package com.example.fitness_app.fragments.achievements;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.fitness_app.R;
import com.example.fitness_app.api.FirestoreService;
import com.example.fitness_app.constrants.AchievementEntryEditFragmentMode;
import com.example.fitness_app.constrants.AchievementTypes;
import com.example.fitness_app.constrants.AchievementUpdateEvents;
import com.example.fitness_app.entities.EventBustEvent;
import com.example.fitness_app.fragments.BaseFragment;
import com.example.fitness_app.interfaces.FirebaseCallback;
import com.example.fitness_app.models.AchievementEntryEntity;
import com.example.fitness_app.views.IconButton;
import com.example.fitness_app.views.IconView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.example.fitness_app.constrants.EventBusEvents.EVENT_BUS_EVENT_ACHIEVEMENT_ADDED;
import static com.example.fitness_app.constrants.EventBusEvents.EVENT_BUS_EVENT_ACHIEVEMENT_DELETED;

public class AchievementEntryEditFragment extends BaseFragment {
    private AchievementEntryEditFragmentMode mode;
    private AchievementEntryEntity achievement;
    private TextWatcher titleWatcher;
    private TextWatcher descriptionWatcher;
    private TextWatcher achievementsWatcher;
    private Button toolbarActionButton;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mode == AchievementEntryEditFragmentMode.ADDING_NEW_ACHIEVEMENT){
            setupNewAchievement();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_achievement_entry_edit, container, false);

        setupToolbar(view);
        if (mode == AchievementEntryEditFragmentMode.ADDING_NEW_ACHIEVEMENT){
            setupTitleWatcher(view);
            setupDesctiptionWatcher(view);
            setupAchievementpointsWatcher(view);
        }
        populateView(view);

        return view;
    }

    private void setupNewAchievement(){
        achievement = new AchievementEntryEntity();
        achievement.setType(AchievementTypes.AUTOMATIC);
        achievement.setTotalPlayersCompleted(0L);
        achievement.setId(UUID.randomUUID().toString());
        achievement.setAmountToCompleteCount(0L);
        List<String> updates = new ArrayList<>();
        updates.add(AchievementUpdateEvents.MANUAL);
        achievement.setWhenToUpdate(updates);
    }

    private void setupToolbar(View view){
        ConstraintLayout toolbar = view.findViewById(R.id.fragment_achievement_entry_edit_toolbar);
        IconButton backArrow = toolbar.findViewById(R.id.application_toolbar_back_arrow);
        backArrow.setOnClickListener(view1 -> fragmentNavigation.popFragment());
        /*
        toolbarActionButton = toolbar.findViewById(R.id.application_toolbar_save_button);
        toolbarActionButton.setVisibility(View.VISIBLE);
        toolbarActionButton.setEnabled(false);

        if (mode == AchievementEntryEditFragmentMode.ADDING_NEW_ACHIEVEMENT){
            toolbarActionButton.setText("Save");
            toolbarActionButton.setBackground(getContext().getDrawable(R.drawable.toolbar_save_button_background));
            toolbarActionButton.setOnClickListener(view12 ->  handleSave());
        } else {
            toolbarActionButton.setText("Delete");
            toolbarActionButton.setBackground(getContext().getDrawable(R.drawable.delete_button_background));
            toolbarActionButton.setOnClickListener(view12 ->  handleDelete());
        }*/
    }

    private void populateView(View view){
        EditText titleValue = view.findViewById(R.id.fragment_achievement_entry_edit_title_value);
        EditText descriptionValue = view.findViewById(R.id.fragment_achievement_entry_edit_description_value);
        EditText achievementPointsValue = view.findViewById(R.id.fragment_achievement_entry_edit_achievement_points_value);
        // view only for displaying an achievement, gone when adding
        IconView totalPlayersCompletedIcon = view.findViewById(R.id.fragment_achievement_entry_edit_amount_to_complete_value);
        TextView totalPlayersCompletedTitle = view.findViewById(R.id.fragment_achievement_entry_edit_players_completed_title);
        TextView totalPlayersCompletedValue = view.findViewById(R.id.fragment_achievement_entry_edit_players_completed_value);
        View totalPlayersCompletedDivider = view.findViewById(R.id.fragment_achievement_entry_edit_players_completed_divider);
        IconView typeIcon = view.findViewById(R.id.fragment_achievement_entry_edit_type_icon);
        TextView typeTitle = view.findViewById(R.id.fragment_achievement_entry_edit_type_title);
        TextView typeValue = view.findViewById(R.id.fragment_achievement_entry_edit_type_value);
        View typeDivider = view.findViewById(R.id.fragment_achievement_entry_edit_type_divider);
        IconView amountToCompleteIcon = view.findViewById(R.id.fragment_achievement_entry_edit_amount_to_complete_icon);
        TextView amountToCompleteTitle = view.findViewById(R.id.fragment_achievement_entry_edit_amount_to_complete_title);
        TextView amountToCompleteValue = view.findViewById(R.id.fragment_achievement_entry_edit_amount_to_complete_value);
        View amountToCompleteDivider = view.findViewById(R.id.fragment_achievement_entry_edit_amount_to_complete_divider);


        if (mode == AchievementEntryEditFragmentMode.ADDING_NEW_ACHIEVEMENT){
            titleValue.addTextChangedListener(titleWatcher);
            descriptionValue.addTextChangedListener(descriptionWatcher);
            achievementPointsValue.addTextChangedListener(achievementsWatcher);

            totalPlayersCompletedIcon.setVisibility(View.GONE);
            totalPlayersCompletedTitle.setVisibility(View.GONE);
            totalPlayersCompletedValue.setVisibility(View.GONE);
            totalPlayersCompletedDivider.setVisibility(View.GONE);
            typeIcon.setVisibility(View.GONE);
            typeTitle.setVisibility(View.GONE);
            typeValue.setVisibility(View.GONE);
            typeDivider.setVisibility(View.GONE);
            amountToCompleteIcon.setVisibility(View.GONE);
            amountToCompleteTitle.setVisibility(View.GONE);
            amountToCompleteValue.setVisibility(View.GONE);
            amountToCompleteDivider.setVisibility(View.GONE);
        } else {
            titleValue.setFocusable(false);
            titleValue.setEnabled(false);
            descriptionValue.setFocusable(false);
            descriptionValue.setEnabled(false);
            achievementPointsValue.setFocusable(false);
            achievementPointsValue.setEnabled(false);
            titleValue.setText(achievement.getTitle());
            descriptionValue.setText(achievement.getDescription());
            achievementPointsValue.setText(String.valueOf(achievement.getAchievementPoints()));
            totalPlayersCompletedValue.setText(String.valueOf(achievement.getTotalPlayersCompleted()));
            typeValue.setText(achievement.getType());
            amountToCompleteValue.setText(String.valueOf(achievement.getAmountToCompleteCount()));
        }
    }

    private void handleSave(){
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

            }
        });
    }

    private void handleDelete(){
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

            }
        });
    }

    private void enableSaveIfValid(){
        if (achievement.getTitle() != null && !achievement.getTitle().isEmpty() &&
            achievement.getDescription() != null && !achievement.getDescription().isEmpty() &&
            achievement.getAchievementPoints() != null && achievement.getAchievementPoints() > 0){
            toolbarActionButton.setEnabled(true);
        } else {
            toolbarActionButton.setEnabled(false);
        }
    }

    private void setupTitleWatcher(View view){
        EditText titleValue = view.findViewById(R.id.fragment_achievement_entry_edit_title_value);
        this.titleWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                achievement.setTitle(titleValue.getText().toString());
                enableSaveIfValid();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
    }

    private void setupDesctiptionWatcher(View view){
        EditText descriptionValue = view.findViewById(R.id.fragment_achievement_entry_edit_description_value);
        this.descriptionWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                achievement.setDescription(descriptionValue.getText().toString());
                enableSaveIfValid();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
    }

    private void setupAchievementpointsWatcher(View view){
        EditText achievementPointsValue = view.findViewById(R.id.fragment_achievement_entry_edit_achievement_points_value);
        this.achievementsWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                achievement.setAchievementPoints(Long.valueOf(achievementPointsValue.getText().toString()));
                enableSaveIfValid();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
    }

    public AchievementEntryEditFragmentMode getMode() {
        return mode;
    }

    public void setMode(AchievementEntryEditFragmentMode mode) {
        this.mode = mode;
    }

    public AchievementEntryEntity getAchievement() {
        return achievement;
    }

    public void setAchievement(AchievementEntryEntity achievement) {
        this.achievement = achievement;
    }
}
