package com.gp.bce;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class TemplateListView extends AppCompatActivity {

    private String Name,Title,Email,Address,Phone,Website;
    private List<String> templates;
    private boolean flag = false; // change
    private int templateNumber; // change

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template_list_view);

        templates = new ArrayList<String>();

        templates.add("front1");
        templates.add("front2");

        ArrayAdapter<String> cardsAdapter = new CardsArrayAdapter(this,0,templates);
        ListView templateList = (ListView) findViewById(R.id.templatesList);
        templateList.setAdapter(cardsAdapter);
        Bundle data = getIntent().getBundleExtra("cardInfo");

        Name = data.getString("Name");
        Title = data.getString("Title");
        Email = data.getString("Email");
        Address = data.getString("Address");
        Phone = data.getString("Phone");
        Website = data.getString("Website");


        onTemplateListItemClickListener(templateList); // change
    }

    private void onTemplateListItemClickListener(ListView templateList) {

        templateList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String template = templates.get(i);

                if ( template.charAt(template.length()-1) == '1' ) {

                    setContentView(R.layout.template_1);

                    TextView name = (TextView) findViewById(R.id.tvName1);
                    TextView title = (TextView) findViewById(R.id.tvTitle1);
                    TextView email = (TextView) findViewById(R.id.tvEmail1);
                    TextView address = (TextView) findViewById(R.id.tvAddress1);
                    TextView phone = (TextView) findViewById(R.id.tvPhone1);
                    TextView website = (TextView) findViewById(R.id.tvWebsite1);

                    name.setText(Name);
                    title.setText(Title);
                    email.setText(Email);
                    address.setText(Address);
                    phone.setText(Phone);
                    website.setText(Website);

                    templateNumber = 1;

                }
                else {

                    setContentView(R.layout.template_2);

                    TextView name = (TextView) findViewById(R.id.tvName2);
                    TextView title = (TextView) findViewById(R.id.tvTitle2);
                    TextView email = (TextView) findViewById(R.id.tvEmail2);
                    TextView address = (TextView) findViewById(R.id.tvAddress2);
                    TextView phone = (TextView) findViewById(R.id.tvPhone2);
                    TextView website = (TextView) findViewById(R.id.tvWebsite2);

                    name.setText(Name);
                    title.setText(Title);
                    email.setText(Email);
                    address.setText(Address);
                    phone.setText(Phone);
                    website.setText(Website);

                    templateNumber = 2;
                }

                setTitle("Card");

                /******************************************Change************************************/
                flag = true;
                Toast.makeText(TemplateListView.this, "Please click the back button to save your card", Toast.LENGTH_LONG).show();
                /******************************************Change************************************/
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_template_list_view, menu);
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

    @Override
    public void onBackPressed(){

        View v = null;

        if ( templateNumber == 1 ) {

            v = this.findViewById(R.id.template1);
        }
        else if ( templateNumber == 2 ){

            v = this.findViewById(R.id.template2);
        }

        if ( flag == false ){

            setResult(RESULT_CANCELED, getIntent());
        }
        else {

            v.setDrawingCacheEnabled(true);
            v.buildDrawingCache();
            Bitmap bitmap = v.getDrawingCache();

            /********************************Change**********************************/
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bytes = stream.toByteArray();
            /********************************Change**********************************/

            getIntent().putExtra("cardImage", bytes);
            setResult(RESULT_OK, getIntent());
        }

        finish();
    }
}
