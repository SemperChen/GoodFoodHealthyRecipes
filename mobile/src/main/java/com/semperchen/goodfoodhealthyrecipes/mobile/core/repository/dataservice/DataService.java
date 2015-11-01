package com.semperchen.goodfoodhealthyrecipes.mobile.core.repository.dataservice;

import java.util.List;

/**
 * Created by 卡你基巴 on 2015/10/30.
 */
public interface DataService<T> {
    public boolean add(T data);
    public boolean deleteByRecipeId(String recipeId);
    public boolean delete(T data);
    public boolean updateByRecipeId(String recipeId,T data);
    public boolean update(T data);
    public T findById(String id);
    public List<T> findAll();
    public void clearDao();
}
