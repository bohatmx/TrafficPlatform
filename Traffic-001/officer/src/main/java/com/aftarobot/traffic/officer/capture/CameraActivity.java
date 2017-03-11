package com.aftarobot.traffic.officer.capture;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialcamera.MaterialCamera;
import com.aftarobot.traffic.library.data.PhotoDTO;
import com.aftarobot.traffic.library.data.ResponseBag;
import com.aftarobot.traffic.officer.R;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CameraActivity extends AppCompatActivity {

    public static final int CAMERA_REQUEST = 9985, VIDEO_REQUEST = 7663;
    private ImageView iconCamera, iconDelete, iconVideo, image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);


        iconCamera = (ImageView)findViewById(R.id.iconCamera);
        iconDelete = (ImageView)findViewById(R.id.iconCancel);
        iconVideo = (ImageView)findViewById(R.id.iconVideo);
        image = (ImageView)findViewById(R.id.image);

        iconCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCamera();
            }
        });
        iconVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startVideoCamera();
            }
        });

    }
    private void startCamera() {
        new MaterialCamera(this)
                .stillShot()
                .start(CAMERA_REQUEST);
    }
     private void startVideoCamera() {
         new MaterialCamera(this)
                 .start(VIDEO_REQUEST);
     }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Received recording or error from MaterialCamera
        if (requestCode == CAMERA_REQUEST) {

            if (resultCode == RESULT_OK) {
                Uri uri = Uri.parse(data.getDataString());
                File file = new File(uri.getPath());
                display(file);
            } else if(data != null) {
                Exception e = (Exception) data.getSerializableExtra(MaterialCamera.ERROR_EXTRA);
                e.printStackTrace();
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
    private void display(File file) {
        Log.e(TAG, "display: image file ".concat(file.getAbsolutePath()).concat(""+file.length()) );

        Glide.with(this).load(file).into(image);
        PhotoDTO p = new PhotoDTO();
        p.setLocalFilePath(file.getAbsolutePath());
        files.add(p);
    }
    @Override
    public void onBackPressed() {
        if (!files.isEmpty()) {
            Intent m = new Intent();
            ResponseBag bag = new ResponseBag();
            bag.setPhotos(files);
            m.putExtra("bag",bag);
            setResult(RESULT_OK, m);
        } else {
            setResult(RESULT_CANCELED);
        }
        finish();
    }
    private List<PhotoDTO> files = new ArrayList<>();
    public static final String TAG = CameraActivity.class.getSimpleName();
}
