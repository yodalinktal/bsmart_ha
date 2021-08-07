package bsmart.technology.rru.base.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    //数据库名称。
    public static final String DATABASE_NAME = "EACPass.db";

    //数据库版本号。
    public static int DATABASE_VERSION = 2;

    private static MySQLiteOpenHelper helper;


    public static MySQLiteOpenHelper getInstance(Context context) {
        if (helper == null) {
            helper = new MySQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        return helper;
    }

    public MySQLiteOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建数据库的表，如果不存在。
        db.execSQL(TableUtils.sql_create_position_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
