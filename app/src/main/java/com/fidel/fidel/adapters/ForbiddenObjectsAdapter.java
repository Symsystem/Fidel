package com.fidel.fidel.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fidel.fidel.R;
import com.fidel.fidel.classes.ForbiddenObjects;
import com.fidel.fidel.classes.Personne;

import java.util.List;

/**
 * Created by jeremyduchesne on 3/05/15.
 */
public class ForbiddenObjectsAdapter extends ArrayAdapter<ForbiddenObjects>{

    private Context mContext;
    private List<ForbiddenObjects> mObjectsList;

    public ForbiddenObjectsAdapter(Context context, List<ForbiddenObjects> objects) {
        super(context, R.layout.object_list_item, objects);

        mContext = context;
        mObjectsList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.object_list_item, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.objectNameId);

            convertView.setTag(holder);
        }

        else {
            holder = (ViewHolder) convertView.getTag();
        }

        ForbiddenObjects forbiddenObject = mObjectsList.get(position);
        holder.name.setText(forbiddenObject.getName());
        return convertView;

    }

    private static class ViewHolder{
        TextView name;
    }
}
