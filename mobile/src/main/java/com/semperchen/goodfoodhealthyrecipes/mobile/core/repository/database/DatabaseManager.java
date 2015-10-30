package com.semperchen.goodfoodhealthyrecipes.mobile.core.repository.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.entity.RecipePreview;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.entity.RecipePreviewData;

import java.sql.SQLException;

/**
 * <p>数据库管理器</p>
 * <p/>
 * <p>>Project HealthExpress.</p>
 * <p>Date 2015/9/3.</p>
 *
 * @author SemperChen
 * @version 1.0-SNAPSHOT
 */
public class DatabaseManager extends OrmLiteSqliteOpenHelper {

    private static final String TAG=DatabaseManager.class.getSimpleName();
    private static final boolean DEBUG=true;

    private static final String DATABASE_NAME="goodfoodhealthyrecipes.db";
    private static final int DATABASE_VERSION=1;

    private static DatabaseManager instance;

    public DatabaseManager(Context context, String databaseName, SQLiteDatabase.CursorFactory factory, int databaseVersion) {
        super(context, databaseName, factory, databaseVersion);
    }

    /**
     * 初始化
     *
     * @param context 上下文
     */
    public static synchronized void init(Context context){
        if(instance==null){
            instance =new DatabaseManager(context,DATABASE_NAME,null,DATABASE_VERSION);
        }
    }

    /**
     * 获取唯一实例
     *
     * @return DatabaseManager实例
     */
    public  static synchronized DatabaseManager getInstance(){
        if (instance==null){
            throw new IllegalStateException(TAG + " DatabaseManager实例没有被初始化。");
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
//            TableUtils.createTable(connectionSource, RecipePreviewData.class);
            TableUtils.createTable(connectionSource, RecipePreview.class);
//            TableUtils.createTable(connectionSource, Recipe.class);
            //TableUtils.createTable(connectionSource, RecipeStep.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {
        try {
//            TableUtils.dropTable(connectionSource, RecipePreviewData.class, true);
            TableUtils.dropTable(connectionSource, RecipePreview.class, true);
            //TableUtils.dropTable(connectionSource, Recipe.class,true);
           // TableUtils.dropTable(connectionSource, RecipeStep.class,true);
            onCreate(sqLiteDatabase,connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获得userDao
     *
     * @return
     */
    public Dao getDao() throws SQLException {
        return getDao(RecipePreview.class);
    }

    @Override
    public void close() {
        super.close();
    }

    public void resetTable(Class clazz) {
        try {
            TableUtils.dropTable(connectionSource, clazz, true);
            TableUtils.createTable(connectionSource, clazz);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
