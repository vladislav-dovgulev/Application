package com.carpark.application.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Класс реализующий надстройку над "SQLiteOpenHelper".
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    /**
     * Имя базы данных
     */
    private static String DB_NAME = "base.db3";
    /**
     * Путь к базе данных
     */
    private static String DB_PATH = "";
    /**
     * Версия базы данных
     */
    private static final int DB_VERSION = 25;
    /**
     * Объект базы данных SQLite
     */
    private SQLiteDatabase mDataBase;
    /**
     * Контекст
     */
    private final Context mContext;
    /**
     * Нуждается ли база данных в обновлении
     */
    private boolean mNeedUpdate = false;

    /**
     * Конструктор класса
     * @param context Контекст
     */
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        if (android.os.Build.VERSION.SDK_INT >= 17)
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        else
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        this.mContext = context;
        copyDataBase();
        this.getReadableDatabase();
    }

    /**
     * Метод обновления используемой базы данных приложения
     * @throws IOException Создаваемое методом исключение (в случае ошибки)
     */
    public void updateDataBase() throws IOException {
        if (mNeedUpdate) {
            File dbFile = new File(DB_PATH + DB_NAME);
            if (dbFile.exists())
                dbFile.delete();
            copyDataBase();
            mNeedUpdate = false;
        }
    }

    /**
     * Метод проверки "существует ли база данных приложения"
     * @return
     */
    private boolean checkDataBase() {
        File dbFile = new File(DB_PATH + DB_NAME);
        return dbFile.exists();
    }

    /**
     * Метод инициализирующий копирование базы данных из пакета приложения в папку данных приложения на конечном устройстве
     */
    private void copyDataBase() {
        if (!checkDataBase()) {
            this.getReadableDatabase();
            this.close();
            try {
                copyDBFile();
            } catch (IOException mIOException) {
                throw new Error("ErrorCopyingDataBase");
            }
        }
    }

    /**
     * Метод копирования базы данных из пакета приложения в папку данных приложения на конечном устройстве
     * @throws IOException Создаваемое методом исключение (в случае ошибки)
     */
    private void copyDBFile() throws IOException {
        InputStream mInput = mContext.getAssets().open(DB_NAME);
        //InputStream mInput = mContext.getResources().openRawResource(R.raw.info);
        OutputStream mOutput = new FileOutputStream(DB_PATH + DB_NAME);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer)) > 0)
            mOutput.write(mBuffer, 0, mLength);
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    /**
     * Метод открытия соединения с базой данных
     * @return Результат операции
     * @throws SQLException Создаваемое методом исключение (в случае ошибки)
     */
    public boolean openDataBase() throws SQLException {
        mDataBase = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        return mDataBase != null;
    }

    /**
     * Метод закрытия соединения с базой данных
     */
    @Override
    public synchronized void close() {
        if (mDataBase != null)
            mDataBase.close();
        super.close();
    }

    /**
     * Метод создания базы данных
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    /**
     * Метод проверки, нуждается ли база данных в обновлении
     * @param db База данных
     * @param oldVersion Версия старой базы данных
     * @param newVersion Версия новой базы данных
     */

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //if (newVersion > oldVersion)
            mNeedUpdate = true;
    }
}
