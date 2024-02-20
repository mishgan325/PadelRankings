package com.example.padelrankings;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class PlayerDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "PlayerStats.db";

    private static final String TABLE_NAME = "PlayerStats";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_PLAYER1 = "player1";
    public static final String COLUMN_PLAYER2 = "player2";
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
        String SQL_CREATE_PLAYER_STATS_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_PLAYER1 + " TEXT NOT NULL, "
                + COLUMN_PLAYER2 + " TEXT, "
                + COLUMN_GAMES + " INTEGER DEFAULT 0, "
                + COLUMN_WINS + " INTEGER DEFAULT 0, "
                + COLUMN_WON_POINTS + " INTEGER DEFAULT 0, "
                + COLUMN_LOST_POINTS + " INTEGER DEFAULT 0, "
                + COLUMN_TIEBREAKS + " INTEGER DEFAULT 0, "
                + COLUMN_TIEBREAK_WINS + " INTEGER DEFAULT 0)";
        db.execSQL(SQL_CREATE_PLAYER_STATS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS PlayerStats");
        onCreate(db);
    }


    public void addPartnerToPlayer(String player1, String player2) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("player1", player1);
        values.put("player2", player2);

        // Вставка строки в таблицу
        db.insert("PlayerStats", null, values);

        // Закрытие базы данных после использования
        db.close();
    }

    public ArrayList<PlayerPartnerInfo> getPlayerPartnersInfo(String nickname) {
        ArrayList<PlayerPartnerInfo> partnersInfo = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String selection = PlayerDBHelper.COLUMN_PLAYER1 + " = ?";
        String[] selectionArgs = { nickname };

        Cursor cursor = db.query(
                TABLE_NAME,   // Таблица для запроса
                null,                        // Все столбцы таблицы
                selection,                   // Условие выборки
                selectionArgs,               // Значения условия выборки
                null,                        // Группировка строк
                null,                        // Условие для группировки строк
                null                         // Сортировка результатов
        );

        while (cursor.moveToNext()) {

            String partner = cursor.getString(cursor.getColumnIndexOrThrow(PlayerDBHelper.COLUMN_PLAYER2));
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

        selection = PlayerDBHelper.COLUMN_PLAYER2 + " = ?";
        cursor = db.query(
                TABLE_NAME,   // Таблица для запроса
                null,                        // Все столбцы таблицы
                selection,                   // Условие выборки
                selectionArgs,               // Значения условия выборки
                null,                        // Группировка строк
                null,                        // Условие для группировки строк
                null                         // Сортировка результатов
        );

        while (cursor.moveToNext()) {

            String partner = cursor.getString(cursor.getColumnIndexOrThrow(PlayerDBHelper.COLUMN_PLAYER1));
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

        String request = "SELECT * FROM " + TABLE_NAME + " WHERE (" + COLUMN_PLAYER1 + " = ? AND " + COLUMN_PLAYER2 + " = ?) OR (" + COLUMN_PLAYER2 + " = ? AND " + COLUMN_PLAYER1 + " = ?)";
        Cursor cursor = db.rawQuery(request, new String[]{player1, player2, player1, player2});

        if (cursor != null && cursor.moveToFirst()) {
            ContentValues values = new ContentValues();

            String nickname = cursor.getString(cursor.getColumnIndexOrThrow(PlayerDBHelper.COLUMN_PLAYER1));
            String partner = cursor.getString(cursor.getColumnIndexOrThrow(PlayerDBHelper.COLUMN_PLAYER2));
            int games = cursor.getInt(cursor.getColumnIndexOrThrow(PlayerDBHelper.COLUMN_GAMES));
            int wins = cursor.getInt(cursor.getColumnIndexOrThrow(PlayerDBHelper.COLUMN_WINS));
            int prevWonPoints = cursor.getInt(cursor.getColumnIndexOrThrow(PlayerDBHelper.COLUMN_WON_POINTS));
            int prevLostPoints = cursor.getInt(cursor.getColumnIndexOrThrow(PlayerDBHelper.COLUMN_LOST_POINTS));
            int tiebreaks = cursor.getInt(cursor.getColumnIndexOrThrow(PlayerDBHelper.COLUMN_TIEBREAKS));
            int tiebreakWins = cursor.getInt(cursor.getColumnIndexOrThrow(PlayerDBHelper.COLUMN_TIEBREAK_WINS));

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

            db.update(TABLE_NAME, values, COLUMN_PLAYER1 + " = ? AND " + COLUMN_PLAYER2 + " = ?", new String[]{nickname, partner});

            Log.i("Database players info", "updated "+values);

            db.close();
        } else {
            Log.i("Database search", "404, creating new pair");
            addPartnerToPlayer(player1, player2);
            updateGameData(player1, player2, wonPoints, lostPoints);
        }
    }

    public boolean exportData(String fileName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        // Папка приложения в директории Downloads
        File appFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "PadelRankings");
        if (!appFolder.exists()) {
            appFolder.mkdirs();
        }

        try (FileWriter writer = new FileWriter(new File(appFolder, fileName))) {
            // Запись заголовков столбцов
            for (String columnName : cursor.getColumnNames()) {
                writer.write(columnName + "\t");
            }
            writer.write("\n");

            // Запись данных
            while (cursor.moveToNext()) {
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    writer.write(cursor.getString(i) + "\t");
                }
                writer.write("\n");
            }

            return true;  // Успешное выполнение
        } catch (IOException e) {
            e.printStackTrace();
            return false;  // Ошибка
        } finally {
            cursor.close();
        }
    }

    public boolean importData(String filePath) {
        SQLiteDatabase db = this.getWritableDatabase();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            db.beginTransaction();
            // Пропускаем первую строку, так как она содержит заголовки столбцов
            String headerLine = reader.readLine();
            if (headerLine == null) {
                return false;
            }

            String[] headerColumns = headerLine.split("\t");
            if (headerColumns.length != 9 || !headerColumns[0].equals(COLUMN_ID) ||
                    !headerColumns[1].equals(COLUMN_PLAYER1) || !headerColumns[2].equals(COLUMN_PLAYER2) ||
                    !headerColumns[3].equals(COLUMN_GAMES) || !headerColumns[4].equals(COLUMN_WINS) ||
                    !headerColumns[5].equals(COLUMN_WON_POINTS) || !headerColumns[6].equals(COLUMN_LOST_POINTS) ||
                    !headerColumns[7].equals(COLUMN_TIEBREAKS) || !headerColumns[8].equals(COLUMN_TIEBREAK_WINS)) {
                return false;
            }

            db.execSQL("DELETE FROM " + TABLE_NAME);
            while ((line = reader.readLine()) != null) {
                String[] values = line.split("\t");

                if (values.length == 9) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(COLUMN_PLAYER1, values[1]);
                    contentValues.put(COLUMN_PLAYER2, values[2]);
                    try {
                        contentValues.put(COLUMN_GAMES, Integer.parseInt(values[3]));
                        contentValues.put(COLUMN_WINS, Integer.parseInt(values[4]));
                        contentValues.put(COLUMN_WON_POINTS, Integer.parseInt(values[5]));
                        contentValues.put(COLUMN_LOST_POINTS, Integer.parseInt(values[6]));
                        contentValues.put(COLUMN_TIEBREAKS, Integer.parseInt(values[7]));
                        contentValues.put(COLUMN_TIEBREAK_WINS, Integer.parseInt(values[8]));
                    } catch (NumberFormatException e) {
                        return false;
                    }

                    db.insert(TABLE_NAME, null, contentValues);
                } else {
                    // Некорректное количество значений в строке
                    return false;
                }
            }
            db.setTransactionSuccessful();

            return true;  // Успешное выполнение
        } catch (IOException e) {
            e.printStackTrace();
            return false;  // Ошибка
        } finally {
            db.endTransaction();
        }
    }

    private boolean isTieBreak(int wonPoints, int lostPoints) {
        return Math.abs(wonPoints - lostPoints) == 1;
    }

    private boolean isWin(int wonPoints, int lostPoints) {
        return wonPoints > lostPoints;
    }
}