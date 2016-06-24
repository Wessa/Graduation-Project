package com.gp.fbce;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.gp.fbce.add.AddManualActivity;
import com.gp.fbce.globe.GlobalSearchActivity;
import com.gp.fbce.add.ScanActivity;
import com.gp.fbce.friends.FriendsActivity;
import com.gp.fbce.local.CardsProvider;
import com.gp.fbce.local.DBOpenHelper;
import com.gp.fbce.profile.ProfileViewActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    private final int TAKE_PICTURE = 0;
    int cards;
    private String resultUrl = "result.xml";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cards = getContentResolver().query(CardsProvider.CONTENT_URI,
                new String[]{DBOpenHelper.CARD_ID},
                null, null, null).getCount();

        Log.d("kam", cards + "");

        if ( cards == 0 ) {
        setContentView(R.layout.start_screen);
        }
        else {
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
        Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath,bmOptions);
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

        Intent intent = new Intent(MainActivity.this, Exchange.class);
        startActivity(intent);
    }
}
