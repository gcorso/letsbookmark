package com.gcappslab.gcorso.letsbookmark;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;

import static com.gcappslab.gcorso.letsbookmark.Const.SEP_I;
import static com.gcappslab.gcorso.letsbookmark.Const.SEP_II;
import static com.gcappslab.gcorso.letsbookmark.Const.SEP_III;

/**
 * Created by Alessandro.c on 11/09/2015.
 *
 */
public class SiteListDatabaseHelper {
    private static final int DATABASE_VERSION = 14;
    private static final String DATABASE_NAME = "bookmarks.db";
    public static final String TABLE_NAME_ALL = "sitelist";
    public static final String TABLE_NAME_FOLDER = "foldersites";

    public static final String ALL_COLUMN_ID = "_id";
    public static final String ALL_COLUMN_NAME = "name";
    public static final String ALL_COLUMN_URL = "url";
    public static final String ALL_COLUMN_IMAGE = "image";

    public static final String FOLDER_COLUMN_ID = "_id";
    public static final String FOLDER_COLUMN_NAME = "name";
    public static final String FOLDER_COLUMN_URL = "url";
    public static final String FOLDER_COLUMN_IMAGE = "image";
    public static final String FOLDER_COLUMN_FOLDER_NAME = "namefolder";

    private static final String SQL_CREATE_TABLE_ALL = "CREATE TABLE " + TABLE_NAME_ALL + "( "
            + ALL_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + ALL_COLUMN_NAME + " TEXT, "
            + ALL_COLUMN_URL + " TEXT, "
            + ALL_COLUMN_IMAGE + " TEXT )";

    private static final String SQL_CREATE_TABLE_FOLDER = "CREATE TABLE " + TABLE_NAME_FOLDER + "( "
        + FOLDER_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
        + FOLDER_COLUMN_NAME + " TEXT, "
        + FOLDER_COLUMN_URL + " TEXT, "
        + FOLDER_COLUMN_IMAGE + " TEXT, "
        + FOLDER_COLUMN_FOLDER_NAME + " TEXT )";

    private SiteListOpenHelper openHelper;
    private SQLiteDatabase database;

    public SiteListDatabaseHelper (Context context){
        openHelper = new SiteListOpenHelper(context);
        database = openHelper.getWritableDatabase();
    }

    public SiteListOpenHelper getOpenHelper (Context context){
        openHelper = new SiteListOpenHelper(context);
        return openHelper;
    }

    public SQLiteDatabase getWritableDatabase (Context context){
        openHelper = new SiteListOpenHelper(context);
        SQLiteDatabase db = openHelper.getWritableDatabase();
        return db;
    }

    public void saveSite(String name, String url, String image){
        ContentValues contentValues= new ContentValues();

        contentValues.put(ALL_COLUMN_NAME, name);
        contentValues.put(ALL_COLUMN_URL, url);
        contentValues.put(ALL_COLUMN_IMAGE, image);

        database.insert(TABLE_NAME_ALL, null, contentValues);
    }

    public void saveSite(Site site){
        ContentValues contentValues= new ContentValues();

        contentValues.put(ALL_COLUMN_NAME, site.getName());
        contentValues.put(ALL_COLUMN_URL, site.getURL());
        contentValues.put(ALL_COLUMN_IMAGE, site.getPhotoRes());

        database.insert(TABLE_NAME_ALL, null, contentValues);
    }

    public void saveSite(String name, String url, String image, SQLiteDatabase db){
        ContentValues contentValues= new ContentValues();

        contentValues.put(ALL_COLUMN_NAME, name);
        contentValues.put(ALL_COLUMN_URL, url);
        contentValues.put(ALL_COLUMN_IMAGE, image);

        db.insert(TABLE_NAME_ALL, null, contentValues);
    }

    public void saveFolderSite(String name, String url, String image, String folderName){
        ContentValues contentValues= new ContentValues();

        contentValues.put(FOLDER_COLUMN_NAME, name);
        contentValues.put(FOLDER_COLUMN_URL, url);
        contentValues.put(FOLDER_COLUMN_IMAGE, image);
        contentValues.put(FOLDER_COLUMN_FOLDER_NAME, folderName);

        database.insert(TABLE_NAME_FOLDER, null, contentValues);
    }

