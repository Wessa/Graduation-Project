package com.gp.fbce.profile;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gp.fbce.BusinessCard;
import com.gp.fbce.R;
import com.gp.fbce.add.AddNoteActivity;
import com.gp.fbce.local.CardsProvider;
import com.gp.fbce.local.DBOpenHelper;

public class ProfileViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_view);

        android.support.v7.app.ActionBar app_bar = getSupportActionBar();
        app_bar.setDisplayHomeAsUpEnabled(true);

        Bundle b = getIntent().getBundleExtra("card_view");

        final BusinessCard user = (BusinessCard) b.getSerializable("user");
        final Boolean global = b.getBoolean("global");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        /*Cursor user = getContentResolver().query(CardsProvider.CONTENT_URI, DBOpenHelper.ALL_COLUMNS,
                "_id = ?", new String[]{user_id}, null);*/

        //Log.d("id", user_id);

        if (!global) {

            /*user = getContentResolver().query(CardsProvider.CONTENT_URI, DBOpenHelper.ALL_COLUMNS,
                    "_id = ?", new String[]{user_id}, null);*/

            if (user.getId().equals("1")) {

                fab.setImageResource(R.drawable.edit48);
                //Log.d("id hna", user_id);

                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(ProfileViewActivity.this, ProfileActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            } else {

                fab.setImageResource(R.drawable.note52);
                //Log.d("id hnak", user_id);

                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(ProfileViewActivity.this, AddNoteActivity.class);
                        intent.putExtra("user_id", user.getId());
                        startActivity(intent);
                        finish();
                    }
                });
            }
        } else {

            fab.setImageResource(R.drawable.plus3);
            //Log.d("id hnak", user_id);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    insert(user);
                    finish();
                }
            });
        }

        TextView name = (TextView) findViewById(R.id.name);
        TextView phone = (TextView) findViewById(R.id.phone);
        TextView email = (TextView) findViewById(R.id.email);
        TextView address = (TextView) findViewById(R.id.address);
        TextView title = (TextView) findViewById(R.id.title);
        TextView website = (TextView) findViewById(R.id.website);
        TextView company = (TextView) findViewById(R.id.company);


        name.setText(user.getName());
        title.setText(user.getTitle());
        email.setText(user.getEmail());
        phone.setText(user.getPhone());
        address.setText(user.getAddress());
        website.setText(user.getWebsite());
        company.setText(user.getCompany());
    }

    public void insert(BusinessCard card) {

        int cards = getContentResolver().query(CardsProvider.CONTENT_URI, DBOpenHelper.ALL_COLUMNS,
                "global_id = ?", new String[]{card.getGlobal_id()}, null).getCount();

        if (cards == 1) {

            Toast.makeText(ProfileViewActivity.this, "Card already exists", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues cardInfo = new ContentValues();

        cardInfo.put(DBOpenHelper.CARD_GLOBAL_ID, card.getGlobal_id());
        cardInfo.put(DBOpenHelper.CARD_NAME, card.getName());
        cardInfo.put(DBOpenHelper.CARD_TITLE, card.getTitle());
        cardInfo.put(DBOpenHelper.CARD_EMAIL, card.getEmail());
        cardInfo.put(DBOpenHelper.CARD_ADDRESS, card.getAddress());
        cardInfo.put(DBOpenHelper.CARD_COMPANY, card.getCompany());
        cardInfo.put(DBOpenHelper.CARD_PHONE, card.getPhone());
        cardInfo.put(DBOpenHelper.CARD_WEBSITE, card.getWebsite());
        cardInfo.put(DBOpenHelper.CARD_NOTE, card.getNote());

        Uri uri = getContentResolver().insert(CardsProvider.CONTENT_URI, cardInfo);
        //Log.d("MainActivityDebug", "inserted card id = " + uri.getLastPathSegment());

        Toast.makeText(ProfileViewActivity.this, "Card added", Toast.LENGTH_SHORT).show();

        Intent contactIntent = new Intent(ContactsContract.Intents.Insert.ACTION);
        contactIntent.setType(ContactsContract.RawContacts.CONTENT_TYPE);

        contactIntent.putExtra(ContactsContract.Intents.Insert.NAME, card.getName());
        contactIntent.putExtra(ContactsContract.Intents.Insert.PHONE, card.getPhone());
        contactIntent.putExtra(ContactsContract.Intents.Insert.JOB_TITLE, card.getTitle());
        contactIntent.putExtra(ContactsContract.Intents.Insert.COMPANY, card.getCompany());
        contactIntent.putExtra(ContactsContract.Intents.Insert.NOTES, card.getNote());
        contactIntent.putExtra(ContactsContract.Intents.Insert.POSTAL, card.getAddress());

        startActivity(contactIntent);
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
