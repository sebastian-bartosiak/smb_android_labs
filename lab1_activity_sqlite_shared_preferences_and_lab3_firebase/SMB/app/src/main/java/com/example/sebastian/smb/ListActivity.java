package com.example.sebastian.smb;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListActivity extends Activity {

    CartAdapter adapter;
    private ICartItemsRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        LayoutHelper.LoadActionBarColor(this);
        repository = new FirebaseCartRepository();

        ListView listView = (ListView)findViewById(R.id.shoppingList);
        adapter = new CartAdapter(this, repository.GetAllItems(), new EditCartItem() {
            @Override
            public void OpenDialog(final CartItem cartItemBeforeUpdate) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ListActivity.this);
                LayoutInflater inflater = ListActivity.this.getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.alertdialog_add_item, null);
                dialogBuilder.setView(dialogView);
                setCartItem(dialogView, cartItemBeforeUpdate);

                dialogBuilder.setPositiveButton(
                        "Zapisz",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                CartItem cartItemAfterUpdate = getCartItem(dialogView);
                                try{
                                    repository.UpdateItem(cartItemAfterUpdate, cartItemBeforeUpdate.getName());
                                    updateData();
                                }
                                catch(Exception e){
                                    Toast.makeText(ListActivity.this, "Edycja się nie powiodła", Toast.LENGTH_LONG).show();
                                }
                                dialog.cancel();
                            }
                        });

                dialogBuilder.setNegativeButton(
                        "Anuluj",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = dialogBuilder.create();
                alert11.show();
            }
        });
        ((FirebaseCartRepository)repository).setCartAdapter(adapter);
        listView.setAdapter(adapter);
        listView.setLongClickable(true);
        listView.setOnItemLongClickListener(itemLongClickListener);
    }

    public void AddItem(View view){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.alertdialog_add_item, null);
        dialogBuilder.setView(dialogView);


        dialogBuilder.setPositiveButton(
                "Zapisz",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        CartItem cartItem = getCartItem(dialogView);
                        try{
                            repository.AddItem(cartItem);
                            updateData();
                        }
                        catch(Exception e){
                            Toast.makeText(ListActivity.this, "Dodanie nie powiodło się", Toast.LENGTH_LONG).show();
                        }
                        dialog.cancel();
                    }
                });

        dialogBuilder.setNegativeButton(
                "Anuluj",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = dialogBuilder.create();
        alert11.show();
    }

    private CartItem getCartItem(View dialogView) {
        EditText et_cartitem = (EditText) dialogView.findViewById(R.id.et_cartitem);
        EditText et_price = (EditText) dialogView.findViewById(R.id.et_price);
        EditText et_quantity = (EditText) dialogView.findViewById(R.id.et_quantity);
        CheckBox cb_isselected = (CheckBox) dialogView.findViewById(R.id.cb_isselected);
        int quantity = 0;
        double price = 0.0;
        try{
            quantity = Integer.parseInt(et_quantity.getText().toString());
            price = Double.parseDouble(et_price.getText().toString());
        }catch(Exception e){

        }
        CartItem item = new CartItem(et_cartitem.getText().toString(), quantity, price, cb_isselected.isChecked());
        return item;
    }

    private void updateData(){
        ArrayList<CartItem> items = repository.GetAllItems();
        adapter.clear();
        adapter.addAll(items);
        adapter.notifyDataSetChanged();
    }

    private final AdapterView.OnItemLongClickListener itemLongClickListener
            = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            CartItem item = adapter.getItem(position);
            repository.RemoveItem(item);
            updateData();

            return true;
        }
    };

    private void setCartItem(View dialogView, CartItem currentCartItem) {
        EditText et_cartitem = (EditText) dialogView.findViewById(R.id.et_cartitem);
        EditText et_price = (EditText) dialogView.findViewById(R.id.et_price);
        EditText et_quantity = (EditText) dialogView.findViewById(R.id.et_quantity);
        CheckBox cb_isselected = (CheckBox) dialogView.findViewById(R.id.cb_isselected);
        et_cartitem.setText(currentCartItem.getName());
        et_price.setText(String.valueOf(currentCartItem.getPrice()));
        et_quantity.setText(String.valueOf(currentCartItem.getQuantity()));
        cb_isselected.setChecked(currentCartItem.getIsPurchased());
    }

    interface EditCartItem{
        void OpenDialog(CartItem cartItem);
    }
}
