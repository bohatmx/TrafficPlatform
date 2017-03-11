package com.aftarobot.traffic.officer.capture;

import com.aftarobot.traffic.library.data.FineDTO;

import java.util.List;

/**
 * Created by aubreymalabie on 3/11/17.
 */

public class FinesContract {
    public interface Presenter {
        void getFines();
    }
    public interface View {
        void onFinesFound(List<FineDTO> fines);
        void onError(String message);
    }
}
