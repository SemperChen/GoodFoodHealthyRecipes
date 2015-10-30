package com.semperchen.goodfoodhealthyrecipes.mobile.core.repository.datadao;

import android.content.Context;
import android.widget.Toast;
import com.j256.ormlite.dao.Dao;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.entity.RecipePreview;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.repository.database.DatabaseManager;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.repository.dataservice.DataService;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by 卡你基巴 on 2015/10/30.
 */
public class DataDao<T> implements DataService<T>{
    private Context mContext;
    private Dao mDao;
    private DatabaseManager dbManager;

    public DataDao(Context context,Class clazz){
        try {
            mContext = context;
            dbManager = DatabaseManager.getInstance();
            mDao = dbManager.getDao(clazz);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean add(T data) {
        boolean result = false;
        try {
            mDao.create(data);
            result = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean deleteByRecipeId(String recipeId) {
        boolean result = false;
        try {
            mDao.executeRaw("delete from table_reciperpreview where recipeId="+recipeId);
            result = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean delete(T data) {
        boolean result = false;
        try {
            mDao.delete(data);
            result = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean updateByRecipeId(String recipeId, T data) {
        boolean result = false;
        //未实现
        return result;
    }

    @Override
    public boolean update(T data) {
        boolean result = false;
        try {
            mDao.update(data);
            result = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public T findById(String id) {
        T result = null;
        try {
            result = (T) mDao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<T> findAll() {
        List<T> list = null;
        try {
            list = mDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
