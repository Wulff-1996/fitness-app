package com.example.fitness_app.services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.fitness_app.models.FirebaseCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
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

    public static void postObject(String collection, String document, Object data)
    {
        try
        {
            getInstance()
                    .collection(collection)
                    .document(document)
                    .set(data);
            Log.i(TAG, "Successfully saved object: " + data + "in " + document + " within: " + collection);
        }
        catch (Exception e)
        {
            Log.w(TAG, "Failed posting object: " + data + " in: " + document + " within: " + collection);
        }

    }

    public static void fetchObject(final String collection, final String document, final Class currentClass, final FirebaseCallback firebaseCallback)
    {
        DocumentReference result = getInstance().collection(collection).document(document);
        result.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
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
                    firebaseCallback.onCallback(object);
                }
                else
                {
                    Log.w(TAG, "Failed fetching object: " + document + " from collection: " + collection);
                }
            }
        });
    }

    /*
    Example of fetching quest object from activity:
    Firestore.fetchObject("quests", "AadwaDbK2BURPvCUhmtABG1vul", Quest.class, new FirebaseCallback()
    {
        @Override
        public void onCallback(Object object)
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
                    firebaseCallback.onCallback(list);
                }
                else
                {
                    Log.w(TAG, "Failed fetching IDs within collection: " + collection);
                }
            }
        });

        /*
        try
        {
            System.out.println("FETCHING DOCUMENTLIST: " + getInstance().collection(collection));
            return getInstance().collection(collection);
        }
        catch (Exception e)
        {
            Log.w(TAG, "Failed fetching documents for: " + collection);
        }
         */
    }
}
