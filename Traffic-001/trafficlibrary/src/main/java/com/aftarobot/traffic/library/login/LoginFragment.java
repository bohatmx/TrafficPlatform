package com.aftarobot.traffic.library.login;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.aftarobot.traffic.library.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {


    public LoginFragment() {
        // Required empty public constructor
    }


    private View view;
    public Button btn;
    private LoginListener listener;

    public interface LoginListener {
        void onLoginRequired();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_login, container, false);
        btn = (Button) view.findViewById(R.id.btnLogin);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onLoginRequired();
            }
        });

        return view;
    }

    public void setListener(LoginListener listener) {
        this.listener = listener;
    }
}
