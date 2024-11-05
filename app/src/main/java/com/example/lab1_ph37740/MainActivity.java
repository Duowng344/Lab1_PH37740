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
    Button btnAdd , btnUpdate, btnDelete;
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

        catAdapter = new CatAdapter(this,listCat);
        lvCat.setAdapter( catAdapter ); // gắn adapter vào listview

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String catName = edName.getText().toString();
                if(catName.length()<3){
                    Toast.makeText(MainActivity.this,
                            "Tên cân nhập ít nhất 3 ký tu", Toast.LENGTH_SHORT).show();
                    return;
                }
                CatDTO objCat = new CatDTO();
                objCat.setName( catName );
                int res = catDAO.AddRow(objCat);
                if (res>0){
                    listCat.clear();
                    listCat.addAll( catDAO.getList());
                    catAdapter.notifyDataSetChanged();
                }else {
                    Toast.makeText(MainActivity.this,
                            "Thêm thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });

        lvCat.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "onItemLongClick: i = " + i + ", l = " + l );
                objCurrentCat = listCat.get(i);
                edName.setText( objCurrentCat.getName() );
                return  true;

            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String catName = edName.getText().toString();
                objCurrentCat.setName(catName);
                boolean res = catDAO.UpdateRow( objCurrentCat );
                if(res){ // sửa thành cong
                    listCat.clear();
                    listCat.addAll( catDAO.getList());
                    catAdapter.notifyDataSetChanged();
                    objCurrentCat = null;// xóa dữ liệu tạm
                    edName.setText(""); // xóa trống ô text
                }else{
                    Toast.makeText(MainActivity.this,
                            "Lỗi không sửa được, có thể trùng dữ liệu...", Toast.LENGTH_SHORT).show();
                }


            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(objCurrentCat!= null){
                    boolean res = catDAO.DeleteRow(objCurrentCat);
                    if(res){
                        listCat.clear();
                        listCat.addAll( catDAO.getList());
                        catAdapter.notifyDataSetChanged();
                        objCurrentCat = null;// xóa dữ liệu tạm
                        edName.setText("");
                    }else{
                        Toast.makeText(MainActivity.this, "Lỗi xóa", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(MainActivity.this,
                            "Bấm giữ dòng để chọn bản ghi cần xóa", Toast.LENGTH_SHORT).show();
                }
            }
        });


//
//        objCat.setName("Oto");
//        int kq = catDAO.AddRow(objCat);
//        if (kq == -1){
//            Log.d(TAG, "Thêm thất bại");
//        }else {
//            Log.d(TAG, "Thêm thành công");
//        }
//
//        ArrayList<CatDTO> listCat = catDAO.getList();
//        for(int i =0; i< listCat.size(); i++){
//            CatDTO tmp = listCat.get(i);
//            Log.d(TAG, "onCreate: phan tu thu "+ i + " la: " + tmp.toString());
//        }

    }

}