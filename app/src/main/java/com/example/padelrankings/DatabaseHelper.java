package com.example.padelrankings;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "rankings.db";
    private static final int DATABASE_VERSION = 1;
    public static final String COLUMN_ID = "_id";
    public static final String TABLE_NAME = "Rankings";
    public static final String COLUMN_NICK = "NICK";
    public static final String COLUMN_NAME = "NAME";
    public static final String COLUMN_CURRENT_RANKING = "RANK";

    private static final String CREATE_TABLE_SQL = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_NICK + " TEXT, " +
            COLUMN_NAME + " TEXT, " +
            COLUMN_CURRENT_RANKING + " INTEGER)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public ArrayList<String> getNicks() {
        ArrayList<String> nameList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            // Выполнение запроса к базе данных
            cursor = db.query(
                    this.TABLE_NAME,
                    new String[]{this.COLUMN_NICK},
                    null,
                    null,
                    null,
                    null,
                    null
            );

            // Обработка результатов запроса
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(this.COLUMN_NICK));
                    if (name != null) {
                        nameList.add(name);
                    }
                } while (cursor.moveToNext());
            }
        } finally {
            // Закрываем курсор, чтобы избежать утечек памяти
            if (cursor != null) {
                cursor.close();
            }
        }

        return nameList;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
