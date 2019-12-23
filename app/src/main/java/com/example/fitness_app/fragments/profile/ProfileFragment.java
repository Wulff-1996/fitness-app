package com.example.fitness_app.fragments.profile;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.fitness_app.R;
import com.example.fitness_app.activities.LoginActivity;
import com.example.fitness_app.activities.SelectApplicationActivity;
import com.example.fitness_app.api.FirestoreService;
import com.example.fitness_app.fragments.BaseFragment;
import com.example.fitness_app.interfaces.FirebaseCallback;
import com.example.fitness_app.models.Account;
import com.google.firebase.auth.FirebaseAuth;

import static com.example.fitness_app.constrants.UserTypes.SUPER_USER;

public class ProfileFragment extends BaseFragment {
    private Account account;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView levelView;
    private TextView experiencePointsView;
    private ProgressBar levelProgressBar;
    private TextView emailValueView;
    private ConstraintLayout changeAppView;
    private ConstraintLayout websiteView;
    private ConstraintLayout facebookView;
    private ConstraintLayout signOutView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fetchAccount();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        setupSwipeToRefresh(view);
        setupLevelViews(view);
        setupWebsiteButton(view);
        setupFacebookButton(view);
        setupChangeAppButton(view);
        setupSignOutButton(view);

        if (account != null){
            populateView(view);
            updateChangeAppVisibility(account.getUserType().equals(SUPER_USER), view);
        }

        return view;
    }

    private void setupSwipeToRefresh(View view){
        swipeRefreshLayout = view.findViewById(R.id.fragment_profile_swipe_to_refresh);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            fetchAccount();
        });
    }

    private void setupLevelViews(View view){
        levelView = view.findViewById(R.id.fragment_profile_level_value);
        experiencePointsView = view.findViewById(R.id.fragment_profile_exp);
        levelProgressBar = view.findViewById(R.id.fragment_profile_progressBar);
        emailValueView = view.findViewById(R.id.fragment_profile_email_textview);
    }

    private void setupWebsiteButton(View view){
        websiteView = view.findViewById(R.id.fragment_profile_website_button);
        websiteView.setOnClickListener(view1 -> handleWebsite());
    }

    private void setupFacebookButton(View view){
        facebookView = view.findViewById(R.id.fragment_profile_facebook_button);
        facebookView.setOnClickListener(view1 -> handleFacebook());
    }

    private void setupChangeAppButton(View view){
        changeAppView = view.findViewById(R.id.fragment_profile_change_app_button);
        changeAppView.setOnClickListener(view1 -> handleChangeApp());
        if (account != null){
            if (account.getUserType().equals(SUPER_USER)){
                updateChangeAppVisibility(true, view);
            }
        }
    }

    private void updateChangeAppVisibility(boolean isVisible, View view){
        if (view == null) return;
        if (isVisible){
            changeAppView.setVisibility(View.VISIBLE);
        } else {
            changeAppView.setVisibility(View.GONE);
        }
    }

    private void setupSignOutButton(View view){
        signOutView = view.findViewById(R.id.fragment_profile_sign_out_button);
        signOutView.setOnClickListener(view1 -> handleSignOut());
    }

    private void handleWebsite(){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://dalfitness.dk/"));
        startActivity(browserIntent);
    }

    private void handleFacebook(){
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

    private void handleChangeApp(){
        Intent intent = new Intent(getActivity(), SelectApplicationActivity.class);
        startActivity(intent);
        if (getActivity() != null){
            getActivity().finish();
        }
    }

    private void handleSignOut(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        if (getActivity() != null){
            getActivity().finish();
        }
    }

    private void populateView(View view){
        if (view == null) return;
        int[] levelProgressionArray = account.getLevelInformation();
        levelView.setText(String.valueOf(levelProgressionArray[0]));
        levelProgressBar.setProgress(levelProgressionArray[1]);
        experiencePointsView.setText(levelProgressionArray[2] + "/" + levelProgressionArray[3]);
        emailValueView.setText(account.getEmail());

        updateChangeAppVisibility(account.getUserType().equals(SUPER_USER), view);
    }

    private void fetchAccount(){
        FirestoreService.getAccount(new FirebaseCallback() {
            @Override
            public void onSuccess(Object object) {
                account = (Account) object;
                populateView(getView());
            }

            @Override
            public void onFailure(Exception e) {

            }

            @Override
            public void onFinish() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void disableButtons(boolean isDisabled){
        if (getView() == null) return;
        if (isDisabled){
            websiteView.setEnabled(false);
            facebookView.setEnabled(false);
            changeAppView.setEnabled(false);
            signOutView.setEnabled(false);
        } else {
            websiteView.setEnabled(true);
            facebookView.setEnabled(true);
            changeAppView.setEnabled(true);
            signOutView.setEnabled(true);
        }
    }
}
