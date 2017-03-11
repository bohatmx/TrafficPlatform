package com.aftarobot.admin.users;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aftarobot.admin.R;
import com.aftarobot.traffic.library.data.UserDTO;
import com.aftarobot.traffic.library.util.PagerFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserFragmentListener} interface
 * to handle interaction events.
 * Use the {@link UserListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserListFragment extends Fragment implements PagerFragment{


    private UserFragmentListener mListener;
    private UserDTO user;
    private List<UserDTO> users = new ArrayList<>();
    private ImageView iconAdd;
    private TextView txtCount;
    private View view;
    private RecyclerView recyclerView;

    public UserListFragment() {
        // Required empty public constructor
    }

    public static UserListFragment newInstance(UserDTO user) {
        UserListFragment fragment = new UserListFragment();
        Bundle args = new Bundle();
        args.putSerializable("user", user);
        fragment.setArguments(args);
        return fragment;
    }
    public static UserListFragment newInstance() {
        UserListFragment fragment = new UserListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
           user = (UserDTO) getArguments().getSerializable("user");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_list, container, false);
        iconAdd = (ImageView)view.findViewById(R.id.iconAdd);
        txtCount = (TextView) view.findViewById(R.id.users);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        LinearLayoutManager lm = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(lm);

        iconAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }

    public void addUserToList(UserDTO user) {
        users.add(0,user);
    }
    private UserItemAdapter userItemAdapter;
    private void setList() {

        userItemAdapter = new UserItemAdapter(users, getActivity(), new UserItemAdapter.UserListener() {
            @Override
            public void onUpdateRequired(UserDTO user) {
                mListener.updateUser(user);
            }

            @Override
            public void onDeactivationRequired(UserDTO user) {
                mListener.deactivateUser(user);
            }
        });
        recyclerView.setAdapter(userItemAdapter);

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof UserFragmentListener) {
            mListener = (UserFragmentListener) context;
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
        return "User Management";
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
    public interface UserFragmentListener {
        void updateUser(UserDTO user);
        void deactivateUser(UserDTO user);
    }
}
