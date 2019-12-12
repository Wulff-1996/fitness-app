package com.example.fitness_app.api;

import com.example.fitness_app.constrants.ApiConstants;
import com.example.fitness_app.interfaces.AchievementsCombinedResponseInterface;
import com.example.fitness_app.interfaces.FirebaseCallback;
import com.example.fitness_app.models.Account;
import com.example.fitness_app.models.AchievementAccountEntity;
import com.example.fitness_app.models.AchievementAccountOnceEntity;
import com.example.fitness_app.models.AchievementAccountStreakEntity;
import com.example.fitness_app.models.AchievementAccountTotalEntity;
import com.example.fitness_app.models.AchievementEntryEntity;
import com.example.fitness_app.models.UserTask;
import com.example.fitness_app.util.AchievementUtil;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FirestoreService {

    /**
     * get  account document by logged in user email
     *
     * @param callBack
     */
    public static void getAllTasks(FirebaseCallback callBack) {
        FirestoreRepository.fetchObject(
                ApiConstants.ACCOUNTS_COLLECTION,
                FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                Account.class,
                callBack
        );
    }

    public static void postNewTask(UserTask userTask, FirebaseCallback callback) {
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> tasks = new HashMap<>();
        String id = UUID.randomUUID().toString();
        userTask.setId(id);
        tasks.put(id, userTask);
        data.put(ApiConstants.TASKS_FIELD_NAME, tasks);

        FirestoreRepository.postObject(ApiConstants.ACCOUNTS_COLLECTION, FirebaseAuth.getInstance().getCurrentUser().getEmail(), data, callback);
    }

    /**
     * a task entry is a field in a document, therefore both add and delete operations is an update
     *
     * @param updates
     * @param callback
     */
    public static void updateTaskEntry(Map<String, Object> updates, FirebaseCallback callback) {
        // create reference to new doc to know its id
        DocumentReference docRef = FirestoreRepository.getDocumentReference(
                ApiConstants.ACCOUNTS_COLLECTION,
                FirebaseAuth.getInstance().getCurrentUser().getEmail()
        );
        FirestoreRepository.updateField(docRef, updates, callback);
    }

    /**
     * method for getting all achievement by account and the account
     * achievement will be updated by the fields from the achievement in the other collection
     * @param callback
     */
    public static void fetchAllAchievementsByUser(FirebaseCallback callback) {
        getAllAccountAchievementsAndAllAchievements(new AchievementsCombinedResponseInterface() {
            @Override
            public void onFailure() {
                callback.onFailure(null);
                callback.onFinish();
            }

            @Override
            public void onSuccess(List<AchievementEntryEntity> allAchievements, List<AchievementAccountEntity> allAccountAchievements) {
                callback.onFinish();
                // update account achievement with data from achievement collection
                for(AchievementAccountEntity accountEntity: allAccountAchievements){

                    // get corresponding entity
                    AchievementEntryEntity entryEntity = AchievementUtil.getById(accountEntity.getId(), allAchievements);

                    if (entryEntity != null){
                        AchievementUtil.update(entryEntity, accountEntity);
                    }
                }
                callback.onSuccess(allAccountAchievements);
            }
        });
    }

    public static void getAllAccountAchievementsAndAllAchievements(AchievementsCombinedResponseInterface callback) {
        List<AchievementAccountEntity> accountAchievements = new ArrayList<>();
        List<AchievementEntryEntity> allAchievements = new ArrayList<>();

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
        combinedTasks.addOnFailureListener(e -> {
            callback.onFailure();
        });
        combinedTasks.addOnSuccessListener(querySnapshots -> {

            // map the achievementsTask to AchievementsEntryEntity
            for (QueryDocumentSnapshot snapshot : querySnapshots.get(0)) {
                allAchievements.add(snapshot.toObject(AchievementEntryEntity.class));
            }

            // map accountAchievementsTask to AchievementAccountEntity
            for (QueryDocumentSnapshot snapshot : querySnapshots.get(1)) {
                String type = (String) snapshot.get("type");
                switch (type) {
                    case "streak":
                        accountAchievements.add(snapshot.toObject(AchievementAccountStreakEntity.class));
                        break;
                    case "total":
                        accountAchievements.add(snapshot.toObject(AchievementAccountTotalEntity.class));
                        break;
                    case "once":
                        accountAchievements.add(snapshot.toObject(AchievementAccountOnceEntity.class));
                        break;
                }
            }

            callback.onSuccess(allAchievements, accountAchievements);
        });
    }

    public static void addAchievement(AchievementEntryEntity achievement, FirebaseCallback callback){
        AchievementAccountEntity accountAchievement = achievement.toAchievementAccountEntity();
        FirestoreRepository.getInstance()
                .collection(ApiConstants.ACCOUNTS_COLLECTION)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()){

                        WriteBatch batch = FirestoreRepository.getInstance().batch();

                        DocumentReference documentReference =
                                FirestoreRepository.getInstance()
                                .collection(ApiConstants.ACHIEVEMENTS_COLLECTION)
                                .document(achievement.getId());
                        batch.set(documentReference, achievement);

                        for (DocumentSnapshot doc: task.getResult()){
                            DocumentReference docRef = doc.getReference()
                                    .collection(ApiConstants.ACCOUNT_ACHIEVEMENT_COLLECTION)
                                    .document(accountAchievement.getId());
                            batch.set(docRef, accountAchievement);
                        }

                        batch.commit().addOnCompleteListener(task1 -> {
                            if (task.isSuccessful()){
                                callback.onSuccess(null);
                            } else {
                                callback.onFailure(((FirebaseFirestoreException)task.getException()).getCode());
                            }
                        });

                    } else {
                        callback.onFailure(((FirebaseFirestoreException)task.getException()).getCode());
                    }

                });
    }

    public static void deleteAchievement(AchievementEntryEntity achievement, FirebaseCallback callback){
        FirestoreRepository.getInstance()
                .collection(ApiConstants.ACCOUNTS_COLLECTION)
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){

                WriteBatch batch = FirestoreRepository.getInstance().batch();

                DocumentReference documentReference =
                        FirestoreRepository.getInstance()
                        .collection(ApiConstants.ACHIEVEMENTS_COLLECTION)
                        .document(achievement.getId());
                batch.delete(documentReference);

                for (DocumentSnapshot doc: task.getResult()){
                    DocumentReference docRef = doc.getReference()
                            .collection(ApiConstants.ACCOUNT_ACHIEVEMENT_COLLECTION)
                            .document(achievement.getId());
                    batch.delete(docRef);
                }

                batch.commit().addOnCompleteListener(task1 -> {
                    if (task.isSuccessful()){
                        callback.onSuccess(null);
                    } else {
                        callback.onFailure(((FirebaseFirestoreException)task.getException()).getCode());
                    }
                });

            } else {
                callback.onFailure(((FirebaseFirestoreException)task.getException()).getCode());
            }

        });
    }
}

