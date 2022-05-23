package com.carpark.application.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * Вспомогательный класс, для работы с базой данных, реализующий запросы приложения к базе данных.
 * Данный класс является надстройкой над "DatabaseHelper".
 */
public class Database {

    /**
     * Объект "databaseHelper"
     */
    private DatabaseHelper databaseHelper;
    /**
     * База данных
     */
    private SQLiteDatabase database;

    /**
     * Конструктор класса
     *
     * @param context Контекст
     */
    public Database(Context context) {
        databaseHelper = new DatabaseHelper(context);
        try {
            databaseHelper.updateDataBase();
            database = databaseHelper.getWritableDatabase();
        } catch (Exception exception) {
            throw new Error(exception.getMessage());
        }
    }

    /**
     * Метод реализующий проверку данных пользователя (реализующий авторизацию)
     *
     * @param email    Электронная почта
     * @param password Пароль
     * @return Флаг успешной проверки данных
     */
    public long Login(String login, String password) {
        long UserID = 0;
        Cursor q = GetQuery("select * from user where login = \"" + login + "\"");
        try {
            if (q.getCount() > 0) {
                q.moveToFirst();
                String psw = q.getString(q.getColumnIndexOrThrow("password"));
                if (psw.equals(password)) {
                    UserID = q.getLong(q.getColumnIndexOrThrow("id"));
                }
            }
        } finally {
            q.close();
        }
        return UserID;
    }

    /**
     * Метод выполнения SQL-запроса
     *
     * @param SQL SQL-запрос к базе данных
     * @return Курсор набора данных
     */
    public Cursor GetQuery(String SQL) {
        return database.rawQuery(SQL, null);
    }



    /**
     * Метод возвращает сведения для конкретного идентификатора
     *
     * @param id Идентификатор автомобиля
     * @return Сведения по автомобилю из базы данных (название, тип кузова, тип двигателя, год выпуска, фото и т.д. )
     */
    public CarItem CarGet(Long id) {

        Cursor cursor = GetQuery("select * from view_car where id = " + id.toString());
        try {
            if (cursor.moveToFirst()) {
                CarItem item = new CarItem();
                item.id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                item.title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                item.description = cursor.getString(cursor.getColumnIndexOrThrow("year")) + " г. / " +
                        cursor.getString(cursor.getColumnIndexOrThrow("power")) + " л.с. / " +
                        cursor.getString(cursor.getColumnIndexOrThrow("gear_box_type"));
                item.cost_str = cursor.getString(cursor.getColumnIndexOrThrow("cost")) + " р.";
                if (cursor.isNull(cursor.getColumnIndexOrThrow("photo")) == false) {
                    byte[] data = cursor.getBlob(cursor.getColumnIndexOrThrow("photo"));
                    item.image = BitmapFactory.decodeByteArray(data, 0, data.length);
                }
                item.body_type = cursor.getString(cursor.getColumnIndexOrThrow("body_type"));
                item.car_status = cursor.getString(cursor.getColumnIndexOrThrow("car_status"));
                item.engine_type = cursor.getString(cursor.getColumnIndexOrThrow("engine_type"));
                item.gear_box_type = cursor.getString(cursor.getColumnIndexOrThrow("gear_box_type"));
                item.pledge = cursor.getDouble(cursor.getColumnIndexOrThrow("pledge"));
                item.cost = cursor.getDouble(cursor.getColumnIndexOrThrow("cost"));
                item.power = cursor.getLong(cursor.getColumnIndexOrThrow("power"));
                item.year = cursor.getLong(cursor.getColumnIndexOrThrow("year"));
                item.pledge_str = cursor.getString(cursor.getColumnIndexOrThrow("pledge")) + " р.";
                return item;
            }
        } finally {
            cursor.close();
        }
        return null;
    }

    /**
     * Метод возвращает список автомобилей
     *
     * @return Список сведений по автомобилям из базы данных (название, тип кузова, тип двигателя, год выпуска, фото и т.д. )
     */
    public List<CarItem> CarListGet() {
        List<CarItem> list = new ArrayList<CarItem>();
        Cursor cursor = GetQuery("select id from view_car");
        try {
            if (cursor.moveToFirst()) {
                do {
                    list.add(CarGet(cursor.getLong(cursor.getColumnIndexOrThrow("id"))));
                }
                while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
        }
        return list;
    }


    /**
     * Метод добавления заявки
     * @param userID
     * @param carID
     * @param beginDate
     * @param endDate
     * @param total
     * @return
     */
    public long RequestAdd(long userID, long carID, long beginDate, long endDate, double total) {
        Calendar calendar = Calendar.getInstance();
        ContentValues contentValues = new ContentValues();
        contentValues.put("registration_date", calendar.getTimeInMillis());
        contentValues.put("begin_date", beginDate);
        contentValues.put("end_date", endDate);
        contentValues.put("total", total);
        contentValues.put("id_request_status", 1);
        contentValues.put("id_user", userID);
        contentValues.put("id_car", carID);
        return database.insert("request", null, contentValues);
    }


    /**
     * Метод добавления заявки
     * @param item
     * @return
     */
    public long RequestAdd(RequestItem item) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("registration_date", item.registration_date);
        contentValues.put("begin_date", item.begin_date);
        contentValues.put("end_date", item.end_date);
        contentValues.put("total", item.total);
        contentValues.put("id_request_status", item.id_request_status);
        contentValues.put("id_user", item.id_user);
        contentValues.put("id_car", item.id_car);
        return database.insert("request", null, contentValues);
    }

