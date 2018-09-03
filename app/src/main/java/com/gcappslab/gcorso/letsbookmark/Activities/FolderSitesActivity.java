package com.gcappslab.gcorso.letsbookmark.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.gcappslab.gcorso.letsbookmark.Const;
import com.gcappslab.gcorso.letsbookmark.R;
import com.gcappslab.gcorso.letsbookmark.Site;
import com.gcappslab.gcorso.letsbookmark.SiteBaseAdapter;
import com.gcappslab.gcorso.letsbookmark.SiteFolderAdapter;
import com.gcappslab.gcorso.letsbookmark.SiteListAdapter;
import com.gcappslab.gcorso.letsbookmark.SiteListDatabaseHelper;

import java.util.List;

public class FolderSitesActivity extends ActionBarActivity implements View.OnClickListener{

    private Button btAddSite;
    private ListView listSite;

    private SiteFolderAdapter siteFolderAdapter;
    private SiteBaseAdapter siteAdapter;
    private SiteListAdapter siteListAdapter;

    private SiteListDatabaseHelper databaseHelper;

    private List<Site> siteList;

    @Override
    protected void onResume() {

        final String folderName = getIntent().getExtras().getString("folderName");
        siteFolderAdapter.changeCursor(databaseHelper.getFolderSites());
        siteList = siteFolderAdapter.getAllSitesOfFolder(folderName);
        siteAdapter = new SiteBaseAdapter();
        siteAdapter.setList(siteList);
        listSite.setAdapter(siteAdapter);

        super.onResume();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_sites);

        final String folderName = getIntent().getExtras().getString("folderName");

        String nameTranf = "<medium>" + folderName + "</medium>";

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(Html.fromHtml(nameTranf));

        this.listSite = (ListView) this.findViewById(R.id.lv_all_sites);
        this.btAddSite = (Button) this.findViewById(R.id.newSourceButton);
        this.btAddSite.setOnClickListener((View.OnClickListener) this);

