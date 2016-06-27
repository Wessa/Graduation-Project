package com.gp.fbce.globe;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gp.fbce.BusinessCard;
import com.gp.fbce.R;
import com.gp.fbce.local.CardsProvider;
import com.gp.fbce.local.DBOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Waseem on 6/20/2016.
 */
public class UserAdapter extends ArrayAdapter<BusinessCard> {

    List<BusinessCard> cards;
    Context context;
    LayoutInflater inflater;

    public UserAdapter(Context context, ArrayList<BusinessCard> cards) {

        super(context, 0, cards);

        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.cards = cards;
    }

    public void add(List<BusinessCard> cards){

        this.cards.clear();

        this.cards.addAll(cards);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return cards.size();
    }

    @Override
    public BusinessCard getItem(int position) {
        return cards.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rootView = inflater.inflate(R.layout.user_list_item, parent, false);

//        String global_ID = cards.get(position).getGlobal_id();
//        Log.d("id_global", global_ID);
//        int count = context.getContentResolver().query(CardsProvider.CONTENT_URI,
//                new String[]{DBOpenHelper.CARD_GLOBAL_ID},
//                "global_id = ?", new String[]{global_ID}, null).getCount();
//
//        Log.d("count", count + "");

        TextView title = (TextView) rootView.findViewById(R.id.title);
        TextView name = (TextView) rootView.findViewById(R.id.name);

        title.setText(cards.get(position).getTitle());
        name.setText(cards.get(position).getName());

        return rootView;
    }
}
