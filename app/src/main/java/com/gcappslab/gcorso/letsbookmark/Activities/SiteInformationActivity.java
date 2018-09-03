package com.gcappslab.gcorso.letsbookmark.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.gcappslab.gcorso.letsbookmark.R;
import com.squareup.picasso.Picasso;


public class SiteInformationActivity extends ActionBarActivity implements View.OnClickListener{

    private TextView tcName;
    private TextView tcURL;

    private Button btEdit;
    private Button btBack;

    private ImageView imageView;
    private ShareActionProvider mShareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_information);

        this.tcName = (TextView) this.findViewById(R.id.tcName);
        this.tcURL = (TextView) this.findViewById(R.id.tcURL);

        this.btEdit = (Button) this.findViewById(R.id.btEdit);
        this.btEdit.setOnClickListener(this);

        this.btBack = (Button) this.findViewById(R.id.btBack);
        this.btBack.setOnClickListener(this);

        this.imageView = (ImageView) this.findViewById(R.id.imageView);

        String name = getIntent().getExtras().getString("name");
        String url = getIntent().getExtras().getString("url");
        String image = getIntent().getExtras().getString("image");

        tcName.setText(name);
        tcURL.setText(url);
        if (image.equals("nothing")) {imageView.setImageResource(R.drawable.cnn);
        } else { Picasso.with(SiteInformationActivity.this).load(image).error(R.drawable.cnn).into(imageView);}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_site_information, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.action_settings) {
            Intent open_settings = new Intent(SiteInformationActivity.this, SettingsActivity.class);
            startActivity(open_settings);
            return true;
        }

        if (id == R.id.action_open) {
            String url = (String) tcURL.getText();
            Intent openBookmark = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(openBookmark);
            return true;
        }

        if (id == R.id.action_share) {
            String name = (String) tcName.getText();
            String url = (String) tcURL.getText();

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setData(Uri.parse("mailto:"));
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Visit " + name + " at " + url + ". Shared with Let's bookmark.");

            try {
                startActivity(Intent.createChooser(shareIntent, "Share with..."));
                finish();
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(SiteInformationActivity.this, "There is no sharing client installed.", Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        if (id == R.id.action_edit) {
            String name = (String) tcName.getText();
            String url = (String) tcURL.getText();
            int image = getIntent().getExtras().getInt("image");
            long from = getIntent().getExtras().getLong("from");
            String folder = getIntent().getExtras().getString("folder");

            Intent openEditBookmarkInformation = new Intent(SiteInformationActivity.this, EditSiteInformationActivity.class);
            openEditBookmarkInformation.putExtra("name", name);
            openEditBookmarkInformation.putExtra("url", url);
            openEditBookmarkInformation.putExtra("image", image);
            openEditBookmarkInformation.putExtra("from", from);
            openEditBookmarkInformation.putExtra("folder", folder);
            startActivity(openEditBookmarkInformation);
        }




        return super.onOptionsItemSelected(item);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.btEdit){

            String name = (String) tcName.getText();
            String url = (String) tcURL.getText();
            int image = getIntent().getExtras().getInt("image");
            final int position = getIntent().getExtras().getInt("position");
            long from = getIntent().getExtras().getLong("from");
            String folder = getIntent().getExtras().getString("folder");

            Intent openEditBookmarkInformation = new Intent(SiteInformationActivity.this, EditSiteInformationActivity.class);
            openEditBookmarkInformation.putExtra("name", name);
            openEditBookmarkInformation.putExtra("url", url);
            openEditBookmarkInformation.putExtra("image", image);
            openEditBookmarkInformation.putExtra("position", position);
            openEditBookmarkInformation.putExtra("from", from);
            openEditBookmarkInformation.putExtra("folder", folder);
            startActivity(openEditBookmarkInformation);
        }

        if(v.getId() == R.id.btBack){

            Intent returnToTheList = new Intent(SiteInformationActivity.this, AllSitesActivity.class);
            startActivity(returnToTheList);

        }

    }
}
