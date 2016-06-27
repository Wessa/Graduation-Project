package com.gp.fbce.globe;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.gp.fbce.BusinessCard;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Waseem on 6/20/2016.
 */
public class GlobalSearchTask extends AsyncTask<String, Void, List<BusinessCard>> {

    private GlobalSearchActivity activity;

    public GlobalSearchTask( GlobalSearchActivity activity ) {

        this.activity = activity;
    }

    @Override
    protected void onPostExecute(List<BusinessCard> businessCards){

        activity.adapter.add(businessCards);
    }

    @Override
    protected List<BusinessCard> doInBackground(String... strings) {

        List <BusinessCard> cards = null;

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String json = null, result = null;

        try {

            final String KEY = "key";

            final String BASE_URL = "http://192.168.1.41:8080//bce_api/get_all_cards.php?";

            // Construct the URL for the MOVIE API query
            Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(KEY, strings[0])
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

            cards = getJsonResult(json);

        } catch (IOException e) {
            Log.e("PlaceholderFragment", "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        }

        catch (JSONException e) {

            e.printStackTrace();
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

        return cards;
    }

    public List<BusinessCard> getJsonResult( String json ) throws JSONException {

        List<BusinessCard> result = new ArrayList<>();

        final String WEBSITE = "website";
        final String COMPANY = "company";
        final String EMAIL = "email";
        final String TITLE = "title";
        final String NAME = "name";
        final String ADDRESS = "address";
        final String PHONE = "phone";
        final String ID = "id";

        JSONObject response = new JSONObject(json);

        JSONArray array = response.getJSONArray("cards");

        for ( int i=0; i<array.length(); i++ ){

            JSONObject cardJson = array.getJSONObject(i);

            BusinessCard card = new BusinessCard();

            card.setGlobal_id(cardJson.getString(ID));
            card.setName(cardJson.getString(NAME));
            card.setWebsite(cardJson.getString(WEBSITE));
            card.setAddress(cardJson.getString(ADDRESS));
            card.setEmail(cardJson.getString(EMAIL));
            card.setPhone(cardJson.getString(PHONE));
            card.setCompany(cardJson.getString(COMPANY));
            card.setTitle(cardJson.getString(TITLE));

            result.add(card);
        }

        return result;
    }
}
