package com.example.lab1_ph37740;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.lab1_ph37740.Adapter.CatAdapter;
import com.example.lab1_ph37740.DAO.CatDAO;
import com.example.lab1_ph37740.DTO.CatDTO;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    CatDAO catDAO;
    String TAG = "zzzzzzzzz";
    ArrayList<CatDTO> listCat;
    ListView lvCat;
    Button btnAdd, btnUpdate, btnDelete;
    EditText edName;
    CatAdapter catAdapter;
    CatDTO objCurrentCat = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        lvCat = findViewById(R.id.lv_cat);
        btnAdd = findViewById(R.id.btn_add);
        btnUpdate = findViewById(R.id.btn_update);
        btnDelete = findViewById(R.id.btn_delete);
        edName = findViewById(R.id.ed_catname);

        catDAO = new CatDAO(this);
        listCat = catDAO.getList();
        catAdapter = new CatAdapter(this, listCat);
        lvCat.setAdapter(catAdapter); // Gắn adapter vào listview

        // Tắt các nút cập nhật và xóa ban đầu
        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);

        btnAdd.setOnClickListener(view -> {
            String catName = edName.getText().toString();
            if (catName.length() < 3) {
                Toast.makeText(MainActivity.this, "Tên cần nhập ít nhất 3 ký tự", Toast.LENGTH_SHORT).show();
                return;
            }
            CatDTO objCat = new CatDTO();
            objCat.setName(catName);
            int res = catDAO.AddRow(objCat);
            if (res > 0) {
                listCat.clear();
                listCat.addAll(catDAO.getList());
                catAdapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
            } else if (res == -1) {
                Toast.makeText(MainActivity.this, "Tên đã tồn tại, vui lòng chọn tên khác", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
            }
        });

        lvCat.setOnItemLongClickListener((adapterView, view, i, l) -> {
            Log.d(TAG, "onItemLongClick: i = " + i + ", l = " + l);
            objCurrentCat = listCat.get(i);
            edName.setText(objCurrentCat.getName());
            btnUpdate.setEnabled(true); // Bật nút cập nhật
            btnDelete.setEnabled(true); // Bật nút xóa
            return true;
        });

        btnUpdate.setOnClickListener(view -> {
            if (objCurrentCat == null) {
                Toast.makeText(MainActivity.this, "Vui lòng chọn một bản ghi để cập nhật", Toast.LENGTH_SHORT).show();
                return;
            }
            String catName = edName.getText().toString();
            if (catName.length() < 3) {
                Toast.makeText(MainActivity.this, "Tên cần nhập ít nhất 3 ký tự", Toast.LENGTH_SHORT).show();
                return;
            }
            objCurrentCat.setName(catName);
            boolean res = catDAO.UpdateRow(objCurrentCat);
            if (res) {
                listCat.clear();
                listCat.addAll(catDAO.getList());
                catAdapter.notifyDataSetChanged();
                objCurrentCat = null;
                edName.setText(""); // Xóa trống ô text
                btnUpdate.setEnabled(false); // Tắt nút cập nhật
                btnDelete.setEnabled(false); // Tắt nút xóa
                Toast.makeText(MainActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Tên đã tồn tại, vui lòng chọn tên khác", Toast.LENGTH_SHORT).show();
            }
        });

        btnDelete.setOnClickListener(view -> {
            if (objCurrentCat == null) {
                Toast.makeText(MainActivity.this, "Vui lòng chọn một bản ghi để xóa", Toast.LENGTH_SHORT).show();
                return;
            }
            boolean res = catDAO.DeleteRow(objCurrentCat);
            if (res) {
                listCat.clear();
                listCat.addAll(catDAO.getList());
                catAdapter.notifyDataSetChanged();
                objCurrentCat = null;
                edName.setText(""); // Xóa trống ô text
                btnUpdate.setEnabled(false); // Tắt nút cập nhật
                btnDelete.setEnabled(false); // Tắt nút xóa
                Toast.makeText(MainActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        catDAO.close(); // Đóng cơ sở dữ liệu khi không còn cần thiết
    }
}
