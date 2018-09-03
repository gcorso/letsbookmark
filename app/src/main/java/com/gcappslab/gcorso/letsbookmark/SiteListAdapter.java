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

/**
 * Created by Alessandro.c on 11/09/2015.
 */
public class SiteListAdapter extends CursorAdapter{

    private SQLiteDatabase mDatabase;
    private SiteListDatabaseHelper dbHelper;
    private Context mContext;

    private String[] mAllColumns = {dbHelper.ALL_COLUMN_ID, dbHelper.ALL_COLUMN_NAME, dbHelper.ALL_COLUMN_URL, dbHelper.ALL_COLUMN_IMAGE};

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
    public SiteListAdapter(Context context, Cursor cursor) {
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
        String URL = cursor.getString(2);
        urlTextView.setText(URL);

        ImageView imageImageView = (ImageView) view.findViewById(R.id.tv_list_image);
        String image = cursor.getString(3);
        if (image.equals("folder")) {
            imageImageView.setImageResource(R.drawable.folder);
            urlTextView.setHeight(0);
        } else if (image.equals("nothing")) {imageImageView.setImageResource(R.drawable.cnn);

        } else if (image.equals("zdroidsoft")){imageImageView.setImageResource(R.drawable.droidsoft);

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

    public ArrayList<String> getAllFolders () {
        ArrayList<String> folderNames = new ArrayList<String>();

        Cursor cursor = mDatabase.query(dbHelper.TABLE_NAME_ALL, mAllColumns, dbHelper.ALL_COLUMN_IMAGE + " = ?", new String[]{"folder"}, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Site site = cursorToSite (cursor);
            String name = site.getName();
            folderNames.add(name);
            cursor.moveToNext();
        }

        cursor.close();
        return folderNames;
    }

    public long findIdSiteByName (String name) {
        long id = -1;
        Cursor cursor = mDatabase.query(dbHelper.TABLE_NAME_ALL, mAllColumns, dbHelper.ALL_COLUMN_NAME + " = ?", new String[]{name}, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            id = cursorToId(cursor);
            cursor.moveToNext();
        }
        cursor.close();
        return id;
    }

    private Site cursorToSite (Cursor cursor){

        String name = cursor.getString(1);
        String url = cursor.getString(2);
        String image = cursor.getString(3);

        Site site = new Site(name, url, image);

        return site;
    }

    private long cursorToId (Cursor cursor){
        long id = cursor.getLong(0);
        return id;
    }

    public Site getSite(final int position, Cursor cursor){

        String name;
        String URL;
        String image;

        cursor.moveToPosition(position);
        name = cursor.getString(1);
        URL = cursor.getString(2);
        image = cursor.getString(3);

        Site varSite = new Site(name,URL, image);
        return varSite;

    }
}