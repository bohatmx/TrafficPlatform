package com.aftarobot.traffic.library.api;

import android.util.Log;

import com.aftarobot.traffic.library.data.DepartmentDTO;
import com.aftarobot.traffic.library.data.FineDTO;
import com.aftarobot.traffic.library.data.PhotoDTO;
import com.aftarobot.traffic.library.data.TicketDTO;
import com.aftarobot.traffic.library.data.UserDTO;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Random;

/**
 * Created by aubreymalabie on 2/23/17.
 */

public class DataAPI {
    FirebaseAuth auth;
    FirebaseDatabase db;

    public DataAPI() {
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
    }

    public static final String
            DEPARTMENTS = "departments",
            USERS = "users",
            CITIES = "cities",
            PHOTOS = "photos",  VIDEOS = "videos",
            COUNTRIES = "countries", FINES = "fines", TICKETS = "tickets",
            TAG = DataAPI.class.getSimpleName();

    public boolean isUserSignedIn(String email) {
        if (auth.getCurrentUser() == null) {
            return false;
        } else {
            return true;
        }
    }

    public interface DataListener {
        void onResponse(String key);

        void onError(String message);
    }

    public interface UserListener {
        void onResponse(UserDTO user);

        void onError(String message);
    }

    public interface DepartmentListener {
        void onResponse(DepartmentDTO department);

        void onError(String message);
    }

    static Random random = new Random(System.currentTimeMillis());

    public String getRandomPassword() {
        StringBuilder sb = new StringBuilder();

        sb.append(getRandomLetter());
        sb.append(getRandomLetter());
        sb.append(getRandomSymbol());
        sb.append(getRandomLetter());
        int count = random.nextInt(6);
        if (count < 3) count = 3;
        for (int i = 0; i < count; i++) {
            sb.append(random.nextInt(9));
        }
        sb.append(getRandomSymbol());
        sb.append(getRandomLetter());

        return sb.toString();
    }

