package com.example.sebastian.smb;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;


public class CartItemsProvider extends ContentProvider {
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI("com.example.sebastian.smb.CartItemsProvider", "cartItems", 1);
    }

    private MainDatabaseHelper mOpenHelper;
    private SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        mOpenHelper = new MainDatabaseHelper(getContext());
        db = mOpenHelper.getWritableDatabase();
        return db != null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if (sUriMatcher.match(uri) == 1) {
            return db.rawQuery("select * from CartItems", null);
        }
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (sUriMatcher.match(uri) == 1) {
            db.insert("CartItems", null, values);
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if (sUriMatcher.match(uri) == 1) {
            return db.delete("CartItems","ItemName = ?" , selectionArgs);
        }
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (sUriMatcher.match(uri) == 1) {
            return db.update("CartItems", values,"ItemName = ?" , selectionArgs);
        }
        return 0;
    }


    protected static final class MainDatabaseHelper extends SQLiteOpenHelper {

        private static final java.lang.String SQL_CREATE_MAIN = "CREATE TABLE IF NOT EXISTS CartItems(ItemName VARCHAR PRIMARY KEY, Quantity INTEGER, Price NUMERIC, Selected INTEGER);";
        private static final String DBNAME = "smb_content_provider.db";

        /*
             * Instantiates an open helper for the provider's SQLite data repository
             * Do not do database creation and upgrade here.
             */
        MainDatabaseHelper(Context context) {
            super(context, DBNAME, null, 1);
        }

        /*
         * Creates the data repository. This is called when the provider attempts to open the
         * repository and SQLite reports that it doesn't exist.
         */
        public void onCreate(SQLiteDatabase db) {

            // Creates the main table
            db.execSQL(SQL_CREATE_MAIN);
            db.execSQL("INSERT INTO CartItems VALUES ('Mąka', 5, 2.2, 1);");
            db.execSQL("INSERT INTO CartItems VALUES ('Woda', 3, 1.4, 1);");
            db.execSQL("INSERT INTO CartItems VALUES ('Jajka', 7, 9.3, 0);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS CartItems");
            onCreate(db);
        }
    }
}
