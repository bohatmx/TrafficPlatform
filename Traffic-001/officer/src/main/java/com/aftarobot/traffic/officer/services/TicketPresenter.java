package com.aftarobot.traffic.officer.services;

import com.aftarobot.traffic.library.api.DataAPI;
import com.aftarobot.traffic.library.data.TicketDTO;

/**
 * Created by aubreymalabie on 3/11/17.
 */

public class TicketPresenter implements TicketContract.Presenter {
    TicketContract.View view;
    DataAPI api;

    public TicketPresenter(TicketContract.View view) {
        this.view = view;
        api = new DataAPI();
    }

    @Override
    public void addTicket(TicketDTO ticket) {
        api.addTicket(ticket, new DataAPI.DataListener() {
            @Override
            public void onResponse(String key) {
                view.onTicketAdded(key);
            }

            @Override
            public void onError(String message) {
               view.onError(message);
            }
        });
    }
}
