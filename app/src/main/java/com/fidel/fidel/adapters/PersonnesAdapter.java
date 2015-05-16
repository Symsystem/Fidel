package com.fidel.fidel.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.fidel.fidel.R;
import com.fidel.fidel.classes.Personne;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by sym on 3/05/15.
 */
public class PersonnesAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private ArrayList<String> mParentItems;
    private ArrayList<Personne> mChildItems;

    private LayoutInflater inflater;

    public PersonnesAdapter(Context context, LayoutInflater inflater, ArrayList<String> parentItems, ArrayList<Personne> childItems){
        mParentItems = parentItems;
        mChildItems = childItems;
        this.inflater = inflater;
        this.mContext = context;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {

            convertView = LayoutInflater.from(mContext).inflate(R.layout.pers_list_item, null);
            holder = new ViewHolder();
            holder.address = (TextView) convertView.findViewById(R.id.pers_adress);
            holder.phoneNumber = (TextView) convertView.findViewById(R.id.pers_phoneNumber);
            holder.birthDate = (TextView) convertView.findViewById(R.id.pers_birthDate);
            holder.passeportValidity = (TextView) convertView.findViewById(R.id.pers_passeportValidity);

            convertView.setTag(holder);
        }

        else {
            holder = (ViewHolder) convertView.getTag();
        }

        Personne personne = mChildItems.get(groupPosition);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'+'SSSS");
        Date birth = null;
        Date passeportDate = null;
        try {
            birth = sdf.parse(personne.getBirthDate());
            passeportDate = sdf.parse(personne.getPasseportValidity());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedBirth = new SimpleDateFormat("dd / MM / yyyy").format(birth);
        String formattedPasseportDate = new SimpleDateFormat("dd / MM / yyyy").format(passeportDate);

        holder.address.setText(("Adresse : " + System.getProperty("line.separator")
                + personne.getAddress() + System.getProperty("line.separator")
                + personne.getPostCode() + " " + personne.getLocality() + System.getProperty("line.separator")
                + personne.getCountry()));
        holder.phoneNumber.setText("N° de téléphone : " + personne.getNumPhone());
        holder.birthDate.setText("Date de naissance : " + formattedBirth);
        holder.passeportValidity.setText("Passeport valide jusqu'au " + formattedPasseportDate);

        return convertView;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.pers_name_item, null);
        }

        ((CheckedTextView) convertView).setText(mParentItems.get(groupPosition));
        ((CheckedTextView) convertView).setChecked(isExpanded);

        return convertView;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public int getGroupCount() {
        return mParentItems.size();
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    private static class ViewHolder{
        TextView address;
        TextView phoneNumber;
        TextView birthDate;
        TextView passeportValidity;
    }
}
