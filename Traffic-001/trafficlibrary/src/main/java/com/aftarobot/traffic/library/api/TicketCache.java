package com.aftarobot.traffic.library.api;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.aftarobot.traffic.library.App;
import com.aftarobot.traffic.library.data.TicketDTO;
import com.google.gson.Gson;
import com.snappydb.DB;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aubreymalabie on 12/15/16.
 */

public class TicketCache {

    static DB snappydb;
    static final Gson gson = new Gson();
    public static final String TICKET_KEY = "ticketKey-";
    public static final String TAG = TicketCache.class.getSimpleName();

    static void setSnappyDB(Context ctx) {
        snappydb = App.getSnappyDB(ctx);
    }

    public interface WriteListener {
        void onDataWritten();

        void onError(String message);
    }

    public interface ReadListener {
        void onDataRead(List<TicketDTO> tickets);

        void onError(String message);
    }

    public static void addTicket(TicketDTO ticket, Context ctx, WriteListener listener) {
        setSnappyDB(ctx);
        MTask task = new MTask(ticket, listener);
        task.execute();
    }

    public static void getTickets(Context ctx,ReadListener listener) {
        setSnappyDB(ctx);
        MTask task = new MTask(listener);
        task.execute();
    }

    public static void deleteTicket(String ticketKey, Context ctx, WriteListener listener) {
        setSnappyDB(ctx);
        MTask task = new MTask(ticketKey, listener);
        task.execute();
    }

    static class MTask extends AsyncTask<Void, Void, Integer> {

        List<TicketDTO> tickets = new ArrayList<>();
        TicketDTO ticket;
        WriteListener writeListener;
        ReadListener readListener;
        String ticketKey;
        int type;
        static final int ADD_TICKET = 1, GET_TICKETS = 2, DELETE_TICKET = 3;

        public MTask(TicketDTO ticket, WriteListener listener) {
            this.ticket = ticket;
            type = ADD_TICKET;
            writeListener = listener;
        }

        public MTask(ReadListener listener) {
            type = GET_TICKETS;
            readListener = listener;
        }

        public MTask(String ticketKey, WriteListener listener) {
            this.ticketKey = ticketKey;
            type = DELETE_TICKET;
            writeListener = listener;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            try {
                switch (type) {
                    case ADD_TICKET:
                        ticket.setLocalKey(TICKET_KEY + ticket.getLocalKey());
                        String json1 = gson.toJson(ticket);
                        snappydb.put(ticket.getLocalKey(), json1);
                        Log.i(TAG, "doInBackground: ticket cached");
                        break;
                    case GET_TICKETS:
                        String[] keys = snappydb.findKeys(TICKET_KEY);
                        tickets = new ArrayList<>();
                        for (String key : keys) {
                            String json2 = snappydb.get(key);
                            tickets.add(gson.fromJson(json2, TicketDTO.class));
                        }
                        Log.i(TAG, "doInBackground: found " + tickets.size() + " tickets");
                        break;
                    case DELETE_TICKET:
                        try {
                            String json3 = snappydb.get(ticketKey);
                            snappydb.del(ticketKey);
                            Log.e(TAG, "doInBackground: ticket deleted from cache");
                        } catch (Exception e) {
                            Log.w(TAG, "doInBackground: no ticket found for delete, ignore" );
                        }
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
                case ADD_TICKET:
                    if (writeListener != null)
                        writeListener.onDataWritten();
                    break;
                case DELETE_TICKET:
                    if (writeListener != null)
                        writeListener.onDataWritten();
                    break;
                case GET_TICKETS:
                    if (readListener != null)
                        readListener.onDataRead(tickets);
                    break;
            }
        }
    }


}
