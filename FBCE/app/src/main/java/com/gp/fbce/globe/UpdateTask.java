package com.gp.fbce.globe;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.gp.fbce.BusinessCard;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateTask extends AsyncTask<BusinessCard, Void, Void> {

    @Override
    protected Void doInBackground(BusinessCard... businessCards) {

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String json = null, result = null;

        try {

            final String WEBSITE = "website";
            final String COMPANY = "company";
            final String EMAIL = "email";
            final String TITLE = "title";
            final String NAME = "name";
            final String ADDRESS = "address";
            final String PHONE = "phone";
            final String ID = "id";

            final String BASE_URL = "http://10.1.11.32:8080/bce_api/update_card.php?";

            Log.d("sent card", businessCards[0].getName() + " " + businessCards[0].getEmail());

            // Construct the URL for the MOVIE API query
            Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(ID, businessCards[0].getGlobal_id())
                    .appendQueryParameter(NAME, businessCards[0].getName())
                    .appendQueryParameter(PHONE, businessCards[0].getPhone())
                    .appendQueryParameter(EMAIL, businessCards[0].getEmail())
                    .appendQueryParameter(ADDRESS, businessCards[0].getAddress())
                    .appendQueryParameter(WEBSITE, businessCards[0].getWebsite())
                    .appendQueryParameter(COMPANY, businessCards[0].getCompany())
                    .appendQueryParameter(TITLE, businessCards[0].getTitle())
                    .build();

            URL url = new URL(builtUri.toString());

            Log.d( "url", builtUri.toString() );

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            if (inputStream == null) {
                // Nothing to do.
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }

            json = buffer.toString();

            Log.d("Insertion JSON", "JSON = " + json);

        } catch (IOException e) {
            Log.e("PlaceholderFragment", "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        }

        finally {

            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {

                    reader.close();
                }
                catch (final IOException e) {

                    Log.e("PlaceholderFragment", "Error closing stream", e);
                }
            }
        }

        return null;
    }
}
