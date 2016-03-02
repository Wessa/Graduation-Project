package com.gp.bce;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by Fady on 11-Feb-16.
 */
public class AllCardsAdapter extends ArrayAdapter<String> {

    private Context context;
    private List<String> objects;

    public AllCardsAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        this.context = context;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String cardPath = objects.get(position);

        LayoutInflater layoutInflater =
                (LayoutInflater) context.getSystemService(Activity.
                        LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.template_item, null);
        ImageView card = (ImageView) view.findViewById(R.id.templateImage);

        Bitmap b = BitmapFactory.decodeFile(cardPath);
        card.setImageBitmap(b);

        return view;
    }
}