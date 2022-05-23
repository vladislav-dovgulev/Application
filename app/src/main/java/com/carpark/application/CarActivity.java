package com.carpark.application;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.carpark.application.database.CarItem;
import com.carpark.application.database.Database;

/**
 * Класс, реализующий Activity для отображения сведений о конкретном авто
 */
public class CarActivity extends AppCompatActivity {
    /**
     * Код данной формы
      */
    public static final Integer RESULT_CODE = 2;
    /**
     * База данных
     */
    protected Database mDatabase;
    /**
     * Данные автомобиля
     */
    protected CarItem carItem;

    /**
     * Обработчик события "Создание Activity"
     * @param savedInstanceState Состояние формы
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        mDatabase = new Database(getApplicationContext());

        Long carId = getIntent().getLongExtra("carId", 0);
        carItem =  mDatabase.CarGet(carId);

        if (carItem.image != null) {
            ((ImageView) this.findViewById(R.id.image)).setImageBitmap(carItem.image);
        }
        ((TextView)this.findViewById(R.id.cost)).setText(carItem.cost_str);
        ((TextView)this.findViewById(R.id.title)).setText(carItem.title);
        ((TextView)this.findViewById(R.id.body_type)).setText(carItem.body_type);
        ((TextView)this.findViewById(R.id.engine_type)).setText(carItem.engine_type);
        ((TextView)this.findViewById(R.id.gear_box_type)).setText(carItem.gear_box_type);
        ((TextView)this.findViewById(R.id.power)).setText(carItem.power.toString());
        ((TextView)this.findViewById(R.id.year)).setText(carItem.year.toString());

        ((Button)this.findViewById(R.id.create_request)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), RequestActivity.class);
                intent.putExtra("carId", carItem.id);
                startActivity(intent);
            }
        });
    }
}
