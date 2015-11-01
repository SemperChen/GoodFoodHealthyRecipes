package com.semperchen.goodfoodhealthyrecipes.mobile.core.repository.datadao;

import android.content.Context;
import android.widget.Toast;
import com.j256.ormlite.dao.Dao;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.entity.Recipe;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.entity.RecipeStep;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.repository.database.DatabaseManager;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.repository.dataservice.RecipeService;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by 卡你基巴 on 2015/11/1.
 */
public class RecipeDao implements RecipeService{
    private Context mContext;
    private DatabaseManager dbManager;
    private Dao mRecipeDao,mStepDao;

    public RecipeDao(Context context){
        mContext = context;
        dbManager = DatabaseManager.getInstance();
        mRecipeDao = dbManager.getDao(Recipe.class);
        mStepDao = dbManager.getDao(RecipeStep.class);
    }

    @Override
    public boolean add(Recipe data) {
        boolean result = false;
        try {
            mRecipeDao.create(data);
            for(RecipeStep step:data.getSteps()){
                step.setRecipeId(data.getRecipeId());
                mStepDao.create(step);
            }
            result = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean deleteByRecipe(Recipe data) {
        boolean result = false;
        try {
            mRecipeDao.executeRaw("delete from table_recipe where recipeId=" + data.getRecipeId());
            mStepDao.executeRaw("delete from table_recipestep where recipeId=" + data.getRecipeId());
            result = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Recipe findByRecipeId(String id) {
        Recipe recipe = null;
        try {
            List<Recipe> recipes = (List<Recipe>) mRecipeDao.queryForEq("recipeId",id);
            if(recipes == null || recipes.size() <= 0){
                return null;
            }
            recipe = recipes.get(0);
            List<RecipeStep> recipeSteps = mStepDao.queryForEq("recipeId", id);
            recipe.setSteps(recipeSteps);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recipe;
    }

    @Override
    public List<Recipe> findByName(String name) {
        List<Recipe> recipes = null;
        try {
            recipes = mRecipeDao.queryBuilder().where().like("title",name).query();
            if(recipes == null || recipes.size() <= 0){
                return null;
            }
            for(Recipe recipe:recipes){
                List<RecipeStep> recipeSteps = mStepDao.queryForEq("recipeId",recipe.getRecipeId());
                recipe.setSteps(recipeSteps);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recipes;
    }

    @Override
    public void clearDao() {
        dbManager.close();
    }
}
