package com.fidel.fidel.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fidel.fidel.R;
import com.fidel.fidel.classes.Bagage;

import java.util.ArrayList;

/**
 * Created by jeremyduchesne on 12/05/15.
 */
public class LuggagesAdapter extends ArrayAdapter<Bagage> {

    private Context mContext;
    private ArrayList<Bagage> mBagageList;

    public LuggagesAdapter(Context context, ArrayList<Bagage> objects) {
        super(context, R.layout.bagage_list_item, objects);

        mContext = context;
        mBagageList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.bagage_list_item, null);
            holder = new ViewHolder();
            holder.id = (TextView) convertView.findViewById(R.id.idId);
            holder.weight = (TextView) convertView.findViewById(R.id.weightId);

            convertView.setTag(holder);
        }

        else {
            holder = (ViewHolder) convertView.getTag();
        }

        Bagage bag = mBagageList.get(position);

        holder.id.setText("N°" + String.valueOf(bag.getId()));
        holder.weight.setText(bag.getWeight() + " Kg");

        return convertView;

    }

    private static class ViewHolder{
        TextView id;
        TextView weight;
    }
}
