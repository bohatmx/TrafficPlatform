package com.aftarobot.admin.departments;

/**
 * Created by aubreymalabie on 3/10/17.
 */

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aftarobot.admin.R;
import com.aftarobot.traffic.library.data.DepartmentDTO;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DepartmentItemAdapter extends RecyclerView.Adapter<DepartmentItemAdapter.ViewHolder> {

    private List<DepartmentDTO> departments = new ArrayList<>();

    private Context context;
    private DepartmentListener listener;
    public static final String TAG = DepartmentItemAdapter.class.getSimpleName();

    public DepartmentItemAdapter(List<DepartmentDTO> departments, Context context, DepartmentListener listener) {
        this.departments = departments;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dept_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder h, int position) {
        Log.w(TAG, "onBindViewHolder: ........" + position );
        final DepartmentDTO m = departments.get(position);
        h.txtName.setText(m.getDepartmentName());
        h.txtcity.setText(m.getCityName());
        h.txtemail.setText(m.getEmail());
        h.btnDeactivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 listener.onDeactivationRequired(m);
            }
        });
        h.btnAddUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               listener.onNewUserRequired(m);
            }
        });
        animateIn(h.lay2);
    }



    @Override
    public int getItemCount() {
        return departments.size();
    }


    protected class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView image;
        private RelativeLayout lay1;
        private TextView txtName;
        private TextView txtcity, txtemail;
        private RelativeLayout lay2;
        private Button btnAddUsers;
        private Button btnDeactivate;

        public ViewHolder(View view) {
            super(view);
            image = (CircleImageView) view.findViewById(R.id.image);
            lay1 = (RelativeLayout) view.findViewById(R.id.lay1);
            txtName = (TextView) view.findViewById(R.id.txtName);
            txtcity = (TextView) view.findViewById(R.id.txtCity);
            txtemail = (TextView) view.findViewById(R.id.txtEmail);
            lay2 = (RelativeLayout) view.findViewById(R.id.lay2);
            btnAddUsers = (Button) view.findViewById(R.id.iconUpdate);
            btnDeactivate = (Button) view.findViewById(R.id.iconRemove);
            Log.d(TAG, "ViewHolder: view prepared");
        }
    }
    public interface DepartmentListener {
        void onDeactivationRequired(DepartmentDTO dept);
        void onNewUserRequired(DepartmentDTO dept);
    }
    private void animateIn(View view) {
        view.setVisibility(View.VISIBLE);
        view.setPivotY(0);
        ObjectAnimator an = ObjectAnimator.ofFloat(view, "scaleY", 0.5f, 1.0f);
        an.setDuration(300);
        an.setInterpolator(new AccelerateDecelerateInterpolator());
        an.start();
    }

    private void animateOut(final View view) {
        view.setPivotY(0f);
        ObjectAnimator an = ObjectAnimator.ofFloat(view, "scaleY", 1.0f, 0.2f);
        an.setDuration(200);

        an.setInterpolator(new AccelerateDecelerateInterpolator());
        an.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        an.start();

    }

}

