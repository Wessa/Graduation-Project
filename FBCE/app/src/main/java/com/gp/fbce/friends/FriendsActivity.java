package com.gp.fbce.friends;


import android.app.LoaderManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.gp.fbce.BusinessCard;
import com.gp.fbce.local.CardsProvider;
import com.gp.fbce.local.DBOpenHelper;
import com.gp.fbce.R;
import com.gp.fbce.profile.ProfileViewActivity;

public class FriendsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private CursorAdapter cards;
    private String selection;
    private String[] selectionArgs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_screen);

        android.support.v7.app.ActionBar app_bar = getSupportActionBar();
        app_bar.setDisplayHomeAsUpEnabled(true);

        cards = new CardCursorAdapter(this, null, 0);

        selection = "NOT _id = ?";
        selectionArgs = new String[]{"1"};

        final ListView friends = (ListView) findViewById(R.id.friends);
        friends.setAdapter(cards);

        getLoaderManager().initLoader(0, null, this);

        SearchView search = (SearchView) findViewById(R.id.search);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (newText.equals("") || newText.equals(null)) {

                    //Log.d("MainActivityDebug", "previous List is used");

                    selection = "NOT _id = ?";
                    selectionArgs = new String[]{"1"};

                    restartLoader();
                } else {

                    selection = "NOT _id = ? and (name like ? or phone like ? or email like ? " +
                            "or address like ? or company like ? or title like ? or website like ? or note like ?)";

                    selectionArgs = new String[]{"1", "%" + newText + "%", "%" + newText + "%", "%" + newText + "%",
                            "%" + newText + "%", "%" + newText + "%", "%" + newText + "%", "%" + newText + "%", "%" + newText + "%"};

                    restartLoader();
                }

                return true;
            }
        });

        friends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Cursor c = (Cursor) cards.getItem(i);
                BusinessCard card = new BusinessCard();

                card.setId(c.getString(c.getColumnIndex(DBOpenHelper.CARD_ID)));
                card.setName(c.getString(c.getColumnIndex(DBOpenHelper.CARD_NAME)));
                card.setPhone(c.getString(c.getColumnIndex(DBOpenHelper.CARD_PHONE)));
                card.setEmail(c.getString(c.getColumnIndex(DBOpenHelper.CARD_EMAIL)));
                card.setTitle(c.getString(c.getColumnIndex(DBOpenHelper.CARD_TITLE)));
                card.setAddress(c.getString(c.getColumnIndex(DBOpenHelper.CARD_ADDRESS)));
                card.setWebsite(c.getString(c.getColumnIndex(DBOpenHelper.CARD_WEBSITE)));
                card.setCompany(c.getString(c.getColumnIndex(DBOpenHelper.CARD_COMPANY)));
                card.setNote(c.getString(c.getColumnIndex(DBOpenHelper.CARD_NOTE)));

                Intent intent = new Intent(FriendsActivity.this, ProfileViewActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("user", card);
                b.putBoolean("global", false);
                intent.putExtra("card_view", b);
                //intent.putExtra("user_id", c.getString(c.getColumnIndex(DBOpenHelper.CARD_ID)));
                startActivity(intent);
            }
        });
    }

    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new android.content.CursorLoader(this, CardsProvider.CONTENT_URI, DBOpenHelper.ALL_COLUMNS, selection, selectionArgs, null);
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {
        cards.swapCursor(data);
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        cards.swapCursor(null);
    }

    public void restartLoader() {

        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if ( id == android.R.id.home ){

            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