    /**
     * Метод удаления заявки
     * @param id
     */
    public void RequestDelete (Long id) {
        database.delete("request", "id = " + id.toString(), null);
    }

    /**
     * Метод извлечения заявки по её идентифкатору
     * @param id идентификатор
     * @return заявка
     */
    public RequestItem RequestGet(Long id) {
        Cursor cursor = GetQuery("select * from request where id = " + id.toString());
        try {
            if (cursor.moveToFirst()) {
                RequestItem item = new RequestItem();
                item.id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                item.begin_date = cursor.getLong(cursor.getColumnIndexOrThrow("begin_date"));
                item.end_date = cursor.getLong(cursor.getColumnIndexOrThrow("end_date"));
                item.registration_date = cursor.getLong(cursor.getColumnIndexOrThrow("registration_date"));
                item.id_car = cursor.getLong(cursor.getColumnIndexOrThrow("id_car"));
                item.car = CarGet(item.id_car);
                item.id_user = cursor.getLong(cursor.getColumnIndexOrThrow("id_user"));
                item.id_request_status = cursor.getLong(cursor.getColumnIndexOrThrow("id_request_status"));
                item.request_status = RequestStatusGet(item.id_request_status);
                item.total = cursor.getDouble(cursor.getColumnIndexOrThrow("total"));
                item.total_str = cursor.getString(cursor.getColumnIndexOrThrow("total")) + " р.";
                item.database = this;
                return item;
            }
        } finally {
            cursor.close();
        }
        return null;
    }

    /**
     * Список заявок пользователя
     * @param userId
     * @return
     */
    public List<RequestItem> RequestListGet(Long userId) {
        List<RequestItem> list = new ArrayList<RequestItem>();
        Cursor cursor = GetQuery("select id from request where id_user = " + userId.toString() + " order by registration_date desc");
        try {
            if (cursor.moveToFirst()) {
                do {
                    list.add(RequestGet(cursor.getLong(cursor.getColumnIndexOrThrow("id"))));
                }
                while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
        }
        return list;
    }


    /**
     * Статус заявки
     * @param id идентификатор статуса
     * @return
     */
    public RequestStatusItem RequestStatusGet(Long id) {
        Cursor cursor = GetQuery("select * from request_status where id = " + id.toString());
        try {
            if (cursor.moveToFirst()) {
                RequestStatusItem item = new RequestStatusItem();
                item.id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                item.title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                return item;
            }
        } finally {
            cursor.close();
        }
        return null;
    }

    /**
     * Метод добавления пользователя
     * @param item
     * @return
     */
    public long UserAdd(UserItem item) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("registration_date", item.registration_date);
        contentValues.put("last_name", item.last_name);
        contentValues.put("first_name", item.first_name);
        contentValues.put("middle_name", item.middle_name);
        contentValues.put("login", item.login);
        contentValues.put("password", item.password);
        contentValues.put("phone", item.phone);
        contentValues.put("id_user_type", item.id_user_type);
        return database.insert("user", null, contentValues);
    }

    /**
     * Метод обновления данных пользователя
     * @param item
     * @return
     */
    public boolean UserUpdate(UserItem item) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("registration_date", item.registration_date);
        contentValues.put("last_name", item.last_name);
        contentValues.put("first_name", item.first_name);
        contentValues.put("middle_name", item.middle_name);
        contentValues.put("login", item.login);
        contentValues.put("password", item.password);
        contentValues.put("phone", item.phone);
        contentValues.put("id_user_type", item.id_user_type);
        return database.update("user", contentValues, "id = ?", new String[] {item.id.toString()}) > 0;
    }

    /**
     * Метод извлечения данных пользователя
     * @param id
     * @return
     */
    public UserItem UserGet(Long id) {
        Cursor cursor = database.rawQuery("select * from user where id = ?", new String[] {id.toString()});
        try {
            if (cursor.moveToFirst()) {
                UserItem item = new UserItem();
                item.id = cursor.getLong(cursor.getColumnIndexOrThrow("id"));
                item.registration_date = cursor.getLong(cursor.getColumnIndexOrThrow("registration_date"));
                item.id_user_type = cursor.getLong(cursor.getColumnIndexOrThrow("id_user_type"));
                item.login = cursor.getString(cursor.getColumnIndexOrThrow("login"));
                item.password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
                item.last_name = cursor.getString(cursor.getColumnIndexOrThrow("last_name"));
                item.first_name = cursor.getString(cursor.getColumnIndexOrThrow("first_name"));
                item.middle_name = cursor.getString(cursor.getColumnIndexOrThrow("middle_name"));
                item.phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
                return item;
            }
        } finally {
            cursor.close();
        }
        return null;
    }


    /**
     * Проверка, существует ли логин
     * @param login
     * @return
     */
    public boolean UserLoginExists (String login) {
        Cursor cursor = database.rawQuery("select * from user where login = ?", new String[] {login});
        try {
            if (cursor.moveToFirst()) {
                return true;
            }
        } finally {
            cursor.close();
        }
        return false;
    }

    /**
     * Проверка, существует ли логин
     * @param login
     * @param exception
     * @return
     */
    public boolean UserLoginExists (String login, String exception) {
        Cursor cursor = database.rawQuery("select * from user where login = ? and login <> ?", new String[] {login, exception});
        try {
            if (cursor.moveToFirst()) {
                return true;
            }
        } finally {
            cursor.close();
        }
        return false;
    }
}
