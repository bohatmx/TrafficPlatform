package com.aftarobot.traffic.officer.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.aftarobot.traffic.library.api.TicketCache;
import com.aftarobot.traffic.library.data.PhotoDTO;
import com.aftarobot.traffic.library.data.TicketDTO;
import com.google.firebase.crash.FirebaseCrash;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class TicketUploadService extends IntentService implements TicketContract.View {

    public static final String TAG = TicketUploadService.class.getSimpleName(),
            BROADCAST_TICKETS_UPLOADED = "COM.AFTAROBOT.TICKETS.BROADCAST";
    public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public TicketUploadService() {
        super("TicketUploadService");
    }

    private List<TicketDTO> tickets;
    private int index = 0;
    private TicketPresenter presenter;

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent: **********************");

        presenter = new TicketPresenter(this);
        TicketCache.getTickets(getApplicationContext(), new TicketCache.ReadListener() {
            @Override
            public void onDataRead(List<TicketDTO> list) {
                Log.w(TAG, "getTickets onDataRead: cached tickets: " + list.size());
                if (list.isEmpty()) {
                    Log.w(TAG, "onDataRead: no tickets were found in the cache. Quitting ..." );
                    return;
                }
                tickets = list;
                control();

            }

            @Override
            public void onError(String message) {
                Log.e(TAG, "onError: ".concat(message));
                FirebaseCrash.report(new Exception(message));
            }
        });
    }

    private void control() {
        if (index < tickets.size()) {
            uploadTicket(tickets.get(index));
        } else {
            uploadPhotos();
        }
    }

    private void uploadTicket(TicketDTO t) {
        Log.e(TAG, "uploadTicket: ".concat(gson.toJson(t)));
        ticket = t;
        presenter.addTicket(getCleanTicket(t));
    }

    private TicketDTO getCleanTicket(TicketDTO t) {
        TicketDTO m = new TicketDTO();
        m.setFirstName(t.getFirstName());
        m.setLastName(t.getLastName());
        m.setDepartmentID(t.getDepartmentID());
        m.setDepartmentName(t.getDepartmentName());
        m.setLicensePlate(t.getLicensePlate());
        m.setLicenseNumber(t.getLicenseNumber());
        m.setIdNumber(t.getIdNumber());
        m.setTicketNumber(t.getTicketNumber());
        m.setDate(t.getDate());
        m.setAddress(t.getAddress());
        m.setCalculatedAddress(t.getCalculatedAddress());
        m.setOfficerName(t.getOfficerName());
        m.setFines(t.getFines());
        m.setLatitude(t.getLatitude());
        m.setLongitude(t.getLongitude());

        return m;
    }

    private void uploadPhotos() {
        index = 0;
        for (TicketDTO t : uploadedTickets) {
            if (t.getPhotos() != null) {
                for (PhotoDTO p : t.getPhotos().values()) {
                    p.setTicketID(t.getTicketID());
                    photos.add(p);
                }
            } else {
                Log.d(TAG, "uploadPhotos: no photos in this ticket: ".concat(t.getTicketID()));
            }
        }
        controlPhotos();
    }

    List<PhotoDTO> photos = new ArrayList<>();

    private void controlPhotos() {
        if (index < photos.size()) {
            uploadPhoto(photos.get(index));
        } else {
            Log.i(TAG, "controlPhotos: TICKET UPLOAD COMPLETE. FUCKING A!!");
            broadcast();
        }
    }

    private void uploadPhoto(PhotoDTO p) {
        CDNUploader.uploadPhoto(p, new CDNUploader.CDNUploaderListener() {
            @Override
            public void onFileUploaded(PhotoDTO photo) {
                Log.w(TAG, "onFileUploaded: photo uploaded" + photo.getTicketID());
                index++;
                controlPhotos();
            }

            @Override
            public void onError(String message) {
                FirebaseCrash.report(new Exception(message));
                Log.e(TAG, "onError: ".concat(message));
            }
        });
    }

    private void broadcast() {
        Log.e(TAG, "broadcast: $$$$$$$$$$ end of ticket upload");

        LocalBroadcastManager bm = LocalBroadcastManager.getInstance(getApplicationContext());
        Intent m = new Intent(BROADCAST_TICKETS_UPLOADED);
        bm.sendBroadcast(m);
    }

    TicketDTO ticket;
    List<TicketDTO> uploadedTickets = new ArrayList<>();

    @Override
    public void onTicketAdded(String key) {
        if (ticket == null) return;
        ticket.setTicketID(key);
        uploadedTickets.add(ticket);
        Log.d(TAG, "onTicketAdded to Firebase: " + ticket.getTicketID());
        Log.w(TAG, "onTicketAdded: will delete from cache, localKey: ".concat(ticket.getLocalKey()));
        TicketCache.deleteTicket(ticket.getLocalKey(), getApplicationContext(), new TicketCache.WriteListener() {
            @Override
            public void onDataWritten() {
                Log.w(TAG, "onDataWritten: uploaded ticket deleted from cache");
                index++;
                control();
            }

            @Override
            public void onError(String message) {
                Log.e(TAG, "onError: ".concat(message));
                FirebaseCrash.report(new Exception("Unable to delete ticket from cache"));
            }
        });
    }

    @Override
    public void onError(String message) {
        FirebaseCrash.report(new Exception(message));
        Log.e(TAG, "onError: ".concat(message));
    }
}
