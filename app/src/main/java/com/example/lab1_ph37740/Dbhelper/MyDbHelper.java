package com.example.lab1_ph37740.Dbhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDbHelper extends SQLiteOpenHelper {
    public MyDbHelper(Context context){
        super(context, "QLBH.db", null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // chuỗi SQL nên tạo ở phần mềm SQLite Studio chạy thành công mới copy vào đây
        String sqlCategory = "CREATE TABLE tb_cat ( id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT UNIQUE  NOT NULL);";
        db.execSQL( sqlCategory);


        String sqlProduct = "CREATE TABLE tb_product (  id  INTEGER PRIMARY KEY AUTOINCREMENT,  name TEXT UNIQUE      NOT NULL,  price REAL DEFAULT (0.0)       NOT NULL,  id_cat INTEGER REFERENCES tb_cat (id)  ); ";
        db.execSQL(sqlProduct);
        //Mỗi khi chỉnh sửa câu lệnh SQL ở trên thì tăng version ở hàm khởi tạo
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        if(i1 > i ){ // i1 là phiên bản mới, i là phiên bản cũ.
            db.execSQL("DROP TABLE IF EXISTS tb_cat ");
            db.execSQL("DROP TABLE IF EXISTS tb_product ");
            onCreate( db );
        }
    }
}
