package com.example.fitness_app.fragments.buttom_sheet_dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.fitness_app.R;
import com.example.fitness_app.api.FirestoreRepository;
import com.example.fitness_app.interfaces.FirebaseCallback;
import com.example.fitness_app.models.Quest;

public class CreateQuestDialog extends DialogFragment
{
    private EditText questName, category, repititions, expReward;


    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_create_quest, null);



        builder.setView(view)
                .setPositiveButton("Create", (dialog, which) -> {
                    questName = view.findViewById(R.id.dialog_create_quest_title);
                    category = view.findViewById(R.id.dialog_create_quest_category);
                    repititions = view.findViewById(R.id.dialog_create_quest_repititions);
                    expReward = view.findViewById(R.id.dialog_create_quest_exp);

                    if (!questName.getText().toString().equals("") &&
                            !category.getText().toString().equals("") &&
                            !repititions.getText().toString().equals("") &&
                            !expReward.getText().toString().equals("") )
                    {
                        Quest quest = new Quest(
                                category.getText().toString(),
                                questName.getText().toString(),
                                Integer.parseInt(expReward.getText().toString()),
                                Integer.parseInt(repititions.getText().toString()));
                        FirestoreRepository.postDocument("quests", quest, new FirebaseCallback()
                        {
                            @Override
                            public void onSuccess(Object object)
                            {

                            }

                            @Override
                            public void onFailure(Exception errorCode)
                            {

                            }

                            @Override
                            public void onFinish()
                            {

                            }
                        });
                    }
                });

        return builder.create();
    }
}