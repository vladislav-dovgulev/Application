package com.carpark.application.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.carpark.application.R;
import com.carpark.application.fragments.CarFragment.OnListFragmentInteractionListener;
import com.carpark.application.database.CarItem;

import java.util.List;

/**
 * Класс-адаптер данных для элемента интерфейса RecyclerView. Обрабатывает объекты класса
 * CarItem и помещает его содержимое в объект класса ViewHolder.
 */
public class CarRecyclerViewAdapter extends RecyclerView.Adapter<CarRecyclerViewAdapter.ViewHolder> {

    /**
     * Список автомобилей
     */
    private final List<CarItem> mValues;
    /**
     * Функция обратного вызова. Отлавливает нажатие на строку формы.
     */
    private final OnListFragmentInteractionListener mListener;

    /**
     * Конструктор класса
     * @param items Список автомобилей
     * @param listener Обратный фызов
     */
    public CarRecyclerViewAdapter(List<CarItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    /**
     * Обработчик события "Создание элемента представления"
     * @param parent Родительский элемент
     * @param viewType Тип представления
     * @return Элемент представления
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_car_item, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Обработчик события "Привязка данных"
     * @param holder Строка формы
     * @param position Номер элемента списка автомобилей
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        CarItem carItem = mValues.get(position);
        holder.mItem = carItem;
        holder.mCarTitle.setText(carItem.title);
        holder.mCarDescription.setText(carItem.description);
        holder.mCarCost.setText(carItem.cost_str);
        if (carItem.image != null) {
            holder.mCarImage.setImageBitmap(carItem.image);
        }
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    /**
     *
     * @return Количество элементов списка данных
     */
    @Override
    public int getItemCount() {
        return mValues.size();
    }


    /**
     * Класс, описывающий одну строку активности "Автопарк" (элементы, строка активности)
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        /**
         * Представление
         */
        public final View mView;
        /**
         * Поле представления "Название авто"
         */
        public final TextView mCarTitle;
        /**
         * Поле представления "Описание авто"
         */
        public final TextView mCarDescription;
        /**
         * Поле представления "Стоимость аренды"
         */
        public final TextView mCarCost;
        /**
         * Поле представления "Фото автомобиля"
         */
        public final ImageView mCarImage;
        /**
         * Объект данных, связанный с дданным элементом
         */
        public CarItem mItem;

        /**
         * Конструктор класса
         * @param view Представление
         */
        public ViewHolder(View view) {
            super(view);
            mView = view;
            mCarTitle = (TextView) view.findViewById(R.id.car_title);
            mCarDescription = (TextView) view.findViewById(R.id.car_description);
            mCarCost = (TextView) view.findViewById(R.id.car_cost);
            mCarImage = (ImageView) view.findViewById(R.id.car_image);
        }

        /**
         * Преобразует объект в строку
         * @return Строка
         */
        @Override
        public String toString() {
            return super.toString() + " '" + mCarTitle.getText() + "'";
        }
    }
}
