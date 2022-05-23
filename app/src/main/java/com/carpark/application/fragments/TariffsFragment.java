package com.carpark.application.fragments;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.carpark.application.R;
import com.carpark.application.database.Database;
import com.carpark.application.database.DatabaseHelper;

import java.io.IOException;

/**
 * Фрагмент, реализующий отображение тарифов.
 */
public class TariffsFragment extends Fragment {

    /**
     * База данных
     */
    protected Database database;

    /**
     * Конструктор класса
     */
    public TariffsFragment() {
        // Required empty public constructor
    }

    /**
     * Обработчик события "Создание фрагмента"
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = new Database(getContext());
    }

    /**
     * Обработчик события "Создание представления"
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return Представление
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tariffs, container, false);
    }

    /**
     * Обработчик события "Представление создано". Выполняем загрузку данных в элементы представления.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        TextView textView = (TextView) this.getView().findViewById(R.id.tariffs_text);
        Cursor cursor = database.GetQuery("SELECT * FROM params");
        try {
            if (cursor.moveToFirst()) {
                textView.setText(cursor.getString(cursor.getColumnIndex("tariffs")));
            }
        } finally {
            cursor.close();
        }
        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * Обработчик события "Присоединение фрагмента к родительской Activity"
     *
     * @param context Контекст
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    /**
     * Обработчик события "Отсоединение фрагмента от родительской Activity"
     */

    @Override
    public void onDetach() {
        super.onDetach();

    }

}
