package com.aftarobot.traffic.officer.services;

import com.aftarobot.traffic.library.data.TicketDTO;

/**
 * Created by aubreymalabie on 3/11/17.
 */

public class TicketContract {
    public interface Presenter {
        void addTicket(TicketDTO ticket);
    }
    public interface View {
        void onTicketAdded(String key);
        void onError(String message);
    }
}
