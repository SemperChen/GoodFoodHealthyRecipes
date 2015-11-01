package com.semperchen.goodfoodhealthyrecipes.mobile.core.repository.dataservice;

import com.semperchen.goodfoodhealthyrecipes.mobile.core.entity.RecipePreview;

import java.util.List;

/**
 * Created by 卡你基巴 on 2015/10/30.
 */
public interface RecipePreviewService {
    public boolean add(RecipePreview data);
    public boolean deleteByRecipeId(String recipeId);
    public boolean delete(RecipePreview data);
    public boolean updateByRecipeId(String recipeId,RecipePreview data);
    public boolean update(RecipePreview data);
    public RecipePreview findById(String id);
    public List<RecipePreview> findByName(String name);
    public List<RecipePreview> findAll();
    public void clearDao();
}