    public boolean importDatabase(String data){

        Log.i("Import data", data);

        String[] str1 = data.split(SEP_I);
        if(str1.length!=2){
            Log.e("Import", "Fail check 1 " + SEP_I + str1.length);
            return false;
        }

        String[] str2 = str1[0].split(SEP_II);
        for(int i = 1; i<str2.length; i++){
            String[] str3 = str2[i].split(SEP_III);
            if(str3.length!=3){
                Log.e("Import", "Fail check 2");
                return false;
            }
            saveSite(str3[0], str3[1], str3[2]);
        }

        str2 = str1[1].split(SEP_II);
        for(int i = 1; i<str2.length; i++){
            String[] str3 = str2[i].split(SEP_III);
            if(str3.length!=4){
                Log.e("Import", "Fail check 3");
                return false;
            }
            saveFolderSite(str3[0], str3[1], str3[2], str3[3]);
        }

        return true;
    }

    public String exportDatabase(){
        StringBuilder export = new StringBuilder();

        // home bookmarks
        export.append("home");
        openHelper.getWritableDatabase();
        Cursor cursor = database.rawQuery(
                "SELECT " + ALL_COLUMN_NAME + ", " + ALL_COLUMN_URL + ", " + ALL_COLUMN_IMAGE +
                        " FROM " + TABLE_NAME_ALL,
                null
        );
        cursor.moveToFirst();

        while (!cursor.isAfterLast()){
            export.append(SEP_II + cursor.getString(0) + SEP_III + cursor.getString(1)
                    + SEP_III + cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();

        // folder bookmarks
        export.append(SEP_I + "folder");
        cursor = database.rawQuery(
                "SELECT " + FOLDER_COLUMN_NAME + ", " + FOLDER_COLUMN_URL + ", " + FOLDER_COLUMN_IMAGE + ", " +
                        FOLDER_COLUMN_FOLDER_NAME + " FROM " + TABLE_NAME_FOLDER,
                null
        );
        cursor.moveToFirst();

        while (!cursor.isAfterLast()){
            export.append(SEP_II + cursor.getString(0) + SEP_III + cursor.getString(1)
                    + SEP_III + cursor.getString(2) + SEP_III + cursor.getString(3));
            cursor.moveToNext();
        }
        cursor.close();

        return export.toString();
    }

    public Cursor getAllSites(){
        openHelper.getWritableDatabase();
        return database.query(TABLE_NAME_ALL, null, null, null, null, null, ALL_COLUMN_IMAGE);
    }


    public Cursor getFolderSites(){
        openHelper.getWritableDatabase();
        return database.rawQuery(
                "select * from " + TABLE_NAME_FOLDER,
                null
        );
    }

    public Cursor searchSiteByInputText(String inputText) throws SQLException {
        String query = "SELECT docid as _id," +
                ALL_COLUMN_NAME + "," +
                " from " + TABLE_NAME_ALL +
                " where " +  ALL_COLUMN_NAME + " MATCH '" + inputText + "';";
        Cursor mCursor = database.rawQuery(query,null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor getSitesMatches(String query, String[] columns) {
        String selection = ALL_COLUMN_NAME + " MATCH ?";
        String[] selectionArgs = new String[] {query+"*"};

        return query(selection, selectionArgs, columns);
    }

    private Cursor query(String selection, String[] selectionArgs, String[] columns) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(ALL_COLUMN_NAME);

        Cursor cursor = builder.query(openHelper.getReadableDatabase(),
                columns, selection, selectionArgs, null, null, null);

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        return cursor;
    }

    public void deleteSite(final long id){
        database.delete(TABLE_NAME_ALL, ALL_COLUMN_ID + " = " + id, null);

    }

    public void deleteSiteFolder(final long id){
        database.delete(TABLE_NAME_FOLDER, FOLDER_COLUMN_ID + " = " + id, null);
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


  private class SiteListOpenHelper extends SQLiteOpenHelper {

      public SiteListOpenHelper(Context context) {
          super(context, DATABASE_NAME, null, DATABASE_VERSION);
      }

      @Override
      public void onCreate(SQLiteDatabase db) {
          db.execSQL(SQL_CREATE_TABLE_ALL);
          db.execSQL(SQL_CREATE_TABLE_FOLDER);

      }

      @Override
      public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
          db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_ALL);
          db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_FOLDER);
          onCreate(db);


      }
  }
}