        Button btnHome = (Button) findViewById(R.id.newSourceButton);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent addSiteIntent = new Intent (FolderSitesActivity.this, NewSiteActivity.class);
                addSiteIntent.putExtra(Const.IntentKeyConst.PARENT_ACTIVITY_EXTRA, "LIST_ACTIVITY");
                FolderSitesActivity.this.startActivityForResult(addSiteIntent, Const.IntentRequest.ADD_SITE_REQUEST);
            }
        });

        databaseHelper = new SiteListDatabaseHelper(this);
        siteFolderAdapter = new SiteFolderAdapter(this,databaseHelper.getFolderSites());
        siteList = siteFolderAdapter.getAllSitesOfFolder(folderName);
        siteAdapter = new SiteBaseAdapter();
        siteAdapter.setList(siteList);
        listSite.setAdapter(siteAdapter);

        listSite.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            /**
             * Callback method to be invoked when an item in this view has been
             * clicked and held.
             * <p/>
             * Implementers can call getItemAtPosition(position) if they need to access
             * the data associated with the selected item.
             *
             * @param parent   The AbsListView where the click happened
             * @param view     The view within the AbsListView that was clicked
             * @param position The position of the view in the list
             * @param id       The row id of the item that was clicked
             * @return true if the callback consumed the long click, false otherwise
             */
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, final long id) {

                final Site varSite = (Site) siteAdapter.getItem(position);
                final String name = varSite.getName();
                final String url = varSite.getURL();
                final String image = varSite.getPhotoRes();

                AlertDialog.Builder builder = new AlertDialog.Builder(FolderSitesActivity.this);
                builder.setItems(R.array.long_list_folder_sites, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item

                        if (which == 0) {
                            // OPEN
                            Site varSite = (Site) siteAdapter.getItem(position);
                            String URL;
                            URL = varSite.getURL();

                            if (URL.startsWith("http://") || URL.startsWith("https://")){
                                Intent openBookmark = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
                                startActivity(openBookmark);
                            } else {
                                Toast.makeText(FolderSitesActivity.this, "There is a problem with your URL address, " +
                                                "it has to start with either http:// or https://. Please modify it and try again.",
                                        Toast.LENGTH_LONG).show();
                            }
                        }

                        if (which == 1) {
                            // EDIT
                            Intent openBookmarkEdit = new Intent(FolderSitesActivity.this, EditSiteInformationActivity.class);

                            Site varSite = (Site) siteAdapter.getItem(position);

                            String name = varSite.getName();
                            String url = varSite.getURL();
                            String image = varSite.getPhotoRes();
                            long from = 2;

                            openBookmarkEdit.putExtra("name", name);
                            openBookmarkEdit.putExtra("url", url);
                            openBookmarkEdit.putExtra("image", image);
                            openBookmarkEdit.putExtra("position", position);
                            openBookmarkEdit.putExtra("from", from);
                            openBookmarkEdit.putExtra("folder", folderName);
                            startActivity(openBookmarkEdit);
                        }

                        if (which == 2) {
                            // INFO
                            Intent openBookmarkInformation = new Intent(FolderSitesActivity.this, SiteInformationActivity.class);

                            Site varSite = (Site) siteAdapter.getItem(position);

                            String name = varSite.getName();
                            String url = varSite.getURL();
                            String image = varSite.getPhotoRes();
                            long from = 2;

                            openBookmarkInformation.putExtra("name", name);
                            openBookmarkInformation.putExtra("url", url);
                            openBookmarkInformation.putExtra("image", image);
                            openBookmarkInformation.putExtra("position", position);
                            openBookmarkInformation.putExtra("from", from);
                            openBookmarkInformation.putExtra("folder", folderName);
                            startActivity(openBookmarkInformation);
                        }

                        if (which == 3) {
                            // Condividere il sito

                            Site varSite = (Site) siteAdapter.getItem(position);

                            String name;
                            name = varSite.getName();

                            String URL;
                            URL = varSite.getURL();

                            Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.setData(Uri.parse("mailto:"));
                            shareIntent.setType("text/plain");


                            shareIntent.putExtra(Intent.EXTRA_TEXT, "Go to " + URL + ". Shared with Let's bookmark.");

                            try {
                                startActivity(Intent.createChooser(shareIntent, "Share with..."));
                                finish();
                            } catch (android.content.ActivityNotFoundException ex) {
                                Toast.makeText(FolderSitesActivity.this, "There is no sharing client installed.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        if (which == 4) {
                            // Eliminare il sito

                            long id = siteFolderAdapter.findIdSiteByName(name);
                            databaseHelper.deleteSiteFolder(id);

                            siteFolderAdapter.changeCursor(databaseHelper.getFolderSites());
                            siteList = siteFolderAdapter.getAllSitesOfFolder(folderName);
                            siteAdapter = new SiteBaseAdapter();
                            siteAdapter.setList(siteList);
                            listSite.setAdapter(siteAdapter);
                        }

                        if (which == 5) {
                            // Rimuovere il sito da questa cartella

                            long id = siteFolderAdapter.findIdSiteByName(name);
                            databaseHelper.deleteSiteFolder(id);
                            databaseHelper.saveSite(name, url, image);

                            siteFolderAdapter.changeCursor(databaseHelper.getFolderSites());
                            siteList = siteFolderAdapter.getAllSitesOfFolder(folderName);
                            siteAdapter = new SiteBaseAdapter();
                            siteAdapter.setList(siteList);
                            listSite.setAdapter(siteAdapter);
                        }


                    }
                });

                builder.create().show();


                return true;
            }


        });
        listSite.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            /**
             * Callback method to be invoked when an item in this AdapterView has
             * been clicked.
             * <p/>
             * Implementers can call getItemAtPosition(position) if they need
             * to access the data associated with the selected item.
             *
             * @param parent   The AdapterView where the click happened.
             * @param view     The view within the AdapterView that was clicked (this
             *                 will be a view provided by the adapter)
             * @param position The position of the view in the adapter.
             * @param id       The row id of the item that was clicked.
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Site varSite = (Site) siteAdapter.getItem(position);
                String image;
                image = varSite.getPhotoRes();

                String URL;
                URL = varSite.getURL();

                Intent openBookmark = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
                startActivity(openBookmark);
            }



        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Const.IntentRequest.ADD_SITE_REQUEST){

            if (data != null){
                if (data.hasExtra(Const.IntentKeyConst.SITE_EXTRA)) {
                    Site tmpSite = (Site) data.getSerializableExtra(Const.IntentKeyConst.SITE_EXTRA);

                    //per aggiungere il sito al database
                    String name = tmpSite.getName();
                    String url = tmpSite.getURL();
                    String image = tmpSite.getPhotoRes();
                    final String folderName = getIntent().getExtras().getString("folderName");

                    databaseHelper = new SiteListDatabaseHelper(this);
                    databaseHelper.saveFolderSite(name, url, image, folderName);
                    siteFolderAdapter = new SiteFolderAdapter(this,databaseHelper.getFolderSites());
                    siteList = siteFolderAdapter.getAllSitesOfFolder(folderName);
                    siteAdapter = new SiteBaseAdapter();
                    siteAdapter.setList(siteList);
                    listSite.setAdapter(siteAdapter);

                    Toast.makeText(FolderSitesActivity.this, "Bookmark added", Toast.LENGTH_SHORT).show();
                }
            }
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_folder_sites, menu);
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
            Intent open_settings = new Intent(FolderSitesActivity.this, SettingsActivity.class);
            startActivity(open_settings);
            return true;
        }

        if (id == R.id.action_new) {
            Intent addSiteIntent = new Intent(this, NewSiteActivity.class);
            addSiteIntent.putExtra(Const.IntentKeyConst.PARENT_ACTIVITY_EXTRA, "LIST_ACTIVITY");
            this.startActivityForResult(addSiteIntent, Const.IntentRequest.ADD_SITE_REQUEST);
            return true;
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
        Intent addSiteIntent = new Intent (this, NewSiteActivity.class);
        addSiteIntent.putExtra(Const.IntentKeyConst.PARENT_ACTIVITY_EXTRA, "LIST_ACTIVITY");
        this.startActivityForResult(addSiteIntent, Const.IntentRequest.ADD_SITE_REQUEST);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */


}
