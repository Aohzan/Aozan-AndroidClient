package me.bourgain.aozan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import me.bourgain.aozan.R;
import me.bourgain.aozan.bdd.Element;

/**
 * Created by Matthieu on 04/12/2016.
 */

public class ElementAdapter extends ArrayAdapter<Element> {
    public ElementAdapter(Context context, ArrayList<Element> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext()).inflate(R.layout.listview_element, parent, false);
        }

        Element item = getItem(position);

        if (item!= null) {
            TextView elementNom = (TextView)convertView.findViewById(R.id.elementNom);
            elementNom.setText(item.getNom());
        }

        return convertView;
    }
}