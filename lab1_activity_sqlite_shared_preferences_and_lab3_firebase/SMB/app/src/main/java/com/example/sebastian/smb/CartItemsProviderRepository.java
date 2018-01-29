package com.example.sebastian.smb;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import java.io.File;
import java.util.ArrayList;

public class CartItemsProviderRepository implements ICartItemsRepository {

    private final ContentResolver contentResolver;
    private Uri providerUri = Uri.parse("content://com.example.sebastian.smb.CartItemsProvider/cartItems");

    public CartItemsProviderRepository(Context context){
        contentResolver = context.getContentResolver();
    }

    @Override
    public ArrayList<CartItem> GetAllItems(){
        Cursor cursor = contentResolver.query(providerUri,null, null, null, null);
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

    @Override
    public void AddItem(CartItem cartItem){
        ContentValues insertValues = new ContentValues();
        insertValues.put("ItemName", cartItem.getName());
        insertValues.put("Quantity", cartItem.getQuantity());
        insertValues.put("Price", cartItem.getPrice());
        insertValues.put("Selected", cartItem.getIsPurchased());
        contentResolver.insert(providerUri, insertValues);
    }

    @Override
    public void RemoveItem(CartItem cartItemToRemove){
        contentResolver.delete(providerUri, "ItemName = ?", new String[]{cartItemToRemove.getName()});
    }

    @Override
    public void UpdateItem(CartItem cartItem, String itemName) {
        ContentValues insertValues = new ContentValues();
        insertValues.put("ItemName", cartItem.getName());
        insertValues.put("Quantity", cartItem.getQuantity());
        insertValues.put("Price", cartItem.getPrice());
        insertValues.put("Selected", cartItem.getIsPurchased());
        contentResolver.update(providerUri, insertValues, "ItemName = ?", new String[]{itemName});
    }
}
