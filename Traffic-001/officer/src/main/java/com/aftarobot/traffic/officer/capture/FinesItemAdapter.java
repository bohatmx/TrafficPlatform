package com.aftarobot.traffic.officer.capture;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aftarobot.traffic.library.data.FineDTO;
import com.aftarobot.traffic.officer.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class FinesItemAdapter extends RecyclerView.Adapter<FinesItemAdapter.ViewHolder> {

    private List<FineDTO> fines = new ArrayList<>();

    private Context context;
    private FinesItemListener listener;
    public static final String TAG = FinesItemAdapter.class.getSimpleName();

    public FinesItemAdapter(List<FineDTO> fines, Context context, FinesItemListener listener) {
        this.fines = fines;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fine_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder h, int position) {
        final FineDTO f = fines.get(position);

        h.txtCode.setText(f.getCode());
        h.txtReg.setText(f.getRegulation());
        if (f.getSection().length() > 1) {
            h.txtReg.setText(f.getSection());
        }
        h.txtAmount.setText(df.format(f.getFine()));
        h.txtCharge.setText(f.getCharge());

        h.btnRemoveFine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onRemoveFineRequested(f);
            }
        });
        h.topLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (h.bottomLayout.getVisibility() == View.GONE) {
                    animateIn(h.bottomLayout);
                } else {
                    animateOut(h.bottomLayout);
                }
            }
        });
        h.txtCharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateOut(h.bottomLayout);
            }
        });

    }



    @Override
    public int getItemCount() {
        return fines.size();
    }


    protected class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private RelativeLayout topLayout;
        private RelativeLayout lay1;
        private TextView txtCode;
        private TextView txtReg;
        private TextView txtAmount;
        private RelativeLayout bottomLayout;
        private TextView txtCharge;
        private Button btnRemoveFine;

        public ViewHolder(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.image);
            topLayout = (RelativeLayout) view.findViewById(R.id.topLayout);
            lay1 = (RelativeLayout) view.findViewById(R.id.lay1);
            txtCode = (TextView) view.findViewById(R.id.txtCode);
            txtReg = (TextView) view.findViewById(R.id.txtReg);
            txtAmount = (TextView) view.findViewById(R.id.txtAmount);
            bottomLayout = (RelativeLayout) view.findViewById(R.id.bottomLayout);
            txtCharge = (TextView) view.findViewById(R.id.txtCharge);
            btnRemoveFine = (Button) view.findViewById(R.id.btnAddFine);
        }
    }
    public interface FinesItemListener {
        void onRemoveFineRequested(FineDTO fine);
    }
    private void animateIn(View view) {
        view.setVisibility(View.VISIBLE);
        view.setPivotY(0f);
        ObjectAnimator an = ObjectAnimator.ofFloat(view, "scaleY", 0.2f, 1.0f);
        an.setDuration(300);
        an.setInterpolator(new AccelerateDecelerateInterpolator());
        an.start();
    }

    private void animateOut(final View view) {
        view.setPivotY(0f);
        ObjectAnimator an = ObjectAnimator.ofFloat(view, "scaleY", 1.0f, 0.2f);
        an.setDuration(300);

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
    private static final DecimalFormat df = new DecimalFormat("###,###,###,###,###,###,##0.00");


}

