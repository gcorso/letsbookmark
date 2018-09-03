package com.gcappslab.gcorso.letsbookmark.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gcappslab.gcorso.letsbookmark.Const;
import com.gcappslab.gcorso.letsbookmark.R;
import com.gcappslab.gcorso.letsbookmark.Site;
import com.gcappslab.gcorso.letsbookmark.SiteListDatabaseHelper;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


public class NewSiteActivity extends ActionBarActivity implements View.OnClickListener {

    private TextView tvSiteName;
    private TextView tvURL;

    private TextView tvEnteredSite;
    private TextView tvProblems;

    private EditText edSiteName;
    private EditText edURL;

    private Button btOk;
    private Button btCancel;

    private SiteListDatabaseHelper databaseHelper;

    private String prefix = "http(s)://";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_site);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        this.edSiteName = (EditText) this.findViewById(R.id.etName);
        this.edURL = (EditText) this.findViewById(R.id.etUrl);

        this.tvSiteName = (TextView) this.findViewById(R.id.tfName);
        this.tvURL = (TextView) this.findViewById(R.id.tfURL);

        this.tvEnteredSite = (TextView) this.findViewById(R.id.tvEnteredSite);
        this.tvProblems = (TextView) this.findViewById(R.id.tvProblems);

        this.btOk = (Button) this.findViewById(R.id.btOk);
        this.btOk.setOnClickListener(this);

        this.btCancel = (Button) this.findViewById(R.id.btCancel);
        this.btCancel.setOnClickListener(this);

        edURL.setText(prefix);
        String urlRecived = getIntent().getExtras().getString("url");
        if (urlRecived != null){
            if (urlRecived.startsWith("http://")){
                edURL.setText(urlRecived);
                prefix = "http://";
            } else if (urlRecived.startsWith("https://")) {
                edURL.setText(urlRecived);
                prefix = "https://";
            } else {
                String conc = prefix + urlRecived;
                edURL.setText(conc);
            }
        }

        edURL.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().startsWith(prefix)) {
                    String str = s.toString();
                    if (str.contains("www")) {
                        str = prefix + str.substring(str.indexOf("www"));
                        edURL.setText(str);
                    } else {
                        edURL.setText(prefix);
                    }
                    Selection.setSelection(edURL.getText(), edURL.getText().length());
                    Toast.makeText(NewSiteActivity.this, "Don't change the prefix!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static String getDomainUrl(String url) throws URISyntaxException {
        URI uri = new URI(url);
        String domain = uri.getHost();
        //return domain.startsWith("www.") ? domain.substring(4) : domain;
        return domain;
    }

    public static String getImageUrl(String url)  {
        String image;

        try {
            String domainUrl = getDomainUrl(url);
            image = "http://" + domainUrl + "/favicon.ico";
        } catch (URISyntaxException e) {
            e.printStackTrace();
            image = "nothing";
        }
        return image;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btOk){

            String name;
            String URL;
            name = this.edSiteName.getText().toString();
            URL = this.edURL.getText().toString();

            if (URL.startsWith(prefix)){
                String site = URL.substring(prefix.length());
                URL = "http://" + site;
                String image = getImageUrl(URL);

                Site varSite = new Site (name, URL, image);

                if(this.getIntent().hasExtra(Const.IntentKeyConst.PARENT_ACTIVITY_EXTRA)) {
				/* se mi chiama la list view */
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(Const.IntentKeyConst.SITE_EXTRA, varSite);

                    this.setResult(RESULT_OK, resultIntent);
                    this.finish();
                }
            }



        }
        if (v.getId() == R.id.btCancel){
            finish();
        }
    }



        /* (non-Javadoc)
         * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
         */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case Const.IntentRequest.CONFIRM_REQUEST:
                if (resultCode == RESULT_OK){
                    this.edSiteName.setText("");
                    this.edURL.setText("");
                    this.tvEnteredSite.setText("Site saved with success");

                }else if (resultCode == RESULT_CANCELED){
                    if (data == null){
                        this.edSiteName.setText("");
                        this.edURL.setText("");
                        this.tvEnteredSite.setText("Site NON Accepted");

                    }else {
                        boolean hasExtra = data.hasExtra(String.valueOf(Const.IntentKeyConst.SITE_EXTRA));
                        Site site = (Site) data.getExtras().get(String.valueOf(Const.IntentKeyConst.SITE_EXTRA));
                        this.edSiteName.setText(site.getName());
                        this.edURL.setText(site.getURL());
                        this.tvEnteredSite.setText("Site Da MODIFICARE");
                    }
                }
                break;
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_site, menu);
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
            Intent open_settings = new Intent(NewSiteActivity.this, SettingsActivity.class);
            startActivity(open_settings);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }


}
