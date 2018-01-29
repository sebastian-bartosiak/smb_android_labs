package com.example.sebastian.smb;

import java.util.ArrayList;

/**
 * Created by Sebastian on 2017-11-24.
 */

interface ICartItemsRepository {
    ArrayList<CartItem> GetAllItems();

    void AddItem(CartItem cartItem);

    void RemoveItem(CartItem cartItemToRemove);

    void UpdateItem(CartItem cartItem, String itemName);
}
