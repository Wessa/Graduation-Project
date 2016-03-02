package com.gp.bce;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by Fady on 09-Feb-16.
 */
public class CardsArrayAdapter extends ArrayAdapter<String> {

    private Context context;
    private List<String> objects;

    public CardsArrayAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        this.context = context;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String imageName = objects.get(position);
        LayoutInflater layoutInflater =
                (LayoutInflater) context.getSystemService(Activity.
                        LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.template_item, null);
        ImageView image = (ImageView) view.findViewById(R.id.templateImage);
        int res = context.getResources().getIdentifier(imageName, "drawable",
                context.getPackageName());
        image.setImageResource(res);
        return view;
    }
}
