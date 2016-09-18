package com.learning.sukhu.newsapppoc;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by quocnguyen on 03/08/2016.
 */
public class CustomListAdapter extends ArrayAdapter<Sources> {

    ArrayList<Sources> sources;
    Context context;
    int resource;
    String pref;

    public CustomListAdapter(Context context, int resource, ArrayList<Sources> sources, String pref) {
        super(context, resource, sources);
        this.sources = sources;
        this.context = context;
        this.resource = resource;
        this.pref = pref;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) getContext()
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null){
            if(pref.equals("GRID")){
                convertView = layoutInflater.inflate(R.layout.custome_grid_layout, null, true);
            }else{
                convertView = layoutInflater.inflate(R.layout.custom_list_layout, null, true);
            }
        }
        Sources sources = getItem(position);

        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
        Picasso.with(context).load(sources.getUrlSize()).into(imageView);

        TextView txtName = (TextView) convertView.findViewById(R.id.sourcesList);
        txtName.setText(sources.getName());

        return convertView;
    }
}
