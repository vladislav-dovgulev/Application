package com.carpark.application.database;

/**
 * Класс, для хранения данных по заявкам
 */
public class RequestItem {
    /**
     * Идентифкатор
     */
    public long id;
    /**
     * Дата регистрации заказа
     */
    public long registration_date;
    /**
     * Идентификатор пользователя
     */
    public long id_user;
    /**
     * Идентификатор авто
     */
    public long id_car;
    /**
     * Авто
     */
    public CarItem car;
    /**
     * Начало периода
     */
    public long begin_date;
    /**
     * Окончание периода
     */
    public long end_date;
    /**
     * Стоимость заявки
     */
    public double total;
    /**
     * Стоимость заявки
     */
    public String total_str;
    /**
     * Идентификатор заявки
     */
    public long id_request_status;
    /**
     * Статус заявки
     */
    public RequestStatusItem request_status;
    /**
     * Ссылка на компонент для работы с БД
     */
    public Database database;
}
