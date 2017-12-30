package com.refresh.chotusalesv1.ui.component;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.refresh.chotusalesv1.domain.salessettings.taxSettings;

import java.util.ArrayList;

/**
 * Created by Lenovo on 12/15/2017.
 */

public class taxSettingsAdapter extends ArrayAdapter<taxSettings> {

    private ArrayList<taxSettings> values;
    private Context con;

    public taxSettingsAdapter(@NonNull Context context, @LayoutRes int resource, ArrayList<taxSettings> value) {
        super(context, resource,value);
        con = context;
        values = value;
    }


    @Override
    public int getCount(){
        return values.size();
    }

    @Override
    public taxSettings getItem(int position){
        return values.get(position);//[position];
    }

    @Override
    public long getItemId(int position){
        return position;
    }


    // And the "magic" goes here
    // This is for the "passive" state of the spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
        TextView label = new TextView(con);
        label.setTextColor(Color.BLACK);
        // Then you can get the current item using the values array (Users array) and the current position
        // You can NOW reference each method you has created in your bean object (User class)
        label.setText(values.get(position).taxname);
        label.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);

        // And finally return your dynamic (or custom) view for each spinner item
        return label;
    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(con);
        label.setTextColor(Color.BLACK);
        label.setText(values.get(position).taxname);

        return label;
    }

}
