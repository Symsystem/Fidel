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
            holder.address = (TextView) convertView.findViewById(R.id.pers_adress);
            holder.phoneNumber = (TextView) convertView.findViewById(R.id.pers_phoneNumber);
            holder.birthDate = (TextView) convertView.findViewById(R.id.pers_birthDate);
            holder.passeportValidity = (TextView) convertView.findViewById(R.id.pers_passeportValidity);

            convertView.setTag(holder);
        }

        else {
            holder = (ViewHolder) convertView.getTag();
        }

        Personne personne = mPersonneList.get(position);

        holder.name.setText("Nom : " + personne.getPrenom() + " " + personne.getNom());
        holder.address.setText(("Adresse : " + personne.getAddress() + System.getProperty("line.separator")
                + personne.getPostCode() + personne.getLocality() + System.getProperty("line.separator")
                + personne.getCountry()));
        holder.phoneNumber.setText("N° de téléphone : " + personne.getNumPhone());
        holder.birthDate.setText("Date de naissance : " + personne.getBirthDate());
        holder.passeportValidity.setText("Passeport valide jusqu'au " + personne.getPasseportValidity());

        return convertView;

    }

    private static class ViewHolder{
        TextView name;
        TextView address;
        TextView phoneNumber;
        TextView birthDate;
        TextView passeportValidity;
    }
}
