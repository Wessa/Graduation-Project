package com.gp.fbce.globe;

import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.gp.fbce.BusinessCard;
import com.gp.fbce.R;
import com.gp.fbce.profile.ProfileViewActivity;

import java.util.ArrayList;

public class GlobalSearchActivity extends AppCompatActivity {

    public UserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.global_search_screen);

        android.support.v7.app.ActionBar app_bar = getSupportActionBar();
        app_bar.setDisplayHomeAsUpEnabled(true);

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);

        ListView global_search = (ListView) findViewById(R.id.global_search);
        adapter = new UserAdapter(this, new ArrayList<BusinessCard>());

        global_search.setAdapter(adapter);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent contactIntent = new Intent(ContactsContract.Intents.Insert.ACTION);
                contactIntent.setType(ContactsContract.RawContacts.CONTENT_TYPE);

                contactIntent.putExtra(ContactsContract.Intents.Insert.PHONE, adapter.getItem(i).getPhone());
                contactIntent.putExtra(ContactsContract.Intents.Insert.NAME, adapter.getItem(i).getName());

                startActivity(contactIntent);
            }
        });*/

        global_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(GlobalSearchActivity.this, ProfileViewActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("user", adapter.getItem(i));
                b.putBoolean("global", true);
                intent.putExtra("card_view", b);
                startActivity(intent);
            }
        });

        new GlobalSearchTask(this).execute("");

        SearchView searchView = (SearchView) findViewById(R.id.search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                new GlobalSearchTask(GlobalSearchActivity.this).execute(s);
                return true;
            }
        });
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
