package com.example.fitness_app.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.fitness_app.R;
import com.example.fitness_app.constrants.ApplicationMode;

import static com.example.fitness_app.constrants.IntentKeys.INTENT_KEY_APPLICATION_MODE;

public class SelectApplicationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_application);

        setupView();
    }

    private void setupView(){
        CardView usersView = findViewById(R.id.activity_select_application_normal_app_card);
        CardView superUserView = findViewById(R.id.activity_select_application_super_user_app_card);

        usersView.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(INTENT_KEY_APPLICATION_MODE, ApplicationMode.APPLICATION_USERS);
            startActivity(intent);
            finish();
        });

        superUserView.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(INTENT_KEY_APPLICATION_MODE, ApplicationMode.APPLICATION_SUPER_USER);
            startActivity(intent);
            finish();
        });
    }
}
