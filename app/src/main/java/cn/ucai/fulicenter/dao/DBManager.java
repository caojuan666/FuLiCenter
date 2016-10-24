package cn.ucai.fulicenter.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.bean.User;

/**
 * Created by Administrator on 2016/10/24 0024.
 */

//对数据库的具体操作
public class DBManager {
    private  static DBManager dbMgr = new DBManager();
    private  DBOpenHelper dbHelper;
    void onInit(Context context){
        dbHelper = new DBOpenHelper(context);

    }
    public static  synchronized   DBManager getInstance(){
        return dbMgr;

    }

    public synchronized void closeDB (){
        if(dbHelper!=null){
            dbHelper.closeDB();
        }
    }
//    保存数据

    public  boolean saveUser(User user){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserDao.USER_COLUMN_NAME,user.getMuserName());
        values.put(UserDao.USER_COLUMN_NICK,user.getMuserNick());
        values.put(UserDao.USER_COLUMN_AVATAR_ID, user.getMavatarId());
        values.put(UserDao.USER_COLUMN_AVATAR_TYPE,user.getMavatarType());
        values.put(UserDao.USER_COLUMN_AVATAR_SUFFIX,user.getMavatarSuffix());
        values.put(UserDao.USER_COLUMN_AVATAR_LASTUPDATE_TIME,user.getMavatarLastUpdateTime());
        if(db.isOpen()){
            return db.replace(UserDao.USER_TABLE_NAME,null,values)!=-1;
        }
        return false;
    }
//  根据一个字段获取整个字段
    public  synchronized User getUser(String username){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "select * from " + UserDao.USER_TABLE_NAME +" where "
                +UserDao.USER_COLUMN_NAME +" =?";
        User user = null;
        Cursor cursor = db.rawQuery(sql, new String[]{username});
        if(cursor.moveToNext()){
            user = new User();
            user.setMuserName(username);
            user.setMuserNick(cursor.getString(cursor.getColumnIndex(UserDao.USER_COLUMN_NICK)));
            user.setMavatarId(cursor.getInt(cursor.getColumnIndex(UserDao.USER_COLUMN_AVATAR_ID)));
            user.setMavatarType(cursor.getInt(cursor.getColumnIndex(UserDao.USER_COLUMN_AVATAR_TYPE)));
            user.setMavatarPath(cursor.getString(cursor.getColumnIndex(UserDao.USER_COLUMN_AVATAR_PATH)));
            user.setMavatarSuffix(cursor.getString(cursor.getColumnIndex(UserDao.USER_COLUMN_AVATAR_SUFFIX)));
            user.setMavatarLastUpdateTime(cursor.getString(cursor.getColumnIndex(UserDao.USER_COLUMN_AVATAR_LASTUPDATE_TIME)));
            return user;
        }


        return user;
    }
//    更新数据
    public synchronized boolean updateUser(User user){
        int result = -1;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = UserDao.USER_COLUMN_NAME+"=?";
        ContentValues values = new ContentValues();
        values.put(UserDao.USER_COLUMN_NICK,user.getMuserNick());
        if(db.isOpen()){
            result = db.update(UserDao.USER_TABLE_NAME, values,sql, new String[]{user.getMuserName()});
        }
        return result>0;
    }
}

