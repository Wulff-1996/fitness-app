package com.example.fitness_app.api;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fitness_app.constrants.AchievementStatusTypes;
import com.example.fitness_app.constrants.AchievementTypes;
import com.example.fitness_app.constrants.Api;
import com.example.fitness_app.interfaces.AchievementsCombinedResponseInterface;
import com.example.fitness_app.interfaces.FirebaseCallback;
import com.example.fitness_app.models.Account;
import com.example.fitness_app.models.AchievementAccountAutomaticEntity;
import com.example.fitness_app.models.AchievementAccountEntity;
import com.example.fitness_app.models.AchievementAccountManualEntity;
import com.example.fitness_app.models.AchievementApprovalRequest;
import com.example.fitness_app.models.AchievementEntryEntity;
import com.example.fitness_app.models.UserTask;
import com.example.fitness_app.util.AchievementUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.example.fitness_app.constrants.Api.ACHIEVEMENT_APPROVAL_REQUESTS_COLLECTION;

public class FirestoreService {

    /**
     * get  account document by logged in user email
     *
     * @param callBack
     */
    public static void getAllTasks(FirebaseCallback callBack) {
        FirestoreRepository.fetchObject(
                Api.ACCOUNTS_COLLECTION,
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
        data.put(Api.TASKS_FIELD_NAME, tasks);

        FirestoreRepository.postObject(Api.ACCOUNTS_COLLECTION, FirebaseAuth.getInstance().getCurrentUser().getEmail(), data, callback);
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
                Api.ACCOUNTS_COLLECTION,
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
                .collection(Api.ACCOUNTS_COLLECTION)
                .document(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                .collection(Api.ACCOUNT_ACHIEVEMENT_COLLECTION)
                .get();

        // get all achievements
        Task achievementsTask = FirestoreRepository.getInstance()
                .collection(Api.ACHIEVEMENTS_COLLECTION)
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
                    case AchievementTypes.MANUAL:
                        accountAchievements.add(snapshot.toObject(AchievementAccountManualEntity.class));
                        break;
                    case AchievementTypes.AUTOMATIC:
                        accountAchievements.add(snapshot.toObject(AchievementAccountAutomaticEntity.class));
                        break;
                }
            }

            callback.onSuccess(allAchievements, accountAchievements);
        });
    }

    public static void addAchievement(AchievementEntryEntity achievement, FirebaseCallback callback){
        AchievementAccountEntity accountAchievement = achievement.toAchievementAccountEntity();
        FirestoreRepository.getInstance()
                .collection(Api.ACCOUNTS_COLLECTION)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()){

                        WriteBatch batch = FirestoreRepository.getInstance().batch();

                        DocumentReference documentReference =
                                FirestoreRepository.getInstance()
                                .collection(Api.ACHIEVEMENTS_COLLECTION)
                                .document(achievement.getId());
                        batch.set(documentReference, achievement);

                        for (DocumentSnapshot doc: task.getResult()){
                            DocumentReference docRef = doc.getReference()
                                    .collection(Api.ACCOUNT_ACHIEVEMENT_COLLECTION)
                                    .document(accountAchievement.getId());
                            batch.set(docRef, accountAchievement);
                        }

                        batch.commit().addOnCompleteListener(task1 -> {
                            if (task.isSuccessful()){
                                callback.onSuccess(null);
                            } else {
                                callback.onFailure(task.getException());
                            }
                        });

                    } else {
                        callback.onFailure(task.getException());
                    }

                });
    }

    public static void deleteAchievement(AchievementEntryEntity achievement, FirebaseCallback callback){
        FirestoreRepository.getInstance()
                .collection(Api.ACCOUNTS_COLLECTION)
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){

                WriteBatch batch = FirestoreRepository.getInstance().batch();

                DocumentReference documentReference =
                        FirestoreRepository.getInstance()
                        .collection(Api.ACHIEVEMENTS_COLLECTION)
                        .document(achievement.getId());
                batch.delete(documentReference);

                for (DocumentSnapshot doc: task.getResult()){
                    DocumentReference docRef = doc.getReference()
                            .collection(Api.ACCOUNT_ACHIEVEMENT_COLLECTION)
                            .document(achievement.getId());
                    batch.delete(docRef);
                }

                batch.commit().addOnCompleteListener(task1 -> {
                    if (task.isSuccessful()){
                        callback.onSuccess(null);
                    } else {
                        callback.onFailure(task.getException());
                    }
                });

            } else {
                callback.onFailure(task.getException());
            }

        });
    }

    public static void getAllAchievements(FirebaseCallback callback){
        FirestoreRepository
                .getInstance()
                .collection(Api.ACHIEVEMENTS_COLLECTION)
                .get()
                .addOnCompleteListener(task -> {
                    callback.onFinish();
                    if (task.isSuccessful()){
                        List<AchievementEntryEntity> achievements = new ArrayList<>();
                        for(DocumentSnapshot doc: task.getResult()){
                            achievements.add(doc.toObject(AchievementEntryEntity.class));
                        }
                        callback.onSuccess(achievements);
                    } else {
                        callback.onFailure(task.getException());
                    }
                });
    }

    public static void getAllAchievementApprovalRequests(FirebaseCallback callback){
        FirestoreRepository
                .getInstance()
                .collection(ACHIEVEMENT_APPROVAL_REQUESTS_COLLECTION)
                .get()
                .addOnCompleteListener(task -> {
                    callback.onFinish();

                    if (task.isSuccessful()){
                        List<AchievementApprovalRequest> requests = new ArrayList<>();
                        for(DocumentSnapshot doc: task.getResult()){
                            requests.add(doc.toObject(AchievementApprovalRequest.class));
                        }
                        callback.onSuccess(requests);
                    } else {
                        callback.onFailure(task.getException());
                    }
                });
    }

    public static void addAchievementApprovalRequest(AchievementApprovalRequest request, FirebaseCallback callback){
        final DocumentReference achievementRef = FirestoreRepository
                .getInstance()
                .collection(Api.ACCOUNTS_COLLECTION)
                .document(FirestoreRepository.getCurrentUser().getEmail())
                .collection(Api.ACCOUNT_ACHIEVEMENT_COLLECTION)
                .document(request.getAchievementId());

        final DocumentReference requestRef = FirestoreRepository
                .getInstance()
                .collection(ACHIEVEMENT_APPROVAL_REQUESTS_COLLECTION)
                .document(request.getId());

        FirestoreRepository.getInstance().runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot achievementDoc = transaction.get(achievementRef);
                DocumentSnapshot requestDoc = transaction.get(requestRef);

                // check if there are an existing request
                if (requestDoc.exists()){
                    // abort if request is already approved
                    if (requestDoc.getString("status").equals(AchievementStatusTypes.ACCEPTED)){
                        return null;
                    }
                }

                // abort if achievement is already accepted
                if (achievementDoc.getString("status").equals(AchievementStatusTypes.ACCEPTED)){
                    return null;
                }

                transaction.set(requestRef, request, SetOptions.merge());
                transaction.update(achievementRef, "hasRequested", true);
                transaction.update(achievementRef, "requestedDate", request.getRequestDate());
                transaction.update(achievementRef, "status", request.getStatus());
                if (request.getUserDescription() != null){
                    transaction.update(achievementRef, "userDescription", request.getUserDescription());
                }

                return null;
            }
        }).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                callback.onFinish();
                if (task.isSuccessful()){
                    callback.onSuccess(null);
                } else {
                    callback.onFailure(task.getException());
                }
            }
        });
    }

    public static void approveAchievementRequest(AchievementApprovalRequest request, FirebaseCallback callback){
        final DocumentReference requestRef = FirestoreRepository
                .getInstance()
                .collection(ACHIEVEMENT_APPROVAL_REQUESTS_COLLECTION)
                .document(request.getId());

        final DocumentReference achieveRef = FirestoreRepository
                .getInstance()
                .collection(Api.ACCOUNTS_COLLECTION)
                .document(request.getUserEmail())
                .collection(Api.ACCOUNT_ACHIEVEMENT_COLLECTION)
                .document(request.getAchievementId());

        final DocumentReference accountRef = FirestoreRepository
                .getInstance()
                .collection(Api.ACCOUNTS_COLLECTION)
                .document(request.getUserEmail());

        final DocumentReference achievementsOverViewRef = FirestoreRepository
                .getInstance()
                .collection(Api.ACHIEVEMENTS_COLLECTION)
                .document(request.getAchievementId());

        FirestoreRepository.getInstance().runTransaction((Transaction.Function<Void>) transaction -> {
            DocumentSnapshot requestDoc = transaction.get(requestRef);
            DocumentSnapshot achievementDoc = transaction.get(achieveRef);
            DocumentSnapshot accountDoc = transaction.get(accountRef);
            DocumentSnapshot achievementOverViewDoc = transaction.get(achievementsOverViewRef);

            // check if already approved
            if (requestDoc.get("status").equals(AchievementStatusTypes.ACCEPTED)) return null;
            if (achievementDoc.get("status").equals(AchievementStatusTypes.ACCEPTED)) return null;

            Long newTotalPlayersCompleted = achievementOverViewDoc.getLong("totalPlayersCompleted") + 1L;
            Long newAchievementPoints = accountDoc.getLong("achievementPoints") + request.getAchievementPoints();

            // update achievement
            transaction.update(achieveRef, "status", AchievementStatusTypes.ACCEPTED);
            transaction.update(achieveRef, "completionDate", System.currentTimeMillis());
            transaction.update(achieveRef, "isCompleted", true);

            // update achievement overview of total players completed
            transaction.update(achievementsOverViewRef, "totalPlayersCompleted", newTotalPlayersCompleted);

            // update account achievement points
            transaction.update(accountRef, "achievementPoints", newAchievementPoints);

            // delete request
            transaction.delete(requestRef);

            return null;
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                callback.onFinish();
                callback.onSuccess(request);
            }
        }).addOnFailureListener(e -> {
            callback.onFinish();
            callback.onFailure(e);
        });
    }

    public static void declineAchievementRequest(AchievementApprovalRequest request, FirebaseCallback callback){
        final DocumentReference requestRef = FirestoreRepository
                .getInstance()
                .collection(ACHIEVEMENT_APPROVAL_REQUESTS_COLLECTION)
                .document(request.getId());

        final DocumentReference achieveRef = FirestoreRepository
                .getInstance()
                .collection(Api.ACCOUNTS_COLLECTION)
                .document(request.getUserEmail())
                .collection(Api.ACCOUNT_ACHIEVEMENT_COLLECTION)
                .document(request.getAchievementId());

        FirestoreRepository.getInstance().runTransaction((Transaction.Function<Void>) transaction -> {
            DocumentSnapshot requestDoc = transaction.get(requestRef);
            DocumentSnapshot achievementDoc = transaction.get(achieveRef);

            // check if already approved or declined
            String requestStatus = requestDoc.getString("status");
            String achieveStatus = achievementDoc.getString("status");

            if (requestStatus.equals(AchievementStatusTypes.ACCEPTED) ||
                    requestStatus.equals(AchievementStatusTypes.DECLINED) ||
                    achieveStatus.equals(AchievementStatusTypes.DECLINED) ||
                    achieveStatus.equals(AchievementStatusTypes.ACCEPTED)){
                return null;
            }

            // update achievement
            transaction.update(achieveRef, "status", AchievementStatusTypes.DECLINED);
            transaction.update(achieveRef, "completionDate", null);
            transaction.update(achieveRef, "isCompleted", false);

            // delete request
            transaction.delete(requestRef);

            return null;
        }).addOnCompleteListener(task -> {
            callback.onFinish();
            if (task.isSuccessful()){
                callback.onSuccess(null);
            } else {
                callback.onFailure(task.getException());
            }
        });
    }
}

