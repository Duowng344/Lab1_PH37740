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


    public ArrayList<ProductDTO> getList(){
        ArrayList<ProductDTO> listProduct = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT id, name, price, id_cat FROM tb_product ", null);
        if (c != null&& c.getCount()>0){
            c.moveToFirst();
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
            }while (c.moveToNext());
        }else {
            Log.d("zzzzzzzzz", "ProductDAO::getList: Khong lay duoc du lieu");
        }
        return listProduct;
    }

    public ProductDTO getOneById(int id){
        ProductDTO objProduct = null;
        String [] params = {String.valueOf(id)};
        Cursor c = db.rawQuery("SELECT id, name, price, id_cat FROM tb_product WHERE id = ? ", params);
        if (c != null&& c.getCount() ==1){
            objProduct = new ProductDTO();
            objProduct.setId(c.getInt(0));
            objProduct.setName(c.getString(1));
            objProduct.setPrice(c.getString(2));
            objProduct.setId_cat(c.getString(3));
        }
        return objProduct;
    }
    public boolean updateRow(ProductDTO objProduct){
        ContentValues v  = new ContentValues();
        v.put("name", objProduct.getName());
        v.put("price", objProduct.getPrice());
        v.put("id_cat", objProduct.getId_cat());
        String [] params = {String.valueOf(objProduct.getId())};
        long kq = db.update("tb_product", v,"id = ?", params);
        return kq > 0;
    }
    public boolean deleteRow(ProductDTO objProduct) {
        String[] params = {String.valueOf(objProduct.getId())};
        long kq = db.delete("tb_product", "id = ?", params);
        return kq > 0;
    }
}
