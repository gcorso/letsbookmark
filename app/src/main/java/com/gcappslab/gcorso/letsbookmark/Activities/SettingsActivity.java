package com.gcappslab.gcorso.letsbookmark.Activities;

// Questa n � quella che ho fatto da solo, l'altra me l'ha preimpostata lui quando gli ho detto che volevo fare una nuova Activity delle impostazioni ma non la riesco ad utilizzare

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.gcappslab.gcorso.letsbookmark.R;


public class SettingsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        String[] settings=new String[]{"Feedback","Share with friends", "Rate this app", "About"};
        ArrayAdapter<String> adapterSettings=new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                settings);
        ListView ListView;
        ListView = (ListView) findViewById(R.id.listViewSettings);
        if (ListView != null){
            ListView.setAdapter(adapterSettings);
            ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    if (position==0) {
                        String[] TO = {"contact@gcappslab.com"};
                        Intent emailIntent = new Intent(Intent.ACTION_SEND);
                        emailIntent.setData(Uri.parse("mailto:"));
                        emailIntent.setType("text/plain");


                        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Let's Bookmark Feedback");

                        try {
                            startActivity(Intent.createChooser(emailIntent, "Send mail with..."));
                            finish();
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(SettingsActivity.this,
                                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
                        }

                    }  else if (position==1) {

                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setData(Uri.parse("mailto:"));
                        shareIntent.setType("text/plain");


                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Let's Bookmark");
                        shareIntent.putExtra(Intent.EXTRA_TEXT, "Try the new app Let's Bookmark on the Play Store");

                        try {
                            startActivity(Intent.createChooser(shareIntent, "Share with..."));
                            finish();
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(SettingsActivity.this, "There is no sharing client installed.", Toast.LENGTH_SHORT).show();
                        }
                    } else if (position == 2){

                        Intent openBookmark = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.gcappslab.gcorso.letsbookmark"));
                        startActivity(openBookmark);


                    } else if (position==3) {
                        Intent openAboutSettings = new Intent(SettingsActivity.this, AboutActivity.class);
                        startActivity(openAboutSettings);
                    }
                }
            });

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        return super.onOptionsItemSelected(item);
    }
}
