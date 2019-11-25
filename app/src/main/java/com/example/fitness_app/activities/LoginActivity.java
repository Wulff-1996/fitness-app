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
import com.example.fitness_app.services.Firestore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

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
        FirebaseUser currentUser = Firestore.getCurrentUser();

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
            Firestore.getFirebaseAuth().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if (task.isSuccessful())
                            {
                                // Sign in successful, log it and sign in user
                                Log.i(TAG, "Sign in with email/password:Success");
                                FirebaseUser user = Firestore.getCurrentUser();
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
    }

    @Override
    public void onBackPressed()
    {
        finish();
    }
}
