package com.example.fitness_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.fitness_app.R;
import com.example.fitness_app.api.FirestoreRepository;
import com.example.fitness_app.constrants.ApplicationMode;
import com.example.fitness_app.constrants.Globals;
import com.example.fitness_app.fragments.buttom_sheet_dialogs.SignupDialog;
import com.example.fitness_app.interfaces.FirebaseCallback;
import com.example.fitness_app.models.Account;
import com.example.fitness_app.storage.StorageManager;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseUser;

import static com.example.fitness_app.constrants.IntentKeys.INTENT_KEY_APPLICATION_MODE;
import static com.example.fitness_app.constrants.UserTypes.SUPER_USER;

public class LoginActivity extends BaseActivity
{
    private static final String TAG = "LoginActivity";

    private EditText email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseApp.initializeApp(this);
        setupProgressBar();
        setupView();
    }

    private void setupProgressBar(){
        ProgressBar progressBar = findViewById(R.id.activity_login_progress_bar);
        progressBar.setIndeterminate(true);
        setProgressBar(progressBar);
        if (!hasShownInitialLoading()){
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    private void setupView()
    {
        this.email = findViewById(R.id.activity_login_email_value);
        this.password = findViewById(R.id.activity_login_password_value);
        Button signIn = findViewById(R.id.activity_login_login_button);
        Button signUp = findViewById(R.id.activity_login_sign_up_button);
        signIn.setOnClickListener(this::signIn);
        signUp.setOnClickListener(this::signUp);
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        // Get current user and sign in with the current user
        FirebaseUser currentUser = FirestoreRepository.getCurrentUser();

        updateUI(currentUser);

    }

    public void updateUI(FirebaseUser user)
    {
        if (user != null)
        {
            Globals.fetchEmail();
            Globals.fetchAccount();
            String email = user.getEmail();
            FirestoreRepository.fetchObject("accounts", email, Account.class, new FirebaseCallback()
            {
                @Override
                public void onSuccess(Object object)
                {
                    Account account = (Account) object;
                    StorageManager.getInstance(getApplicationContext()).setAccount(account);
                    handleSignIn((Account) object);
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
        } else {
            showLogInElement();
        }
    }

    private void showLogInElement(){
        EditText usernameView = findViewById(R.id.activity_login_email_value);
        EditText passwordView = findViewById(R.id.activity_login_password_value);
        Button loginButton = findViewById(R.id.activity_login_login_button);
        Button signUpButton = findViewById(R.id.activity_login_sign_up_button);
        usernameView.setVisibility(View.VISIBLE);
        passwordView.setVisibility(View.VISIBLE);
        loginButton.setVisibility(View.VISIBLE);
        signUpButton.setVisibility(View.VISIBLE);
        showProgress(false);
    }

    private void handleSignIn(Account account){
        if (account.getUserType().equals(SUPER_USER)){
            Intent intent = new Intent(this, SelectApplicationActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(INTENT_KEY_APPLICATION_MODE, ApplicationMode.APPLICATION_USERS);
            startActivity(intent);
        }
        this.finish();
    }

    public void signIn(View v)
    {
        String email = this.email.getText().toString();
        String password = this.password.getText().toString();

        if (email.isEmpty() || password.isEmpty())
        {
            Toast.makeText(this, "Email or password is missing.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            showProgress(true);
            // Sign in using firestore
            FirestoreRepository.getFirebaseAuth().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        showProgress(false);
                        if (task.isSuccessful())
                        {
                            // Sign in successful, log it and sign in user
                            Log.i(TAG, "Sign in with email/password:Success");
                            FirebaseUser user = FirestoreRepository.getCurrentUser();
                            Globals.fetchEmail();
                            Globals.fetchAccount();
                            updateUI(user);
                        }
                        else
                        {
                            // Sign in failed, log it and tell the user
                            Log.i(TAG, "Sign in with email/password:Failed");
                            Toast.makeText(getApplicationContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public void signUp(View v)
    {
        // Create popup with input fields for date/value
        SignupDialog signupDialog = new SignupDialog();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev =  fm.findFragmentByTag("dialog");
        if(prev != null)
        {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        signupDialog.show(ft, "dialog");
    }

    @Override
    public void onBackPressed()
    {
        finish();
    }
}
