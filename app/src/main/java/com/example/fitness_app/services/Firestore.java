package com.example.fitness_app.services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.fitness_app.models.FirebaseCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Firestore
{
    private static final String TAG = "Firestore";

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

    public static void postObject(final String collection, final String document, final Object data, final FirebaseCallback firebaseCallback)
    {
        getInstance()
                .collection(collection)
                .document(document)
                .set(data).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
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
                firebaseCallback.onFinish();
            }
        });
    }

    public static void fetchObject(final String collection, final String document, final Class currentClass, final FirebaseCallback firebaseCallback)
    {
        getInstance()
            .collection(collection)
            .document(document).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task)
            {
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
                    firebaseCallback.onFailure(((FirebaseFirestoreException)task.getException()).getCode());
                    Log.w(TAG, "Failed fetching object: " + document + " from collection: " + collection);
                }
                firebaseCallback.onFinish();
            }
        });
    }

    /*
    Example of fetching quest object from activity:
    Firestore.fetchObject("quests", "AadwaDbK2BURPvCUhmtABG1vul", Quest.class, new FirebaseCallback()
    {
        @Override
        public void onSuccess(Object object)
        {
            if (object != null)
            {
                // use object
            }
            else
            {
                // do something
            }
        }
    });
     */

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
                    firebaseCallback.onFailure(((FirebaseFirestoreException)task.getException()).getCode());
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
                            firebaseCallback.onFailure(((FirebaseFirestoreException)task.getException()).getCode());
                            Log.w(TAG, "Failed to delete ID:" + id + " from collection: " + collection);
                        }
                        firebaseCallback.onFinish();
                    }
                });
    }
}
