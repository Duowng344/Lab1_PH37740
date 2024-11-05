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
    MyDbHelper dbHelper;
    SQLiteDatabase db;

    public CatDAO(Context context){
        dbHelper = new MyDbHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public int AddRow (CatDTO objCat){
        ContentValues v  = new ContentValues();
        v.put("name", objCat.getName());
        int kq  = (int) db.insert("tb_cat", null, v);
        return kq;
    }

    public ArrayList<CatDTO> getList(){
        ArrayList<CatDTO> listCat = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT id, name FROM tb_cat ", null);
        if (c != null&& c.getCount()>0){
            c.moveToFirst();
            do {
                int id = c.getInt(0);
                String name = c.getString(1);
                CatDTO objCat = new CatDTO();
                objCat.setId(id);
                objCat.setName(name);
                listCat.add(objCat);
            }while (c.moveToNext());
        }else {
            Log.d("zzzzzzzzz", "CatDAO::getList: Khong lay duoc du lieu");
        }
        return listCat;

    }
    public CatDTO getOneById(int id){
        CatDTO objCat = null;
        String [] params = {String.valueOf(id)};
        Cursor c = db.rawQuery("SELECT id, name FROM tb_cat WHERE id = ? ", params);
        if (c != null&& c.getCount() ==1){
            objCat = new CatDTO();
            objCat.setId(c.getInt(0));
            objCat.setName(c.getString(1));
        }
        return objCat;

    }
    public boolean UpdateRow(CatDTO objCat){
        ContentValues v  = new ContentValues();
        v.put("name", objCat.getName());
        String [] params = {String.valueOf(objCat.getId())};
        long kq = db.update("tb_cat", v,"id = ?", params);
        return kq > 0;

    }

    public boolean DeleteRow(CatDTO objCat) {
        String[] params = {String.valueOf(objCat.getId())};
        long kq = db.delete("tb_cat", "id = ?", params);
        return kq > 0;
    }
}
