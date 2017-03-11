package com.aftarobot.traffic.library.util;

/**
 * Created by aubreymalabie on 12/26/16.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;

public class RealPathUtil {

    public static final String TAG = RealPathUtil.class.getSimpleName();

    @SuppressLint("NewApi")
    public static File getRealPathFromURI(Context context, Uri uri) {
        Log.d(TAG, ".........uri: " + uri.getPath());
        String filePath = "";
        if (DocumentsContract.isDocumentUri(context, uri)) {
            String wholeID = DocumentsContract.getDocumentId(uri);
            Log.w("wholeID? ", wholeID);
// Split at colon, use second item in the array
            String[] splits = wholeID.split(":");
            if (splits.length == 2) {
                String id = splits[1];

                String[] column = {MediaStore.Images.Media.DATA};
// where id is equal to
                String sel = MediaStore.Images.Media._ID + "=?";
                Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        column, sel, new String[]{id}, null);
                int columnIndex = cursor.getColumnIndex(column[0]);
                if (cursor.moveToFirst()) {
                    filePath = cursor.getString(columnIndex);
                }
                cursor.close();
            }
        } else {
            filePath = uri.getPath();
        }
        File f = new File(filePath);
        if (f.exists()) {
            Log.w(TAG, "getRealPathFromURI: file exists: " + f.getAbsolutePath()
            + " length: " + f.length());
        } else {
            Log.e(TAG, "getRealPathFromURI: FILE DOES NOT EXIST" );
        }
        Log.e(TAG, "................getRealPathFromURI: " + filePath);
        return f;
    }
}

