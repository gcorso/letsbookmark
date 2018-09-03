package com.gcappslab.gcorso.letsbookmark.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gcappslab.gcorso.letsbookmark.Const;
import com.gcappslab.gcorso.letsbookmark.R;
import com.gcappslab.gcorso.letsbookmark.Site;
import com.gcappslab.gcorso.letsbookmark.SiteListDatabaseHelper;


public class NewFolderActivity extends ActionBarActivity implements View.OnClickListener{

    private TextView tvSiteName;
    private TextView tvURL;

    private EditText edSiteName;
    private EditText edURL;

    private Button btOk;
    private Button btCancel;

    private SiteListDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_folder);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        this.btOk = (Button) this.findViewById(R.id.btOk);
        this.btOk.setOnClickListener(this);

        this.btCancel = (Button) this.findViewById(R.id.btCancel);
        this.btCancel.setOnClickListener(this);

        this.edSiteName = (EditText) this.findViewById(R.id.etName);
        this.edURL = (EditText) this.findViewById(R.id.etUrl);

        this.tvSiteName = (TextView) this.findViewById(R.id.tfName);
        this.tvURL = (TextView) this.findViewById(R.id.tfURL);
    }


    public void onClick(View v) {
        if (v.getId() == R.id.btOk){

                String name;
                String URL;
                name = this.edSiteName.getText().toString();
                URL = this.edURL.getText().toString();
                String image = "folder";

                Site varSite = new Site (name, URL, image);

                if(this.getIntent().hasExtra(Const.IntentKeyConst.PARENT_ACTIVITY_EXTRA)) {
				/* se mi chiama la list view */
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(Const.IntentKeyConst.SITE_EXTRA, varSite);

                    this.setResult(RESULT_OK, resultIntent);
                    this.finish();

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

                }else if (resultCode == RESULT_CANCELED){
                    if (data == null){
                        this.edSiteName.setText("");
                        this.edURL.setText("");

                    }else {
                        boolean hasExtra = data.hasExtra(String.valueOf(Const.IntentKeyConst.SITE_EXTRA));
                        Site site = (Site) data.getExtras().get(String.valueOf(Const.IntentKeyConst.SITE_EXTRA));
                        this.edSiteName.setText(site.getName());
                        this.edURL.setText(site.getURL());
                    }
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_folder, menu);
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
            Intent open_settings = new Intent(NewFolderActivity.this, SettingsActivity.class);
            startActivity(open_settings);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
