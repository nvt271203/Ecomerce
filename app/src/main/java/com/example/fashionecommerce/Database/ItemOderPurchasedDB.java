package com.example.fashionecommerce.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.fashionecommerce.model.ItemOder;

public class ItemOderPurchasedDB {
    private SQLiteDatabase write;
    private SQLiteDatabase read;
    public ItemOderPurchasedDB(Context context){
        DBhelper dBhelper = new DBhelper(context);
        write = dBhelper.getWritableDatabase();
        read = dBhelper.getReadableDatabase();
    }
    public boolean save(ItemOder itemOder){
        ContentValues values = new ContentValues();
        values.put("id_product", itemOder.getIdProduct());
        values.put("id_user", itemOder.getIdUser());
        values.put("price", itemOder.getPrice());
        values.put("quantity", itemOder.getQuantity());
        try{
            write.insert(DBhelper.TB_ITEM_ODER, null, values);
        }catch (Exception e){
            Log.i("CreateDB", "Error save class: itemOderBD" + e.getMessage());
            return false;
        }
        return true;
    }
}
