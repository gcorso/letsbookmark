package com.gcappslab.gcorso.letsbookmark;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;

/**
 * Created by Alessandro.c on 11/09/2015.
 *
 */
public class SiteListDatabaseHelper {
    private static final int DATABASE_VERSION = 14;
    private static final String DATABASE_NAME = "bookmarks.db";
    public static final String TABLE_NAME_ALL = "sitelist";
    public static final String TABLE_NAME_HOME = "homesites";
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

    public static final String FTS_COLUMN_NAME = "name";
    public static final String FTS_COLUMN_URL = "url";
    public static final String FTS_COLUMN_IMAGE = "image";


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

    //Create a FTS3 Virtual Table for fast searches
    private static final String FTS_VIRTUAL_TABLE = "FTS";

    private static final String DATABASE_CREATE =
            "CREATE VIRTUAL TABLE " + FTS_VIRTUAL_TABLE + " USING fts3("
                    + ALL_COLUMN_NAME
                    + " UNIQUE (" + ALL_COLUMN_NAME + "));";

    private SiteListOpenHelper openHelper;
    private SQLiteDatabase database;

    public SiteListDatabaseHelper (Context context){
        openHelper = new SiteListOpenHelper(context);
        database = openHelper.getWritableDatabase();
        //super(context, DATABASE_NAME, null, DATABASE_VERSION);
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

    public Cursor getAllSites(){
        openHelper.getWritableDatabase();
        //return database.rawQuery( "select * from " + TABLE_NAME_ALL,  null );
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

      /*private static final String FTS_TABLE_CREATE =
              "CREATE VIRTUAL TABLE " + FTS_VIRTUAL_TABLE +
                      " USING fts3 (" +
                      FTS_COLUMN_NAME + ", " +
                      FTS_COLUMN_URL + ", " +
                      FTS_COLUMN_IMAGE + ")";*/

      /**
       * Called when the database is created for the first time. This is where the
       * creation of tables and the initial population of the tables should happen.
       *
       * @param db The database.
       */

      @Override
      public void onCreate(SQLiteDatabase db) {
          db.execSQL(SQL_CREATE_TABLE_ALL);
          db.execSQL(SQL_CREATE_TABLE_FOLDER);

      }

      /**
       * Called when the database needs to be upgraded. The implementation
       * should use this method to drop tables, add tables, or do anything else it
       * needs to upgrade to the new schema version.
       * <p/>
       * <p>
       * The SQLite ALTER TABLE documentation can be found
       * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
       * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
       * you can use ALTER TABLE to rename the old table, then create the new table and then
       * populate the new table with the contents of the old table.
       * </p><p>
       * This method executes within a transaction.  If an exception is thrown, all changes
       * will automatically be rolled back.
       * </p>
       *
       * @param db         The database.
       * @param oldVersion The old database version.
       * @param newVersion The new database version.
       */
      @Override
      public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
          db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_ALL);
          db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_FOLDER);
          onCreate(db);


      }
  }
}
