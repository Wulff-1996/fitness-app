package com.example.fitness_app.fragments.buttom_sheet_dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.fitness_app.R;
import com.example.fitness_app.api.FirestoreRepository;
import com.example.fitness_app.api.FirestoreService;
import com.example.fitness_app.constrants.Api;
import com.example.fitness_app.interfaces.FirebaseCallback;
import com.example.fitness_app.models.Account;
import com.example.fitness_app.models.AchievementEntryEntity;
import com.example.fitness_app.models.QuestEntity;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SignupDialog extends DialogFragment
{
    private static final String TAG = "SignupDialog";

    private EditText username, email, password;
    private Button createBtn;


    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_signup, null);


        builder.setView(view);

        username = view.findViewById(R.id.dialog_signup_username_value);
        email = view.findViewById(R.id.dialog_signup_email_value);
        password = view.findViewById(R.id.dialog_signup_password_value);
        createBtn = view.findViewById(R.id.dialog_signup_createBtn);
        createBtn.setOnClickListener(v -> createAccount());

        return builder.create();
    }

    private void createAccount()
    {
        // Implement sign up functionality
        final String email = this.email.getText().toString();
        String username = this.username.getText().toString();
        String password = this.password.getText().toString();

        if (email.isEmpty() || password.isEmpty() || username.isEmpty())
        {
            Toast.makeText(getContext(), "Username, Email or password is missing.", Toast.LENGTH_SHORT).show();
        }
        else
        {

            if (username.length() < 5)
            {
                Toast.makeText(getContext(), "Username needs to be atleast 5 characters.", Toast.LENGTH_SHORT).show();
            }
            else
            {
                FirestoreRepository.fetchIDs(Api.USERNAMES_COLLECTION, new FirebaseCallback()
                {
                    @Override
                    public void onSuccess(Object object)
                    {
                        ArrayList<String> usernames = (ArrayList) object;
                        if (!usernames.contains(username))
                        {
                            FirestoreRepository.getFirebaseAuth().createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(getActivity(), task -> {
                                        if (task.isSuccessful())
                                        {
                                            // Sign in success, update UI with the signed-in user's information
                                            Log.d(TAG, "createUserWithEmail:success");
                                            FirebaseUser user = FirestoreRepository.getCurrentUser();
                                            Account userAccount = new Account("User", username, email);

                                            FirestoreRepository.postObject("accounts", email, userAccount, new FirebaseCallback()
                                            {
                                                @Override
                                                public void onSuccess(Object object)
                                                {
                                                    // Add all existing achievements to the new account
                                                    FirestoreService.getAllAchievements(new FirebaseCallback()
                                                    {
                                                        @Override
                                                        public void onSuccess(Object object)
                                                        {
                                                            for (AchievementEntryEntity AEE : (List<AchievementEntryEntity>) object)
                                                            {
                                                                FirestoreService.addAchievementToUser(email, AEE, new FirebaseCallback()
                                                                {
                                                                    @Override
                                                                    public void onSuccess(Object object)
                                                                    {

                                                                    }

                                                                    @Override
                                                                    public void onFailure(Exception e)
                                                                    {

                                                                    }

                                                                    @Override
                                                                    public void onFinish()
                                                                    {

                                                                    }
                                                                });
                                                            }
                                                        }

                                                        @Override
                                                        public void onFailure(Exception e)
                                                        {

                                                        }

                                                        @Override
                                                        public void onFinish()
                                                        {

                                                        }
                                                    });

                                                    // Add all existing quests to the new account
                                                    FirestoreService.getAllQuests(new FirebaseCallback()
                                                    {
                                                        @Override
                                                        public void onSuccess(Object object)
                                                        {
                                                            for (QuestEntity quest : (List<QuestEntity>) object)
                                                            {
                                                                FirestoreService.addQuestToUser(email, quest, new FirebaseCallback()
                                                                {
                                                                    @Override
                                                                    public void onSuccess(Object object)
                                                                    {

                                                                    }

                                                                    @Override
                                                                    public void onFailure(Exception e)
                                                                    {

                                                                    }

                                                                    @Override
                                                                    public void onFinish()
                                                                    {

                                                                    }
                                                                });
                                                            }
                                                        }

                                                        @Override
                                                        public void onFailure(Exception e)
                                                        {

                                                        }

                                                        @Override
                                                        public void onFinish()
                                                        {

                                                        }
                                                    });
                                                }

                                                @Override
                                                public void onFailure(Exception e)
                                                {
                                                    // Failures are already handled
                                                }

                                                @Override
                                                public void onFinish()
                                                {
                                                }
                                            });
                                            Toast.makeText(getContext(), "User created", Toast.LENGTH_LONG).show();
                                            FirestoreRepository.postObject(Api.USERNAMES_COLLECTION, username, new HashMap<>(), new FirebaseCallback()
                                            {
                                                @Override
                                                public void onSuccess(Object object)
                                                {

                                                }

                                                @Override
                                                public void onFailure(Exception e)
                                                {

                                                }

                                                @Override
                                                public void onFinish()
                                                {

                                                }
                                            });
                                            dismiss();
                                        } else
                                        {
                                            // If sign in fails, display a message to the user.
                                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                            Toast.makeText(getContext(), task.getException().getLocalizedMessage(),
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                        else
                        {
                            Toast.makeText(getContext(), "Username already in use.", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Exception e)
                    {

                    }

                    @Override
                    public void onFinish()
                    {

                    }
                });
            }
        }
    }
}