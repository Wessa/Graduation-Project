package com.gp.bce;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;

import java.util.List;

public class ViewAllCards extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private List<String> cardsPath;
    private List<String> searchCardsResult;
    private ArrayAdapter adapter;
    private ListView allCards;
    private Spinner sortMenu;
    private String selectedItem;
    private CursorAdapter cards;

    private String selection;
    private String[] selectionArgs;
    private String sortOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_cards);

        cards = new CardCursorAdapter(this, null, 0);

        selection = "NOT _id = ?";
        selectionArgs = new String[]{"1"};

        //cardsPath = new ArrayList<String>();
        //searchCardsResult = new ArrayList<String>();

        allCards = (ListView) findViewById(R.id.allCards);
        allCards.setAdapter(cards);

        onItemClickListener();

        sortMenu = (Spinner) findViewById(R.id.sortMenu);

        sortMenu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selection = "NOT _id = ?";
                selectionArgs = new String[]{"1"};

                //cardsPath.clear();

                selectedItem = (String) sortMenu.getSelectedItem();
                Log.d("MainActivityDebug", "Item = " + selectedItem);

                if (selectedItem.equals("Name")) {
//                    data = getContentResolver().query(CardsProvider.CONTENT_URI, new String[]{DBOpenHelper.CARD_FRONT},
//                            "NOT _id = ?", new String[]{"1"}, DBOpenHelper.CARD_FNAME + " DESC");
                    sortOrder = DBOpenHelper.CARD_FNAME + " DESC";
                }
                if (selectedItem.equals("Email")) {
//                    data = getContentResolver().query(CardsProvider.CONTENT_URI, new String[]{DBOpenHelper.CARD_FRONT},
//                            "NOT _id = ?", new String[]{"1"}, DBOpenHelper.CARD_EMAIL + " DESC");
                    sortOrder = DBOpenHelper.CARD_EMAIL + " DESC";
                }
                if (selectedItem.equals("Phone")) {
//                    data = getContentResolver().query(CardsProvider.CONTENT_URI, new String[]{DBOpenHelper.CARD_FRONT},
//                            "NOT _id = ?", new String[]{"1"}, DBOpenHelper.CARD_PHONE + " DESC");
                    sortOrder = DBOpenHelper.CARD_PHONE + " DESC";
                }
                if (selectedItem.equals("Title")) {
//                    data = getContentResolver().query(CardsProvider.CONTENT_URI, new String[]{DBOpenHelper.CARD_FRONT},
//                            "NOT _id = ?", new String[]{"1"}, DBOpenHelper.CARD_TITLE + " DESC");
                    sortOrder = DBOpenHelper.CARD_TITLE + " DESC";
                }
                if (selectedItem.equals("Note")) {
                    //data = getContentResolver().query(CardsProvider.CONTENT_URI, new String[]{DBOpenHelper.CARD_FRONT},
                           // "NOT _id = ?", new String[]{"1"}, DBOpenHelper.CARD_NOTE + " DESC");
                    sortOrder = DBOpenHelper.CARD_NOTE + " DESC";
                }if (selectedItem.equals("Date")) {
                    //data = getContentResolver().query(CardsProvider.CONTENT_URI, new String[]{DBOpenHelper.CARD_FRONT},
                            //"NOT _id = ?", new String[]{"1"}, DBOpenHelper.CARD_CREATED + " DESC");
                    sortOrder = DBOpenHelper.CARD_CREATED + " DESC";
                }

                restartLoader();
//
//                /*try {
//                    data.moveToFirst();
//
//                    do {
//                        cardsPath.add(data.getString(data.getColumnIndex(DBOpenHelper.CARD_FRONT)));
//
//                    }
//                    while (data.moveToNext());
//                }
//                catch (Exception e) {
//
//                    Log.d("MainActivityDebug", "error " + e.getMessage());
//                }
//                finally {
//
//                    data.close();
//                }*/

                onItemClickListener();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        SearchView cardSearch = (SearchView) findViewById(R.id.cardSearch);
        cardSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (newText.equals("") || newText.equals(null)) {

                    Log.d("MainActivityDebug", "previous List is used");

                    selection = "NOT _id = ?";
                    selectionArgs = new String[]{"1"};

                    restartLoader();

                    onItemClickListener();
                }
                else {

                    Log.d("MainActivityDebug", "new List is used");
//                    searchCardsResult.clear();

                    selection = "firstName like ? or lastName like ? or phone like ? or email like ? " +
                            "or address like ? or company like ? or title like ? or website like ?";

                    selectionArgs = new String[]{"%" + newText + "%", "%" + newText + "%", "%" + newText + "%", "%" + newText + "%",
                            "%" + newText + "%", "%" + newText + "%", "%" + newText + "%", "%" + newText + "%"};

//                    /*Cursor searchRes = getContentResolver().query(CardsProvider.CONTENT_URI, new String[]{DBOpenHelper.CARD_FRONT},
//                            "firstName like ? or lastName like ? or phone like ? or email like ? " +
//                                    "or address like ? or company like ? or title like ? or website like ?",
//                            new String[]{"%" + newText + "%", "%" + newText + "%", "%" + newText + "%", "%" + newText + "%",
//                                    "%" + newText + "%", "%" + newText + "%", "%" + newText + "%", "%" + newText + "%"}, null);*/
//
//                    /*try {
//                        searchRes.moveToFirst();
//
//                        do {
//                            searchCardsResult.add(searchRes.getString(searchRes.getColumnIndex(DBOpenHelper.CARD_FRONT)));
//
//                        }
//                        while (searchRes.moveToNext());
//                    }
//                    catch (Exception e) {
//                        Log.d("MainActivityDebug", "error " + e.getMessage());
//                    }
//                    finally {
//                        searchRes.close();
//                    }*/
                    restartLoader();
                    onItemClickListener();
                }

                return true;
            }
        });
    }

//    private void onSearchItemClickListener() {
//
//        allCards.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//                String cardImagePath = searchCardsResult.get(i);
//                Log.d("MainActivityDebug", cardImagePath);
//
//                Intent intent = new Intent(getApplicationContext(), ViewCard.class);
//                intent.putExtra("cardPath", cardImagePath);
//                startActivity(intent);
//            }
//        });
//    }

    private void onItemClickListener() {

        allCards.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Cursor clickedCard = (Cursor) cards.getItem(i);
                String cardImagePath = clickedCard.getString(clickedCard.getColumnIndex(DBOpenHelper.CARD_FRONT));

                Log.d("MainActivityDebug", cardImagePath);

                Intent intent = new Intent(getApplicationContext(), ViewCard.class);
                intent.putExtra("cardPath", cardImagePath);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_all_cards, menu);
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

    public void addCard(View view) {

        Intent i = new Intent(this, AddCardManually.class);
        startActivity(i);
    }

    @Override
    protected void onResume() {

        super.onResume();
        restartLoader();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new CursorLoader(this, CardsProvider.CONTENT_URI, DBOpenHelper.ALL_COLUMNS, selection, selectionArgs, sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cards.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cards.swapCursor(null);
    }

    public void restartLoader(){

        getLoaderManager().restartLoader(0, null, this);
    }
}
