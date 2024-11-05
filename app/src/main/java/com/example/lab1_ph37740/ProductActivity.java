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

import com.example.lab1_ph37740.Adapter.ProductAdapter;
import com.example.lab1_ph37740.DAO.ProductDAO;
import com.example.lab1_ph37740.DTO.ProductDTO;

import java.util.ArrayList;

public class ProductActivity extends AppCompatActivity {
    ProductDAO productDAO;
    String TAG = "zzzzzzzzz";
    ArrayList<ProductDTO> listProduct;
    ListView lvProduct;
    Button btnAdd, btnUpdate, btnDelete;
    EditText edPName, edPrice, edIdCat;
    ProductAdapter productAdapter;
    ProductDTO objCurrentProduct = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_category);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        lvProduct = findViewById(R.id.lv_product);
        btnAdd = findViewById(R.id.btn_add);
        btnUpdate = findViewById(R.id.btn_update);
        btnDelete = findViewById(R.id.btn_delete);
        edPName = findViewById(R.id.ed_productname);
        edPrice = findViewById(R.id.ed_productprice);
        edIdCat = findViewById(R.id.ed_id_cat);

        productDAO = new ProductDAO(this);
        listProduct = productDAO.getList();
        productAdapter = new ProductAdapter(this, listProduct);
        lvProduct.setAdapter(productAdapter);

        btnAdd.setOnClickListener(view -> {
            String pName = edPName.getText().toString().trim();
            String price = edPrice.getText().toString().trim();
            String idCat = edIdCat.getText().toString().trim();
            if (pName.isEmpty() || price.isEmpty() || idCat.isEmpty()) {
                Toast.makeText(ProductActivity.this, "Không được bỏ trống", Toast.LENGTH_SHORT).show();
                return;
            }

            ProductDTO objProduct = new ProductDTO();
            objProduct.setName(pName);
            objProduct.setPrice(price);
            objProduct.setId_cat(idCat);
            int res = productDAO.addRow(objProduct);

            if (res > 0) {
                listProduct.clear();
                listProduct.addAll(productDAO.getList());
                productAdapter.notifyDataSetChanged();
                clearInputFields();
            } else {
                Toast.makeText(ProductActivity.this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
            }
        });

        lvProduct.setOnItemLongClickListener((adapterView, view, i, l) -> {
            Log.d(TAG, "onItemLongClick: i = " + i + ", l = " + l);
            objCurrentProduct = listProduct.get(i);
            edPName.setText(objCurrentProduct.getName());
            edPrice.setText(objCurrentProduct.getPrice());
            edIdCat.setText(objCurrentProduct.getId_cat());
            return true;
        });

        btnUpdate.setOnClickListener(view -> {
            if (objCurrentProduct == null) {
                Toast.makeText(ProductActivity.this, "Chọn sản phẩm để cập nhật", Toast.LENGTH_SHORT).show();
                return;
            }

            String pName = edPName.getText().toString().trim();
            String price = edPrice.getText().toString().trim();
            String idCat = edIdCat.getText().toString().trim();
            objCurrentProduct.setName(pName);
            objCurrentProduct.setPrice(price);
            objCurrentProduct.setId_cat(idCat);
            boolean res = productDAO.updateRow(objCurrentProduct);

            if (res) {
                listProduct.clear();
                listProduct.addAll(productDAO.getList());
                productAdapter.notifyDataSetChanged();
                objCurrentProduct = null;
                clearInputFields();
            } else {
                Toast.makeText(ProductActivity.this, "Lỗi không sửa được, có thể trùng dữ liệu...", Toast.LENGTH_SHORT).show();
            }
        });

        btnDelete.setOnClickListener(view -> {
            if (objCurrentProduct != null) {
                boolean res = productDAO.deleteRow(objCurrentProduct.getId());
                if (res) {
                    listProduct.clear();
                    listProduct.addAll(productDAO.getList());
                    productAdapter.notifyDataSetChanged();
                    objCurrentProduct = null;
                    clearInputFields();
                } else {
                    Toast.makeText(ProductActivity.this, "Lỗi xóa", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ProductActivity.this, "Bấm giữ dòng để chọn bản ghi cần xóa", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearInputFields() {
        edPName.setText("");
        edPrice.setText("");
        edIdCat.setText("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        productDAO.close(); // Đóng kết nối cơ sở dữ liệu
    }
}
