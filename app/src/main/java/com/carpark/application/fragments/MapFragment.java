package com.carpark.application.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.carpark.application.CarActivity;
import com.carpark.application.R;
import com.carpark.application.database.Database;
import com.carpark.application.database.DatabaseHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Фрагмент, реализующий отображение карты.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {

    /**
     * База данных
     */
    protected Database database;
    /**
     * Компонент, реализующий карту Google
     */
    private GoogleMap mMap;

    /**
     * Обработчик события "Нажатие на маркер карты"
     */
    private GoogleMap.OnMarkerClickListener markerClickListener = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(final Marker marker) {
            View view = getView();

            Snackbar snackbar = Snackbar.make(view, getCarInfo((long) marker.getTag()), Snackbar.LENGTH_LONG)
                    .setAction("Сведения", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getContext(), CarActivity.class);
                            intent.putExtra("carId", (long) marker.getTag());
                            startActivity(intent);
                        }
                    });
            snackbar.show();
            return false;
        }
    };


    /**
     * Конструктор класса
     */
    public MapFragment() {
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
        database = new Database(this.getContext());
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
        return inflater.inflate(R.layout.fragment_map, container, false);
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

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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




    /**
     * Метод создает описание для точки карты
     *
     * @param carId Идентификатор автомобиля
     * @return Описание
     */
    public String getCarInfo(Long carId) {
        String value = "";
        Cursor cursor = database.GetQuery("SELECT * FROM car where id = " + carId.toString());
        try {
            if (cursor.moveToFirst()) {
                do {
                    value += cursor.getString(cursor.getColumnIndex("title")) + "\n";
                    value += "Год выпуска: " + cursor.getString(cursor.getColumnIndex("year")) + "\n";
                    value += "Мощность (л.с.): " + cursor.getString(cursor.getColumnIndex("power")) + "\n";
                } while (cursor.moveToNext());
            }
        }
        finally {
            cursor.close();
        }
        return value;
    }


    /**
     * Метод обратной связи карты Google. Вызывается когда объект карты создан и готов к использованию.
     *
     * @param googleMap Экземпляр карты Google
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMinZoomPreference(1.0f);
        mMap.setMaxZoomPreference(30.0f);
        mMap.setOnMarkerClickListener(markerClickListener);
        Cursor cursor = database.GetQuery("SELECT * FROM car where id_car_status = 1");
        try {
            if (cursor.moveToFirst()) {
                do {
                    double latitude = cursor.getDouble(cursor.getColumnIndex("gps_latitude"));
                    double longitude = cursor.getDouble(cursor.getColumnIndex("gps_longitude"));
                    if (cursor.isFirst()) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 13.0f));
                    }
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(new LatLng(latitude, longitude));
                    markerOptions.title(cursor.getString(cursor.getColumnIndex("title")));
                    //markerOptions.snippet(getSnippet(cursor));
                    mMap.addMarker(markerOptions).setTag(cursor.getLong(cursor.getColumnIndex("id")));
                } while (cursor.moveToNext());
            }
        }
        finally {
            cursor.close();
        }
    }
}
