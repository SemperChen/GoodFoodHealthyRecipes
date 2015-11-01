package com.semperchen.goodfoodhealthyrecipes.mobile.core.repository.datadao;

import android.content.Context;
import android.widget.Toast;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.entity.Recipe;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.entity.RecipePreview;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.repository.database.DatabaseManager;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.repository.dataservice.RecipePreviewService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by 卡你基巴 on 2015/10/30.
 */
public class RecipePreviewDao implements RecipePreviewService {
    private Context mContext;
    private DatabaseManager dbManager;
    private Dao mDao;

    public RecipePreviewDao(Context context){
        mContext = context;
        dbManager = DatabaseManager.getInstance();
        mDao = dbManager.getDao(RecipePreview.class);
    }

    @Override
    public void clearDao(){
        dbManager.close();
    }

    @Override
    public boolean add(RecipePreview data) {
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
    public boolean delete(RecipePreview data) {
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
    public boolean updateByRecipeId(String recipeId, RecipePreview data) {
        boolean result = false;
        //未实现
        return result;
    }

    @Override
    public boolean update(RecipePreview data) {
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
    public RecipePreview findById(String id) {
        RecipePreview result = null;
        try {
            result = (RecipePreview) mDao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<RecipePreview> findByName(String name) {
        List<RecipePreview> recipePreviews = null;
        try {
            GenericRawResults<RecipePreview> result =  mDao.queryRaw("select * from table_reciperpreview where title like '%"+name+"%'");
            Iterator<RecipePreview> iterator = result.iterator();
            recipePreviews = new ArrayList<>();
            while(iterator.hasNext()){
                recipePreviews.add(iterator.next());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recipePreviews;
    }

    @Override
    public List<RecipePreview> findAll() {
        List<RecipePreview> list = null;
        try {
            list = mDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
