package com.aftarobot.admin.users;

/**
 * Created by aubreymalabie on 3/10/17.
 */

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aftarobot.admin.R;
import com.aftarobot.traffic.library.data.UserDTO;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserItemAdapter extends RecyclerView.Adapter<UserItemAdapter.ViewHolder> {

    private List<UserDTO> users = new ArrayList<>();

    private Context context;
    private UserListener listener;
    private LayoutInflater layoutInflater;

    public UserItemAdapter(List<UserDTO> users, Context context, UserListener listener) {
        this.users = users;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder h, int position) {

        final UserDTO m = users.get(position);
        h.txtName.setText(m.getFullName());
        h.txtEmail.setText(m.getEmail());
        h.iconRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 listener.onDeactivationRequired(m);
            }
        });
        h.iconUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               listener.onUpdateRequired(m);
            }
        });
        animateIn(h.lay2);
    }



    @Override
    public int getItemCount() {
        return 0;
    }


    protected class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView image;
        private RelativeLayout lay1;
        private TextView txtName;
        private TextView txtEmail;
        private RelativeLayout lay2;
        private ImageView iconUpdate;
        private ImageView iconRemove;

        public ViewHolder(View view) {
            super(view);
            image = (CircleImageView) view.findViewById(R.id.image);
            lay1 = (RelativeLayout) view.findViewById(R.id.lay1);
            txtName = (TextView) view.findViewById(R.id.txtName);
            txtEmail = (TextView) view.findViewById(R.id.txtEmail);
            lay2 = (RelativeLayout) view.findViewById(R.id.lay2);
            iconUpdate = (ImageView) view.findViewById(R.id.iconUpdate);
            iconRemove = (ImageView) view.findViewById(R.id.iconRemove);
        }
    }
    public interface UserListener {
        void onUpdateRequired(UserDTO user);
        void onDeactivationRequired(UserDTO user);
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

