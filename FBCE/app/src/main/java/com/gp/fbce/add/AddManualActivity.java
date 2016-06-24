package com.gp.fbce.add;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.gp.fbce.local.CardsProvider;
import com.gp.fbce.local.DBOpenHelper;
import com.gp.fbce.globe.InsertTask;
import com.gp.fbce.MainActivity;
import com.gp.fbce.R;

import java.util.concurrent.ExecutionException;

public class AddManualActivity extends AppCompatActivity {

    BusinessCard card;
    EditText edit_website, edit_address, edit_email, edit_phone, edit_title, edit_name, edit_company;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_screen);

        String result = getIntent().getStringExtra("error-msg");

        if ( result != null ) {
            Log.d("errorrrrr", result);
            Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        }

        android.support.v7.app.ActionBar app_bar = getSupportActionBar();
        app_bar.setDisplayHomeAsUpEnabled(true);

        final TextView name = (TextView) findViewById(R.id.name);
        final TextView title = (TextView) findViewById(R.id.title);

        edit_name = (EditText) findViewById(R.id.input_name);
        edit_title = (EditText) findViewById(R.id.input_title);
        edit_phone = (EditText) findViewById(R.id.input_phone);
        edit_email = (EditText) findViewById(R.id.input_email);
        edit_address = (EditText) findViewById(R.id.input_address);
        edit_website = (EditText) findViewById(R.id.input_website);
        edit_company = (EditText) findViewById(R.id.input_company);

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
            }
        });

        card = new BusinessCard();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String result = "default";

                CheckBox add_to_public = (CheckBox) findViewById(R.id.add_to_public);

                card.setName(String.valueOf(edit_name.getText()));
                card.setTitle(String.valueOf(edit_title.getText()));
                card.setPhone(String.valueOf(edit_phone.getText()));
                card.setEmail(String.valueOf(edit_email.getText()));
                card.setAddress(String.valueOf(edit_address.getText()));
                card.setWebsite(String.valueOf(edit_website.getText()));
                card.setCompany(String.valueOf(edit_company.getText()));

                if ( add_to_public.isChecked() ){

                    InsertTask insertTask = new InsertTask();

                    try {

                        result = insertTask.execute(card).get();
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }

                if ( result.equals("default") || !(result.charAt(0) == 'e') ){

                    card.setGlobal_id(result);
                    insert(card);

                    Intent intent = new Intent(AddManualActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{

                    Toast toast = Toast.makeText(AddManualActivity.this, result, Toast.LENGTH_SHORT);
                    TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                    if( v != null) v.setGravity(Gravity.CENTER);
                    toast.show();
                }
            }
        });
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

    public void insert(BusinessCard card) {

//        Intent contactIntent = new Intent(ContactsContract.Intents.Insert.ACTION);
//        contactIntent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
//
//        contactIntent.putExtra(ContactsContract.Intents.Insert.NAME, card.getName());
//        contactIntent.putExtra(ContactsContract.Intents.Insert.PHONE, card.getPhone());
//        contactIntent.putExtra(ContactsContract.Intents.Insert.JOB_TITLE, card.getTitle());
//        contactIntent.putExtra(ContactsContract.Intents.Insert.COMPANY, card.getCompany());
//        contactIntent.putExtra(ContactsContract.Intents.Insert.NOTES, card.getNote());
//        contactIntent.putExtra(ContactsContract.Intents.Insert.POSTAL, card.getAddress());
//
//        startActivity(contactIntent);

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
        Log.d("MainActivityDebug", "inserted card id = " + uri.getLastPathSegment());
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }
}
