package com.carpark.application;

import android.app.DatePickerDialog;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.carpark.application.database.CarItem;
import com.carpark.application.database.Database;

import android.widget.DatePicker;

import java.util.Calendar;


public class RequestActivity extends AppCompatActivity {

    Double RentCost;
    Double PledgeCost;
    Double TotalCost;

    TextView viewBeginDate;
    TextView viewEndDate;
    TextView viewRentCost;
    TextView viewPledgeCost;
    TextView viewTotalCost;
    Button buttonSend;
    Boolean Complete = false;


    Calendar beginDateCalendar = Calendar.getInstance();
    Calendar endDateCalendar = Calendar.getInstance();


    /**
     * База данных
     */
    protected Database mDatabase;
    /**
     * Данные автомобиля
     */
    protected CarItem carItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        mDatabase = new Database(getApplicationContext());
        Long carId = getIntent().getLongExtra("carId", 0);
        carItem = mDatabase.CarGet(carId);
        if (carItem.image != null) {
            ((ImageView) this.findViewById(R.id.image)).setImageBitmap(carItem.image);
        }
        ((TextView) this.findViewById(R.id.cost)).setText(carItem.cost_str);
        ((TextView) this.findViewById(R.id.title)).setText(carItem.title);
        viewBeginDate = ((TextView) this.findViewById(R.id.begin_date));
        viewEndDate = ((TextView) this.findViewById(R.id.end_date));
        viewRentCost = ((TextView) this.findViewById(R.id.rent_cost));
        viewPledgeCost = ((TextView) this.findViewById(R.id.pledge_cost));
        viewTotalCost = ((TextView) this.findViewById(R.id.total_cost));
        Button buttonSend = ((Button) this.findViewById(R.id.button_send));
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Complete) {
                    if (Global.Logged) {
                        mDatabase.RequestAdd(Global.UserID, carItem.id, beginDateCalendar.getTimeInMillis(), endDateCalendar.getTimeInMillis(), TotalCost);
                        Snackbar snackbar = Snackbar.make(v, "Заказ оформлен", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        Complete = true;
                    } else {
                        Snackbar snackbar = Snackbar.make(v, "Необходимо выполнить авторизацию", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                } else {
                    Snackbar snackbar = Snackbar.make(v, "Заказ уже оформлен", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }

            }
        });


        endDateCalendar.add(Calendar.DAY_OF_MONTH, 1);
        viewBeginDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(RequestActivity.this, beginDateOnDateSetListener,
                        beginDateCalendar.get(Calendar.YEAR),
                        beginDateCalendar.get(Calendar.MONTH),
                        beginDateCalendar.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });
        viewEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(RequestActivity.this, endDateOnDateSetListener,
                        endDateCalendar.get(Calendar.YEAR),
                        endDateCalendar.get(Calendar.MONTH),
                        endDateCalendar.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });
        setBeginDate();
        setEndDate();

    }

    void setBeginDate() {
        viewBeginDate.setText(DateUtils.formatDateTime(this,
                beginDateCalendar.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));
        RequestCostRecalc();
    }

    void setEndDate() {
        viewEndDate.setText(DateUtils.formatDateTime(this,
                endDateCalendar.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));
        RequestCostRecalc();
    }

    DatePickerDialog.OnDateSetListener beginDateOnDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            beginDateCalendar.set(Calendar.YEAR, year);
            beginDateCalendar.set(Calendar.MONTH, monthOfYear);
            beginDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setBeginDate();
        }
    };

    DatePickerDialog.OnDateSetListener endDateOnDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            endDateCalendar.set(Calendar.YEAR, year);
            endDateCalendar.set(Calendar.MONTH, monthOfYear);
            endDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setEndDate();
        }
    };

    void RequestCostRecalc() {
        int Range = (endDateCalendar.get(Calendar.DAY_OF_YEAR) - beginDateCalendar.get(Calendar.DAY_OF_YEAR));
        RentCost = Range * carItem.cost;
        PledgeCost = carItem.pledge;
        TotalCost = RentCost + PledgeCost;
        viewRentCost.setText(RentCost.toString() + " р.");
        viewPledgeCost.setText(PledgeCost.toString() + " р.");
        viewTotalCost.setText(TotalCost.toString() + " р.");
    }
}
