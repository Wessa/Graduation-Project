package com.gp.fbce.friends;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.gp.fbce.local.DBOpenHelper;
import com.gp.fbce.R;

/**
 * Created by MW on 3/13/2016.
 */
public class CardCursorAdapter extends CursorAdapter {

    public CardCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.friend_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        String name = cursor.getString(cursor.getColumnIndex(DBOpenHelper.CARD_NAME));
        String title = cursor.getString(cursor.getColumnIndex(DBOpenHelper.CARD_TITLE));

        TextView cardName = (TextView) view.findViewById(R.id.name);
        TextView cardTitle = (TextView) view.findViewById(R.id.title);

        cardName.setText(name);
        cardTitle.setText(title);
    }
}
