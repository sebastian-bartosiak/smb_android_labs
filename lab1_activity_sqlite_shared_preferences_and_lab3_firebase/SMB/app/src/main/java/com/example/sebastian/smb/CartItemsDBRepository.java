package com.example.sebastian.smb;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.internal.util.Predicate;

import java.io.File;
import java.util.ArrayList;

import static java.security.AccessController.getContext;

public class CartItemsDBRepository implements ICartItemsRepository {
    private final SQLiteDatabase database;

    public CartItemsDBRepository(Context context){
        File mDatabaseFile = context.getDatabasePath("smb.db").getAbsoluteFile();
        database = SQLiteDatabase.openOrCreateDatabase(mDatabaseFile, null);
        database.execSQL("CREATE TABLE IF NOT EXISTS CartItems(ItemName VARCHAR PRIMARY KEY, Quantity INTEGER, Price NUMERIC, Selected INTEGER);");
    }

    public ArrayList<CartItem> GetAllItems(){
        Cursor cursor = database.rawQuery("select * from CartItems", null);
        ArrayList<CartItem> listResult = new ArrayList<>();

        if (cursor.moveToFirst()) {

            while (cursor.isAfterLast() == false) {
                String itemName = cursor.getString(cursor.getColumnIndex("ItemName"));
                int quantity = cursor.getInt(cursor.getColumnIndex("Quantity"));
                double price = cursor.getDouble(cursor.getColumnIndex("Price"));
                boolean selected = cursor.getInt(cursor.getColumnIndex("Selected")) == 1 ? true : false;
                listResult.add(new CartItem(itemName, quantity, price, selected));
                cursor.moveToNext();
            }
        }

        return listResult;
    }

    public void AddItem(CartItem cartItem){
        ContentValues insertValues = new ContentValues();
        insertValues.put("ItemName", cartItem.getName());
        insertValues.put("Quantity", cartItem.getQuantity());
        insertValues.put("Price", cartItem.getPrice());
        insertValues.put("Selected", cartItem.getIsPurchased());
        database.insert("CartItems", null, insertValues);
    }

    public void RemoveItem(CartItem cartItemToRemove){
        database.delete("CartItems", "ItemName = ?", new String[]{cartItemToRemove.getName()});
    }

    public void UpdateItem(CartItem cartItem, String itemName) {
        ContentValues insertValues = new ContentValues();
        insertValues.put("ItemName", cartItem.getName());
        insertValues.put("Quantity", cartItem.getQuantity());
        insertValues.put("Price", cartItem.getPrice());
        insertValues.put("Selected", cartItem.getIsPurchased());
        database.update("CartItems", insertValues, "ItemName = ?", new String[]{itemName});
    }
}
