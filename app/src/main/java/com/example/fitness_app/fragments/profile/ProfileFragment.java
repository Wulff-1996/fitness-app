package com.example.fitness_app.fragments.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.fitness_app.R;
import com.example.fitness_app.activities.LoginActivity;
import com.example.fitness_app.fragments.BaseFragment;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends BaseFragment {

    private Button signOutBtn;
    private View view;

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
    }
}
