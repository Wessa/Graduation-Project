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
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gp.fbce.BusinessCard;
import com.gp.fbce.local.CardsProvider;
import com.gp.fbce.local.DBOpenHelper;
import com.gp.fbce.MainActivity;
import com.gp.fbce.R;
import com.gp.fbce.abby.AbbyTask;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.FileInputStream;
import java.io.IOException;

public class ScanActivity extends AppCompatActivity {

    String outputPath;
    TextView tv;
    BusinessCard card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_screen);

        android.support.v7.app.ActionBar app_bar = getSupportActionBar();
        app_bar.setDisplayHomeAsUpEnabled(true);

        card = new BusinessCard();

        String imageUrl = "unknown";

        Bundle extras = getIntent().getExtras();
        if( extras != null) {
            imageUrl = extras.getString("IMAGE_PATH");
            outputPath = extras.getString("RESULT_PATH");

            // Starting recognition process
            new AbbyTask(this).execute(imageUrl, outputPath);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);

    }

    public void updateResults(Boolean success) {

        if (!success)
            return;

        try {

            StringBuffer contents = new StringBuffer();
            FileInputStream fis = openFileInput(outputPath);

            try {

                XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(fis, null);
                parser.nextTag();
                readEntry(parser);

            }
            finally {

                fis.close();
            }

            final TextView name = (TextView) findViewById(R.id.name);
            final TextView title = (TextView) findViewById(R.id.title);

            EditText edit_name = (EditText) findViewById(R.id.input_name);
            EditText edit_title = (EditText) findViewById(R.id.input_title);
            EditText edit_phone = (EditText) findViewById(R.id.input_phone);
            EditText edit_email = (EditText) findViewById(R.id.input_email);
            EditText edit_address = (EditText) findViewById(R.id.input_address);
            EditText edit_website = (EditText) findViewById(R.id.input_website);
            EditText edit_company = (EditText) findViewById(R.id.input_company);

            name.setText(card.getName());
            title.setText(card.getTitle());

            edit_name.setText(card.getName());
            edit_title.setText(card.getTitle());
            edit_phone.setText(card.getPhone());
            edit_email.setText(card.getEmail());
            edit_address.setText(card.getAddress());
            edit_website.setText(card.getWebsite());
            edit_company.setText(card.getCompany());

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

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    insert(card);

                    Intent intent = new Intent( ScanActivity.this, MainActivity.class );
                    startActivity(intent);
                    finish();
                }
            });
        }
        catch (Exception e) {

            displayMessage("Error: " + e.getMessage());
        }
    }

    public void displayMessage( String text )
    {
        tv.post( new MessagePoster( text ) );
    }

    class MessagePoster implements Runnable {
        public MessagePoster( String message )
        {
            _message = message;
        }

        public void run() {
            tv.append(_message + "\n");
            //setContentView( tv );
        }

        private final String _message;
    }

    public void insert( BusinessCard card ){

        int cards = getContentResolver().query(CardsProvider.CONTENT_URI, DBOpenHelper.ALL_COLUMNS,
                "email = ?", new String[]{card.getEmail()}, null).getCount();

        if (cards == 1) {

            Toast.makeText(ScanActivity.this, "Card already exists", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues cardInfo = new ContentValues();

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

        if ( !uri.getLastPathSegment().equals("1") ){

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

    private BusinessCard readEntry ( XmlPullParser parser ) throws XmlPullParserException, IOException {

        parser.require(XmlPullParser.START_TAG, null, "document");

        while ( parser.next() != XmlPullParser.END_TAG ) {

            if (parser.getEventType() != XmlPullParser.START_TAG) {

                continue;
            }

            String name = parser.getName();

            if (name.equals("businessCard")) {

                readCard(parser);
            }
            else {

                skip(parser);
            }
        }

        return card;
    }

    private void readCard ( XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require( XmlPullParser.START_TAG, null, "businessCard" );

        while ( parser.next() != XmlPullParser.END_TAG ) {

            if (parser.getEventType() != XmlPullParser.START_TAG) {

                continue;
            }

            String name = parser.getName();
            // Starts by looking for the field tag
            if (name.equals("field")) {

                readField(parser);
            }
        }
    }

    // Processes title tags in the feed.
    private void readField ( XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, null, "field");
        String tag = parser.getName();
        String type = parser.getAttributeValue(null, "type");

        while ( parser.next() != XmlPullParser.END_TAG ) {

            if ( parser.getEventType() != XmlPullParser.START_TAG ) {

                continue;
            }

            if (tag.equals("field")) {

                Log.d("Parser type" , "type = " + type );

                if (type.equals("Phone")) {

                    card.setPhone( readValue(parser) );
                }
                else if (type.equals("Email")) {

                    card.setEmail( readValue(parser) );
                }
                else if (type.equals("Web")) {

                    card.setWebsite( readValue(parser) );
                }
                else if (type.equals("Address")) {

                    card.setAddress( readValue(parser) );
                }
                else if (type.equals("Name")) {

                    card.setName( readValue(parser) );
                }
                else if (type.equals("Company")) {

                    card.setCompany( readValue(parser) );
                }
                else if (type.equals("Job")) {

                    card.setTitle( readValue(parser) );
                }
                else {

                    skip(parser);
                }
            }
        }
    }

    // Processes value tag in the field.
    private String readValue(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, null, "value");
        String value = readText(parser);
        Log.d("Parser value" , value);
        parser.require(XmlPullParser.END_TAG, null, "value");

        return value;
    }

    // For the tags title and summary, extracts their text values.
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {

        String result = "";

        if (parser.next() == XmlPullParser.TEXT) {

            result = parser.getText();
            //Log.d("Parser res" , result);
            parser.nextTag();
        }

        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {

        if (parser.getEventType() != XmlPullParser.START_TAG) {

            throw new IllegalStateException();
        }

        int depth = 1;

        while (depth != 0) {

            switch (parser.next()) {

                case XmlPullParser.END_TAG:
                    depth--;
                    break;

                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
