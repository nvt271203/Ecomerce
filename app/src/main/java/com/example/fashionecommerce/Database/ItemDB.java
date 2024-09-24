package com.example.fashionecommerce.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.fashionecommerce.Helper.FirebaseHelper;
import com.example.fashionecommerce.model.ItemOder;
import com.example.fashionecommerce.model.Product;

public class ItemDB {
    private SQLiteDatabase write;
    private SQLiteDatabase read;
    public ItemDB(Context context){
//      ở constructor đã tạo ra đc bảng, nên sẽ truy xuất trực tiếp vào bảng.
        DBhelper dBhelper = new DBhelper(context);
        // duyệt trực tiếp vào bảng thông qua write, read
        write = dBhelper.getWritableDatabase();
        read = dBhelper.getReadableDatabase();
    }

    public long save(Product product){
        long idReturn = 0;
//         đối tượng ContentValues được tạo để lưu trữ các cặp giá trị cần thiết để chèn vào bảng.
        ContentValues values = new ContentValues();
        values.put("id_product", product.getId());
        values.put("id_user", FirebaseHelper.getUIDpersonCurrent());
        values.put("name", product.getName());
        values.put("price", product.getSellingPrice());

        for (int i=0; i<product.getUrlsImages().size(); i++){
            if (product.getUrlsImages().get(i).getIndex()==0){
                values.put("url_image", product.getUrlsImages().get(i).getPathUrlSelected());
            }
        }

        try{
//            để chèn dữ liệu vào bảng TB_ITEM.
            idReturn = write.insert(DBhelper.TB_ITEM, null, values);
        }catch (Exception e){
            Log.i("CreateDB", "Error save class: itemBD" + e.getMessage());
        }
        return idReturn;
    }

    public boolean remove(ItemOder itemOder) {
        String where = "id=?";
        String[] args = {String.valueOf(itemOder.getId())};

        try {
            write.delete(DBhelper.TB_ITEM, where, args);
        }catch (Exception e){
            return false;
        }
        return true;
    }
}
