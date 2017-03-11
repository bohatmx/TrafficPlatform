package com.aftarobot.admin.departments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aftarobot.admin.R;
import com.aftarobot.traffic.library.data.DepartmentDTO;
import com.aftarobot.traffic.library.data.UserDTO;
import com.aftarobot.traffic.library.util.PagerFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DepartmentrFragmentListener} interface
 * to handle interaction events.
 * Use the {@link DepartmentListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DepartmentListFragment extends Fragment implements PagerFragment, DeptListContract.View {


    private DepartmentrFragmentListener mListener;
    private UserDTO user;
    private List<DepartmentDTO> departments = new ArrayList<>();
    private ImageView iconAdd;
    private TextView txtCount;
    private View view;
    private RecyclerView recyclerView;
    private DepartmentPresenter presenter;
    public static final String TAG = DepartmentListFragment.class.getSimpleName();

    public DepartmentListFragment() {
        // Required empty public constructor
    }

    public static DepartmentListFragment newInstance(UserDTO user) {
        DepartmentListFragment fragment = new DepartmentListFragment();
        Bundle args = new Bundle();
        args.putSerializable("user", user);
        fragment.setArguments(args);
        return fragment;
    }

    public static DepartmentListFragment newInstance() {
        DepartmentListFragment fragment = new DepartmentListFragment();
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
        Log.d(TAG, "onCreateView: ..........................");
        view = inflater.inflate(R.layout.fragment_dept_list, container, false);
        iconAdd = (ImageView) view.findViewById(R.id.iconAdd);
        txtCount = (TextView) view.findViewById(R.id.departments);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        LinearLayoutManager lm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(lm);
        presenter = new DepartmentPresenter(this);
        getDepartments();

        iconAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.addNewDepartment();
            }
        });

        return view;
    }

    private void getDepartments() {
        Log.d(TAG, "getDepartments: getting ........");
        presenter.getDepartments();
    }

    public void addDepartments(List<DepartmentDTO> list) {
        departments.addAll(0, list);
        Collections.sort(departments);
        departmentItemAdapter.notifyDataSetChanged();
        txtCount.setText(String.valueOf(departments.size()));

    }

    private DepartmentItemAdapter departmentItemAdapter;

    private void setList() {
        Log.w(TAG, "setList: ..............");
        departmentItemAdapter = new DepartmentItemAdapter(departments, getActivity(),
                new DepartmentItemAdapter.DepartmentListener() {
                    @Override
                    public void onDeactivationRequired(DepartmentDTO dept) {
                         mListener.deactivateDepartment(dept);
                    }

                    @Override
                    public void onNewUserRequired(DepartmentDTO dept) {
                        mListener.addNewUser(dept);
                    }
                });
        recyclerView.setAdapter(departmentItemAdapter);
        txtCount.setText(String.valueOf(departments.size()));

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DepartmentrFragmentListener) {
            mListener = (DepartmentrFragmentListener) context;
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
        return "Departments";
    }

    @Override
    public void onDepartmentAdded(String key) {

    }

    @Override
    public void onDepartmentsFound(List<DepartmentDTO> departments) {
        Log.i("DepartmentListFragment", "onDepartmentsFound: " + departments.size());
        this.departments = departments;
        setList();
    }

    @Override
    public void onError(String message) {

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
    public interface DepartmentrFragmentListener {
        void addNewDepartment();
        void addNewUser(DepartmentDTO department);
        void updateDepartment(DepartmentDTO department);

        void deactivateDepartment(DepartmentDTO department);
    }
}
