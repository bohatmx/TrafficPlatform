package com.aftarobot.traffic.library.api;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.aftarobot.traffic.library.App;
import com.aftarobot.traffic.library.data.FineDTO;
import com.google.gson.Gson;
import com.snappydb.DB;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by aubreymalabie on 12/15/16.
 */

public class FineCache {

    static DB snappydb;
    static final Gson gson = new Gson();
    public static final String FINE_KEY = "fineKey-";
    public static final String TAG = FineCache.class.getSimpleName();

    static void setSnappyDB(Context ctx) {
        snappydb = App.getSnappyDB(ctx);
    }

    public interface WriteListener {
        void onDataWritten();

        void onError(String message);
    }

    public interface ReadListener {
        void onDataRead(List<FineDTO> tickets);

        void onError(String message);
    }

    public static void addFines(List<FineDTO> fines, Context ctx, WriteListener listener) {
        setSnappyDB(ctx);
        for (FineDTO f: fines) {
            MTask task = new MTask(f, listener);
            task.execute();
        }
    }

    public static void addFine(FineDTO ticket, Context ctx, WriteListener listener) {
        setSnappyDB(ctx);
        MTask task = new MTask(ticket, listener);
        task.execute();
    }

    public static void getFines(Context ctx,ReadListener listener) {
        setSnappyDB(ctx);
        MTask task = new MTask(listener);
        task.execute();
    }


    static class MTask extends AsyncTask<Void, Void, Integer> {

        List<FineDTO> fines = new ArrayList<>();
        FineDTO fine;
        WriteListener writeListener;
        ReadListener readListener;
        String fineKey;
        int type;
        static final int ADD_FINE = 1, GET_FINES = 2;

        public MTask(FineDTO fine, WriteListener listener) {
            this.fine = fine;
            type = ADD_FINE;
            writeListener = listener;
        }

        public MTask(ReadListener listener) {
            type = GET_FINES;
            readListener = listener;
        }


        @Override
        protected Integer doInBackground(Void... voids) {
            try {
                switch (type) {
                    case ADD_FINE:
                        fine.setCacheDate(new Date().getTime());
                        snappydb.put(FINE_KEY + fine.getFineID(), gson.toJson(fine));
                        Log.i(TAG, "doInBackground: fine cached: ".concat(fine.getCode()));
                        break;
                    case GET_FINES:
                        String[] keys = snappydb.findKeys(FINE_KEY);
                        fines = new ArrayList<>();
                        for (String key : keys) {
                            String json2 = snappydb.get(key);
                            fines.add(gson.fromJson(json2, FineDTO.class));
                        }
                        Log.i(TAG, "doInBackground: found " + fines.size() + " fines");
                        break;


                }
            } catch (Exception e) {
                Log.e(TAG, "snappydb problem: ", e);
                return 9;
            }

            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {
            if (result > 0) {
                if (writeListener != null) {
                    writeListener.onError("Unable to process data cache");
                    return;
                }
                if (readListener != null) {
                    readListener.onError("Unable to read data cache");
                    return;
                }
                return;
            }
            switch (type) {
                case ADD_FINE:
                    if (writeListener != null)
                        writeListener.onDataWritten();
                    break;

                case GET_FINES:
                    if (readListener != null)
                        readListener.onDataRead(fines);
                    break;
            }
        }
    }


}
