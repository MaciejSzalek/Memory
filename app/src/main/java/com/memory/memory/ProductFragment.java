package com.memory.memory;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Maciej Szalek on 2018-11-17.
 */

public class ProductFragment extends Fragment {

    private DBHelper dbHelper;
    public DialogManager dialogManager;

    public ImageButton addButton;
    public ImageButton editButton;
    public ImageButton deleteButton;

    private List<String> productList = new ArrayList<>();
    private List<Integer> productChecked = new ArrayList<>();
    private Map<String, Integer> productMap = new HashMap<>();
    private ListView productListView;
    public ArrayAdapter arrayAdapter;

    private String positionStr;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle saveInstanceState){
        View subjectFragment = inflater.inflate(R.layout.subject_layout, container, false);

        productListView = subjectFragment.findViewById(R.id.subject_list_view);
        addButton = subjectFragment.findViewById(R.id.add_subject_button);
        editButton = subjectFragment.findViewById(R.id.edit_subject_button);
        deleteButton = subjectFragment.findViewById(R.id.delete_subject_button);

        dbHelper = new DBHelper(getContext());

        getAllProductFromSQL();
        setCheckStatus();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNewPositionDialog();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteAllMarkedDialog();
            }
        });

        productListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Integer productId = productMap.get(productList.get(position));
                if(productListView.isItemChecked(position)){
                    updateProductCheckedById(productId, 1);
                }else{
                    updateProductCheckedById(productId, 0);
                }
            }
        });

        productListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Integer productId = productMap.get(productList.get(position));
                String posStr = productList.get(position);
                showEditDialog(productId, posStr);
                return false;
            }
        });
        return subjectFragment;
    }

    private void showEditDialog(final Integer id, final String posStr){
        String title = getResources().getString(R.string.edit_subject);
        dialogManager = new DialogManager(getContext());
        DialogManager.Action action = new DialogManager.Action() {
            @Override
            public void setConfirmButton() {
                updateProductById(id, positionStr);
                getAllProductFromSQL();
                setCheckStatus();
            }

            @Override
            public void setDeleteButton() {
                deleteProductById(posStr);
                getAllProductFromSQL();
                setCheckStatus();
            }

            @Override
            public void setPositionString(String positionString) {
                positionStr = positionString;
            }
        };
        dialogManager.editPositionDialogBuilder(title, posStr, action);
    }

    private void showNewPositionDialog(){
        String title = getResources().getString(R.string.new_product);
        dialogManager = new DialogManager(getContext());
        DialogManager.Action action = new DialogManager.Action() {
            @Override
            public void setConfirmButton() {
                if(positionStr != null){
                    addNewPosition(positionStr);
                }
                getAllProductFromSQL();
                setCheckStatus();
            }
            @Override
            public void setDeleteButton() {}
            @Override
            public void setPositionString(String positionString) {
                positionStr = positionString;
            }
        };
        dialogManager.addNewPositionDialogBuilder(title, action);
    }

    private void showDeleteAllMarkedDialog(){
        dialogManager = new DialogManager(getContext());
        DialogManager.Action action = new DialogManager.Action() {
            @Override
            public void setConfirmButton() {
                deleteAllMarked();
                getAllProductFromSQL();
            }

            @Override
            public void setDeleteButton() {}
            @Override
            public void setPositionString(String positionString) {}
        };
        dialogManager.deleteAllCheckedDialogBuilder(action);
    }

    private void addNewPosition(String str){
        Product product = new Product();
        product.setProduct(str);
        product.setChecked(0);
        try {
            dbHelper.createOrUpdateProduct(product);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void updateProductById(Integer id, String posStr){
        try {
            dbHelper.updateProductById(id, posStr);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateProductCheckedById(Integer id, Integer check){
        try {
            dbHelper.updateProductCheckedById(id, check);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void deleteAllMarked(){
        for(int i=0; i<productListView.getCount(); i++){
            if(productListView.isItemChecked(i)){
                String task = (String) productListView.getItemAtPosition(i);
                deleteProductById(task);
            }
        }
    }

    private void deleteProductById(String product){
        try {
            Product prod = new Product();
            prod.setId(productMap.get(product));
            dbHelper.deleteProductById(prod);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setCheckStatus(){
        for(int i=0; i < productChecked.size(); i++){
            Integer checked = productChecked.get(i);
            if(checked == 0){
                productListView.setItemChecked(i, false);
            }else{
                productListView.setItemChecked(i, true);
            }
        }
    }

    private void getAllProductFromSQL(){
        if(productList.size() > 0){
            productList.clear();
            productChecked.clear();
            productMap.clear();
        }
        try {
            List<Product> prodList = dbHelper.getAllProduct();
            for(Product product: prodList){
                productList.add(product.getProduct());
                productChecked.add(product.getChecked());
                productMap.put(product.getProduct(), product.getId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(getContext() != null){
            Collections.sort(productList);
            arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_checked,
                    productList);
            productListView.setAdapter(arrayAdapter);
        }
    }
    @Override
    public void onResume(){
        super.onResume();
        getAllProductFromSQL();
        setCheckStatus();
    }
}
