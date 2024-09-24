package com.example.fashionecommerce.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.fashionecommerce.Helper.FirebaseHelper;
import com.example.fashionecommerce.model.Item;
import com.example.fashionecommerce.model.ItemOder;
import com.example.fashionecommerce.model.Product;
import com.example.fashionecommerce.model.UploadImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ItemOderDB {
    private SQLiteDatabase write;
    private SQLiteDatabase read;
    public ItemOderDB(Context context){
        DBhelper dBhelper = new DBhelper(context);
        write = dBhelper.getWritableDatabase();
        read = dBhelper.getReadableDatabase();
    }
    public Double getTotalPriceProducts(){
        double total = 0;
        for (ItemOder itemOder : getList()){
            total += itemOder.getPrice() * itemOder.getQuantity();
        }
        return total;
    }
    public int getCountlPriceProductsSeleted(List<ItemOder> itemOderList){
        return itemOderList.size();
    }
    public Double getTotalPriceProductsSelected(List<ItemOder> itemOderList){
        double total = 0;
        for (ItemOder itemOder : itemOderList){
            total += itemOder.getPrice() * itemOder.getQuantity();
        }
        return total;
    }
    public Item getItemDB(int idItemDB){
        Item item = null;
        List<UploadImage> uploadImageList = new ArrayList<>();

        String sql = "SELECT * FROM " + DBhelper.TB_ITEM + " WHERE id = " + idItemDB + ";";
        Cursor cursor = read.rawQuery(sql, null);

        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String id_product = cursor.getString(cursor.getColumnIndexOrThrow("id_product"));
            String id_user = cursor.getString(cursor.getColumnIndexOrThrow("id_user"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
            String url_image = cursor.getString(cursor.getColumnIndexOrThrow("url_image"));

            item = new Item();
            item.setId(id);
            item.setIdProduct(id_product);
            item.setIdUser(id_user);
            item.setNameProduct(name);
            item.setPrice(price);
            item.setUrlImage(url_image);

        }
        cursor.close();
        return item;
    }

//    public Product getProduct(int idProduct){
    public Product getProductIdInt(int idProduct){
        Product product = null;
        List<UploadImage> uploadImageList = new ArrayList<>();

        String sql = "SELECT * FROM " + DBhelper.TB_ITEM + " WHERE id = " + idProduct + ";";
        Cursor cursor = read.rawQuery(sql, null);

        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String id_product = cursor.getString(cursor.getColumnIndexOrThrow("id_product"));
            String title = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
            String url_image = cursor.getString(cursor.getColumnIndexOrThrow("url_image"));

            product = new Product();
            product.setIdLocal(id);
            product.setId(id_product);
            product.setName(title);
            product.setSellingPrice(price);

            uploadImageList.add(new UploadImage(0, url_image));

            product.setUrlsImages(uploadImageList);
        }
        cursor.close();
        return product;
    }
    public List<ItemOder> getList(){
        List<ItemOder> itemOderList = new ArrayList<>();

        String sql = "SELECT * FROM " + DBhelper.TB_ITEM_ODER +";";
        Cursor cursor = read.rawQuery(sql, null);

        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String id_product = cursor.getString(cursor.getColumnIndexOrThrow("id_product"));
            String id_user = cursor.getString(cursor.getColumnIndexOrThrow("id_user"));
            double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
            String quantity = cursor.getString(cursor.getColumnIndexOrThrow("quantity"));

            ItemOder itemOder = new ItemOder();
            itemOder.setId(id);
            itemOder.setIdProduct(id_product);
            itemOder.setIdUser(id_user);
            itemOder.setPrice(price);
            itemOder.setQuantity(Integer.parseInt(quantity));
            itemOder.setNameProduct(getProductIdInt(id).getName());

            itemOderList.add(itemOder);

        }
        cursor.close();
        return itemOderList;
    }
    public List<ItemOder> getListOderUser(){
        List<ItemOder> itemOderList = new ArrayList<>();

        String sql = "SELECT * FROM " + DBhelper.TB_ITEM_ODER +";";
        Cursor cursor = read.rawQuery(sql, null);

        while (cursor.moveToNext()){
            String id_user = cursor.getString(cursor.getColumnIndexOrThrow("id_user"));
            if (Objects.equals(id_user, FirebaseHelper.getUIDpersonCurrent())){
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String id_product = cursor.getString(cursor.getColumnIndexOrThrow("id_product"));

                double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
                String quantity = cursor.getString(cursor.getColumnIndexOrThrow("quantity"));

                ItemOder itemOder = new ItemOder();
                itemOder.setId(id);
                itemOder.setIdProduct(id_product);
                itemOder.setIdUser(id_user);
                itemOder.setPrice(price);
                itemOder.setQuantity(Integer.parseInt(quantity));
                itemOder.setNameProduct(getProductIdInt(id).getName());
                itemOderList.add(itemOder);
            }




        }
        cursor.close();
        return itemOderList;
    }
    public boolean update(ItemOder itemOder){
        ContentValues values = new ContentValues();
        values.put("quantity", itemOder.getQuantity());

        String where = "id=?";
        String[] args = {String.valueOf(itemOder.getId())};

        try{
            write.update(DBhelper.TB_ITEM_ODER, values, where, args);
        }catch (Exception e){
            Log.i("CreateDB", "Error save class: itemOderBD" + e.getMessage());
            return false;
        }
        return true;
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

    public boolean remove(ItemOder itemOder) {
        String where = "id=?";
        String[] args = {String.valueOf(itemOder.getId())};

        try {
            write.delete(DBhelper.TB_ITEM_ODER, where, args);
        }catch (Exception e){
            return false;
        }
        return true;
    }
}
