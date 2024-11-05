package com.example.lab1_ph37740.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.example.lab1_ph37740.DTO.ProductDTO;
import com.example.lab1_ph37740.Dbhelper.MyDbHelper;

import java.util.ArrayList;

public class ProductDAO {
    MyDbHelper dbHelper;
    SQLiteDatabase db;
    Context context; // Thêm biến context để sử dụng

    public ProductDAO(Context context) {
        this.context = context; // Khởi tạo context
        dbHelper = new MyDbHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public int addRow(ProductDTO objProduct) {
        // Kiểm tra xem id_cat có hợp lệ không
        CatDAO catDAO = new CatDAO(context); // Tạo đối tượng CatDAO
        if (catDAO.getOneById(Integer.parseInt(objProduct.getId_cat())) == null) {
            Log.d("ProductDAO", "id_cat không hợp lệ: " + objProduct.getId_cat());
            return -1; // Trả về -1 nếu id_cat không tồn tại
        }

        ContentValues values = new ContentValues();
        values.put("name", objProduct.getName());
        values.put("price", objProduct.getPrice());
        values.put("id_cat", objProduct.getId_cat());

        try {
            long result = db.insert("tb_product", null, values);
            return (int) result;
        } catch (SQLiteException e) {
            Log.e("ProductDAO", "Error inserting row", e);
            return -1;
        }
    }

    public ArrayList<ProductDTO> getList() {
        ArrayList<ProductDTO> listProduct = new ArrayList<>();
        Cursor c = null;
        try {
            c = db.rawQuery("SELECT id, name, price, id_cat FROM tb_product", null);
            if (c != null && c.moveToFirst()) {
                do {
                    int id = c.getInt(0);
                    String name = c.getString(1);
                    String price = c.getString(2);
                    String id_cat = c.getString(3);

                    ProductDTO objProduct = new ProductDTO();
                    objProduct.setId(id);
                    objProduct.setName(name);
                    objProduct.setPrice(price);
                    objProduct.setId_cat(id_cat);
                    listProduct.add(objProduct);
                } while (c.moveToNext());
            } else {
                Log.d("ProductDAO", "No products found.");
            }
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return listProduct;
    }

    public ProductDTO getOneById(int id) {
        ProductDTO objProduct = null;
        Cursor c = null;
        try {
            String[] params = {String.valueOf(id)};
            c = db.rawQuery("SELECT id, name, price, id_cat FROM tb_product WHERE id = ?", params);
            if (c != null && c.moveToFirst()) {
                objProduct = new ProductDTO();
                objProduct.setId(c.getInt(0));
                objProduct.setName(c.getString(1));
                objProduct.setPrice(c.getString(2));
                objProduct.setId_cat(c.getString(3));
            } else {
                Log.d("ProductDAO", "Product not found with id: " + id);
            }
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return objProduct;
    }

    public boolean updateRow(ProductDTO objProduct) {
        // Kiểm tra xem id_cat có hợp lệ không
        CatDAO catDAO = new CatDAO(context); // Tạo đối tượng CatDAO
        if (catDAO.getOneById(Integer.parseInt(objProduct.getId_cat())) == null) {
            Log.d("ProductDAO", "id_cat không hợp lệ: " + objProduct.getId_cat());
            return false; // Trả về false nếu id_cat không tồn tại
        }

        ContentValues values = new ContentValues();
        values.put("name", objProduct.getName());
        values.put("price", objProduct.getPrice());
        values.put("id_cat", objProduct.getId_cat());
        String[] params = {String.valueOf(objProduct.getId())};

        int rowsAffected = db.update("tb_product", values, "id = ?", params);
        return rowsAffected > 0;
    }

    public boolean deleteRow(int id) {
        String[] params = {String.valueOf(id)};
        int rowsDeleted = db.delete("tb_product", "id = ?", params);
        return rowsDeleted > 0;
    }

    public void close() {
        if (db != null && db.isOpen()) {
            db.close();
        }
    }
}
