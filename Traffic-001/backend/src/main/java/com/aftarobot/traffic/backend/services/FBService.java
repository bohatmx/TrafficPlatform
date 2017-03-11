package com.aftarobot.traffic.backend.services;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by aubreymalabie on 11/13/16.
 */

public class FBService {

    private static final Logger log = Logger.getLogger(FBService.class.getName());

    //todo - create service file for traffic platform
    private static final String SERVICE_FILE = "WEB-INF/AftaRobot-Development-ce6192fe2044.json";

    public interface FireListener {
        void onResponse(String json);
        void onError(String message);
    }

    public void setFirebase(final FireListener listener)  {

        log.log(Level.WARNING, "Starting Firebase initialization .........");
        try {
            File file = new File(SERVICE_FILE);
            InputStream inputStream = new FileInputStream(file);
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setServiceAccount(inputStream)
                    .setDatabaseUrl("https://aftarobot-development.firebaseio.com//")
                    .build();
            try {
                FirebaseApp.initializeApp(options);
                FirebaseApp.getInstance();
                log.log(Level.WARNING, "Firebase on Google App Engine " +
                        "has been initialized. The admin SDK is now in the house!  "
                        + FirebaseApp.getInstance().getName());

            } catch (IllegalStateException err) {
                log.log(Level.WARNING, "IllegalStateException: ERROR IN FB", err);
            }

            FirebaseDatabase db = FirebaseDatabase.getInstance();
            DatabaseReference ref = db.getReference().child("landmarks");

            log.log(Level.WARNING,"Testing Firebase database read, using landmarks ...");
            log.log(Level.WARNING, "....Firebase database reference: " + ref.toString());
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String msg = "We can get data!!! Landmarks found in GAE FIREBASE test: "
                            + dataSnapshot.getChildrenCount();
                    Object document = dataSnapshot.getValue();
                    System.out.println(document);
                    log.log(Level.WARNING, msg);
                    listener.onResponse(msg);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    log.log(Level.SEVERE, databaseError.getMessage());
                    listener.onError(databaseError.getMessage());
                }
            });

        } catch (Exception error) {
            log.log(Level.SEVERE, "***** Error setting up firebase: ", error);
            listener.onError(error.getMessage());
        }

    }
}
