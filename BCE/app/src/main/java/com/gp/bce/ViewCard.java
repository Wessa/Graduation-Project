package com.gp.bce;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewCard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_card);

        TextView tvNameValue = (TextView) findViewById(R.id.tvNameValue);
        TextView tvTitleValue = (TextView) findViewById(R.id.tvTitleValue);
        TextView tvEmailValue = (TextView) findViewById(R.id.tvEmailValue);
        TextView tvPhoneValue = (TextView) findViewById(R.id.tvPhoneValue);
        TextView tvAddressValue = (TextView) findViewById(R.id.tvAddressValue);
        TextView tvCompanyValue = (TextView) findViewById(R.id.tvCompanyValue);
        TextView tvWebsiteValue = (TextView) findViewById(R.id.tvWebsiteValue);
        TextView tvNoteValue = (TextView) findViewById(R.id.tvNoteValue);

        String cardPath = getIntent().getStringExtra("cardPath");

        Bitmap b = BitmapFactory.decodeFile(cardPath);
        ImageView card = (ImageView) findViewById(R.id.cardImage);
        card.setImageBitmap(b);

        Cursor data = getContentResolver().query(CardsProvider.CONTENT_URI, DBOpenHelper.ALL_COLUMNS, "front = ?", new String[]{cardPath}, null);

        //Log.d("MainActivityDebug", "Img = " + cardPath);

        try {

            data.moveToFirst();

            tvNameValue.setText(data.getString(data.getColumnIndex(DBOpenHelper.CARD_FNAME)) + " " +
                    data.getString(data.getColumnIndex(DBOpenHelper.CARD_LNAME)));

            tvTitleValue.setText(data.getString(data.getColumnIndex(DBOpenHelper.CARD_TITLE)));
            tvEmailValue.setText(data.getString(data.getColumnIndex(DBOpenHelper.CARD_EMAIL)));
            tvPhoneValue.setText(data.getString(data.getColumnIndex(DBOpenHelper.CARD_PHONE)));
            tvAddressValue.setText(data.getString(data.getColumnIndex(DBOpenHelper.CARD_ADDRESS)));
            tvCompanyValue.setText(data.getString(data.getColumnIndex(DBOpenHelper.CARD_COMPANY)));
            tvWebsiteValue.setText(data.getString(data.getColumnIndex(DBOpenHelper.CARD_WEBSITE)));
            tvNoteValue.setText(data.getString(data.getColumnIndex(DBOpenHelper.CARD_NOTE)));

        }
        catch (Exception e) {

            Log.e("error of single Card", e.getMessage());
        }
        finally {

            data.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
