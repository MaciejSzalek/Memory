package com.memory.memory;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Maciej Szalek on 2018-11-21.
 */

@DatabaseTable(tableName = Product.TABLE_PRODUCT)
public class Product {

    public static final String TABLE_PRODUCT = "product_table";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_PRODUCT = "product";
    public static final String COLUMN_CHECKED = "checked";

    public Product(){}

    @DatabaseField(columnName = COLUMN_ID,generatedId = true)
    private Integer id;

    @DatabaseField(columnName = COLUMN_PRODUCT)
    private String product;

    @DatabaseField(columnName = COLUMN_CHECKED)
    private Integer checked;


    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getProduct() {
        return product;
    }
    public void setProduct(String product) {
        this.product = product;
    }

    public Integer getChecked() {
        return checked;
    }
    public void setChecked(Integer checked) {
        this.checked = checked;
    }
}
