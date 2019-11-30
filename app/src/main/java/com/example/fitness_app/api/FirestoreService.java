package com.example.fitness_app.api;

import com.example.fitness_app.constrants.ApiConstants;
import com.example.fitness_app.models.Account;
import com.example.fitness_app.models.FirebaseCallback;
import com.example.fitness_app.models.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FirestoreService {

    private static String currentUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

    /**
     * get  account document by logged in user email
     *
     * @param callBack
     */
    public static void getAllTasks(FirebaseCallback callBack) {
        FirestoreRepository.fetchObject(
                ApiConstants.ACCOUNTS_COLLECTION,
                currentUserEmail,
                Account.class,
                callBack
        );
    }

    public static void postNewTask(Task task, FirebaseCallback callback) {
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> tasks = new HashMap<>();
        String id = UUID.randomUUID().toString();
        task.setId(id);
        tasks.put(id, task);
        data.put(ApiConstants.TASKS_FIELD_NAME, tasks);

        FirestoreRepository.postObject(ApiConstants.ACCOUNTS_COLLECTION, currentUserEmail, data, callback);
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
                currentUserEmail
        );
        FirestoreRepository.updateField(docRef, updates, callback);
    }
}
