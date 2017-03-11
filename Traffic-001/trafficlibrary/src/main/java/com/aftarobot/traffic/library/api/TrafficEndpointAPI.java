package com.aftarobot.traffic.library.api;

import android.os.AsyncTask;
import android.util.Log;

import com.aftarobot.traffic.backend.trafficApi.TrafficApi;
import com.aftarobot.traffic.backend.trafficApi.model.FCMResponseDTO;
import com.aftarobot.traffic.backend.trafficApi.model.FCMUserDTO;
import com.aftarobot.traffic.backend.trafficApi.model.FCMessageDTO;
import com.aftarobot.traffic.backend.trafficApi.model.PayLoad;
import com.aftarobot.traffic.library.util.Constants;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;


/**
 * Created by aubreymalabie on 3/10/17.
 */

public class TrafficEndpointAPI {

    public interface TrafficEndpointListener {
        void onResponse(FCMResponseDTO response);

        void onError(String message);
    }

    private static final int
            SAVE_USER = 1,
            SEND_MESSAGE = 2,
            SEND_ADMIN_TOPIC = 3,
            SEND_OFFICERS_TOPIC = 4,
            SEND_DEPARTMENT_MESSAGE = 5;

    public void saveUser(FCMUserDTO user, TrafficEndpointListener listener) {
        new MTask(user, listener);
    }

    public void sendMessage(FCMessageDTO message, TrafficEndpointListener listener) {
        new MTask(message, listener).execute();
    }
    public void sendDepartmentMessage(String departmentID, PayLoad payLoad, TrafficEndpointListener listener) {
        new MTask(departmentID,payLoad, SEND_DEPARTMENT_MESSAGE);
    }
    public void sendOfficersMessage(String departmentID, PayLoad payLoad, TrafficEndpointListener listener) {
        new MTask(departmentID,payLoad, SEND_OFFICERS_TOPIC);
    }
    public void sendAdminsMessage(String departmentID, PayLoad payLoad, TrafficEndpointListener listener) {
        new MTask(departmentID,payLoad, SEND_ADMIN_TOPIC);
    }

    private class MTask extends AsyncTask<Void, Void, Integer> {

        TrafficApi trafficApi;
        FCMessageDTO fcMessage;
        FCMUserDTO user;
        TrafficEndpointListener listener;
        int type;
        FCMResponseDTO response;
        String departmentID;
        PayLoad payLoad;

        public MTask(String departmentID, PayLoad payLoad, int type) {
            this.departmentID = departmentID;
            this.payLoad = payLoad;
            this.type = type;
        }

        public MTask(FCMessageDTO fcMessage, TrafficEndpointListener listener) {
            this.fcMessage = fcMessage;
            this.listener = listener;
            type = SEND_MESSAGE;
        }

        public MTask(FCMUserDTO user, TrafficEndpointListener listener) {
            this.user = user;
            this.listener = listener;
            type = SAVE_USER;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            Log.d(TAG, "doInBackground: ................... fcm starting");
            if (trafficApi == null) {
                TrafficApi.Builder builder = new TrafficApi.Builder(AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(), null)
                        // options for running against local devappserver
                        // - 10.0.2.2 is localhost's IP address in Android emulator
                        // - turn off compression when running against local devappserver
                        .setRootUrl(Constants.APP_ENGINE_ROOT_URL)
                        .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                            @Override
                            public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                                //todo - set false when in production
                                abstractGoogleClientRequest.setDisableGZipContent(true);
                            }
                        });

                trafficApi = builder.build();
            }
            response = new FCMResponseDTO();
            try {
                switch (type) {
                    case SAVE_USER:
                        response = trafficApi.saveUser(user).execute();
                        break;
                    case SEND_MESSAGE:
                        response = trafficApi.sendMessage(fcMessage).execute();
                        break;
                    case SEND_ADMIN_TOPIC:
                        response = trafficApi.sendAdminsMessage(departmentID, payLoad).execute();
                        break;
                    case SEND_OFFICERS_TOPIC:
                        response = trafficApi.sendOfficersMessage(departmentID, payLoad).execute();
                        break;
                }
            } catch (Exception e) {
                Log.e(TAG, "doInBackground: fcm error", e);
                return 9;
            }


            return 0;
        }
        @Override
        protected void onPostExecute(Integer result) {
            Log.i(TAG, "onPostExecute: fcm responded");
            if (result > 0) {
                listener.onError("Failed to send FCM message");
            } else {
                listener.onResponse(response);
            }
        }
    }

    public static final String TAG = TrafficEndpointAPI.class.getSimpleName();
}
