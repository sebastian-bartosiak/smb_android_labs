package com.example.sebastian.smb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

public class CartAdapter extends ArrayAdapter<CartItem> {
    private final ListActivity.EditCartItem editCartItem;

    public CartAdapter(Context context, ArrayList<CartItem> cartItems, ListActivity.EditCartItem editCartItem) {
            super(context, 0, cartItems);
            this.editCartItem = editCartItem;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            final CartItem cartItem = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.cart_item, parent, false);
            }
            convertView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //Toast.makeText(getContext(), "test", Toast.LENGTH_LONG).show();
                    return true;
                }
            });
            // Lookup view for data population
            TextView tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            TextView tv_quantity = (TextView) convertView.findViewById(R.id.tv_quantity);
            TextView tv_price = (TextView) convertView.findViewById(R.id.tv_price);
            CheckBox cbListElement = (CheckBox) convertView.findViewById(R.id.listElementCheckBox);
            Button edit_button = (Button) convertView.findViewById(R.id.edit_button);
            // Populate the data into the template view using the data object
            tv_name.setText(cartItem.getName());
            tv_price.setText(cartItem.priceAsString());
            tv_quantity.setText(String.valueOf(cartItem.getQuantity()));
            cbListElement.setChecked(cartItem.getIsPurchased());
            edit_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editCartItem.OpenDialog(cartItem);
                }
            });
            // Return the completed view to render on screen
            return convertView;
        }
}
