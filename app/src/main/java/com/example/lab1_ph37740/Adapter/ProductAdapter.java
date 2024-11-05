package com.example.lab1_ph37740.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.lab1_ph37740.DTO.ProductDTO;
import com.example.lab1_ph37740.R;

import java.util.ArrayList;

public class ProductAdapter extends BaseAdapter {
    Context context;
    ArrayList<ProductDTO> listProduct;

    public ProductAdapter(Context context, ArrayList<ProductDTO> listProduct) {
        this.context = context;
        this.listProduct = listProduct;
    }

    @Override
    public int getCount() {
        return listProduct.size();
    }

    @Override
    public Object getItem(int i) {
        return listProduct.get(i);
    }

    @Override
    public long getItemId(int i) {
        return listProduct.get(i).getId();
    }

    @Override
    public View getView(int i, View v, ViewGroup viewGroup) {
        View row;
        if (v != null)
            row = v;
        else {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(com.example.lab1_ph37740.R.layout.row_list_product, null);
        }
        ProductDTO objProduct = listProduct.get(i);

        TextView tvId = row.findViewById(R.id.tv_idproduct);
        TextView tvName = row.findViewById(R.id.tv_nameproduct);
        TextView tvPrice = row.findViewById(R.id.tv_price);
        TextView tvId_cat = row.findViewById(R.id.tv_id_cat);

        tvId.setText(objProduct.getId() + "");
        tvName.setText(objProduct.getName());
        tvPrice.setText(objProduct.getPrice());
        tvId_cat.setText(objProduct.getId_cat());

        return row;
    }
}
