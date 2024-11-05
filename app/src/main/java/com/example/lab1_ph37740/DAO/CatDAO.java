package com.example.lab1_ph37740.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.lab1_ph37740.DTO.CatDTO;
import com.example.lab1_ph37740.Dbhelper.MyDbHelper;

import java.util.ArrayList;

public class CatDAO {
    private MyDbHelper dbHelper;
    private SQLiteDatabase db;
    private static final String TAG = "CatDAO";

    public CatDAO(Context context) {
        dbHelper = new MyDbHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    // Phương thức thêm một bản ghi
    public int AddRow(CatDTO objCat) {
        if (!isValidCat(objCat)) {
            return -1; // Nếu không hợp lệ, trả về -1
        }
        if (isCatNameExists(objCat.getName())) {
            return -2; // Nếu tên đã tồn tại, trả về -2
        }

        ContentValues values = new ContentValues();
        values.put("name", objCat.getName());
        long result = db.insert("tb_cat", null, values);
        return (result != -1) ? (int) result : -3; // Trả về -3 nếu thêm thất bại
    }

    // Kiểm tra xem tên mèo đã tồn tại chưa
    private boolean isCatNameExists(String name) {
        Cursor cursor = null;
        try {
            cursor = db.query("tb_cat", new String[]{"id"}, "name=?", new String[]{name}, null, null, null);
            return cursor.moveToFirst();
        } finally {
            if (cursor != null) {
                cursor.close(); // Đóng cursor
            }
        }
    }

    // Kiểm tra tính hợp lệ của CatDTO
    private boolean isValidCat(CatDTO cat) {
        return cat != null && cat.getName() != null && cat.getName().length() >= 3; // Tên mèo phải có ít nhất 3 ký tự
    }

    // Lấy danh sách tất cả mèo
    public ArrayList<CatDTO> getList() {
        ArrayList<CatDTO> listCat = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT id, name FROM tb_cat", null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                CatDTO objCat = new CatDTO();
                objCat.setId(cursor.getInt(0));
                objCat.setName(cursor.getString(1));
                listCat.add(objCat);
            } while (cursor.moveToNext());
        } else {
            Log.d(TAG, "getList: Không lấy được dữ liệu");
        }
        cursor.close(); // Đóng cursor
        return listCat;
    }

    // Lấy một mèo theo ID
    public CatDTO getOneById(int id) {
        CatDTO objCat = null;
        String[] params = {String.valueOf(id)};
        Cursor cursor = db.rawQuery("SELECT id, name FROM tb_cat WHERE id = ?", params);
        if (cursor != null && cursor.getCount() == 1) {
            cursor.moveToFirst();
            objCat = new CatDTO();
            objCat.setId(cursor.getInt(0));
            objCat.setName(cursor.getString(1));
        }
        cursor.close(); // Đóng cursor
        return objCat;
    }

    // Cập nhật một bản ghi
    public boolean UpdateRow(CatDTO objCat) {
        if (objCat == null || objCat.getId() <= 0 || !isValidCat(objCat)) {
            return false; // Nếu không hợp lệ hoặc không có ID, trả về false
        }

        // Kiểm tra xem tên mèo mới có trùng với tên mèo khác không
        if (isCatNameExistsExceptId(objCat.getName(), objCat.getId())) {
            return false; // Nếu tên đã tồn tại cho bản ghi khác, trả về false
        }

        ContentValues values = new ContentValues();
        values.put("name", objCat.getName());
        String[] params = {String.valueOf(objCat.getId())};
        long result = db.update("tb_cat", values, "id = ?", params);
        return result > 0; // Trả về true nếu cập nhật thành công
    }

    // Kiểm tra xem tên mèo đã tồn tại trong cơ sở dữ liệu ngoại trừ ID hiện tại
    private boolean isCatNameExistsExceptId(String name, int id) {
        Cursor cursor = null;
        try {
            cursor = db.query("tb_cat", new String[]{"id"}, "name=? AND id != ?", new String[]{name, String.valueOf(id)}, null, null, null);
            return cursor.moveToFirst();
        } finally {
            if (cursor != null) {
                cursor.close(); // Đóng cursor
            }
        }
    }

    // Xóa một bản ghi
    public boolean DeleteRow(CatDTO objCat) {
        if (objCat == null || objCat.getId() <= 0) {
            return false; // Nếu không có bản ghi để xóa, trả về false
        }

        String[] params = {String.valueOf(objCat.getId())};
        long result = db.delete("tb_cat", "id = ?", params);
        return result > 0; // Trả về true nếu xóa thành công
    }

    // Đóng cơ sở dữ liệu khi không còn cần thiết
    public void close() {
        if (db != null && db.isOpen()) {
            db.close();
        }
    }
}