    public void addFine(final FineDTO fine, final DataListener listener) {
        DatabaseReference ref = db.getReference(FINES);
        ref.push().setValue(fine, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    databaseReference.child("fineID").setValue(databaseReference.getKey());
                    Log.i(TAG, "***************** onComplete: fine added: " + fine.getCode());
                    fine.setFineID(databaseReference.getKey());
                    listener.onResponse(databaseReference.getKey());
                } else {
                    listener.onError(databaseError.getMessage());
                }
            }
        });

    }

    public void addTicket(final TicketDTO ticket, final DataListener listener) {
        Log.d(TAG, "addTicket: ticket to be added to firebase");
        DatabaseReference ref = db.getReference(TICKETS);
        ref.push().setValue(ticket, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    databaseReference.child("ticketID").setValue(databaseReference.getKey());
                    databaseReference.child("localKey").removeValue();
                    Log.i(TAG, "***************** onComplete: ticket added: " + databaseReference.getKey());
                    ticket.setTicketID(databaseReference.getKey());
                    listener.onResponse(databaseReference.getKey());
                } else {
                    listener.onError(databaseError.getMessage());
                }
            }
        });

    }

    private String getRandomLetter() {
        return letters[random.nextInt(letters.length - 1)];
    }

    private String getRandomSymbol() {
        return symbols[random.nextInt(symbols.length - 1)];
    }

    public void addUser(final UserDTO user, final DataListener listener) {

        getUserByEmail(user.getEmail(), new UserListener() {
            @Override
            public void onResponse(UserDTO u) {
                if (u != null) {
                    listener.onError("User already exists with email: ".concat(user.getEmail()));
                    return;
                }
            }

            @Override
            public void onError(String message) {
                if (!message.contains("User not found")) {
                    listener.onError(message);
                    return;
                }
                Log.d(TAG, "onError: user not found, adding user");
                writeUser(user, listener);
            }
        });

    }

    private void writeUser(final UserDTO user, final DataListener listener) {
        DatabaseReference ref = db.getReference(USERS);
        user.setPassword(getRandomPassword());
        ref.push().setValue(user, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    databaseReference.child("userID").setValue(databaseReference.getKey());
                    Log.i(TAG, "***************** onComplete: user added: " + user.getFullName());
                    user.setUserID(databaseReference.getKey());
                    listener.onResponse(databaseReference.getKey());
                } else {
                    listener.onError(databaseError.getMessage());
                }
            }
        });
    }

    public void getUserByEmail(final String email, final UserListener listener) {
        Log.d(TAG, "################## getUserByEmail: find user by mail: " + email);
        DatabaseReference usersRef = db.getReference(USERS);
        Query q = usersRef.orderByChild("email").equalTo(email);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(TAG, "onDataChange: getUser: dataSnapshot:" + dataSnapshot);
                if (dataSnapshot.getValue() == null) {
                    Log.e(TAG, "onDataChange: getUser: no users found for email: " + email);
                    listener.onError("User not found");
                    return;
                }
                Log.w(TAG, "onDataChange: getUser: users found for email: "
                        + dataSnapshot.getChildrenCount());
                for (DataSnapshot shot : dataSnapshot.getChildren()) {
                    UserDTO u = shot.getValue(UserDTO.class);
                    Log.e(TAG, "********* onDataChange: getUser:".concat(u.getFullName()));
                    listener.onResponse(u);
                    return;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onError(databaseError.getMessage());
            }
        });
    }

    public void addDepartment(final DepartmentDTO dept, final DataListener listener) {
        getDepartmentByName(dept.getDepartmentName(), new DepartmentListener() {
            @Override
            public void onResponse(DepartmentDTO department) {
                listener.onError("Department already exists");
            }

            @Override
            public void onError(String message) {
                if (!message.contains("Department not found")) {
                    listener.onError(message);
                    return;
                }
                writeDepartment(dept, listener);
            }
        });

    }

    private void writeDepartment(final DepartmentDTO dept, final DataListener listener) {
        DatabaseReference ref = db.getReference(DEPARTMENTS);
        ref.push().setValue(dept, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    databaseReference.child("departmentID").setValue(databaseReference.getKey());
                    Log.i(TAG, "***************** onComplete: department added: " + dept.getDepartmentID());
                    dept.setDepartmentID(databaseReference.getKey());
                    listener.onResponse(databaseReference.getKey());
                } else {
                    listener.onError(databaseError.getMessage());
                }

            }
        });
    }

    public void getDepartmentByName(final String name, final DepartmentListener listener) {
        Log.d(TAG, "################## getUserByEmail: find dept by mail: " + name);
        DatabaseReference usersRef = db.getReference(DEPARTMENTS);
        Query q = usersRef.orderByChild("departmentName").equalTo(name);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(TAG, "onDataChange: getDepartmentByName: dataSnapshot:" + dataSnapshot);
                if (dataSnapshot.getValue() == null) {
                    Log.e(TAG, "onDataChange: getDepartmentByName: no depts found for name: " + name);
                    listener.onError("Department not found");
                    return;
                }
                Log.w(TAG, "onDataChange: getDepartmentByName: depts found for name: "
                        + dataSnapshot.getChildrenCount());
                for (DataSnapshot shot : dataSnapshot.getChildren()) {
                    DepartmentDTO u = shot.getValue(DepartmentDTO.class);
                    Log.e(TAG, "********* onDataChange: getDepartmentByName:".concat(u.getDepartmentName()));
                    listener.onResponse(u);
                    return;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onError(databaseError.getMessage());
            }
        });
    }

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public void addPhoto(final PhotoDTO photo, final DataListener listener) {
        Log.d(TAG, "....................addPhoto: \n" + GSON.toJson(photo));
        DatabaseReference photosRef;
        switch (photo.getType()) {
            case PhotoDTO.TRAFFIC_FINE:
                photosRef = db.getReference(TICKETS)
                        .child(photo.getTicketID())
                        .child(PHOTOS);
                Log.d(TAG, ".....addPhoto: adding traffic photo: "
                        + photosRef.getRef().toString());
                break;

            case PhotoDTO.USER:
                photosRef = db.getReference(USERS)
                        .child(photo.getUserID())
                        .child(PHOTOS);
                Log.d(TAG, ".....addPhoto: adding user photo: "
                        + photosRef.getRef().toString());
                break;
            default:
                listener.onError("@@@@@@@@@@@@@ Photo must have a proper type");
                return;
        }

        photosRef.push().setValue(photo, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, final DatabaseReference resultRef) {
                if (databaseError == null) {
                    Log.i(TAG, "------------- onComplete: photo  added: type: "
                            + photo.getType()
                            + " url: " + photo.getUrl() + "\n" + resultRef.getRef().toString());
                    photo.setPhotoID(resultRef.getKey());
                    resultRef.child("photoID").setValue(resultRef.getKey());
                    if (listener != null)
                        listener.onResponse(resultRef.getKey());

                } else {
                    if (listener != null)
                        listener.onError(databaseError.getMessage());
                }

            }
        });
    }
    public void addVideo(final PhotoDTO photo, final DataListener listener) {
        Log.d(TAG, "....................addPhoto: \n" + GSON.toJson(photo));
        DatabaseReference photosRef;
        switch (photo.getType()) {
            case PhotoDTO.TRAFFIC_FINE:
                photosRef = db.getReference(TICKETS)
                        .child(photo.getTicketID())
                        .child(PHOTOS);
                Log.d(TAG, ".....addPhoto: adding traffic photo: "
                        + photosRef.getRef().toString());
                break;

            case PhotoDTO.USER:
                photosRef = db.getReference(USERS)
                        .child(photo.getUserID())
                        .child(PHOTOS);
                Log.d(TAG, ".....addPhoto: adding user photo: "
                        + photosRef.getRef().toString());
                break;
            default:
                listener.onError("@@@@@@@@@@@@@ Photo must have a proper type");
                return;
        }

        photosRef.push().setValue(photo, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, final DatabaseReference resultRef) {
                if (databaseError == null) {
                    Log.i(TAG, "------------- onComplete: photo  added: type: "
                            + photo.getType()
                            + " url: " + photo.getUrl() + "\n" + resultRef.getRef().toString());
                    photo.setPhotoID(resultRef.getKey());
                    resultRef.child("photoID").setValue(resultRef.getKey());
                    if (listener != null)
                        listener.onResponse(resultRef.getKey());

                } else {
                    if (listener != null)
                        listener.onError(databaseError.getMessage());
                }

            }
        });
    }


    private String[] letters = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k",
            "m", "n", "p", "q", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "L", "M", "N", "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z"};
    private String[] symbols = {"*", "#", "^", "%"};
}
