package com.example.lab1_ph37740.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.example.lab1_ph37740.DTO.ProductDTO;
import com.example.lab1_ph37740.Dbhelper.MyDbHelper;

public class ProductDAO {
    MyDbHelper dbHelper;
    SQLiteDatabase db;

    public ProductDAO(Context context){
        dbHelper = new MyDbHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public int addRow(ProductDTO objProduct){
        ContentValues values = new ContentValues();
        values.put("name", objProduct.getName()); // Thêm tên sản phẩm
        values.put("price", objProduct.getPrice()); // Thêm giá sản phẩm
        values.put("id_cat", objProduct.getId_cat()); // Thêm ID danh mục sản phẩm

        try {
            long result = db.insert("tb_product", null, values); // Chèn dòng mới vào bảng
            return (int) result; // Trả về ID dòng đã chèn (cast sang int)
        } catch (SQLiteException e) {
            // Xử lý ngoại lệ
            e.printStackTrace(); // In ra thông tin lỗi
            return -1; // Trả về -1 để chỉ ra rằng đã có lỗi xảy ra
        }
    }

    public void close() {
        if (db != null && db.isOpen()) {
            db.close(); // Đóng kết nối cơ sở dữ liệu
        }
    }
}
