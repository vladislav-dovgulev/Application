package com.carpark.application.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.carpark.application.CarActivity;
import com.carpark.application.R;
import com.carpark.application.database.CarItem;
import com.carpark.application.database.Database;


/**
 * Фрагмент, реализующий отображение списка автомобилей автопарка.
 */
public class CarFragment extends Fragment {

    /**
     * База данных
     */
    protected Database database;
    /**
     *
     */
    private static final String ARG_COLUMN_COUNT = "column-count";
    /**
     * Количество столбцов формы
     */
    private int mColumnCount = 1;
    /**
     * Обработчик нажатия на пункт списка
     */
    private OnListFragmentInteractionListener mListener = new OnListFragmentInteractionListener() {
        @Override
        public void onListFragmentInteraction(CarItem item) {
            Intent intent = new Intent(getContext(), CarActivity.class);
            intent.putExtra("carId", item.id);
            startActivity(intent);
        }
    };

    /**
     * Конструктор класса
     */
    public CarFragment() {
    }

    /**
     * Обработчик события "Создание фрагмента"
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = new Database(this.getContext());
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    /**
     * Обработчик события "Создание представления"
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return Представление
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_car_item_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            recyclerView.setAdapter(new CarRecyclerViewAdapter(database.CarListGet(), mListener));
        }
        return view;
    }

    /**
     * Обработчик события "Присоединение фрагмента к родительской Activity"
     * @param context Контекст
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
        */
    }


    /**
     * Обработчик события "Отсоединение фрагмента от родительской Activity"
     */
    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * Интерфейс для реализации взаимодействия между фрагментом и другими активностями
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(CarItem item);
    }
}
