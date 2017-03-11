package com.aftarobot.traffic.officer.capture;

import com.aftarobot.traffic.library.api.ListAPI;
import com.aftarobot.traffic.library.data.ResponseBag;

/**
 * Created by aubreymalabie on 3/11/17.
 */

public class FinesPresenter implements FinesContract.Presenter {
    private FinesContract.View view;
    private ListAPI api;

    public FinesPresenter(FinesContract.View view) {
        this.view = view;
        api = new ListAPI();
    }

    @Override
    public void getFines() {
        api.getFines(new ListAPI.ListListener() {
            @Override
            public void onResponse(ResponseBag bag) {
                view.onFinesFound(bag.getFines());
            }

            @Override
            public void onError(String message) {
                view.onError(message);
            }
        });
    }
}
