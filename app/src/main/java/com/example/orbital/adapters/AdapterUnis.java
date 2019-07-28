package com.example.orbital.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.orbital.R;

import java.util.ArrayList;

public class AdapterUnis extends ArrayAdapter {

    ArrayList<String> items;

    //Constructor for university adapters
    public AdapterUnis(Context context, int layout, ArrayList<String> items) {

        super(context,layout);
        this.items = items;

    }

    //Update method for
    public void update(ArrayList<String> results) {
        items = new ArrayList<>();
        items.addAll(results);
        notifyDataSetChanged();
    }

    public class ViewHolder {

        TextView textView;

    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row;
        row = convertView;
        ViewHolder viewHolder;

        if (row == null) {
            row = LayoutInflater.from(getContext()).inflate(R.layout.reviewlayout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textView = row.findViewById(R.id.uniNameTv);
            row.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) row.getTag();
        }

        viewHolder.textView.setText(items.get(position));

        return row;

    }
}
