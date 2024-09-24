package com.example.fashionecommerce.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DBhelper extends SQLiteOpenHelper {
    private static int VERSION = 1;   // Phiên bản CSDL
    static String DB_NAME = "DB_ECOMMERCE"; //  Tên CSDL
    static String TB_ITEM = "TB_ITEM";   // Tên bảng của CSDL
    static String TB_ITEM_ODER = "TB_ITEM_ODER";   // Tên bảng của CSDL
    static String TB_ITEM_ODER_PURCHASED = "TB_ITEM_ODER_PURCHASED";   // Tên bảng của CSDL

    public DBhelper(@Nullable Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    // Tự động đc gọi giống Activity
    @Override
    public void onCreate(SQLiteDatabase db) {
//        CREATE TABLE IF NOT EXISTS --- đảm bảo chỉ đc tạo khi bảng đó chưa tồn tại, tránh trường hợp
//        bảng đó đc tạo nhiều lần khi chương trình đc thực thi

//        1.
        String tbItem =   "CREATE TABLE IF NOT EXISTS "+ TB_ITEM
                        + "( id INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + "id_product TEXT NOT NULL, "
                        + "id_user TEXT NOT NULL, "
                        + "name TEXT NOT NULL, "
                        + "price DOUBLE NOT NULL, "
                        + "url_image TEXT NOT NULL ); ";

        String tbItemOder =   "CREATE TABLE IF NOT EXISTS "+ TB_ITEM_ODER
                            + "( id INTEGER PRIMARY KEY AUTOINCREMENT, "
                            + "id_product TEXT NOT NULL, "
                            + "id_user TEXT NOT NULL, "
                            + "price DOUBLE NOT NULL, "
                            + "quantity INTEGER NOT NULL ); ";

        String tbItemOderPurchased =   "CREATE TABLE IF NOT EXISTS "+ TB_ITEM_ODER_PURCHASED
                                        + "( id INTEGER PRIMARY KEY AUTOINCREMENT, "
                                        + "id_user TEXT NOT NULL, "
                                        + "id_product TEXT NOT NULL, "
                                        + "name TEXT NOT NULL, "
                                        + "url_image TEXT NOT NULL, "
                                        + "price DOUBLE NOT NULL, "
                                        + "quantity INTEGER NOT NULL);"
                                    ;

        try {
            db.execSQL(tbItem);
            db.execSQL(tbItemOder);
            db.execSQL(tbItemOderPurchased);
            Log.i("CreateDB", "Tạo 3 bảng thành công.");
        }catch (Exception e){
            Log.i("CreateDB", "Tạo 3 bảng thất bại.");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
