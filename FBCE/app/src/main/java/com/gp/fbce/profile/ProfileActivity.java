package com.gp.fbce.profile;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gp.fbce.BusinessCard;
import com.gp.fbce.R;
import com.gp.fbce.globe.InsertTask;
import com.gp.fbce.local.CardsProvider;
import com.gp.fbce.local.DBOpenHelper;

import java.util.concurrent.ExecutionException;

public class ProfileActivity extends AppCompatActivity {

    BusinessCard card;
    EditText edit_website, edit_address, edit_email, edit_phone, edit_title, edit_name, edit_company;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_screen);

        android.support.v7.app.ActionBar app_bar = getSupportActionBar();
        app_bar.setDisplayHomeAsUpEnabled(true);

        Cursor data = getContentResolver().query(CardsProvider.CONTENT_URI, DBOpenHelper.ALL_COLUMNS,
                "_id = ?", new String[]{"1"}, null);

        card = new BusinessCard();

        final TextView name = (TextView) findViewById(R.id.name);
        final TextView title = (TextView) findViewById(R.id.title);

        edit_name = (EditText) findViewById(R.id.input_name);
        edit_title = (EditText) findViewById(R.id.input_title);
        edit_phone = (EditText) findViewById(R.id.input_phone);
        edit_email = (EditText) findViewById(R.id.input_email);
        edit_address = (EditText) findViewById(R.id.input_address);
        edit_website = (EditText) findViewById(R.id.input_website);
        edit_company = (EditText) findViewById(R.id.input_company);

        try {

            data.moveToFirst();

            Log.d("Name", data.getString(data.getColumnIndex(DBOpenHelper.CARD_NAME)));

            card.setName(data.getString(data.getColumnIndex(DBOpenHelper.CARD_NAME)));
            card.setTitle(data.getString(data.getColumnIndex(DBOpenHelper.CARD_TITLE)));
            card.setPhone(data.getString(data.getColumnIndex(DBOpenHelper.CARD_PHONE)));
            card.setEmail(data.getString(data.getColumnIndex(DBOpenHelper.CARD_EMAIL)));
            card.setAddress(data.getString(data.getColumnIndex(DBOpenHelper.CARD_ADDRESS)));
            card.setCompany(data.getString(data.getColumnIndex(DBOpenHelper.CARD_COMPANY)));
            card.setWebsite(data.getString(data.getColumnIndex(DBOpenHelper.CARD_WEBSITE)));

            name.setText(card.getName());
            title.setText(card.getTitle());

            edit_name.setText(card.getName());
            edit_title.setText(card.getTitle());
            edit_email.setText(card.getEmail());
            edit_phone.setText(card.getPhone());
            edit_address.setText(card.getAddress());
            edit_website.setText(card.getWebsite());
            edit_company.setText(card.getCompany());
        } catch (Exception e) {

            Log.e("error of single Card", e.getMessage());
        } finally {

            data.close();
        }

        edit_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                name.setText(editable.toString());
                card.setName(editable.toString());
            }
        });

        edit_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                title.setText(editable.toString());
                card.setTitle(editable.toString());
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                card.setName(String.valueOf(edit_name.getText()));
                card.setTitle(String.valueOf(edit_title.getText()));
                card.setPhone(String.valueOf(edit_phone.getText()));
                card.setEmail(String.valueOf(edit_email.getText()));
                card.setAddress(String.valueOf(edit_address.getText()));
                card.setCompany(String.valueOf(edit_company.getText()));
                card.setWebsite(String.valueOf(edit_website.getText()));

                update(card);
                Intent intent = new Intent(ProfileActivity.this, ProfileViewActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("user", card);
                b.putBoolean("global", false);
                intent.putExtra("card_view", b);
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {

            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void update(BusinessCard card) {

        String result = "default";

        CheckBox add_to_puplic = (CheckBox) findViewById(R.id.add_to_public);

        if (add_to_puplic.isChecked() && card.getGlobal_id() == null ) {

            InsertTask insertTask = new InsertTask();

            try {

                result = insertTask.execute(card).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        ContentValues cardInfo = new ContentValues();

        if (!(result.charAt(0) == 'e')) {

            cardInfo.put(DBOpenHelper.CARD_GLOBAL_ID, result);
        }
        else{

            Toast toast = Toast.makeText(ProfileActivity.this, result, Toast.LENGTH_SHORT);
            TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
            if( v != null) v.setGravity(Gravity.CENTER);
            toast.show();
        }

        cardInfo.put(DBOpenHelper.CARD_NAME, card.getName());
        cardInfo.put(DBOpenHelper.CARD_TITLE, card.getTitle());
        cardInfo.put(DBOpenHelper.CARD_EMAIL, card.getEmail());
        cardInfo.put(DBOpenHelper.CARD_ADDRESS, card.getAddress());
        cardInfo.put(DBOpenHelper.CARD_COMPANY, card.getCompany());
        cardInfo.put(DBOpenHelper.CARD_PHONE, card.getPhone());
        cardInfo.put(DBOpenHelper.CARD_WEBSITE, card.getWebsite());
        cardInfo.put(DBOpenHelper.CARD_NOTE, card.getNote());

        getContentResolver().update(CardsProvider.CONTENT_URI, cardInfo, "_id = ?", new String[]{"1"});
        //Log.d("MainActivityDebug", "inserted card id = " + uri.getLastPathSegment());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
