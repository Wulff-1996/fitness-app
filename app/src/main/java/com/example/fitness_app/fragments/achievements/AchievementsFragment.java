package com.example.fitness_app.fragments.achievements;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitness_app.R;
import com.example.fitness_app.adapters.AchievementsAdapter;
import com.example.fitness_app.adapters.AdapterOnItemClickListener;
import com.example.fitness_app.api.FirestoreRepository;
import com.example.fitness_app.api.FirestoreService;
import com.example.fitness_app.constrants.ApiConstants;
import com.example.fitness_app.fragments.BaseFragment;
import com.example.fitness_app.models.AchievementAccountEntity;
import com.example.fitness_app.models.AchievementAccountOnceEntity;
import com.example.fitness_app.models.AchievementAccountStreakEntity;
import com.example.fitness_app.models.AchievementAccountTotalEntity;
import com.example.fitness_app.models.AchievementEntryEntity;
import com.example.fitness_app.models.FirebaseCallback;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.List;

public class AchievementsFragment extends BaseFragment implements AdapterOnItemClickListener {
    private AchievementsAdapter adapter;
    private List<AchievementEntryEntity> achievementEntryEntities = new ArrayList<>();
    private List<AchievementAccountEntity> achievementAccountEntities = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fetchAchievements();
    }

    private void fetchAchievements(){
        // get all achievements from the current users account
        Task accountAchievementsTask = FirestoreRepository.getInstance()
                .collection(ApiConstants.ACCOUNTS_COLLECTION)
                .document(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                .collection(ApiConstants.ACCOUNT_ACHIEVEMENT_COLLECTION)
                .get();

        // get all achievements
        Task achievementsTask = FirestoreRepository.getInstance()
                .collection(ApiConstants.ACHIEVEMENTS_COLLECTION)
                .get();

        // combine the two calls, because we need to compare the two lists
        Task<List<QuerySnapshot>> combinedTasks = Tasks.whenAllSuccess(achievementsTask, accountAchievementsTask);
        combinedTasks.addOnSuccessListener(querySnapshots -> {

            // map the achievementsTask to AchievementsEntryEntity
            for (QueryDocumentSnapshot snapshot: querySnapshots.get(0)){
                achievementEntryEntities.add(snapshot.toObject(AchievementEntryEntity.class));
            }

            // map accountAchievementsTask to AchievementAccountEntity
            for(QueryDocumentSnapshot snapshot: querySnapshots.get(1)){
                String type = (String) snapshot.get("type");
                switch (type){
                    case "streak":
                        achievementAccountEntities.add(snapshot.toObject(AchievementAccountStreakEntity.class));
                        break;
                    case "total":
                        achievementAccountEntities.add(snapshot.toObject(AchievementAccountTotalEntity.class));
                        break;
                    case "once":
                        achievementAccountEntities.add(snapshot.toObject(AchievementAccountOnceEntity.class));
                        break;
                }
            }

            List<AchievementEntryEntity> entriesNotToAdd = new ArrayList<>(achievementEntryEntities);

            // check if any new achievements, if account are missing some achievements
            for(AchievementAccountEntity entity: achievementAccountEntities){
                for(AchievementEntryEntity achievementEntryEntity: achievementEntryEntities){
                    if (entity.getId().equals(achievementEntryEntity.getId())){
                        entriesNotToAdd.add(achievementEntryEntity);
                        break;
                    }
                }
            }

            achievementEntryEntities.removeAll(entriesNotToAdd);

            // write missing achievements to the account
            WriteBatch batch = FirestoreRepository.getInstance().batch();
                for (AchievementEntryEntity entity: achievementEntryEntities){
                    DocumentReference nycRef = FirestoreRepository.getInstance()
                            .collection(ApiConstants.ACCOUNTS_COLLECTION)
                            .document(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                            .collection(ApiConstants.ACCOUNT_ACHIEVEMENT_COLLECTION)
                            .document(entity.getId());
                    batch.set(nycRef, entity.toAchievementAccountEntity());
                }
                batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_achievement, container, false);

        setupToolbar();
        setupAdapter(view);

        return view;
    }

    private void setupToolbar(){
    }

    private void setupAdapter(View view){
        RecyclerView recyclerView = view.findViewById(R.id.fragment_achievements_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new AchievementsAdapter(getContext(), new ArrayList<>());
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    private void fetchAllAchievementsFromUser(){
        showProgressBar(true);
        FirestoreService.fetchAllAchievementsByUser(new FirebaseCallback() {
            @Override
            public void onSuccess(Object object) {
                adapter.setAchievementEntryEntities((ArrayList<AchievementEntryEntity>) object);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(FirebaseFirestoreException.Code errorCode) {

            }

            @Override
            public void onFinish() {
                showProgressBar(false);
            }
        });
    }
}
