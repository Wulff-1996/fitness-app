package com.example.fitness_app.future_features;

import androidx.annotation.NonNull;

import com.example.fitness_app.api.FirestoreRepository;
import com.example.fitness_app.constrants.Api;
import com.example.fitness_app.models.Account;
import com.example.fitness_app.models.UserTask;
import com.example.fitness_app.util.Dates;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.List;

public class CompleteAnyTaskOnceUpdate extends UpdateEntity {
    private Account account;
    private List<UserTask> userTasks;

    public CompleteAnyTaskOnceUpdate() {
        List<String> updateKeys = new ArrayList<>();
        updateKeys.add("tasks");
        updateKeys.add("once");
        updateKeys.add("any");
        this.setUpdateKeys(updateKeys);
    }

    @Override
    public void onUpdate() {
        getAccount();
    }

    private void getAccount(){
        FirestoreRepository
                .getInstance()
                .collection(Api.ACCOUNTS_COLLECTION)
                .document(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){

                            account = task.getResult().toObject(Account.class);
                            userTasks = (ArrayList<UserTask>)account.getTasks().values();

                            if (hasCompletedAchievement()){
                                writeUpdateToAchievements();
                            }
                        }
                    }
                });
    }

    private boolean hasCompletedAchievement(){
        for (UserTask task: userTasks){
            if (task.getEntries().size() > 0){
                return true;
            }
        }
        return false;
    }

    private void writeUpdateToAchievements(){
        DocumentReference docRef =
                    FirestoreRepository
                            .getInstance()
                            .collection(Api.ACHIEVEMENTS_COLLECTION)
                            .document("61ab1661-b88f-4148-87d5-bb14828a66cc");

        FirestoreRepository.getInstance()
                .runTransaction((Transaction.Function<Void>) transaction -> {
                    DocumentSnapshot snapshot = transaction.get(docRef);
                    long newTotalPlayersCompleted = snapshot.getLong("totalPlayersCompleted") + 1;
                    transaction.update(docRef, "totalPlayersCompleted", newTotalPlayersCompleted);
                    return null;
                }).addOnSuccessListener(aVoid -> writeUpdateToAccountsAchievements());
    }

    private void writeUpdateToAccountsAchievements(){
        DocumentReference docRef = FirestoreRepository
                .getInstance()
                .collection(Api.ACCOUNTS_COLLECTION)
                .document(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                .collection(Api.ACCOUNT_ACHIEVEMENT_COLLECTION)
                .document("61ab1661-b88f-4148-87d5-bb14828a66cc");

        FirestoreRepository.getInstance()
                .runTransaction((Transaction.Function<Void>) transaction -> {
                    DocumentSnapshot snapshot = transaction.get(docRef);

                    long completedCount = snapshot.getLong("amountToCompleteCount");
                    String datetime = Dates.formatDate(System.currentTimeMillis());

                    transaction.update(docRef, "completedCount", completedCount);
                    transaction.update(docRef, "completionDate", datetime);
                    transaction.update(docRef, "isCompleted", true);
                    return null;
                }).addOnSuccessListener(aVoid -> {
                    getCompletedListener().onCompleted();
        });
    }
}
