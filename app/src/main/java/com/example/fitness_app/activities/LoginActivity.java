package com.example.fitness_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fitness_app.R;
import com.example.fitness_app.api.FirestoreRepository;
import com.example.fitness_app.models.Account;
import com.example.fitness_app.models.FirebaseCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class LoginActivity extends AppCompatActivity
{
    private static final String TAG = "LoginActivity";

    private EditText email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseApp.initializeApp(this);
        init();
    }

    private void init()
    {
        this.email = findViewById(R.id.login_email);
        this.password = findViewById(R.id.login_password);
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
            String email = user.getEmail();
            Bundle bundle = new Bundle();
            bundle.putString("EMAIL", email);
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("BUNDLE", bundle);
            startActivity(intent);
        }
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
            // Sign in using firestore
            FirestoreRepository.getFirebaseAuth().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if (task.isSuccessful())
                            {
                                // Sign in successful, log it and sign in user
                                Log.i(TAG, "Sign in with email/password:Success");
                                FirebaseUser user = FirestoreRepository.getCurrentUser();
                                updateUI(user);
                            }
                            else
                            {
                                // Sign in failed, log it and tell the user
                                Log.i(TAG, "Sign in with email/password:Failed");
                                Toast.makeText(getApplicationContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    public void signUp(View v)
    {
        // Implement sign up functionality
        final String email = this.email.getText().toString();
        String password = this.password.getText().toString();

        if (email.isEmpty() || password.isEmpty())
        {
            Toast.makeText(this, "Email or password is missing.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            FirestoreRepository.getFirebaseAuth().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if (task.isSuccessful())
                            {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = FirestoreRepository.getCurrentUser();
                                Account userAccount = new Account(0, "User");
                                FirestoreRepository.postObject("accounts", user.getEmail(), userAccount, new FirebaseCallback()
                                {
                                    @Override
                                    public void onSuccess(Object object)
                                    {
                                        // Maybe make toast?
                                    }

                                    @Override
                                    public void onFailure(FirebaseFirestoreException.Code errorCode)
                                    {
                                        // Failures are already handled
                                    }

                                    @Override
                                    public void onFinish()
                                    {}
                                });
                                updateUI(user);
                            }
                            else
                            {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(getApplicationContext(),task.getException().getLocalizedMessage(),
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }
                        }
                    });
        }
    }

    @Override
    public void onBackPressed()
    {
        finish();
    }
}
