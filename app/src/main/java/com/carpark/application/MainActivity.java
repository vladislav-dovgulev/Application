package com.carpark.application;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.carpark.application.database.Database;
import com.carpark.application.fragments.CarFragment;
import com.carpark.application.fragments.ContactsFragment;
import com.carpark.application.fragments.MapFragment;
import com.carpark.application.fragments.PrivacyPolicyFragment;
import com.carpark.application.fragments.TariffsFragment;
import com.carpark.application.fragments.TenancyRulesFragment;
import com.carpark.application.fragments.RequestFragment;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

/**
 * Класс галвной Activity приложения.
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected static int CurrentFragment = 0;

    protected NavigationView navigationView;

    protected MenuItem menuItemSettings;

    protected long UserID;
    /**
     * База данных
     */
    protected Database database;
    /**
     * Фрагмент, реализующий раздел "Политика конфиденциальности"
     */
    protected PrivacyPolicyFragment privacyPolicyFragment;
    /**
     * Фрагмент, реализующий раздел "Тарифы"
     */
    protected TariffsFragment tariffsFragment;
    /**
     * Фрагмент, реализующий раздел "Правила аренды"
     */
    protected TenancyRulesFragment tenancyRulesFragment;
    /**
     * Фрагмент, реализующий раздел "Контакты"
     */
    protected ContactsFragment contactsFragment;
    /**
     * Фрагмент, реализующий раздел "Автопарк"
     */
    protected CarFragment carFragment;
    /**
     * Фрагмент, реализующий раздел "Карта"
     */
    protected MapFragment mapFragment;


    protected RequestFragment requestFragment;
    /**
     * Обработчик события "Создание Activity"
     *
     * @param savedInstanceState Состояние Activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database = new Database(getBaseContext());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        privacyPolicyFragment = new PrivacyPolicyFragment();
        tariffsFragment = new TariffsFragment();
        tenancyRulesFragment = new TenancyRulesFragment();
        contactsFragment = new ContactsFragment();
        carFragment = new CarFragment();
        mapFragment = new MapFragment();
        requestFragment = new RequestFragment();
        if (CurrentFragment == 0) {
            CurrentFragment = R.id.nav_cars;
        }
        SetCurrentFragment(CurrentFragment);
    }


    /**
     * Обработчик события "Нажатие кнопки возврата смартфона"
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Обработчик события "Создание пункта меню"
     *
     * @param menu меню
     * @return Результат выпонения операции
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        menuItemSettings = menu.findItem(R.id.action_settings);
        menuItemSettings.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(getBaseContext(), UserProfileActivity.class);
                startActivity(intent);
                return false;
            }
        });
        EnableUserControls(Global.Logged);
        return true;
    }

    /**
     * Обработчик события ""
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //SetCurrentFragment(id);

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void SetCurrentFragment (int id)
    {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (id == R.id.nav_tenancy_rules) {
            fragmentTransaction.replace(R.id.container, tenancyRulesFragment);
            CurrentFragment = id;
        } else if (id == R.id.nav_contacts) {
            fragmentTransaction.replace(R.id.container, contactsFragment);
            CurrentFragment = id;
        } else if (id == R.id.nav_privacy_policy) {
            fragmentTransaction.replace(R.id.container, privacyPolicyFragment);
            CurrentFragment = id;
        } else if (id == R.id.nav_cars) {
            fragmentTransaction.replace(R.id.container, carFragment);
            CurrentFragment = id;
        } else  if (id == R.id.nav_enter) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, LoginActivity.RESULT_LOGIN);
        } else if (id == R.id.nav_logout) {
            Global.Logged = false;
            Global.UserID = 0;
            EnableUserControls(false);
            if (CurrentFragment == R.id.nav_cabinet)
                SetCurrentFragment (R.id.nav_cars);
        } else if (id == R.id.nav_cabinet) {
            fragmentTransaction.replace(R.id.container, requestFragment);
            CurrentFragment = id;
        }
        fragmentTransaction.commit();
    }

    public void EnableUserControls (boolean Enable) {
        navigationView.getMenu().findItem(R.id.nav_enter).setVisible(!Enable);
        navigationView.getMenu().findItem(R.id.nav_logout).setVisible(Enable);
        navigationView.getMenu().findItem(R.id.nav_cabinet).setVisible(Enable);
        if (menuItemSettings != null)
            menuItemSettings.setEnabled(Enable);
    }

    /**
     * Обработчик события "Нажатие на пункт меню навигации"
     * @param item Пункт меню
     * @return Результат операции
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        SetCurrentFragment(id);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Обработчик события "Обработка ответов вызываемых Activity".
     * Пример: в форме авторизации мы вводим логин и пароль и эти введенные данные мы получаем от формы авторизации в данном методе.
     *
     * @param requestCode Код запроса
     * @param resultCode  Код ответа
     * @param data        Данные ответа
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LoginActivity.RESULT_LOGIN) {
            if (resultCode == RESULT_OK && Global.Logged) {
                EnableUserControls(true);
            } else {

            }
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        EnableUserControls(Global.Logged);
    }
}
