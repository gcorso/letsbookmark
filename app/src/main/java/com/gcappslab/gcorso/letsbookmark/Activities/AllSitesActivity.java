package com.gcappslab.gcorso.letsbookmark.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.gcappslab.gcorso.letsbookmark.Const;
import com.gcappslab.gcorso.letsbookmark.R;
import com.gcappslab.gcorso.letsbookmark.Site;
import com.gcappslab.gcorso.letsbookmark.SiteFolderAdapter;
import com.gcappslab.gcorso.letsbookmark.SiteListAdapter;
import com.gcappslab.gcorso.letsbookmark.SiteListDatabaseHelper;

import java.util.Calendar;
import java.util.List;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

//import com.gcappslab.gcorso.letsbookmark.AppIntroActivity;


public class AllSitesActivity extends ActionBarActivity implements View.OnClickListener{
    //private Button btAddSite;
    private ListView listSite;

    private SiteListAdapter siteListAdapter;
    private SiteFolderAdapter siteFolderAdapter;

    private SiteListDatabaseHelper databaseHelper;
    /* (non-Javadoc)
     * @see android.app.Activity#onResume()
     */
    @Override
    protected void onResume() {
        siteListAdapter.changeCursor(databaseHelper.getAllSites());
        listSite.setAdapter(siteListAdapter);

        super.onResume();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_all_sites);

        this.listSite = (ListView) this.findViewById(R.id.lv_all_sites);
        //this.btAddSite = (Button) this.findViewById(R.id.newSourceButton);
        //this.btAddSite.setOnClickListener((View.OnClickListener) this);
        //siteListAdapter = new SiteListAdapter(this,R.layout.listview, listSite);

        databaseHelper = new SiteListDatabaseHelper(this);
        siteListAdapter = new SiteListAdapter(this,databaseHelper.getAllSites());
        listSite.setAdapter(siteListAdapter);
        //siteListAdapter.changeCursor(databaseHelper.getAllSites());
        //listSite.setAdapter(siteListAdapter);

