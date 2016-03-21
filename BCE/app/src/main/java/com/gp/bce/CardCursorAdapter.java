package com.gp.bce;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

/**
 * Created by MW on 3/13/2016.
 */
public class CardCursorAdapter extends CursorAdapter {

    public CardCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.card_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        String cardImage = cursor.getString(cursor.getColumnIndex(DBOpenHelper.CARD_FRONT));

        Log.d("Images ", cardImage);

        Bitmap bitmap = BitmapFactory.decodeFile(cardImage);

        ImageView card = (ImageView) view.findViewById(R.id.card_list_item_image);
        card.setImageBitmap(bitmap);
    }
}
