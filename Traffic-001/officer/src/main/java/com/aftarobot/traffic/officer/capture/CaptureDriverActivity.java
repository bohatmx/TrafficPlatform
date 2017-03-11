package com.aftarobot.traffic.officer.capture;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aftarobot.traffic.library.api.TicketCache;
import com.aftarobot.traffic.library.data.PhotoDTO;
import com.aftarobot.traffic.library.data.ResponseBag;
import com.aftarobot.traffic.library.data.TicketDTO;
import com.aftarobot.traffic.officer.R;
import com.google.firebase.crash.FirebaseCrash;

import java.util.HashMap;
import java.util.UUID;

import es.dmoral.toasty.Toasty;

public class CaptureDriverActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView txtCount;
    ImageView iconCamera;
    TextInputEditText editFirst, editLast, editLicense, editRegistration;
    Button btnNext;
    public static final int REQUEST_CAMERA = 4436, REQUEST_FINES = 7764;
    TicketDTO ticket = new TicketDTO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_admin);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Traffic Officer");
        getSupportActionBar().setSubtitle("Driver Detail Capture");

        setup();

    }

    private void setup() {
        btnNext = (Button) findViewById(R.id.btnSend);
        editFirst = (TextInputEditText) findViewById(R.id.editFirstName);
        editLast = (TextInputEditText) findViewById(R.id.editLastName);
        editRegistration = (TextInputEditText) findViewById(R.id.editNumberPlate);
        editLicense = (TextInputEditText) findViewById(R.id.editLicenseNumber);

        iconCamera = (ImageView)findViewById(R.id.iconCamera);
        txtCount = (TextView) findViewById(R.id.txtCount);
        iconCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCamera();
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startFines();
            }
        });
    }

    private void startFines() {
        Intent m = new Intent(getApplicationContext(),CaptureFinesActivity.class);
        m.putExtra("ticket",ticket);
        startActivityForResult(m,REQUEST_FINES);
    }

    private void startCamera() {
        Intent m = new Intent(this,CameraActivity.class);
        startActivityForResult(m,REQUEST_CAMERA);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (resultCode == RESULT_OK) {
                    ResponseBag bag = (ResponseBag)data.getSerializableExtra("bag");
                    if (ticket.getPhotos() == null) {
                        ticket.setPhotos(new HashMap<String, PhotoDTO>());
                    }
                    for (PhotoDTO p: bag.getPhotos()) {
                        ticket.getPhotos().put(UUID.randomUUID().toString(),p);
                    }
                    txtCount.setText(String.valueOf(ticket.getPhotos().size()));
                    Log.i(TAG, "onActivityResult: pictures taken: " + bag.getPhotos().size());
                }  else {
                    Toasty.warning(this,"No pictures were taken", Toast.LENGTH_LONG, true).show();
                }
                break;
            case REQUEST_FINES:
                 if (resultCode == RESULT_OK) {
                     ticket = (TicketDTO)data.getSerializableExtra("ticket");
                     if (ticket != null) {
                         TicketCache.addTicket(ticket, getApplicationContext(), new TicketCache.WriteListener() {
                             @Override
                             public void onDataWritten() {
                                 Log.i(TAG, "onDataWritten: ticket has been safely cached");
                             }

                             @Override
                             public void onError(String message) {
                                 Log.e(TAG, "onError: ".concat(message) );
                                 FirebaseCrash.report(new Exception("Unable to cached ticket on disk"));
                             }
                         });
                     } else {
                         throw new RuntimeException("Things are royally fucked!");
                     }
                 }

                break;
        }
    }

    public static final String TAG = CaptureDriverActivity.class.getSimpleName();
}
