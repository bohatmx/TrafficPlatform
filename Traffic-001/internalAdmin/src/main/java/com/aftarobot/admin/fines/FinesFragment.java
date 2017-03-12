package com.aftarobot.admin.fines;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aftarobot.admin.R;
import com.aftarobot.traffic.library.data.FineDTO;
import com.aftarobot.traffic.library.util.PagerFragment;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FinesFragmentListener} interface
 * to handle interaction events.
 * Use the {@link FinesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FinesFragment extends Fragment implements PagerFragment{


    private FinesFragmentListener mListener;
    private FineDTO fine;

    public FinesFragment() {
        // Required empty public constructor
    }

    public static FinesFragment newInstance(FineDTO fine) {
        FinesFragment fragment = new FinesFragment();
        Bundle args = new Bundle();
        args.putSerializable("fine", fine);
        fragment.setArguments(args);
        return fragment;
    }
    public static FinesFragment newInstance() {
        FinesFragment fragment = new FinesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
           fine = (FineDTO) getArguments().getSerializable("fine");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fines, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FinesFragmentListener) {
            mListener = (FinesFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FinesFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public String getTitle() {
        return "Traffic Fines";
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface FinesFragmentListener {
        void onFineCreated();
    }
}
