package ru.mishgan325.padelrankings.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import ru.mishgan325.padelrankings.entities.Player;

import java.util.ArrayList;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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

    public ArrayList<Player> getPlayers() {
        ArrayList<Player> recordsList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String nick = cursor.getString(1);
                String name = cursor.getString(2);
                int rank = cursor.getInt(3);

                Player tmp_player = new Player(id, nick, name, rank);

                recordsList.add(tmp_player);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return recordsList;
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
    public int getRankByNick(String nick) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        int rank = -1;

        try {
            // Выполнение запроса к базе данных
            cursor = db.query(
                    TABLE_NAME,
                    new String[]{COLUMN_CURRENT_RANKING}, // Замените COLUMN_SECOND_COLUMN на имя вашей второй колонки
                    COLUMN_NICK + "=?",
                    new String[]{nick},
                    null,
                    null,
                    null
            );

            // Обработка результатов запроса
            if (cursor != null && cursor.moveToFirst()) {
                rank = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CURRENT_RANKING));
            }
        } finally {
            // Закрываем курсор, чтобы избежать утечек памяти
            if (cursor != null) {
                cursor.close();
            }
            // Закрываем базу данных
            db.close();
        }

        return rank;
    }

    public void updateUserRank(String nick, int newRank) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CURRENT_RANKING, newRank);

        db.update(TABLE_NAME, values, COLUMN_NICK + "=?", new String[]{nick});
        db.close();
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


    public boolean importData(String fileContent) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            String[] lines = fileContent.split("\\r?\\n"); // Разделение содержимого файла на строки
            if (lines.length == 0) {
                // Файл пустой

                Log.e("DBHelper", "file is empty");

                return false;
            }

            // Пропускаем первую строку, так как она содержит заголовки столбцов
            String headerLine = lines[0];
            String[] values = headerLine.split("\t");

            if (!values[0].equals(COLUMN_ID) ||
                    !values[1].equals(COLUMN_NICK) || !values[2].equals(COLUMN_NAME) ||
                    !values[3].equals(COLUMN_CURRENT_RANKING)) {
                // Некорректный формат файла

                Log.e("DBHelper", "columns is wrong");

                return false;
            }

            db.beginTransaction();
            db.execSQL("DELETE FROM " + TABLE_NAME);

            for (int i = 4; i < values.length; i += 4) {

                Log.d("Line in file", values[i] + " " + values[i+1] + " " + values[i+2] + " " + values[i+3] );

                ContentValues contentValues = new ContentValues();
                contentValues.put(COLUMN_NICK, values[i+1]);
                contentValues.put(COLUMN_NAME, values[i+2]);
                try {
                    contentValues.put(COLUMN_CURRENT_RANKING, Integer.parseInt(values[i+3]));
                } catch (NumberFormatException e) {
                    // Некорректное значение для INTEGER

                    Log.e("DBHelper", "integer error");

                    return false;
                }

                db.insert(TABLE_NAME, null, contentValues);
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();

            return true;  // Успешное выполнение
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
            return false;  // Ошибка
        }
    }

    public String findNicknameById(long id) {
        String nickname = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_NICK}, COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            nickname = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NICK));
            cursor.close();
        }
        db.close();
        return nickname;
    }

    public void addPlayer(String nickname, String name, int currentRanking) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NICK, nickname);
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_CURRENT_RANKING, currentRanking);

        // Вставляем новую запись в базу данных
        long newRowId = db.insert(TABLE_NAME, null, values);

        // Проверяем успешность выполнения операции
        if (newRowId == -1) {
            // Произошла ошибка при добавлении записи
            Log.e("PlayerDBHelper", "Error inserting player into database");
        } else {
            // Запись успешно добавлена
            Log.d("PlayerDBHelper", "Player inserted into database with row ID: " + newRowId);
        }

        // Закрываем базу данных после использования
        db.close();
    }

    public void updatePlayer(String nickname, String name, int currentRanking, long userId) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NICK, nickname);
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_CURRENT_RANKING, currentRanking);


        db.update(TABLE_NAME, values, COLUMN_ID + "=" + userId, null);
        
        db.close();
    }

    public void deletePlayerById(long playerId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "_id = ?", new String[]{String.valueOf(playerId)});
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
