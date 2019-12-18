package com.example.fitness_app.api;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.fitness_app.constrants.Globals;
import com.example.fitness_app.models.FirebaseCallback;
import com.example.fitness_app.activities.LoginActivity;
import com.example.fitness_app.interfaces.FirebaseCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FirestoreRepository
{
    private static final String TAG = "FirestoreRepository";

    public static FirebaseFirestore getInstance()
    {
        return FirebaseFirestore.getInstance();
    }

    public static FirebaseUser getCurrentUser()
    {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public static FirebaseAuth getFirebaseAuth()
    {
        return FirebaseAuth.getInstance();
    }

    private void readData(final FirebaseCallback firebaseCallback, String collection)
    {
        CollectionReference result = getInstance().collection(collection);

    }

    public static DocumentReference getDocumentReference(final String collection, final String document){
        return getInstance()
                .collection(collection)
                .document(document);
    }

    public static void getCollectionReference(final String collection, final FirebaseCallback firebaseCallback)
    {
        getInstance()
                .collection(collection)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task)
            {
                firebaseCallback.onFinish();
                if (task.isSuccessful())
                {
                    firebaseCallback.onSuccess(task.getResult());
                }
                else
                {
                    firebaseCallback.onFailure(((FirebaseFirestoreException)task.getException()).getCode());
                }
            }
        });
    }

    public static void updateField(final DocumentReference docRef, Map<String, Object> updates, final FirebaseCallback firebaseCallback){
        docRef.update(updates).addOnCompleteListener(task -> {
            firebaseCallback.onFinish();
            if (task.isSuccessful()){
                firebaseCallback.onSuccess(updates);
            } else {
                firebaseCallback.onFailure(((FirebaseFirestoreException)task.getException()).getCode());
            }
        });
    }

    public static void postObject(final String collection, final String document, final Object data, final FirebaseCallback firebaseCallback)
    {
        getInstance()
                .collection(collection)
                .document(document)
                .set(data).addOnCompleteListener(task -> {
                    firebaseCallback.onFinish();
                    if (task.isSuccessful())
                    {
                        firebaseCallback.onSuccess(data);
                        Log.i(TAG, "Successfully saved object: " + data + "in " + document + " within: " + collection);
                    }
                    else
                    {
                        firebaseCallback.onFailure(((FirebaseFirestoreException)task.getException()).getCode());
                        Log.w(TAG, "Failed posting object: " + data + " in: " + document + " within: " + collection);

                    }
                });
    }

    public static void postDocument(final String collection, final Object data, final FirebaseCallback firebaseCallback)
    {
        getInstance()
                .collection(collection).add(data)
                .addOnCompleteListener(task -> {
            firebaseCallback.onFinish();
            if (task.isSuccessful())
            {
                firebaseCallback.onSuccess(data);
                Log.i(TAG, "Successfully saved document: " + data + " to: " + collection);
            }
            else
            {
                firebaseCallback.onFailure(((FirebaseFirestoreException)task.getException()).getCode());
                Log.w(TAG, "Failed posting document: " + data + " to: " + collection);

            }
        });
    }

    public static void postObjectMerge(final String collection, final String document, final Object data, final FirebaseCallback firebaseCallback)
    {
        getInstance()
                .collection(collection)
                .document(document)
                .set(data, SetOptions.merge()).addOnCompleteListener(task -> {
            firebaseCallback.onFinish();
            if (task.isSuccessful())
            {
                firebaseCallback.onSuccess(data);
                Log.i(TAG, "Successfully saved object: " + data + "in " + document + " within: " + collection);
            }
            else
            {
                firebaseCallback.onFailure(((FirebaseFirestoreException)task.getException()).getCode());
                Log.w(TAG, "Failed posting object: " + data + " in: " + document + " within: " + collection);

                    }
                });
    }

    public static void postCurrentAccount()
    {
        postObject("accounts", Globals.email, Globals.userAccount, new FirebaseCallback()
        {
            @Override
            public void onSuccess(Object object)
            {
            }

            @Override
            public void onFailure(Exception e)
            {
            }

            @Override
            public void onFinish()
            {
            }
        });
    }

    /**
     * get all documents from a collection
     * every doc will be mapped to the specified class type
     * callback with a list of the specified class
     * @param collection to get documents from
     * @param currentClass class to map every entry to
     * @param callback callback on finish, failure with exception and success with a list of objects
     */
    public static void fetchAllDocumentsFromCollection(final String collection, final Class currentClass, final FirebaseCallback callback){
        getInstance()
                .collection(collection)
                .get()
                .addOnCompleteListener(task -> {
                    callback.onFinish();
                    if (task.isSuccessful()){
                        List<Object> list = new ArrayList<>();
                        for (DocumentSnapshot doc: task.getResult()) {
                            list.add(doc.toObject(currentClass));
                        }
                        callback.onSuccess(list);
                    } else {
                        callback.onFailure(task.getException());
                        Log.w(TAG, "Failed fetching all documents from collection: " + collection);
                    }
                });
    }



    public static void fetchObject(final String collection, final String document, final Class currentClass, final FirebaseCallback firebaseCallback)
    {
        getInstance()
            .collection(collection)
            .document(document).get().addOnCompleteListener(task -> {
                if (task.isSuccessful())
                {
                    Object object = task.getResult().toObject(currentClass);
                    if (object != null)
                    {
                        Log.i(TAG,"Successfully fetched object: " + object + " from collection: " + collection);
                    }
                    else
                    {
                        Log.w(TAG, "Failed fetching object: " + document + " from collection: " + collection);
                    }
                    firebaseCallback.onSuccess(object);
                }
                else
                {
                    firebaseCallback.onFailure(task.getException());
                    Log.w(TAG, "Failed fetching object: " + document + " from collection: " + collection);
                }
                firebaseCallback.onFinish();
            });
    }

    public static void fetchIDs(final String collection, final FirebaseCallback firebaseCallback)
    {
        getInstance().collection(collection).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task)
            {
                if (task.isSuccessful())
                {
                    List list = new ArrayList();
                    for (DocumentSnapshot ds: task.getResult())
                    {
                        list.add(ds.getId());
                    }
                    if (!list.isEmpty())
                    {
                        Log.i(TAG,"Successfully fetched IDs for collection: " + collection);
                    }
                    else
                    {
                        Log.w(TAG, "Failed fetching IDs within collection: " + collection);
                    }
                    firebaseCallback.onSuccess(list);
                }
                else
                {
                    firebaseCallback.onFailure(task.getException());
                    Log.w(TAG, "Failed fetching IDs within collection: " + collection);
                }
                firebaseCallback.onFinish();
            }
        });
    }

    public static void deleteID(final String collection, final String id, final FirebaseCallback firebaseCallback)
    {
        getInstance().collection(collection).document(id).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            firebaseCallback.onSuccess(null);
                            Log.i(TAG, "ID: " + id + " is deleted from collection: " + collection);
                        }
                        /*
                            Will never fail as this is not implemented for delete operations in Firestore yet:
                            https://stackoverflow.com/questions/53251138/firebase-firestore-returning-true-on-failed-document-delete
                         */
                        else
                        {
                            firebaseCallback.onFailure(task.getException());
                            Log.w(TAG, "Failed to delete ID:" + id + " from collection: " + collection);
                        }
                        firebaseCallback.onFinish();
                    }
                });
    }
}
