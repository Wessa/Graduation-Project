package com.gp.fbce;

import android.content.Intent;
import android.database.Cursor;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.gp.fbce.local.CardsProvider;
import com.gp.fbce.local.DBOpenHelper;

public class Exchange extends AppCompatActivity implements NfcAdapter.CreateNdefMessageCallback {

    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange);

        Cursor data = getContentResolver().query(CardsProvider.CONTENT_URI, DBOpenHelper.ALL_COLUMNS,
                "_id = ?", new String[]{"1"}, null);

        Log.d("count", data.getCount()+"");

        data.moveToFirst();

        name = data.getString(data.getColumnIndex(DBOpenHelper.CARD_NAME));
        Log.d("name", name.toString());

        NfcAdapter mAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mAdapter == null) {
            Log.d("res","Sorry this device does not have NFC.");
            return;
        }

        if (!mAdapter.isEnabled()) {
            Toast.makeText(this, "Please enable NFC via Settings.", Toast.LENGTH_LONG).show();
        }

        mAdapter.setNdefPushMessageCallback(this, this);
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent nfcEvent) {
        String message = name;
        NdefRecord ndefRecord = NdefRecord.createMime("text/plain", message.getBytes());
        NdefMessage ndefMessage = new NdefMessage(ndefRecord);
        return ndefMessage;
    }

    @Override
    protected void onResume(){
        super.onResume();
        Intent intent = getIntent();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Parcelable[] rawMessages = intent.getParcelableArrayExtra(
                    NfcAdapter.EXTRA_NDEF_MESSAGES);

            NdefMessage message = (NdefMessage) rawMessages[0]; // only one message transferred

            Log.d("res", new String(message.getRecords()[0].getPayload()));

        } //else
        //mTextView.setText("Waiting for NDEF Message");
    }
}
