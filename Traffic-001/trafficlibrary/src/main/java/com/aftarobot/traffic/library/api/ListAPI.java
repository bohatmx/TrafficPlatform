package com.aftarobot.traffic.library.api;

import android.util.Log;

import com.aftarobot.traffic.library.data.DepartmentDTO;
import com.aftarobot.traffic.library.data.FineDTO;
import com.aftarobot.traffic.library.data.ResponseBag;
import com.aftarobot.traffic.library.data.UserDTO;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.aftarobot.traffic.library.api.DataAPI.DEPARTMENTS;
import static com.aftarobot.traffic.library.api.DataAPI.FINES;
import static com.aftarobot.traffic.library.api.DataAPI.USERS;

/**
 * Created by aubreymalabie on 2/23/17.
 */

public class ListAPI {
    public static final String TAG = ListAPI.class.getSimpleName();
    FirebaseDatabase db;
    public static final int MAXIMUM_ARRIVALS = 500, MAXIMUM_DEPARTURES = 500;

    public ListAPI() {
        this.db = FirebaseDatabase.getInstance();
    }

    public interface ListListener {
        void onResponse(ResponseBag bag);
        void onError(String message);
    }
    public void getDepartmentUsers(String departmentID, final ListListener listener) {

        DatabaseReference ref = db.getReference(USERS);
        Query q = ref.orderByChild("trafficDepartmentID").equalTo(departmentID);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<UserDTO> list = new ArrayList<>();
                for (DataSnapshot shot : dataSnapshot.getChildren()) {
                    Log.i(TAG, "getDepartmentUsers onDataChange: shot: " + shot);
                    try {
                        UserDTO v = shot.getValue(UserDTO.class);
                        list.add(v);
                    } catch (DatabaseException e) {
                        Log.e(TAG, "onDataChange: failed to parse user", e);
                    }

                }
                Log.d(TAG, "getCountries, addListenerForSingleValueEvent: onDataChange: users found: " + list.size());
                ResponseBag bag = new ResponseBag();
                Collections.sort(list);
                bag.setUsers(list);

                listener.onResponse(bag);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                long end = System.currentTimeMillis();
                listener.onError(databaseError.getMessage());
            }
        });

    }
    public void getDepartments(final ListListener listener) {

        DatabaseReference ref = db.getReference(DEPARTMENTS);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<DepartmentDTO> list = new ArrayList<>();
                for (DataSnapshot shot : dataSnapshot.getChildren()) {
                    Log.i(TAG, "getDepartments onDataChange: shot: " + shot);
                    try {
                        DepartmentDTO v = shot.getValue(DepartmentDTO.class);
                        list.add(v);
                    } catch (DatabaseException e) {
                        Log.e(TAG, "onDataChange: failed to parse dept", e);
                    }

                }
                Log.d(TAG, "getDepartments, addListenerForSingleValueEvent: onDataChange: depts found: " + list.size());
                ResponseBag bag = new ResponseBag();
                Collections.sort(list);
                bag.setDepartments(list);
                listener.onResponse(bag);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                long end = System.currentTimeMillis();
                listener.onError(databaseError.getMessage());
            }
        });

    }
    public void getFines(final ListListener listener) {

        DatabaseReference ref = db.getReference(FINES);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<FineDTO> list = new ArrayList<>();
                for (DataSnapshot shot : dataSnapshot.getChildren()) {
                    Log.i(TAG, "getFines onDataChange: shot: " + shot);
                    try {
                        FineDTO v = shot.getValue(FineDTO.class);
                        list.add(v);
                    } catch (DatabaseException e) {
                        Log.e(TAG, "onDataChange: failed to parse dept", e);
                    }

                }
                Log.d(TAG, "getFines, addListenerForSingleValueEvent: onDataChange: fines found: " + list.size());
                ResponseBag bag = new ResponseBag();
                bag.setFines(list);
                listener.onResponse(bag);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                long end = System.currentTimeMillis();
                listener.onError(databaseError.getMessage());
            }
        });

    }
}
