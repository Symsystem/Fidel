package com.fidel.fidel.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fidel.fidel.R;
import com.fidel.fidel.classes.Achat;

import java.util.List;

/**
 * Created by jeremyduchesne on 5/05/15.
 */

public class AchatsAdapter extends ArrayAdapter<Achat> {

        private Context mContext;
        private List<Achat> mAchatList;

        public AchatsAdapter(Context context, List<Achat> objects) {
            super(context, R.layout.pers_list_item, objects);

            mContext = context;
            mAchatList = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;

            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.pers_list_item, null);
                holder = new ViewHolder();
                holder.libelle = (TextView) convertView.findViewById(R.id.libelId);
                holder.prix = (TextView) convertView.findViewById(R.id.prixId);
                holder.quantite = (TextView) convertView.findViewById(R.id.quantiteId);

                convertView.setTag(holder);
            }

            else {
                holder = (ViewHolder) convertView.getTag();
            }

            Achat achat = mAchatList.get(position);

            holder.libelle.setText(achat.getLibelle());
            holder.prix.setText("" + achat.getPrix());
            holder.quantite.setText("" + achat.getQuantite());

            return convertView;

        }

        private static class ViewHolder{
            TextView libelle;
            TextView prix;
            TextView quantite;
        }
}

