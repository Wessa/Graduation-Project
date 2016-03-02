package com.gp.bce;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class AddCardManually extends AppCompatActivity {

    private EditText fName, lName, title, email, address, phone, website , note , company ;
    private String cardPath ;
    private Button saveButton;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card_manually);

        fName = (EditText) findViewById(R.id.etFName);
        lName = (EditText) findViewById(R.id.etLName);
        title = (EditText) findViewById(R.id.etTitle);
        email = (EditText) findViewById(R.id.etEmail);
        address = (EditText) findViewById(R.id.etAddress);
        phone = (EditText) findViewById(R.id.etPhone);
        website = (EditText) findViewById(R.id.etWebsite);
        note = (EditText) findViewById(R.id.etNote);
        company = (EditText) findViewById(R.id.etCompany);
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

    public void viewTemplates(View view) {

        /*if ( fName.getText().toString().trim().length() == 0 ){

            Toast.makeText(this, "Please enter your first name", Toast.LENGTH_SHORT).show();
            fName.requestFocus();
            return;
        }
        else if ( lName.getText().toString().matches("") ){

            Toast.makeText(this, "Please enter your last name", Toast.LENGTH_SHORT).show();
            lName.requestFocus();
            return;
        }
        else if ( phone.getText().toString().matches("") ){

            Toast.makeText(this, "Please enter your phone", Toast.LENGTH_SHORT).show();
            phone.requestFocus();
            return;
        }
        else if ( email.getText().toString().matches("") ){

            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
            email.requestFocus();
            return;
        }
        else if ( address.getText().toString().matches("") ){

            Toast.makeText(this, "Please enter your address", Toast.LENGTH_SHORT).show();
            address.requestFocus();
            return;
        }
        else if ( title.getText().toString().matches("") ){

            Toast.makeText(this, "Please enter your title", Toast.LENGTH_SHORT).show();
            title.requestFocus();
            return;
        }
        else if ( website.getText().toString().matches("") ){

            Toast.makeText(this, "Please enter your website", Toast.LENGTH_SHORT).show();
            website.requestFocus();
            return;
        }*/

        Intent intent = new Intent(this, TemplateListView.class);
        Bundle data = new Bundle();

        data.putString("Name", fName.getText() + " " + lName.getText());
        data.putString("Title", String.valueOf(title.getText()));
        data.putString("Email", String.valueOf(email.getText()));
        data.putString("Address", String.valueOf(address.getText()));
        data.putString("Phone", String.valueOf(phone.getText()));
        data.putString("Website", String.valueOf(website.getText()));

        intent.putExtra("cardInfo", data);

        startActivityForResult(intent, 1001);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1001 && resultCode == RESULT_OK) {

            byte[] bytes = data.getByteArrayExtra("cardImage");
            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

            ImageView card = (ImageView) findViewById(R.id.card);
            card.setImageBitmap(bitmap);

            RelativeLayout layout = (RelativeLayout) findViewById(R.id.addCardManually);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            params.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
            params.addRule(RelativeLayout.ALIGN_BASELINE, R.id.button);

            saveButton = new Button(this);
            saveButton.setLayoutParams(params);

            saveButton.setText("Save");

            layout.addView(saveButton);

            onSaveButtonClickListener();
        }
        else if ( requestCode == 1001 && resultCode == RESULT_CANCELED ){

            Toast.makeText(AddCardManually.this, "Please choose a design for your card", Toast.LENGTH_LONG).show();
        }
    }

    private void onSaveButtonClickListener() {

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    //you can create a new file name "test.jpg" in sdcard folder.
                    File f = getOutputMediaFile();

                    //write the bytes in file
                    FileOutputStream fo = new FileOutputStream(f);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fo);

                    // remember close de FileOutput
                    fo.close();
                }
                catch (IOException e){
                    e.printStackTrace();
                }

                /********************************Change**********************************/
                cardPath = getOutputMediaFile().getAbsoluteFile().toString();

                bitmap = BitmapFactory.decodeFile(cardPath);

                ContentValues cardInfo = new ContentValues();

                cardInfo.put(DBOpenHelper.CARD_FNAME, String.valueOf(fName.getText()));
                cardInfo.put(DBOpenHelper.CARD_LNAME, String.valueOf(lName.getText()));
                cardInfo.put(DBOpenHelper.CARD_TITLE, String.valueOf(title.getText()));
                cardInfo.put(DBOpenHelper.CARD_EMAIL, String.valueOf(email.getText()));
                cardInfo.put(DBOpenHelper.CARD_ADDRESS, String.valueOf(address.getText()));
                cardInfo.put(DBOpenHelper.CARD_COMPANY, String.valueOf(company.getText()));
                cardInfo.put(DBOpenHelper.CARD_PHONE, String.valueOf(phone.getText()));
                cardInfo.put(DBOpenHelper.CARD_WEBSITE, String.valueOf(website.getText()));
                cardInfo.put(DBOpenHelper.CARD_NOTE, String.valueOf(note.getText()));
                cardInfo.put(DBOpenHelper.CARD_FRONT, cardPath);

                Uri uri = getContentResolver().insert(CardsProvider.CONTENT_URI, cardInfo);
                Log.d("MainActivityDebug", "inserted card id = " + uri.getLastPathSegment());

                finish();
            }
        });
    }

    private static File getOutputMediaFile(){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File( Environment.getExternalStorageDirectory(), "BCE");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }

        // Create a media file name
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "Front_" + ( CardsProvider.cardID + 1 ) + ".jpg" );

        //Log.d("MainActivityDebug", mediaFile.getAbsolutePath());
        return mediaFile;
    }
}
