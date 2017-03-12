package com.aftarobot.traffic.officer.services;

import android.util.Log;

import com.aftarobot.traffic.library.api.DataAPI;
import com.aftarobot.traffic.library.data.PhotoDTO;
import com.aftarobot.traffic.library.util.Util;
import com.cloudinary.Cloudinary;
import com.google.firebase.crash.FirebaseCrash;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * This class manages the image upload to the Cloudinary CDN.
 * The upload runs in a Thread and returns its response via CDNUploaderListener
 * <p/>
 * Created by aubreyM on 15/06/08.
 */
public class CDNUploader {
    public interface CDNUploaderListener {
        void onFileUploaded(PhotoDTO photo);
        void onError(String message);
    }

    static CDNUploaderListener mListener;
    static final String LOG = CDNUploader.class.getSimpleName();
    public static final String
            API_KEY = "626742792878829",
            API_SECRET = "OS1lw143mGPbaVtV30GslVxwImw",
            CLOUD_NAME = "oneconnect-technologies";

    /**
     * Upload photo to CDN (Cloudinary at this date). On return of the CDN response, a call is made
     * to the backend to add the metadata of the photo to the backend database
     *
     * @param photo
     * @param uploaderListener
     * @see PhotoDTO
     */
    public static void uploadPhoto(final PhotoDTO photo, final CDNUploaderListener uploaderListener) {
        mListener = uploaderListener;
        Log.d(LOG, "##### ###################.........starting CDNUploader uploadFile: "
                + photo.getLocalFilePath());
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final long start = System.currentTimeMillis();
                Map config = new HashMap();
                config.put("cloud_name", CLOUD_NAME);
                config.put("api_key", API_KEY);
                config.put("api_secret", API_SECRET);

                Cloudinary cloudinary = new Cloudinary(config);
                File file = new File(photo.getLocalFilePath());
                Map map;
                try {
                    map = cloudinary.uploader().upload(file, config);
                    if (map != null) {
                        long end = System.currentTimeMillis();
                        Log.i(LOG, "----> photo uploaded: " + map.get("secure_url") + "\nelapsed upload time: "
                                + Util.getElapsed(start, end) + " seconds, bytes: " + map.get("bytes"));

                        photo.setUrl((String) map.get("secure_url"));
                        photo.setHeight((int) map.get("height"));
                        photo.setWidth((int) map.get("width"));
                        photo.setBytes((int) map.get("bytes"));
                        photo.setDateUploaded(new Date().getTime());

                        DataAPI dataAPI = new DataAPI();
                        Log.d(TAG, "uploadPhoto: calling dataAPI to save photo metadata");
                        photo.setLocalFilePath(null);
                        dataAPI.addPhoto(photo, new DataAPI.DataListener() {
                            @Override
                            public void onResponse(String key) {
                                Log.e("CDNUploader", "+++++++++++++ onResponse: photo added to firebase: " + key );
                                photo.setPhotoID(key);
                                uploaderListener.onFileUploaded(photo);
                            }

                            @Override
                            public void onError(String message) {
                                Log.e(TAG, "uploadPhoto onError: " + message );
                                uploaderListener.onError(message);
                            }
                        });

                    } else {
                        Log.e(TAG, "uploadPhoto: we have a fucking problem!" );
                        mListener.onError(ERROR_MESSAGE);
                    }

                } catch (Exception e) {
                    Log.e(LOG, "############# CDN upload Failed", e);
                    FirebaseCrash.report(e);
                    mListener.onError(ERROR_MESSAGE);
                }


            }
        });
        thread.start();
    }

    public static final String TAG = CDNUploader.class.getSimpleName();

    public static final String ERROR_MESSAGE = "Unable to upload photo to CDN", ERROR_DATABASE = "Unable to write photo metadata to Firebase" ;

}
