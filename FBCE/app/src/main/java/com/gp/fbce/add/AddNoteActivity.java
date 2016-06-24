package com.gp.fbce.add;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.gp.fbce.local.CardsProvider;
import com.gp.fbce.local.DBOpenHelper;
import com.gp.fbce.R;

public class AddNoteActivity extends AppCompatActivity {

    EditText edit_note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_note_screen);

        android.support.v7.app.ActionBar app_bar = getSupportActionBar();
        app_bar.setDisplayHomeAsUpEnabled(true);

        final String user_id = getIntent().getStringExtra("user_id");

        Cursor data = getContentResolver().query(CardsProvider.CONTENT_URI, DBOpenHelper.ALL_COLUMNS,
                "_id = ?", new String[]{user_id}, null);

        try {

            data.moveToFirst();

            edit_note = (EditText) findViewById(R.id.input_note);
            edit_note.setText(data.getString(data.getColumnIndex(DBOpenHelper.CARD_NOTE)));
        }
        catch (Exception e) {

            Log.e("error of single Card", e.getMessage());
        }
        finally {

            data.close();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateNote(edit_note.getText().toString(), user_id);
            }
        });
    }

    public void updateNote ( String newNote, String user_id ){

        ContentValues cardInfo = new ContentValues();

        cardInfo.put(DBOpenHelper.CARD_NOTE, newNote);

        getContentResolver().update(CardsProvider.CONTENT_URI, cardInfo, "_id = ?", new String[]{user_id});
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
