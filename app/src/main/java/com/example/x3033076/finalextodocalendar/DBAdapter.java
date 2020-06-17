package com.example.x3033076.finalextodocalendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAdapter {

    private final static String DB_NAME = "todolist.db"; // DB名
    private final static String DB_TABLE = "todolist";   // DBのテーブル名
    private final static int DB_VERSION = 1;             // DBのバージョン

    public final static String COL_ID = "_id";            // id
    public final static String COL_TITLE = "title";       // タイトル
    public final static String COL_DEADLINE = "deadline"; // 期限
    public final static String COL_MEMO = "memo";         // メモ

    private SQLiteDatabase db = null;           // SQLiteDatabase
    private DBHelper dbHelper = null;           // DBHepler
    protected Context context;                  // Context

    public DBAdapter(Context context) {
        this.context = context;
        dbHelper = new DBHelper(this.context);
    }

    public DBAdapter openDB() {
        db = dbHelper.getWritableDatabase();        // DBの読み書き
        return this;
    }

    public DBAdapter readDB() {
        db = dbHelper.getReadableDatabase();        // DBの読み込み
        return this;
    }

    public void closeDB() {
        db.close();     // DBを閉じる
        db = null;
    }

    public void saveDB(String title, String deadline, String memo) {

        db.beginTransaction(); // トランザクション開始

        try {
            ContentValues values = new ContentValues(); // ContentValuesでデータを設定していく
            values.put(COL_TITLE, title);
            values.put(COL_DEADLINE, deadline);
            values.put(COL_MEMO, memo);

            db.insert(DB_TABLE, null, values); // レコードへ登録

            db.setTransactionSuccessful();      // トランザクションへコミット
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction(); // トランザクションの終了
        }
    }

    public Cursor getDB(String[] columns) {
        return db.query(DB_TABLE, columns, null, null, null, null, COL_DEADLINE);
    }

    public void selectDelete(String position) {

        db.beginTransaction(); // トランザクション開始
        try {
            db.delete(DB_TABLE, COL_ID + "=?", new String[]{position});
            db.setTransactionSuccessful();          // トランザクションへコミット
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction(); // トランザクションの終了
        }
    }

    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String createTbl = "CREATE TABLE " + DB_TABLE + " ("
                    + COL_ID + " INTEGER PRIMARY KEY,"
                    + COL_TITLE + " TEXT NOT NULL,"
                    + COL_DEADLINE + " TEXT NOT NULL,"
                    + COL_MEMO + " TEXT NOT NULL"
                    + ");";

            db.execSQL(createTbl); //SQL文の実行
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
