package com.example.fitness_app.fragments.profile;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.fitness_app.R;
import com.example.fitness_app.activities.LoginActivity;
import com.example.fitness_app.constrants.Globals;
import com.example.fitness_app.fragments.BaseFragment;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends BaseFragment {

    private TextView emailTextView, levelTextView, expProgression;
    private Button signOutBtn, websiteBtn, facebookBtn;
    private View view;
    private ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        init();

        return view;
    }

    private void init()
    {
        progressBar = view.findViewById(R.id.fragment_profile_progressBar);
        levelTextView = view.findViewById(R.id.fragment_profile_level_textview);
        expProgression = view.findViewById(R.id.fragment_profile_exp);
        calculateLevel(Globals.userAccount.getExp());

        emailTextView = view.findViewById(R.id.fragment_profile_email_textview);
        emailTextView.setText(Globals.email);
        signOutBtn = view.findViewById(R.id.signOutBtn);
        signOutBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
        websiteBtn = view.findViewById(R.id.websitelinkBtn);
        websiteBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://dalfitness.dk/"));
                startActivity(browserIntent);
            }
        });
        facebookBtn = view.findViewById(R.id.facebooklinkBtn);
        facebookBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Uri uri = Uri.parse("https://www.facebook.com/DalFitnessDK/");
                try {
                    ApplicationInfo applicationInfo = getContext().getPackageManager().getApplicationInfo("com.facebook.katana", 0);
                    if (applicationInfo.enabled) {
                        // http://stackoverflow.com/a/24547437/1048340
                       uri = Uri.parse("fb://facewebmodal/f?href=" + "https://www.facebook.com/DalFitnessDK/");
                    }
                }
                catch (PackageManager.NameNotFoundException ignored)
                {}
                Intent fbIntent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(fbIntent);
            }
        });
    }

    private void calculateLevel(int exp)
    {
        int level = 1;
        int expNeeded = 100;
        int lastExp = 0;
        int maxLevel = 60;

        for (int i = level; i < maxLevel; i++)
        {
            if (exp < expNeeded)
            {
                break;
            }
            lastExp = expNeeded;
            if (i < 30)
            {
                expNeeded = 100 * i + (int) (expNeeded * 1.10);
            }
            else if (i < 50)
            {
                expNeeded = 100 * i + (int) (expNeeded * 1.05);
            }
            else
            {
                expNeeded = 100 * i + (int) (expNeeded * 1.025);
            }
            System.out.println("Exp needed for levelup from: " + (level + 1) + ": " + expNeeded);
            level++;
        }

        if (level < maxLevel)
        {
            float percentDone = (exp-lastExp)*100.0f/(expNeeded-lastExp);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            {
                progressBar.setProgress((int) percentDone, true);
            }
            else
            {
                progressBar.setProgress((int) percentDone);
            }
            levelTextView.setText(String.valueOf(level));
            expProgression.setText((exp-lastExp) + "/" + (expNeeded-lastExp));
        }
        else
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            {
                progressBar.setProgress(100, true);
            }
            else
            {
                progressBar.setProgress(100);
            }
        }

    }
}
