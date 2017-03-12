package com.aftarobot.traffic.officer.capture;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aftarobot.traffic.library.data.FineDTO;
import com.aftarobot.traffic.library.data.PhotoDTO;
import com.aftarobot.traffic.library.data.ResponseBag;
import com.aftarobot.traffic.library.data.TicketDTO;
import com.aftarobot.traffic.library.data.UserDTO;
import com.aftarobot.traffic.library.util.Constants;
import com.aftarobot.traffic.library.util.SharedUtil;
import com.aftarobot.traffic.officer.R;
import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.snapshot.LocationResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.firebase.crash.FirebaseCrash;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.UUID;

import es.dmoral.toasty.Toasty;

public class CaptureDriverActivity extends AppCompatActivity {

    GoogleApiClient googleApiClient;
    Toolbar toolbar;
    TextView txtCount;
    ImageView iconCamera;
    TextInputEditText editFirst, editLast, editLicense, editRegistration, editIDNumber;
    Button btnNext;
    public static final int REQUEST_CAMERA = 4436, REQUEST_FINES = 7764;
    TicketDTO ticket = new TicketDTO();
    Location location;
    static final int MAX_ATTEMPTS = 3;
    int count = 0;
    UserDTO user;

    private interface LocationListener {
        void onLocation(Location loc);

