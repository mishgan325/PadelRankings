package com.example.padelrankings;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class PlayerDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "PlayerStats.db";

    private static final String TABLE_NAME = "PlayerStats";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NICKNAME = "nickname";
    public static final String COLUMN_PARTNER = "partner";
    public static final String COLUMN_GAMES = "games";
    public static final String COLUMN_WINS = "wins";
    public static final String COLUMN_WON_POINTS = "won_points";
    public static final String COLUMN_LOST_POINTS = "lost_points";
    public static final String COLUMN_TIEBREAKS = "tiebreaks";
    public static final String COLUMN_TIEBREAK_WINS = "tiebreak_wins";
    private static final int DATABASE_VERSION = 1;

    // Конструктор
    public PlayerDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Создание таблицы
        String SQL_CREATE_PLAYER_STATS_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
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

    public ArrayList<PlayerPartnerInfo> getPlayerPartnersInfo(String nickname) {
        ArrayList<PlayerPartnerInfo> partnersInfo = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String selection = PlayerDBHelper.COLUMN_NICKNAME + " = ?";
        String[] selectionArgs = { nickname };

        Cursor cursor = db.query(
                PlayerDBHelper.TABLE_NAME,   // Таблица для запроса
                null,                        // Все столбцы таблицы
                selection,                   // Условие выборки
                selectionArgs,               // Значения условия выборки
                null,                        // Группировка строк
                null,                        // Условие для группировки строк
                null                         // Сортировка результатов
        );

        while (cursor.moveToNext()) {

            String partner = cursor.getString(cursor.getColumnIndexOrThrow(PlayerDBHelper.COLUMN_PARTNER));
            int games = cursor.getInt(cursor.getColumnIndexOrThrow(PlayerDBHelper.COLUMN_GAMES));
            int wins = cursor.getInt(cursor.getColumnIndexOrThrow(PlayerDBHelper.COLUMN_WINS));
            int wonPoints = cursor.getInt(cursor.getColumnIndexOrThrow(PlayerDBHelper.COLUMN_WON_POINTS));
            int lostPoints = cursor.getInt(cursor.getColumnIndexOrThrow(PlayerDBHelper.COLUMN_LOST_POINTS));
            int tiebreaks = cursor.getInt(cursor.getColumnIndexOrThrow(PlayerDBHelper.COLUMN_TIEBREAKS));
            int tiebreakWins = cursor.getInt(cursor.getColumnIndexOrThrow(PlayerDBHelper.COLUMN_TIEBREAK_WINS));

            PlayerPartnerInfo partnerInfo = new PlayerPartnerInfo(partner, games, wins, wonPoints, lostPoints, tiebreaks, tiebreakWins);
            partnersInfo.add(partnerInfo);
        }

        cursor.close();

        selection = PlayerDBHelper.COLUMN_PARTNER + " = ?";
        cursor = db.query(
                PlayerDBHelper.TABLE_NAME,   // Таблица для запроса
                null,                        // Все столбцы таблицы
                selection,                   // Условие выборки
                selectionArgs,               // Значения условия выборки
                null,                        // Группировка строк
                null,                        // Условие для группировки строк
                null                         // Сортировка результатов
        );

        while (cursor.moveToNext()) {

            String partner = cursor.getString(cursor.getColumnIndexOrThrow(PlayerDBHelper.COLUMN_NICKNAME));
            int games = cursor.getInt(cursor.getColumnIndexOrThrow(PlayerDBHelper.COLUMN_GAMES));
            int wins = cursor.getInt(cursor.getColumnIndexOrThrow(PlayerDBHelper.COLUMN_WINS));
            int wonPoints = cursor.getInt(cursor.getColumnIndexOrThrow(PlayerDBHelper.COLUMN_WON_POINTS));
            int lostPoints = cursor.getInt(cursor.getColumnIndexOrThrow(PlayerDBHelper.COLUMN_LOST_POINTS));
            int tiebreaks = cursor.getInt(cursor.getColumnIndexOrThrow(PlayerDBHelper.COLUMN_TIEBREAKS));
            int tiebreakWins = cursor.getInt(cursor.getColumnIndexOrThrow(PlayerDBHelper.COLUMN_TIEBREAK_WINS));

            PlayerPartnerInfo partnerInfo = new PlayerPartnerInfo(partner, games, wins, wonPoints, lostPoints, tiebreaks, tiebreakWins);
            partnersInfo.add(partnerInfo);
        }

        cursor.close();
        db.close();
        return partnersInfo;
    }

    public void updateGameData(String player1, String player2, int wonPoints, int lostPoints) {

        SQLiteDatabase db = this.getWritableDatabase();

        String request = "SELECT * FROM " + TABLE_NAME + " WHERE (nickname = ? AND partner = ?) OR (partner = ? AND nickname = ?)";
        Cursor cursor = db.rawQuery(request, new String[]{player1, player2, player2, player1});

        if (cursor != null && cursor.moveToFirst()) {
            ContentValues values = new ContentValues();

            String nickname = cursor.getString(cursor.getColumnIndexOrThrow(PlayerDBHelper.COLUMN_NICKNAME));
            String partner = cursor.getString(cursor.getColumnIndexOrThrow(PlayerDBHelper.COLUMN_PARTNER));
            int games = cursor.getInt(cursor.getColumnIndexOrThrow(PlayerDBHelper.COLUMN_GAMES));
            int wins = cursor.getInt(cursor.getColumnIndexOrThrow(PlayerDBHelper.COLUMN_WINS));
            int prevWonPoints = cursor.getInt(cursor.getColumnIndexOrThrow(PlayerDBHelper.COLUMN_WON_POINTS));
            int prevLostPoints = cursor.getInt(cursor.getColumnIndexOrThrow(PlayerDBHelper.COLUMN_LOST_POINTS));
            int tiebreaks = cursor.getInt(cursor.getColumnIndexOrThrow(PlayerDBHelper.COLUMN_TIEBREAKS));
            int tiebreakWins = cursor.getInt(cursor.getColumnIndexOrThrow(PlayerDBHelper.COLUMN_TIEBREAK_WINS));

//            values.put("nickname", nickname);
//            values.put("partner", partner);
            values.put("games", games+1);
            if (isWin(wonPoints, lostPoints)) {
                values.put("wins", wins + 1);
            }
            values.put("won_points", prevWonPoints + wonPoints);
            values.put("lost_points", prevLostPoints + lostPoints);
            if (isTieBreak(wonPoints, lostPoints)) {
                values.put("tiebreaks", tiebreaks + 1);
                if (isWin(wonPoints, lostPoints)) {
                    values.put("tiebreak_wins", tiebreakWins+1);
                }
            }

            db.update(TABLE_NAME, values, "nickname = ? AND partner = ?", new String[]{nickname, partner});

            Log.d("Database players info", "updated "+values);

        } else {
            Log.e("Database search", "404");
        }

        db.close();
    }

    private boolean isTieBreak(int wonPoints, int lostPoints) {
        return Math.abs(wonPoints - lostPoints) == 1;
    }

    private boolean isWin(int wonPoints, int lostPoints) {
        return wonPoints > lostPoints;
    }
}