package com.gp.fbce;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.gp.fbce.add.AddManualActivity;
import com.gp.fbce.add.ScanActivity;
import com.gp.fbce.friends.FriendsActivity;
import com.gp.fbce.globe.GlobalSearchActivity;
import com.gp.fbce.local.CardsProvider;
import com.gp.fbce.local.DBOpenHelper;
import com.gp.fbce.profile.ProfileViewActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity implements NfcAdapter.CreateNdefMessageCallback {

    private final int TAKE_PICTURE = 0;
    int cards;
    private String resultUrl = "result.xml";
    BusinessCard card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //android.support.v7.app.ActionBar app_bar = getSupportActionBar();
        //app_bar.setLogo(R.drawable.add);

        cards = getContentResolver().query(CardsProvider.CONTENT_URI,
                new String[]{DBOpenHelper.CARD_ID},
                null, null, null).getCount();

        Log.d("kam", cards + "");

        if (cards == 0) {
            setContentView(R.layout.start_screen);
        } else {
            setContentView(R.layout.home_screen);
        }
    }

    public void scan(View view) {

        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        Uri fileUri = getOutputMediaFileUri(); // create a file to save the image
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

        startActivityForResult(intent, TAKE_PICTURE);
    }

    private static Uri getOutputMediaFileUri() {
        return Uri.fromFile(getOutputMediaFile());
    }

    /**
     * Create a File for saving an image or video
     */
    private static File getOutputMediaFile() {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "FBCE");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("error", "Oops! Failed create directory");
                return null;
            }
        }

        // Create a media file name
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "image.jpg");

        return mediaFile;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK)
            return;

        String imageFilePath = null;

        imageFilePath = getOutputMediaFileUri().getPath();

        //Remove output file
        deleteFile(resultUrl);

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath, bmOptions);
        OutputStream stream = null;
        try {
            stream = new FileOutputStream(imageFilePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        /* Write bitmap to file using JPEG and 80% quality hint for JPEG. */
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);

        Intent results = new Intent(this, ScanActivity.class);
        results.putExtra("IMAGE_PATH", imageFilePath);
        results.putExtra("RESULT_PATH", resultUrl);
        startActivity(results);
    }

    public void add(View view) {

        Intent intent = new Intent(this, AddManualActivity.class);
        startActivity(intent);
    }

    public void gotoProfile(View view) {

        Cursor c = getContentResolver().query(CardsProvider.CONTENT_URI, DBOpenHelper.ALL_COLUMNS,
                "_id = ?", new String[]{"1"}, null);

        c.moveToFirst();

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

        Intent intent = new Intent(this, ProfileViewActivity.class);
        Bundle b = new Bundle();
        b.putSerializable("user", card);
        b.putBoolean("global", false);
        intent.putExtra("card_view", b);
        startActivity(intent);
    }

    public void globalSearch(View view) {

        Intent intent = new Intent(this, GlobalSearchActivity.class);
        startActivity(intent);
    }

    public void gotoFriends(View view) {

        Intent intent = new Intent(this, FriendsActivity.class);
        startActivity(intent);
    }

    public void exchange(View view) {

//        Cursor data = getContentResolver().query(CardsProvider.CONTENT_URI, DBOpenHelper.ALL_COLUMNS,
//                "_id = ?", new String[]{"1"}, null);
//
//        Log.d("count", data.getCount()+"");
//
//        data.moveToFirst();
//
//        String name = data.getString(data.getColumnIndex(DBOpenHelper.CARD_NAME));
//        Log.d("name", name.toString());

        Cursor data = getContentResolver().query(CardsProvider.CONTENT_URI, DBOpenHelper.ALL_COLUMNS,
                "_id = ?", new String[]{"1"}, null);

        data.moveToFirst();

        card = new BusinessCard();

        card.setName(data.getString(data.getColumnIndex(DBOpenHelper.CARD_NAME)));
        card.setTitle(data.getString(data.getColumnIndex(DBOpenHelper.CARD_TITLE)));
        card.setAddress(data.getString(data.getColumnIndex(DBOpenHelper.CARD_ADDRESS)));
        card.setPhone(data.getString(data.getColumnIndex(DBOpenHelper.CARD_PHONE)));
        card.setCompany(data.getString(data.getColumnIndex(DBOpenHelper.CARD_COMPANY)));
        card.setEmail(data.getString(data.getColumnIndex(DBOpenHelper.CARD_EMAIL)));
        card.setWebsite(data.getString(data.getColumnIndex(DBOpenHelper.CARD_WEBSITE)));

        data.close();

        NfcAdapter mAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mAdapter == null) {
            //Log.d("res", "Sorry this device does not have NFC.");
            Toast.makeText(this, "Sorry this device does not have NFC.", Toast.LENGTH_LONG).show();
            return;
        }

        if (!mAdapter.isEnabled()) {
            Toast.makeText(this, "Please enable NFC via Settings.", Toast.LENGTH_LONG).show();
            startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
        } else {
            Toast.makeText(this, "NFC is enabled close to mobile from each other", Toast.LENGTH_LONG).show();
        }

        mAdapter.setNdefPushMessageCallback(this, this);

        /*Intent intent = new Intent(MainActivity.this, Exchange.class);
        startActivity(intent);*/
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent nfcEvent) {

        String message = null;

        try {
            message = card.toJson().toString();
            //String message = card.getName();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        NdefRecord ndefRecord = NdefRecord.createMime("text/plain", message.getBytes());
        NdefMessage ndefMessage = new NdefMessage(ndefRecord);
        return ndefMessage;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Parcelable[] rawMessages = intent.getParcelableArrayExtra(
                    NfcAdapter.EXTRA_NDEF_MESSAGES);

            NdefMessage message = (NdefMessage) rawMessages[0]; // only one message transferred
            //Toast.makeText(this, new String(message.getRecords()[0].getPayload()), Toast.LENGTH_LONG).show();
            try {
                card = parseCard(new String(message.getRecords()[0].getPayload()));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            insert(card);
            /*TextView res = (TextView) findViewById(R.id.message);
            res.setText(new String(message.getRecords()[0].getPayload()));*/

        } //else
        //mTextView.setText("Waiting for NDEF Message");
    }

    public BusinessCard parseCard(String json) throws JSONException {

        BusinessCard card = new BusinessCard();

        final String WEBSITE = "website";
        final String COMPANY = "company";
        final String EMAIL = "email";
        final String TITLE = "title";
        final String NAME = "name";
        final String ADDRESS = "address";
        final String PHONE = "phone";
        //final String ID = "id";

        JSONObject exchangedCard = new JSONObject(json);

        card.setName(exchangedCard.getString(NAME));
        card.setEmail(exchangedCard.getString(EMAIL));
        card.setTitle(exchangedCard.getString(TITLE));
        card.setAddress(exchangedCard.getString(ADDRESS));
        card.setPhone(exchangedCard.getString(PHONE));
        card.setCompany(exchangedCard.getString(COMPANY));
        card.setWebsite(exchangedCard.getString(WEBSITE));

        return card;
    }

    public void insert(BusinessCard card) {

        int cards = getContentResolver().query(CardsProvider.CONTENT_URI, DBOpenHelper.ALL_COLUMNS,
                "email = ?", new String[]{card.getEmail()}, null).getCount();


        Log.d("cards = ", cards + "");
        if (cards > 0 ){

            //Toast.makeText(MainActivity.this, "Card already exists", Toast.LENGTH_SHORT).show();
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

        //if ( !uri.getLastPathSegment().equals("1") ){

        Intent contactIntent = new Intent(ContactsContract.Intents.Insert.ACTION);
        contactIntent.setType(ContactsContract.RawContacts.CONTENT_TYPE);

        contactIntent.putExtra(ContactsContract.Intents.Insert.NAME, card.getName());
        contactIntent.putExtra(ContactsContract.Intents.Insert.PHONE, card.getPhone());
        contactIntent.putExtra(ContactsContract.Intents.Insert.JOB_TITLE, card.getTitle());
        contactIntent.putExtra(ContactsContract.Intents.Insert.COMPANY, card.getCompany());
        contactIntent.putExtra(ContactsContract.Intents.Insert.NOTES, card.getNote());
        contactIntent.putExtra(ContactsContract.Intents.Insert.POSTAL, card.getAddress());

        startActivity(contactIntent);
        //}
    }

}
