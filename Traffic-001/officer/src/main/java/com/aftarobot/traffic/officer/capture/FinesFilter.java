package com.aftarobot.traffic.officer.capture;

import android.widget.Filter;

import com.aftarobot.traffic.library.data.FineDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aubreymalabie on 3/11/17.
 */


class FinesFilter extends Filter {

    FinesFilterAdapter adapter;
    List<FineDTO> originalList;
    List<FineDTO> filteredList;

    public FinesFilter(FinesFilterAdapter adapter, List<FineDTO> originalList) {
        super();
        this.adapter = adapter;
        this.originalList = originalList;
        this.filteredList = new ArrayList<>();
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        filteredList.clear();
        final FilterResults results = new FilterResults();

        if (constraint == null || constraint.length() == 0) {
            filteredList.addAll(originalList);
        } else {
            final String filterPattern = constraint.toString().toLowerCase().trim();

            // Your filtering logic goes in here
            for (final FineDTO fine : originalList) {
                StringBuilder sb = new StringBuilder();
                sb.append(fine.getCode())
                        .append(" ") .append(fine.getRegulation())
                        .append(" ").append(fine.getSection())
                        .append(" ").append(fine.getCharge());
                String check = sb.toString();
                if (check.toLowerCase().contains(filterPattern)) {
                    filteredList.add(fine);
                }
            }
        }
        results.values = filteredList;
        results.count = filteredList.size();
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.filteredFines.clear();
        adapter.filteredFines.addAll((List) results.values);
        adapter.notifyDataSetChanged();

    }
}