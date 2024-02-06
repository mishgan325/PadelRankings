package com.example.padelrankings;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PlayerDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "PlayerStats.db";
    private static final int DATABASE_VERSION = 1;

    // Конструктор
    public PlayerDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Создание таблицы
        String SQL_CREATE_PLAYER_STATS_TABLE = "CREATE TABLE PlayerStats ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "nickname TEXT NOT NULL, "
                + "partner TEXT, "
                + "games INTEGER DEFAULT 0, "
                + "wins INTEGER DEFAULT 0, "
                + "won_points INTEGER DEFAULT 0, "
                + "lost_points INTEGER DEFAULT 0, "
                + "tiebreaks INTEGER DEFAULT 0, "
                + "tiebreak_wins INTEGER DEFAULT 0)";
        db.execSQL(SQL_CREATE_PLAYER_STATS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Обновление базы данных, если необходимо
        // В данном примере просто удаляем старую таблицу и создаем новую
        db.execSQL("DROP TABLE IF EXISTS PlayerStats");
        onCreate(db);
    }


    public void addPartnerToPlayer(String playerNickname, String partnerNickname) {
        // Получение доступа к базе данных для записи
        SQLiteDatabase db = this.getWritableDatabase();

        // Подготовка данных для вставки в базу данных
        ContentValues values = new ContentValues();
        values.put("nickname", playerNickname);
        values.put("partner", partnerNickname);

        // Вставка строки в таблицу
        db.insert("PlayerStats", null, values);

        // Закрытие базы данных после использования
        db.close();
    }

}
