package com.gcappslab.gcorso.letsbookmark;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alessandro.c on 03/10/2015.
 */
public class SiteFolderAdapter extends CursorAdapter {

    private SQLiteDatabase mDatabase;
    private SiteListDatabaseHelper dbHelper;
    private Context mContext;

    private String[] mAllColumns = {dbHelper.FOLDER_COLUMN_ID, dbHelper.FOLDER_COLUMN_NAME, dbHelper.FOLDER_COLUMN_URL, dbHelper.FOLDER_COLUMN_IMAGE, dbHelper.FOLDER_COLUMN_FOLDER_NAME};
    /**
     * Constructor that always enables auto-requery.
     *
     * @param context The context
     * @param cursor       The cursor from which to get the data.
     * @deprecated This option is discouraged, as it results in Cursor queries
     * being performed on the application's UI thread and thus can cause poor
     * responsiveness or even Application Not Responding errors.  As an alternative,
     * use {@link LoaderManager} with a {@link CursorLoader}.
     */
    public SiteFolderAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        this.mContext = context;
        dbHelper= new SiteListDatabaseHelper(context);
        // open the database
        try{
            open();
        } catch (SQLException e){

        }
    }

    public void open() throws SQLException {
        mDatabase = dbHelper.getWritableDatabase(mContext);
    }


    /**
     * Bind an existing view to the data pointed to by cursor
     *
     * @param view    Existing view, returned earlier by newView
     * @param context Interface to application's global information
     * @param cursor  The cursor from which to get the data. The cursor is already
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView nameTextView = (TextView) view.findViewById(R.id.tv_list_name);
        nameTextView.setText(cursor.getString(1));

        TextView urlTextView = (TextView) view.findViewById(R.id.tv_list_location);
        urlTextView.setText(cursor.getString(2));

        ImageView imageImageView = (ImageView) view.findViewById(R.id.tv_list_image);
        String image = cursor.getString(3);
        if (image.equals("folder")) {
            imageImageView.setImageResource(R.drawable.folder);
        } else if (image.equals("nothing")) {imageImageView.setImageResource(R.drawable.cnn);
        } else { Picasso.with(context).load(image).error(R.drawable.cnn).into(imageImageView);}
    }

    /**
     * Makes a new view to hold the data pointed to by cursor.
     *
     * @param context Interface to application's global information
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.listview, parent, false);

        //LayoutInflater inflater = LayoutInflater.from(context);
        //View view = inflater.inflate(R.layout.listview, null);

        return view;
    }

    public List<Site> getAllSitesOfFolder (String folderName) {
        List<Site> listSites = new ArrayList<Site>();


        Cursor cursor = mDatabase.query(dbHelper.TABLE_NAME_FOLDER, mAllColumns, dbHelper.FOLDER_COLUMN_FOLDER_NAME + " = ?", new String[]{folderName}, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Site site = cursorToSite (cursor);
            listSites.add(site);
            cursor.moveToNext();
        }

        cursor.close();
        return listSites;
    }

    public void deleteAllSitesOfFolder (String folderName) {
        Cursor cursor = mDatabase.query(dbHelper.TABLE_NAME_FOLDER, mAllColumns, dbHelper.FOLDER_COLUMN_FOLDER_NAME + " = ?", new String[]{folderName}, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            long daEliminare = cursorToId(cursor);
            dbHelper.deleteSiteFolder(daEliminare);
            cursor.moveToNext();
        }
        cursor.close();
    }

    public void keepAllSitesOfFolder (String folderName) {
        Cursor cursor = mDatabase.query(dbHelper.TABLE_NAME_FOLDER, mAllColumns, dbHelper.FOLDER_COLUMN_FOLDER_NAME + " = ?", new String[]{folderName}, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            long daSpostare = cursorToId(cursor);
            Site site = cursorToSite(cursor);

            String name = site.getName();
            String url = site.getURL();
            String image = site.getPhotoRes();
            dbHelper.saveSite(name, url, image);

            dbHelper.deleteSiteFolder(daSpostare);
            cursor.moveToNext();

        }
        cursor.close();
    }

    public long findIdSiteByName (String name) {
        long id = -1;
        Cursor cursor = mDatabase.query(dbHelper.TABLE_NAME_FOLDER, mAllColumns, dbHelper.FOLDER_COLUMN_NAME + " = ?", new String[]{name}, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            id = cursorToId(cursor);
            cursor.moveToNext();
        }
        cursor.close();
        return id;
    }

    public String findFolderNameBySiteName (String name) {
        String folderName = "";
        Cursor cursor = mDatabase.query(dbHelper.TABLE_NAME_FOLDER, mAllColumns, dbHelper.FOLDER_COLUMN_NAME + " = ?", new String[]{name}, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            folderName = cursorToFolderName(cursor);
            cursor.moveToNext();
        }
        cursor.close();
        return folderName;
    }

    private Site cursorToSite (Cursor cursor){

        String name = cursor.getString(1);
        String url = cursor.getString(2);
        String image = cursor.getString(3);
        String folderName = cursor.getString(4);

        Site site = new Site(name, url, image, folderName);

        return site;
    }

    private long cursorToId (Cursor cursor){
        long id = cursor.getLong(0);
        return id;
    }

    private String cursorToFolderName (Cursor cursor){
        String folderName = cursor.getString(4);
        return folderName;
    }

    public Site getSite(final int position, Cursor cursor){

        String name;
        String URL;
        String image;

        cursor.moveToPosition(position);
        name = cursor.getString(1);
        URL = cursor.getString(2);
        image = cursor.getString(3);
        String folderName = cursor.getString(4);


        Site varSite = new Site(name,URL, image, folderName);
        return varSite;
    }

}