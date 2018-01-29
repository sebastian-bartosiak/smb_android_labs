package com.example.sebastian.smb;



import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class FirebaseCartRepository implements ICartItemsRepository {

    private final DatabaseReference database;
    private CartAdapter cartAdapter;

    public FirebaseCartRepository(){
        database = FirebaseDatabase.getInstance().getReference("cartItems");
    }

    public void setCartAdapter(CartAdapter cartAdapter){
        this.cartAdapter = cartAdapter;
    }

    @Override
    public ArrayList<CartItem> GetAllItems() {
        final ArrayList<CartItem> list = new ArrayList<CartItem>();
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                GenericTypeIndicator<HashMap<String, CartItem>> t = new GenericTypeIndicator<HashMap<String, CartItem>>(){};
                HashMap<String, CartItem> items = dataSnapshot.getValue(t);
                list.addAll(items.values());
                cartAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return list;
    }

    @Override
    public void AddItem(CartItem cartItem) {
        database.child(cartItem.getName()).setValue(cartItem);
    }

    @Override
    public void RemoveItem(CartItem cartItemToRemove) {
        database.child(cartItemToRemove.getName()).removeValue();
    }

    @Override
    public void UpdateItem(CartItem cartItem, String itemName) {
        database.child(itemName).removeValue();
        database.child(cartItem.getName()).setValue(cartItem);
    }
}
