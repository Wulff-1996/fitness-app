package com.example.fitness_app.api;

import com.example.fitness_app.constrants.ApiConstants;
import com.example.fitness_app.models.Account;
import com.example.fitness_app.models.AchievementAccountEntity;
import com.example.fitness_app.models.AchievementAccountOnceEntity;
import com.example.fitness_app.models.AchievementAccountStreakEntity;
import com.example.fitness_app.models.AchievementAccountTotalEntity;
import com.example.fitness_app.models.AchievementEntryEntity;
import com.example.fitness_app.models.FirebaseCallback;
import com.example.fitness_app.models.UserTask;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;

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

    public static void fetchAllAchievementsByUser(FirebaseCallback callback){
        FirestoreRepository.getInstance()
                .collection(ApiConstants.ACCOUNTS_COLLECTION)
                .document(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                .collection(ApiConstants.ACCOUNT_ACHIEVEMENT_COLLECTION)
                .get().addOnCompleteListener(task -> {
                    callback.onFinish();
                    if (task.isSuccessful()){
                        List<AchievementAccountEntity> achievements = new ArrayList<>();
                        for (DocumentSnapshot doc: task.getResult()){
                            AchievementAccountEntity entity = doc.toObject(AchievementAccountEntity.class);
                            switch (entity.getType()){
                                case "streak":
                                    achievements.add(doc.toObject(AchievementAccountStreakEntity.class));
                                    break;
                                case "once":
                                    achievements.add(doc.toObject(AchievementAccountOnceEntity.class));
                                    break;
                                case "total":
                                    achievements.add(doc.toObject(AchievementAccountTotalEntity.class));
                                    break;
                            }
                        }
                        callback.onSuccess(achievements);
                    } else {
                        callback.onFailure(((FirebaseFirestoreException)task.getException()).getCode());
                    }
                });
    }

    public static void fetchAllAchievementEntries(FirebaseCallback callback){
        FirestoreRepository.fetchAllDocumentsFromCollection(
                ApiConstants.ACHIEVEMENTS_COLLECTION,
                AchievementEntryEntity.class,
                callback
        );
    }

    public static void fetchAccount(String accountEmail, FirebaseCallback callback){
        FirestoreRepository.fetchObject(
                ApiConstants.ACCOUNTS_COLLECTION,
                accountEmail,
                Account.class,
                callback
        );
    }

    public static void postAccount(Account account, FirebaseCallback callback){
        FirestoreRepository.postObject(
                ApiConstants.ACCOUNTS_COLLECTION,
                FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                account,
                callback
        );
    }
}
