package com.fidel.fidel.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fidel.fidel.R;
import com.fidel.fidel.classes.Personne;

import java.util.List;

/**
 * Created by sym on 3/05/15.
 */
public class PersonnesAdapter extends ArrayAdapter<Personne>{

    private Context mContext;
    private List<Personne> mPersonneList;

    public PersonnesAdapter(Context context, List<Personne> objects) {
        super(context, R.layout.pers_list_item, objects);

        mContext = context;
        mPersonneList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.pers_list_item, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.pers_name);
            convertView.setTag(holder);
        }

        else {
            holder = (ViewHolder) convertView.getTag();
        }

        Personne personne = mPersonneList.get(position);

        holder.name.setText("Nom : " + personne.getPrenom() + " " + personne.getNom());

        return convertView;

    }

    private static class ViewHolder{
        TextView name;

    }
}
