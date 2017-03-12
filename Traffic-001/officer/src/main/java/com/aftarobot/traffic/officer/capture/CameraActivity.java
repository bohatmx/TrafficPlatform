package com.aftarobot.traffic.officer.capture;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialcamera.MaterialCamera;
import com.aftarobot.traffic.library.data.PhotoDTO;
import com.aftarobot.traffic.library.data.ResponseBag;
import com.aftarobot.traffic.library.util.Constants;
import com.aftarobot.traffic.library.util.Util;
import com.aftarobot.traffic.officer.R;
import com.bumptech.glide.Glide;
import com.google.firebase.crash.FirebaseCrash;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CameraActivity extends AppCompatActivity {

    public static final int CAMERA_REQUEST = 9985, VIDEO_REQUEST = 7663;
    private ImageView iconCamera, iconDelete, iconVideo, image;
    private double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);


        iconCamera = (ImageView) findViewById(R.id.iconCamera);
        iconDelete = (ImageView) findViewById(R.id.iconCancel);
        iconVideo = (ImageView) findViewById(R.id.iconVideo);
        image = (ImageView) findViewById(R.id.image);

        latitude = getIntent().getDoubleExtra("latitude", 0.0);
        longitude = getIntent().getDoubleExtra("longitude", 0.0);

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
                photoFile = new File(uri.getPath());
                display();
            } else if (data != null) {
                Exception e = (Exception) data.getSerializableExtra(MaterialCamera.ERROR_EXTRA);
                e.printStackTrace();
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void display() {
        Glide.with(this).load(photoFile).into(image);
        new PhotoTask().execute();
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: ************** photos: " + files.size());
        if (!files.isEmpty()) {
            Intent m = new Intent();
            ResponseBag bag = new ResponseBag();
            bag.setPhotos(files);
            m.putExtra("bag", bag);
            setResult(RESULT_OK, m);
        } else {
            setResult(RESULT_CANCELED);
        }
        finish();
    }

    private List<PhotoDTO> files = new ArrayList<>();
    private File photoFile, currentThumbFile;
    public static final String TAG = CameraActivity.class.getSimpleName();

    class PhotoTask extends AsyncTask<Void, Void, Integer> {

        double latitude, longitude;

        /**
         * Scale the image to required size and delete the larger one
         *
         * @param voids
         * @return
         */
        @Override
        protected Integer doInBackground(Void... voids) {
            try {
                if (photoFile == null || photoFile.length() == 0) {
                    Log.e(TAG, "----->> photoFile is null or length 0, exiting");
                    return 99;
                } else {
                    Log.w(TAG, "## PhotoTask starting, photoFile length: "
                            + photoFile.length());
                }
                processFile();
            } catch (Exception e) {
                Log.e(TAG, "Camera file processing failed", e);
                return 9;
            }


            return 0;
        }


        protected void processFile() throws Exception {

            Log.d(TAG, "processFile: photoFile: " + photoFile.length());
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;
            options.outHeight = 768;
            options.outWidth = 1024;
            Bitmap bm = BitmapFactory.decodeFile(photoFile.getAbsolutePath(), options);
            getLog(bm, "main bitmap");

            if (bm.getWidth() > bm.getHeight()) {
                Log.d(TAG, "*** this image in landscape ...............................");
                bm = Util.rotateBitmap(bm);

            }
            getLog(bm, "scaled Bitmap");
            currentThumbFile = getFileFromBitmap(bm);

            //write exif data
            Util.writeLocationToExif(currentThumbFile.getAbsolutePath(), latitude, longitude);
            bm.recycle();
            Log.i(TAG, "## photo file length: " + getLength(currentThumbFile.length())
                    + ", original size: " + getLength(photoFile.length()));
            //write exif from original photo

        }

        protected File getFileFromBitmap(Bitmap bm)
                throws Exception {
            if (bm == null) throw new Exception();
            File file = null;
            try {
                File rootDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                if (rootDir == null) {
                    rootDir = Environment.getRootDirectory();
                }
                File imgDir = new File(rootDir, "traffic_app");
                if (!imgDir.exists()) {
                    imgDir.mkdir();
                }
                OutputStream outStream = null;
                file = new File(imgDir, "traffic" + System.currentTimeMillis() + ".jpg");

                outStream = new FileOutputStream(file);
                bm.compress(Bitmap.CompressFormat.JPEG, 80, outStream);
                outStream.flush();
                outStream.close();

            } catch (Exception e) {
                Log.e(TAG, "Failed to get file from bitmap", e);
            }
            Log.i(TAG, "getFileFromBitmap: size: " + file.length());
            return file;

        }

        private void getLog(Bitmap bm, String which) {
            if (bm == null) return;
            Log.e(TAG, which + " - bitmap: width: "
                    + bm.getWidth() + " height: "
                    + bm.getHeight() + " rowBytes: "
                    + bm.getRowBytes());
        }

        private String getLength(long num) {
            BigDecimal decimal = new BigDecimal(num).divide(new BigDecimal(1024), 2, BigDecimal.ROUND_HALF_UP);

            return "" + decimal.doubleValue() + " KB";
        }

        @Override
        protected void onPostExecute(Integer result) {
            Log.e(TAG, "onPostExecute completed image resizing, result: " + result.intValue());
            if (result > 0) {
                FirebaseCrash.report(new Exception("Unable to process image from camera"));
                showSnackBar("Unable to process photo","Bad Camera?", Constants.RED);
                return;
            }
            PhotoDTO p = new PhotoDTO();
            p.setLocalFilePath(currentThumbFile.getAbsolutePath());
            files.add(p);
            Glide.with(getApplicationContext()).load(currentThumbFile).into(image);
            showSnackBar("Phtoto taken, number of photos: " + files.size(),"OK",Constants.GREEN);
        }
    }
    Snackbar snackbar;

    public void showSnackBar(String title, String action, String color) {
        snackbar = Snackbar.make(iconCamera, title, Snackbar.LENGTH_INDEFINITE);
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
        snackbar = Snackbar.make(iconCamera, title, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }
}
