package com.semperchen.goodfoodhealthyrecipes.mobile.core.repository.dataservice;

import com.semperchen.goodfoodhealthyrecipes.mobile.core.entity.Recipe;
import java.util.List;


/**
 * Created by 卡你基巴 on 2015/11/1.
 */
public interface RecipeService {
    public boolean add(Recipe data);
    public boolean deleteByRecipe(Recipe data);
    public Recipe findByRecipeId(String id);
    public List<Recipe> findByName(String name);
    public void clearDao();
}
