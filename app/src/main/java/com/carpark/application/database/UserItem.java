package com.carpark.application.database;

/**
 * Класс (структура) для хранения данных пользователя
 */
public class UserItem {
    /**
     * Идентификатор
     */
    public Long id;
    /**
     * Дата регистрации
     */
    public long registration_date;
    /**
     * Фамилия
     */
    public String last_name;
    /**
     * Имя
     */
    public String first_name;
    /**
     * Отчество
     */
    public String middle_name;
    /**
     * Логин
     */
    public String login;
    /**
     * Пароль
     */
    public String password;
    /**
     * Номер телефона
     */
    public String phone;
    /**
     * Тип пользователя
     */
    public long id_user_type;
}