        void onError(String message);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_admin);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Traffic Officer");
        getSupportActionBar().setSubtitle("Driver Detail Capture");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Awareness.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        count = 0;
                        startLocation();
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        Log.w(TAG, "onConnectionSuspended: ");
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Log.e(TAG, "onConnectionFailed: Google API client failed");
                    }
                })
                .build();
        user = SharedUtil.getUser(this);
        setup();

    }

    @Override
    public void onResume() {
        super.onResume();

        if (googleApiClient.isConnected()) {
            Log.w(TAG, "onResume: ............. get location coordinates" );
            startLocation();
        }
    }
    private void startLocation() {
        getLocation(new LocationListener() {
            @Override
            public void onLocation(Location loc) {
                location = loc;
//                Toasty.success(getApplicationContext(),
//                        "Location coordinates found", Toast.LENGTH_SHORT, true).show();
            }

            @Override
            public void onError(String message) {
                if (count < MAX_ATTEMPTS) {
                    count++;
                    startLocation();
                } else {
                    Toasty.warning(getApplicationContext(),
                            "Unable to get location", Toast.LENGTH_LONG, true).show();
                    showSnackBar("Unable to get location","Not OK", Constants.RED);
                }

            }
        });
    }

    private void getLocation(final LocationListener listener) {

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Log.e(TAG, "..........getLocation: requesting location ............");
        Awareness.SnapshotApi.getLocation(googleApiClient)
                .setResultCallback(new ResultCallback<LocationResult>() {
                    @Override
                    public void onResult(@NonNull LocationResult locationResult) {
                        if (!locationResult.getStatus().isSuccess()) {
                            FirebaseCrash.report(new Exception(
                                    "Unable to get location: Awareness.SnapshotApi.getLocation"));
                            Log.e(TAG, "###########Could not get location.");
                            listener.onError("Unable to get location");
                            return;
                        }
                        if (locationResult.getLocation() == null) {
                            FirebaseCrash.report(new Exception(
                                    "Unable to get location: Awareness.SnapshotApi.getLocation"));
                            Log.e(TAG, "###########Could not get location.");
                            listener.onError("Unable to get location");
                            return;
                        }
                        location = locationResult.getLocation();
                        Log.i(TAG, ".................getLocation: snapshot: Lat: "
                                + location.getLatitude() + ", Lon: "
                                + location.getLongitude() + " accuracy: "
                                + location.getAccuracy());

                        listener.onLocation(location);

                    }
                });
    }
    private void setup() {
        btnNext = (Button) findViewById(R.id.btnSend);
        editFirst = (TextInputEditText) findViewById(R.id.editFirstName);
        editLast = (TextInputEditText) findViewById(R.id.editLastName);
        editRegistration = (TextInputEditText) findViewById(R.id.editNumberPlate);
        editLicense = (TextInputEditText) findViewById(R.id.editLicenseNumber);
        editIDNumber = (TextInputEditText) findViewById(R.id.editIDNumber);

        iconCamera = (ImageView)findViewById(R.id.iconCamera);
        txtCount = (TextView) findViewById(R.id.txtCount);
        iconCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCamera();
            }
        });
        btnNext.setText("Get Traffic fines");

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });
        ticket.setPhotos(new HashMap<String, PhotoDTO>());
        ticket.setTicketNumber(UUID.randomUUID().toString());
    }

    private void validate() {
          if (TextUtils.isEmpty(editFirst.getText())) {
              editFirst.setError("Enter the first name");
              return;
          }
        if (TextUtils.isEmpty(editLast.getText())) {
            editLast.setError("Enter the surname");
            return;
        }
        if (TextUtils.isEmpty(editRegistration.getText())) {
            editRegistration.setError("Enter the  number plate");
            return;
        }
        if (TextUtils.isEmpty(editIDNumber.getText())) {
            editIDNumber.setError("Enter the ID number");
            return;
        }
        setTicketData();
        startFines();
    }

    private void setTicketData() {
        ticket.setFirstName(editFirst.getText().toString());
        ticket.setLastName(editLast.getText().toString());
        ticket.setIdNumber(editIDNumber.getText().toString());
        ticket.setLicensePlate(editRegistration.getText().toString());

        ticket.setOfficerName(user.getFullName());
        ticket.setDepartmentID(user.getDepartmentID());
        ticket.setDepartmentName(user.getDepartmentName());
        ticket.setUserID(user.getUserID());

        if (!TextUtils.isEmpty(editLicense.getText()))  {
            ticket.setLicenseNumber(editLicense.getText().toString());
        }
        if (location != null) {
            ticket.setLatitude(location.getLatitude());
            ticket.setLongitude(location.getLongitude());
        }
    }

    private void startFines() {

        Log.d(TAG, "startFines: ".concat(gson.toJson(ticket)));
        Intent m = new Intent(getApplicationContext(),CaptureFinesActivity.class);
        m.putExtra("ticket",ticket);
        startActivityForResult(m,REQUEST_FINES);
        SharedUtil.saveTicket(ticket,this);
    }

    private void startCamera() {
        try {
            setTicketData();
        } catch (Exception e) {}
        Intent m = new Intent(this,CameraActivity.class);
        if (location != null) {
            m.putExtra("latitude",location.getLatitude()) ;
            m.putExtra("longitude",location.getLongitude()) ;
        }
        startActivityForResult(m,REQUEST_CAMERA);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult: ############## resultCode: " + resultCode);
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (resultCode == RESULT_OK) {
                    ResponseBag bag = (ResponseBag)data.getSerializableExtra("bag");
                    if (ticket.getPhotos() == null) {
                        ticket.setPhotos(new HashMap<String, PhotoDTO>());
                    }
                    Log.w(TAG, "onActivityResult: loading photos into ticket: " + bag.getPhotos().size() );
                    for (PhotoDTO p: bag.getPhotos()) {
                        p.setType(PhotoDTO.TRAFFIC_FINE);
                        ticket.getPhotos().put(UUID.randomUUID().toString(),p);
                    }
                    txtCount.setText(String.valueOf(ticket.getPhotos().size()));
                    Log.i(TAG, "onActivityResult: pictures taken: " + bag.getPhotos().size() + " " + gson.toJson(ticket));
                }  else {
                    Toasty.warning(this,"No pictures were taken", Toast.LENGTH_LONG, true).show();
                }
                break;
            case REQUEST_FINES:
                 if (resultCode == RESULT_OK) {
                     ticket = (TicketDTO)data.getSerializableExtra("ticket");
                     if (ticket != null) {
                            showSnackBar("Ticket has been successfully issued. Thank you!", "OK", Constants.GREEN);
                         resetFields();
                     } else {
                         throw new RuntimeException("Things are royally fucked!, ticket is null");
                     }
                 }

                break;
        }
    }

    private void resetFields() {
        editFirst.setText("");
        editLast.setText("");
        editLicense.setText("");
        editRegistration.setText("");
        editIDNumber.setText("");
        ticket = new TicketDTO();
        ticket.setFines(new HashMap<String, FineDTO>());
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart: +++++++++++++ starting googleApiClient.connect");
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.w(TAG, "onStop: stopping and disconnecting from googleApiClient" );
        googleApiClient.disconnect();
    }
    Snackbar snackbar;

    public void showSnackBar(String title, String action, String color) {
        snackbar = Snackbar.make(toolbar, title, Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(Color.parseColor(color));
        snackbar.setAction(action, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    public void showSnackBar(String title) {
        snackbar = Snackbar.make(toolbar, title, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }
    public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static final String TAG = CaptureDriverActivity.class.getSimpleName();
}