        // Get intent, action and MIME type
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();


        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent); // Handle text being sent
            }
        }


        //ask for review
        String firststr = "firstday";
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_WEEK);
        if (day == Calendar.SUNDAY) {

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(AllSitesActivity.this);
            boolean notFirstOfDay = prefs.getBoolean(firststr, false);
            if (!notFirstOfDay){

                SharedPreferences.Editor edit = prefs.edit();
                edit.putBoolean(firststr, Boolean.TRUE);
                edit.apply();

                LayoutInflater inflater = this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.recommend_dialog, null);

                final AlertDialog dialog = new AlertDialog.Builder(AllSitesActivity.this)
                        .setView(dialogView)
                        .show();

                Button btRate = (Button) dialogView.findViewById(R.id.btRate);
                btRate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent openBookmark = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.gcappslab.gcorso.letsbookmark"));
                        startActivity(openBookmark);
                    }
                });

                Button btNo = (Button) dialogView.findViewById(R.id.btNo);
                btNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }



        } else {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(AllSitesActivity.this);
            boolean notFirstOfDay = prefs.getBoolean(firststr, false);
            if (notFirstOfDay) {

                SharedPreferences.Editor edit = prefs.edit();
                edit.putBoolean(firststr, Boolean.FALSE);
                edit.apply();
            }
        }


        FabSpeedDial fabSpeedDial = (FabSpeedDial) findViewById(R.id.readsaved_fabspeeddial);
        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();

                if (id== R.id.action_bookmark){

                    Intent addSiteIntent = new Intent (AllSitesActivity.this, NewSiteActivity.class);
                    addSiteIntent.putExtra(Const.IntentKeyConst.PARENT_ACTIVITY_EXTRA, "LIST_ACTIVITY");
                    AllSitesActivity.this.startActivityForResult(addSiteIntent, Const.IntentRequest.ADD_SITE_REQUEST);


                } else if (id == R.id.action_suggestions){
                    Intent provaSuggestions = new Intent(AllSitesActivity.this, SuggestionsActivity.class);
                    AllSitesActivity.this.startActivity(provaSuggestions);

                } else if (id == R.id.action_folder){
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(AllSitesActivity.this);
                    builder.setTitle("Folder name:");

                    // Set up the input
                    final EditText input = new EditText(AllSitesActivity.this);
                    // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    builder.setView(input);

                    // Set up the buttons
                    builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            String name = input.getText().toString();

                            databaseHelper.saveSite(name, "", "folder");
                            siteListAdapter.changeCursor(databaseHelper.getAllSites());
                            listSite.setAdapter(siteListAdapter);


                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();
                }
                return true;
            }
        });


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
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final Site varSite = (Site) siteListAdapter.getSite(position, databaseHelper.getAllSites());
                String name = varSite.getName();
                String url = varSite.getURL();
                String image = varSite.getPhotoRes();

                if (image.equals("folder")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AllSitesActivity.this);
                    builder.setItems(R.array.long_folder, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // The 'which' argument contains the index position
                            // of the selected item

                            if (which == 0) {
                                Intent openFolderSites = new Intent(AllSitesActivity.this, FolderSitesActivity.class);
                                String name = varSite.getName();
                                openFolderSites.putExtra("folderName", name);
                                startActivity(openFolderSites);
                            }

                            if (which == 1) {
                                // Eliminare il folder

                                new AlertDialog.Builder(AllSitesActivity.this)
                                        //.setTitle("Delete entry")
                                        .setMessage("Are you sure you want to delete this folder?")
                                        .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // continue with delete

                                                new AlertDialog.Builder(AllSitesActivity.this)
                                                        //.setTitle("Delete entry")
                                                        .setMessage("Do you want to delete also the bookmarks inside the folder?")
                                                        .setPositiveButton(R.string.yes_delete_them, new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                listSite.setAdapter(siteListAdapter);
                                                                long id = siteListAdapter.getItemId(position);
                                                                Site varSite = (Site) siteListAdapter.getSite(position, databaseHelper.getAllSites());
                                                                final String folderName;
                                                                folderName = varSite.getName();
                                                                databaseHelper.deleteSite(id);
                                                                siteFolderAdapter = new SiteFolderAdapter(AllSitesActivity.this, databaseHelper.getFolderSites());

                                                                siteFolderAdapter.deleteAllSitesOfFolder(folderName);
                                                                siteListAdapter.changeCursor(databaseHelper.getAllSites());
                                                                listSite.setAdapter(siteListAdapter);
                                                            }
                                                        })
                                                        .setNegativeButton(R.string.no_keep_them, new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                listSite.setAdapter(siteListAdapter);
                                                                long id = siteListAdapter.getItemId(position);
                                                                Site varSite = (Site) siteListAdapter.getSite(position, databaseHelper.getAllSites());
                                                                final String folderName;
                                                                folderName = varSite.getName();
                                                                databaseHelper.deleteSite(id);
                                                                siteFolderAdapter = new SiteFolderAdapter(AllSitesActivity.this, databaseHelper.getFolderSites());

                                                                siteFolderAdapter.keepAllSitesOfFolder(folderName);
                                                                siteListAdapter.changeCursor(databaseHelper.getAllSites());
                                                                listSite.setAdapter(siteListAdapter);
                                                            }
                                                        })
                                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                                        .show();
                                            }
                                        })
                                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // do nothing
                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();
                            }
                            //.setTitle(R.string.title)
                        }
                    });
                    builder.create().show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AllSitesActivity.this);
                    builder.setItems(R.array.long_list, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // The 'which' argument contains the index position
                            // of the selected item

                            if (which == 0) {
                                Site varSite = (Site) siteListAdapter.getSite(position, databaseHelper.getAllSites());
                                String URL;
                                URL = varSite.getURL();

                                if (URL.startsWith("http://") || URL.startsWith("https://")){
                                    Intent openBookmark = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
                                    startActivity(openBookmark);
                                } else {
                                    Toast.makeText(AllSitesActivity.this, "There is a problem with your URL address, " +
                                                    "it has to start with either http:// or https://. Please modify it and try again.",
                                            Toast.LENGTH_LONG).show();
                                }
                            }

                            if (which == 1) {
                                Intent openBookmarkEdit = new Intent(AllSitesActivity.this, EditSiteInformationActivity.class);
                                Site varSite = (Site) siteListAdapter.getSite(position, databaseHelper.getAllSites());
                                String name = varSite.getName();
                                String url = varSite.getURL();
                                String image = varSite.getPhotoRes();
                                long from = 1;
                                String folder = "";

                                openBookmarkEdit.putExtra("name", name);
                                openBookmarkEdit.putExtra("url", url);
                                openBookmarkEdit.putExtra("image", image);
                                openBookmarkEdit.putExtra("position", position);
                                openBookmarkEdit.putExtra("from", from);
                                openBookmarkEdit.putExtra("folder", folder);
                                startActivity(openBookmarkEdit);
                            }

                            if (which == 2) {
                                Intent openBookmarkInformation = new Intent(AllSitesActivity.this, SiteInformationActivity.class);
                                Site varSite = (Site) siteListAdapter.getSite(position, databaseHelper.getAllSites());
                                String name = varSite.getName();
                                String url = varSite.getURL();
                                String image = varSite.getPhotoRes();
                                long from = 1;
                                String folder = "";

                                openBookmarkInformation.putExtra("name", name);
                                openBookmarkInformation.putExtra("url", url);
                                openBookmarkInformation.putExtra("image", image);
                                openBookmarkInformation.putExtra("position", position);
                                openBookmarkInformation.putExtra("from", from);
                                openBookmarkInformation.putExtra("folder", folder);
                                startActivity(openBookmarkInformation);
                            }

                            if (which == 3) {
                                // Condividere il sito
                                Site varSite = (Site) siteListAdapter.getSite(position, databaseHelper.getAllSites());
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
                                    Toast.makeText(AllSitesActivity.this, "There is no sharing client installed.", Toast.LENGTH_SHORT).show();
                                }
                            }

                            if (which == 4) {
                                // Eliminare il sito
                                listSite.setAdapter(siteListAdapter);
                                long id = siteListAdapter.getItemId(position);
                                Site varSite = (Site) siteListAdapter.getSite(position, databaseHelper.getAllSites());

                                databaseHelper.deleteSite(id);
                                listSite.setAdapter(siteListAdapter);
                                siteListAdapter.changeCursor(databaseHelper.getAllSites());
                            }

                            if (which == 5) {
                                // Aggiungere  il sito a una cartella

                                // Per l'elenco folders
                                List<String> foldersNames = siteListAdapter.getAllFolders();
                                final ArrayAdapter<String> folderNamesAdapter = new ArrayAdapter<String>(AllSitesActivity.this,
                                        android.R.layout.select_dialog_item, foldersNames);

                                AlertDialog.Builder builder = new AlertDialog.Builder(AllSitesActivity.this);
                                builder.setTitle(R.string.title_choose_folder)
                                        .setAdapter(folderNamesAdapter, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                                String folderChosenName = folderNamesAdapter.getItem(which);
                                                Site daSpostare = siteListAdapter.getSite(position, databaseHelper.getAllSites());
                                                String sname = daSpostare.getName();
                                                String surl = daSpostare.getURL();
                                                String simage = daSpostare.getPhotoRes();
                                                long daEliminare = siteListAdapter.findIdSiteByName(sname);
                                                databaseHelper.saveFolderSite(sname, surl, simage, folderChosenName);

                                                databaseHelper.deleteSite(daEliminare);

                                                siteListAdapter.changeCursor(databaseHelper.getAllSites());
                                                listSite.setAdapter(siteListAdapter);
                                            }
                                        });
                                builder.create().show();
                            }
                            //.setTitle(R.string.title)
                        }
                    });
                    builder.create().show();
                }
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

                Site varSite = (Site) siteListAdapter.getSite(position, databaseHelper.getAllSites());
                String image;
                image = varSite.getPhotoRes();
                if (image.equals("folder")) {
                    Intent openFolderSites = new Intent(AllSitesActivity.this, FolderSitesActivity.class);
                    String name = varSite.getName();
                    openFolderSites.putExtra("folderName", name);
                    startActivity(openFolderSites);

                } else {
                    String URL;
                    URL = varSite.getURL();
                    if (URL.startsWith("http://") || URL.startsWith("https://")){
                        Intent openBookmark = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
                        startActivity(openBookmark);
                    } else {
                        Toast.makeText(AllSitesActivity.this, "There is a problem with your URL address, " +
                                "it has to start with either http:// or https://. Please modify it and try again.",
                                Toast.LENGTH_LONG).show();
                    }

                }
            }


        });
    }

    void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            // Update UI to reflect text being shared
            Intent addSiteIntent = new Intent (this, NewSiteActivity.class);
            addSiteIntent.putExtra(Const.IntentKeyConst.PARENT_ACTIVITY_EXTRA, "LIST_ACTIVITY");
            addSiteIntent.putExtra("url", sharedText);
            this.startActivityForResult(addSiteIntent, Const.IntentRequest.ADD_SITE_REQUEST);

        }
    }

    public void onClick(View v) {
        if(v.getId() == R.id.newSourceButton){
            //Intent addSiteIntent = new Intent (this, NewSiteActivity.class);
            //addSiteIntent.putExtra(Const.IntentKeyConst.PARENT_ACTIVITY_EXTRA, "LIST_ACTIVITY");
            //this.startActivityForResult(addSiteIntent, Const.IntentRequest.ADD_SITE_REQUEST);
            Intent provaSuggestions = new Intent(this, SuggestionsActivity.class);
            this.startActivity(provaSuggestions);
        }
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
     */
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

                    databaseHelper = new SiteListDatabaseHelper(this);
                    siteListAdapter = new SiteListAdapter(this,databaseHelper.getAllSites());
                    databaseHelper.saveSite(name, url, image);
                    listSite.setAdapter(siteListAdapter);
                    siteListAdapter.changeCursor(databaseHelper.getAllSites());
                    listSite.setAdapter(siteListAdapter);
                    if (image.equals("folder")) {
                        Toast.makeText(AllSitesActivity.this, "Folder added", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AllSitesActivity.this, "Bookmark added", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }

        if (requestCode == Const.IntentRequest.ADD_FOLDER_REQUEST){

            if (data != null){
                if (data.hasExtra(Const.IntentKeyConst.SITE_EXTRA)) {
                    Site tmpSite = (Site) data.getSerializableExtra(Const.IntentKeyConst.SITE_EXTRA);

                    //per aggiungere il sito al database
                    String name = tmpSite.getName();
                    String url = tmpSite.getURL();
                    String image = tmpSite.getPhotoRes();

                    databaseHelper = new SiteListDatabaseHelper(this);
                    siteListAdapter = new SiteListAdapter(this,databaseHelper.getAllSites());
                    databaseHelper.saveSite(name, url, image);
                    listSite.setAdapter(siteListAdapter);
                    siteListAdapter.changeCursor(databaseHelper.getAllSites());
                    listSite.setAdapter(siteListAdapter);
                    Toast.makeText(AllSitesActivity.this, "Bookmark added", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_all_sites, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent open_settings = new Intent(AllSitesActivity.this, SettingsActivity.class);
            startActivity(open_settings);
            return true;
        }

        if (id == R.id.action_new) {
            Intent addSiteIntent = new Intent(this, NewSiteActivity.class);
            addSiteIntent.putExtra(Const.IntentKeyConst.PARENT_ACTIVITY_EXTRA, "LIST_ACTIVITY");
            this.startActivityForResult(addSiteIntent, Const.IntentRequest.ADD_SITE_REQUEST);
            return true;
        }

        if (id == R.id.action_new_folder) {
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(AllSitesActivity.this);
            builder.setTitle("Folder name:");

            // Set up the input
            final EditText input = new EditText(AllSitesActivity.this);
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

            // Set up the buttons
            builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    String name = input.getText().toString();

                    databaseHelper.saveSite(name, "", "folder");
                    siteListAdapter.changeCursor(databaseHelper.getAllSites());
                    listSite.setAdapter(siteListAdapter);


                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
        }

        return super.onOptionsItemSelected(item);
    }



}
