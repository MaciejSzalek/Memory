package com.memory.memory;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Maciej Szalek on 2018-11-21.
 */

public class DBHelper extends OrmLiteSqliteOpenHelper {

    private static final String DB_NAME = "memory.db";
    private static final int DB_VERSION = 1;
    private Dao<Tasks, String> tasksDao = null;
    private Dao<Product, String> productDao = null;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try{
            TableUtils.createTable(connectionSource, Tasks.class);
            TableUtils.createTable(connectionSource, Product.class);
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try{
            TableUtils.dropTable(connectionSource, Product.class, true);
            TableUtils.dropTable(connectionSource, Tasks.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    public void updateTaskById(Integer id, String posStr) throws SQLException {
        UpdateBuilder<Tasks, String> updateBuilder = tasksDao.updateBuilder();
        updateBuilder.where().eq("id", id);
        updateBuilder.updateColumnValue("task", posStr);
        updateBuilder.update();
    }
    public void updateProductById(Integer id, String posStr) throws SQLException {
        UpdateBuilder<Product, String> updateBuilder = productDao.updateBuilder();
        updateBuilder.where().eq("id", id);
        updateBuilder.updateColumnValue("product", posStr);
        updateBuilder.update();
    }


    public void updateTaskCheckedById(Integer id, Integer checked) throws SQLException {
        UpdateBuilder<Tasks, String> updateBuilder = tasksDao.updateBuilder();
        updateBuilder.where().eq("id", id);
        updateBuilder.updateColumnValue("checked", checked);
        updateBuilder.update();
    }
    public void updateProductCheckedById(Integer id, Integer checked) throws SQLException {
        UpdateBuilder<Product, String> updateBuilder = productDao.updateBuilder();
        updateBuilder.where().eq("id", id);
        updateBuilder.updateColumnValue("checked", checked);
        updateBuilder.update();
    }


    public int deleteTaskById(Tasks id) throws SQLException {
        getTasksDao();
        return tasksDao.delete(id);
    }
    public int deleteProductById(Product id) throws SQLException {
        getProductDao();
        return productDao.delete(id);
    }


    public List<Tasks> getAllTasks() throws SQLException {
        getTasksDao();
        return tasksDao.queryForAll();
    }
    public List<Product> getAllProduct() throws SQLException {
        getProductDao();
        return productDao.queryForAll();
    }

    public int createNewTasks(Tasks tasks) throws SQLException{
        getTasksDao();
        return tasksDao.create(tasks);
    }

    public int createProduct(Product product) throws SQLException {
        getProductDao();
        return productDao.create(product);
    }

    public Dao.CreateOrUpdateStatus createOrUpdateStatus(Tasks object) throws SQLException {
        getTasksDao();
        return tasksDao.createOrUpdate(object);
    }
    public Dao.CreateOrUpdateStatus createOrUpdateProduct(Product object) throws SQLException {
        getProductDao();
        return productDao.createOrUpdate(object);
    }


    public Dao<Tasks, String> getTasksDao() throws SQLException {
        if(tasksDao == null){
            tasksDao = getDao(Tasks.class);
        }
        return tasksDao;
    }
    public Dao<Product, String> getProductDao() throws SQLException {
        if(productDao == null){
            productDao = getDao(Product.class);
        }
        return productDao;
    }
    @Override
    public void close(){
        tasksDao = null;
        productDao = null;
        super.close();
    }
}
