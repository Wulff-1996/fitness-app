package com.example.fitness_app.fragments.quests;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.fitness_app.R;
import com.example.fitness_app.api.FirestoreRepository;
import com.example.fitness_app.constrants.Globals;
import com.example.fitness_app.fragments.BaseFragment;
import com.example.fitness_app.fragments.buttom_sheet_dialogs.CreateQuestDialog;
import com.example.fitness_app.models.FirebaseCallback;
import com.example.fitness_app.models.Quest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class QuestFragment extends BaseFragment {

    private View view;
    private ListView questsListView, questStatusListView;
    private List questNames, questStatuses;
    private ArrayAdapter questAdapter, questStatusAdapter;
    private TreeMap<String, Quest> questList;
    private FloatingActionButton addQuestBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_quests, container, false);

        init();

        return view;
    }

    private void init()
    {
        addQuestBtn = view.findViewById(R.id.fragment_quests_addBtn);
        addQuestBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                addQuest();
            }
        });
        questList = new TreeMap<>();
        questStatuses = new ArrayList();
        questNames = new ArrayList();
        addQuestBtn = view.findViewById(R.id.fragment_quests_addBtn);
        if (Globals.userAccount.getUserType().equals("User"))
        {
            addQuestBtn.hide();
        }
        questsListView = view.findViewById(R.id.fragment_quests_listview);
        questStatusListView = view.findViewById(R.id.fragment_quests_status_listview);
        FirestoreRepository.getCollectionReference("quests", new FirebaseCallback()
        {
            @Override
            public void onSuccess(Object object)
            {
                // Get all quests from DB and put in questlist
                QuerySnapshot dbSnapshot = (QuerySnapshot) object;
                for (DocumentSnapshot quest:dbSnapshot)
                {
                    Quest currentQuest = quest.toObject(Quest.class);
                    questList.put(quest.getId(), currentQuest);
                }
                questAdapter = new ArrayAdapter(view.getContext(), android.R.layout.simple_list_item_1, questNames);
                questsListView.setAdapter(questAdapter);

                // Make statuslist move the same as questlist if scroll on.
                questsListView.setOnTouchListener(new View.OnTouchListener()
                {
                    @Override
                    public boolean onTouch(View v, MotionEvent event)
                    {
                        if (event != null)
                        {
                            questStatusListView.dispatchTouchEvent(event);
                        }
                        return false;
                    }
                });

                // Check if user contains the quest (did the quest) and add "Done" to status list if true
                for (TreeMap.Entry<String, Quest> entry: questList.entrySet())
                {
                    questNames.add(entry.getValue().getTitle());
                    if (Globals.userAccount.getQuests().contains(entry.getKey()))
                    {

                        questStatuses.add("Done");
                    }
                    else
                    {
                        questStatuses.add("");
                    }
                }
                questStatusAdapter = new ArrayAdapter(view.getContext(), android.R.layout.simple_list_item_1, questStatuses);
                questStatusListView.setAdapter(questStatusAdapter);
            }

            @Override
            public void onFailure(FirebaseFirestoreException.Code errorCode)
            {
            }

            @Override
            public void onFinish()
            {
            }
        });
    }

    private void addQuest()
    {
        // Create popup with input fields for date/value
        CreateQuestDialog createQuestDialog = new CreateQuestDialog();
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev =  fm.findFragmentByTag("dialog");
        if(prev != null)
        {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        createQuestDialog.show(ft, "dialog");
    }
}
