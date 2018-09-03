package com.gcappslab.gcorso.letsbookmark.Activities;

import android.content.Intent;
import android.os.Bundle;
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

import com.gcappslab.gcorso.letsbookmark.R;
import com.gcappslab.gcorso.letsbookmark.SiteFolderAdapter;
import com.gcappslab.gcorso.letsbookmark.SiteListAdapter;
import com.gcappslab.gcorso.letsbookmark.SiteListDatabaseHelper;

import java.net.URI;
import java.net.URISyntaxException;


public class EditSiteInformationActivity extends ActionBarActivity implements View.OnClickListener{

    private TextView tfName;
    private TextView tfURL;

    private Button btOk;
    private Button btCancel;

    private EditText edName;
    private EditText edUrl;

    private SiteListAdapter siteListAdapter;
    private SiteFolderAdapter siteFolderAdapter;

    private SiteListDatabaseHelper databaseHelper;
    private String prefix = "http://";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_site_information);

        this.edName = (EditText) this.findViewById(R.id.etName);
        this.edUrl = (EditText) this.findViewById(R.id.etUrl);

        this.tfName = (TextView) this.findViewById(R.id.tfName);
        this.tfURL = (TextView) this.findViewById(R.id.tfURL);

        this.btOk = (Button) this.findViewById(R.id.btOk);
        this.btOk.setOnClickListener(this);

        this.btCancel = (Button) this.findViewById(R.id.btCancel);
        this.btCancel.setOnClickListener(this);

        String name = getIntent().getExtras().getString("name");
        String url = getIntent().getExtras().getString("url");

        edName.setText(name);

        if (url != null){
            if (url.startsWith("http://")){
                edUrl.setText(url);
                prefix = "http://";
            } else if (url.startsWith("https://")) {
                edUrl.setText(url);
                prefix = "https://";
            } else {
                String conc = prefix + url;
                edUrl.setText(conc);
            }
        }

        edUrl.addTextChangedListener(new TextWatcher() {

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
                        edUrl.setText(str);
                    } else {
                        edUrl.setText(prefix);
                    }
                    Selection.setSelection(edUrl.getText(), edUrl.getText().length());
                    Toast.makeText(EditSiteInformationActivity.this, "Don't change the prefix!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_site_information, menu);
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

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btOk){

            databaseHelper = new SiteListDatabaseHelper(this);
            siteListAdapter = new SiteListAdapter(this,databaseHelper.getAllSites());
            siteFolderAdapter = new SiteFolderAdapter(this,databaseHelper.getFolderSites());

            String name;
            String url;
            name = this.edName.getText().toString();
            url = this.edUrl.getText().toString();

            String nameBefore = getIntent().getExtras().getString("name");
            String urlBefore = getIntent().getExtras().getString("url");
            String imageBefore = getIntent().getExtras().getString("image");
            final int position = getIntent().getExtras().getInt("position");

            long fromchecklist = 1;
            long fromcheckfolder = 2;
            long from = getIntent().getExtras().getLong("from");
            String folder = siteFolderAdapter.findFolderNameBySiteName(nameBefore);

            String image = getImageUrl(url);

            if (from == fromchecklist){
                long id = siteListAdapter.findIdSiteByName(nameBefore);
                databaseHelper.deleteSite(id);
                databaseHelper.saveSite(name, url, image);
            }
            if (from == fromcheckfolder){
                long id = siteFolderAdapter.findIdSiteByName(nameBefore);
                databaseHelper.deleteSiteFolder(id);
                databaseHelper.saveFolderSite(name, url, image, folder);
            }

            Toast.makeText(EditSiteInformationActivity.this, "Bookmark edited", Toast.LENGTH_SHORT).show();
            Intent returnToAllSites = new Intent(EditSiteInformationActivity.this, AllSitesActivity.class);
            startActivity(returnToAllSites);
        }

        if(v.getId() == R.id.btCancel){

            finish();

        }

    }
}
