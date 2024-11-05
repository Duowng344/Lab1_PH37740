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

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pName = edPName.getText().toString();
                String price = edPrice.getText().toString();
                String idCat = edIdCat.getText().toString();
                if (pName.isEmpty() || price.isEmpty() || idCat.isEmpty()) {
                    Toast.makeText(ProductActivity.this, "Không được bỏ trống bất kỳ trường nào", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(ProductActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProductActivity.this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });

        lvProduct.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "onItemLongClick: i = " + i + ", l = " + l);
                objCurrentProduct = listProduct.get(i);
                edPName.setText(objCurrentProduct.getName());
                edPrice.setText(objCurrentProduct.getPrice());
                edIdCat.setText(objCurrentProduct.getId_cat());
                return true;
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (objCurrentProduct != null) {
                    String pName = edPName.getText().toString();
                    String price = edPrice.getText().toString();
                    String idCat = edIdCat.getText().toString();
                    objCurrentProduct.setName(pName);
                    objCurrentProduct.setPrice(price);
                    objCurrentProduct.setId_cat(idCat);
                    boolean res = productDAO.updateRow(objCurrentProduct);
                    if (res) {
                        listProduct.clear();
                        listProduct.addAll(productDAO.getList());
                        productAdapter.notifyDataSetChanged();
                        objCurrentProduct = null;
                        edPName.setText("");
                        edPrice.setText("");
                        edIdCat.setText("");
                        Toast.makeText(ProductActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ProductActivity.this, "Lỗi: không thể cập nhật. Có thể dữ liệu trùng", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ProductActivity.this, "Vui lòng chọn một sản phẩm để cập nhật", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (objCurrentProduct != null) {
                    boolean res = productDAO.deleteRow(objCurrentProduct.getId());
                    if (res) {
                        listProduct.clear();
                        listProduct.addAll(productDAO.getList());
                        productAdapter.notifyDataSetChanged();
                        objCurrentProduct = null;
                        edPName.setText("");
                        edPrice.setText("");
                        edIdCat.setText("");
                        Toast.makeText(ProductActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ProductActivity.this, "Lỗi: không thể xóa sản phẩm", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ProductActivity.this, "Bấm giữ một dòng để chọn sản phẩm cần xóa", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
