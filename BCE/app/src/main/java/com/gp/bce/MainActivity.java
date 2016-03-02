package com.gp.bce;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize current cardID with the number of cards in the database
        CardsProvider.cardID = getContentResolver().query( CardsProvider.CONTENT_URI, new String[]{DBOpenHelper.CARD_FRONT},
                null, null, null ).getCount();
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

    public void viewMyCard(View view) {

        Log.d( "MainActivityDebug", "ID = " + CardsProvider.cardID );

        if ( CardsProvider.cardID == 0 ){

            Intent i = new Intent(this, AddCardManually.class);
            startActivity(i);
        }
        else{

            Intent i = new Intent(this, ViewCard.class);
            i.putExtra( "cardPath", Environment.getExternalStorageDirectory() + "/BCE/Front_1.jpg" );

            startActivity(i);
        }
    }

    public void viewAllCards(View view) {

        if ( CardsProvider.cardID > 0 ) {

            Intent i = new Intent(this, ViewAllCards.class);
            startActivity(i);
        }
        else{

            Toast.makeText( MainActivity.this, "Fill your profile first", Toast.LENGTH_SHORT ).show();
        }
    }
}
