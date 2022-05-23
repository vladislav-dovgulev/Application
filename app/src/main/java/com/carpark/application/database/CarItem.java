package com.carpark.application.database;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Класс (структура) для хранения данных об автомобиле
 */
public class CarItem {
    /**
     * Идентификатор автомобиля
     */
    public long id;
    /**
     * Название
     */
    public String title;
    /**
     * Описание
     */
    public String description;
    /**
     * Тип КПП
     */
    public String gear_box_type;
    /**
     * Тип кузова
     */
    public String body_type;
    /**
     * Тип двигателя
     */
    public String engine_type;
    /**
     * Статус автомобиля
     */
    public String car_status;
    /**
     * Мощность (л.с.)
     */
    public Long power;
    /**
     * Год выпуска
     */
    public Long year;
    /**
     * Предоплата
     */
    public String pledge_str;
    /**
     * Стоимость дня проката (руб.)
     */
    public String cost_str;
    /**
     * Стоимость дня проката (руб.)
     */
    public Double cost;
    /**
     * Предоплата
     */
    public Double pledge;
    /**
     * Фотография авто
     */
    public Bitmap image = null;
}
