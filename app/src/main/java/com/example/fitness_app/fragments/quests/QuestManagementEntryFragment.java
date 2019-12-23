package com.example.fitness_app.fragments.quests;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.fitness_app.R;
import com.example.fitness_app.api.FirestoreService;
import com.example.fitness_app.constrants.QuestManagementEntryModes;
import com.example.fitness_app.entities.EventBustEvent;
import com.example.fitness_app.fragments.BaseFragment;
import com.example.fitness_app.interfaces.FirebaseCallback;
import com.example.fitness_app.models.QuestEntity;
import com.example.fitness_app.util.Dates;
import com.example.fitness_app.views.IconButton;
import com.example.fitness_app.views.IconView;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

import static com.example.fitness_app.constrants.EventBusEvents.EVENT_BUS_EVENT_QUEST_ADDED;
import static com.example.fitness_app.constrants.EventBusEvents.EVENT_BUS_EVENT_QUEST_DELETED;

public class QuestManagementEntryFragment extends BaseFragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    private QuestManagementEntryModes mode;
    private QuestEntity quest;
    private Button actionButton;
    private String exigencePointsInput = "";
    private String levelRequirementInput = "";
    private TextView expireDateValue;
    private ConstraintLayout expireDate;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mode == QuestManagementEntryModes.ADDING) {
            setupNewQuest();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quest_management_entry, container, false);

        setupToolbar(view);
        setupActionButton(view);
        setupProgressBar(view);
        populateView(view);

        return view;
    }

    private void setupNewQuest() {
        quest = new QuestEntity();
        quest.setId(UUID.randomUUID().toString());
    }

    private void setupProgressBar(View view) {
        ProgressBar progressBar = view.findViewById(R.id.fragment_quest_management_entry_progress_bar);
        progressBar.setIndeterminate(true);
        setProgressBar(progressBar);
    }

    private void setupToolbar(View view) {
        ConstraintLayout toolbar = view.findViewById(R.id.fragment_quest_management_entry_toolbar);
        IconButton backArrow = toolbar.findViewById(R.id.application_toolbar_back_arrow);
        backArrow.setOnClickListener(view1 -> fragmentNavigation.popFragment());
    }

    private void populateView(View view) {
        TextView modeHeader = view.findViewById(R.id.fragment_quest_management_entry_mode_header);
        EditText titleValue = view.findViewById(R.id.fragment_quest_management_entry_title_value);
        EditText descriptionValue = view.findViewById(R.id.fragment_quest_management_entry_description_value);
        EditText levelValue = view.findViewById(R.id.fragment_quest_management_entry_level_value);
        EditText experiencePointsValue = view.findViewById(R.id.fragment_quest_management_entry_experience_points_value);
        expireDate = view.findViewById(R.id.fragment_quest_management_entry_expire_date);
        expireDateValue = view.findViewById(R.id.fragment_quest_entry_expire_date_value);
        IconView expireDateOpenDialogIcon = view.findViewById(R.id.fragment_quest_entry_expire_date_open_dialog_icon);
        IconButton expireDateClearButton = view.findViewById(R.id.fragment_quest_management_entry_expire_date_clear_button);


        // views gone when in adding mode
        // date created
        IconView dateCreatedIcon = view.findViewById(R.id.fragment_quest_entry_date_created_icon);
        TextView dateCreatedTitle = view.findViewById(R.id.fragment_quest_entry_date_created_title);
        TextView dateCreatedValue = view.findViewById(R.id.fragment_quest_entry_date_created_value);
        View dateCreatedDivider = view.findViewById(R.id.fragment_quest_management_entry_date_created_divider);


        if (mode == QuestManagementEntryModes.ADDING) {
            modeHeader.setText("Add New quest");
            // setup text watcher for listening for input
            titleValue.addTextChangedListener(getTitleWatcher());
            descriptionValue.addTextChangedListener(getDescriptionWatcher());
            levelValue.addTextChangedListener(getLevelRequirementWatcher());
            experiencePointsValue.addTextChangedListener(getExperiencePointsWatcher());
            expireDate.setOnClickListener(this);

            enableClearExpireDateButton(quest.getExpireDate() != null, view);
            expireDateClearButton.setOnClickListener(view1 -> handleClearExpireDate());

            // remove views associated with details mode
            dateCreatedIcon.setVisibility(View.GONE);
            dateCreatedTitle.setVisibility(View.GONE);
            dateCreatedValue.setVisibility(View.GONE);
            dateCreatedDivider.setVisibility(View.GONE);
        } else {
            modeHeader.setText("Quest Details");
            // disable input views
            titleValue.setFocusable(false);
            titleValue.setEnabled(false);
            descriptionValue.setFocusable(false);
            descriptionValue.setEnabled(false);
            levelValue.setFocusable(false);
            levelValue.setEnabled(false);
            experiencePointsValue.setFocusable(false);
            experiencePointsValue.setEnabled(false);

            // remove views for expire date
            expireDateOpenDialogIcon.setVisibility(View.GONE);
            expireDateClearButton.setVisibility(View.GONE);


            // populate details views
            titleValue.setText(quest.getTitle());
            descriptionValue.setText(quest.getDescription());
            levelValue.setText(String.valueOf(quest.getLevelRequirement()));
            experiencePointsValue.setText(String.valueOf(quest.getExperiencePoints()));
            expireDateValue.setText(Dates.formatDate(quest.getExpireDate()));
            dateCreatedValue.setText(Dates.formatDate(quest.getDateCreated()));
        }
    }

    private void handleClearExpireDate(){
        quest.setExpireDate(null);
        updateExpireDateValue(null);
    }

    private void setupActionButton(View view) {
        actionButton = view.findViewById(R.id.fragment_quest_management_entry_action_button);
        if (mode == QuestManagementEntryModes.ADDING) {
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

    private void disableActionButton(boolean isDisable) {
        if (getView() == null) return;
        if (isDisable) {
            actionButton.setEnabled(false);
        } else {
            actionButton.setEnabled(true);
        }
    }

    private void handleSave() {
        setFetching(true);
        FirestoreService.addQuest(quest, new FirebaseCallback() {
            @Override
            public void onSuccess(Object object) {
                EventBus.getDefault().post(new EventBustEvent<>(EVENT_BUS_EVENT_QUEST_ADDED, quest));
                Toast.makeText(getContext(), "Quest added", Toast.LENGTH_LONG).show();
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

    private void handleDelete() {
        setFetching(true);
        FirestoreService.deleteQuest(quest, new FirebaseCallback() {
            @Override
            public void onSuccess(Object object) {
                EventBus.getDefault().post(new EventBustEvent<>(EVENT_BUS_EVENT_QUEST_DELETED, quest));
                Toast.makeText(getContext(), "Quest deleted", Toast.LENGTH_LONG).show();
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

    private void enableActionButtonIfValid() {
        if (mode == QuestManagementEntryModes.DETAILS) {
            actionButton.setEnabled(true);
        } else {
            if (quest.getTitle() != null && !quest.getTitle().isEmpty() &&
                    quest.getDescription() != null && !quest.getDescription().isEmpty() &&
                    isValidNumber(exigencePointsInput) &&
                    isValidNumber(levelRequirementInput)) {
                actionButton.setEnabled(true);
            } else {
                actionButton.setEnabled(false);
            }
        }
    }

    private TextWatcher getTitleWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                quest.setTitle(charSequence.toString());
                enableActionButtonIfValid();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        };
    }

    private TextWatcher getDescriptionWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                quest.setDescription(charSequence.toString());
                enableActionButtonIfValid();
            }
        };
    }

    private TextWatcher getLevelRequirementWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int lengthBefore, int lengthAfter) {
                levelRequirementInput = charSequence.toString(); // string to validate on
                if (charSequence.length() <= 0) {
                    hideToast();
                    showToast("Must not be empty");
                } else if (charSequence.toString().contains("[a-zA-Z]+")) {
                    hideToast();
                    showToast("Only numbers allowed");
                } else {
                    // valid now set points
                    quest.setLevelRequirement(Long.valueOf(charSequence.toString()));
                }
                enableActionButtonIfValid();
            }
        };
    }

    private TextWatcher getExperiencePointsWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int lengthBefore, int lengthAfter) {
                exigencePointsInput = charSequence.toString(); // string to validate on
                if (charSequence.length() <= 0) {
                    hideToast();
                    showToast("Must not be empty");
                } else if (charSequence.toString().contains("[a-zA-Z]+")) {
                    hideToast();
                    showToast("Only numbers allowed");
                } else {
                    // valid now set points
                    quest.setExperiencePoints(Long.valueOf(charSequence.toString()));
                }
                enableActionButtonIfValid();
            }
        };
    }

    private boolean isValidNumber(String input) {
        if (input == null) return false;
        if (input.length() == 0) return false;
        return !input.contains("[a-zA-Z]+");
    }

    public void setMode(QuestManagementEntryModes mode) {
        this.mode = mode;
    }

    public void setQuest(QuestEntity quest) {
        this.quest = quest;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fragment_quest_management_entry_expire_date:
                showDatePicker();
                break;

            case R.id.fragment_quest_management_entry_action_button:
                handleActionButtonPressed();
                break;
        }
    }

    private void handleActionButtonPressed(){
        // disable buttons until response from api
        disableActionButton(true); // prevent user from sending multiple requests

        if (mode == QuestManagementEntryModes.ADDING) {
            // set quest date created
            quest.setDateCreated(System.currentTimeMillis());
            handleSave();
        } else {
            handleDelete();
        }
    }

    private void showDatePicker() {
        int year;
        int month;
        int day;

        // if null set to current date by default
        if (quest.getExpireDate() == null) {
            Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(quest.getExpireDate());
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH) - 1;
            day = calendar.get(Calendar.DAY_OF_MONTH);
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), R.style.datePickerTheme, this, year, month, day);
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        updateExpireDateValue(Dates.formatDate(year, month + 1, day));
        Date expireDate = new GregorianCalendar(year, month , day, 12, 60).getTime();
        quest.setExpireDate(expireDate.getTime());
        enableClearExpireDateButton(true, getView());
    }

    private void enableClearExpireDateButton(boolean isEnabled, View view){
        if (view == null) return;
        IconButton expireDateClearButton = view.findViewById(R.id.fragment_quest_management_entry_expire_date_clear_button);
        expireDateClearButton.setEnabled(isEnabled);
    }

    private void updateExpireDateValue(String date){
        if (date == null){
            expireDateValue.setText("");
        } else {
            expireDateValue.setText(date);
        }
    }
}