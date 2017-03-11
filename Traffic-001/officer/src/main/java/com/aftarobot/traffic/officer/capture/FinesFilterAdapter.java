package com.aftarobot.traffic.officer.capture;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.aftarobot.traffic.library.data.FineDTO;
import com.aftarobot.traffic.officer.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by aubreymalabie on 3/11/17.
 */


class FinesFilterAdapter extends ArrayAdapter<FineDTO> {

    public final List<FineDTO> fines;
    public List<FineDTO> filteredFines = new ArrayList<>();
    private LayoutInflater mInflater;
    public interface FilterAdapterListener {
        void onFilteredFines(List<FineDTO> filteredFines);
    }

    public FinesFilterAdapter(Context context, List<FineDTO> fines) {
        super(context, R.layout.auto_item, fines);
        this.fines = fines;
        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return filteredFines.size();
    }

    @Override
    public Filter getFilter() {
        return new FinesFilter(this, fines);
    }
    static class ViewHolderItem {
        TextView txtCode, txtReg,txtAmount;
        TextView txtCharge, txtStaff;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item from filtered list.
        FineDTO f = filteredFines.get(position);
        final ViewHolderItem item;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.auto_item, null);
            item = new ViewHolderItem();
            item.txtCode = (TextView) convertView.findViewById(R.id.txtCode);
            item.txtReg = (TextView) convertView.findViewById(R.id.txtReg);
            item.txtAmount = (TextView) convertView.findViewById(R.id.txtAmount);
            item.txtCharge = (TextView) convertView.findViewById(R.id.txtCharge);
            convertView.setTag(item);
        } else {
            item = (ViewHolderItem)convertView.getTag();
        }

        item.txtCode.setText(f.getCode());
        item.txtReg.setText(f.getRegulation());
        if (f.getSection().length() > 1) {
            item.txtReg.setText(f.getSection());
        }
        item.txtAmount.setText(df.format(f.getFine()));
        item.txtCharge.setText(f.getCharge());

        return convertView;

    }
     public List<FineDTO> getFilteredFines() {
         return filteredFines;
     }
    private static final DecimalFormat df = new DecimalFormat("###,###,###,###,###,###,##0.00");
}
