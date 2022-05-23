package com.carpark.application;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.carpark.application.database.Database;
import com.carpark.application.database.UserItem;

import java.util.Calendar;

public class RegistrationActivity extends AppCompatActivity {

    private Database mDatabase;
    private EditText mLogin;
    private EditText mPassword;
    private EditText mPasswordConfirm;
    private EditText mLastName;
    private EditText mFirstName;
    private EditText mMiddleName;
    private EditText mPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mDatabase = new Database(this.getBaseContext());
        mLogin = (EditText) findViewById(R.id.login);
        mPassword = (EditText) findViewById(R.id.password);
        mPasswordConfirm = (EditText) findViewById(R.id.password_confirm);
        mLastName = (EditText) findViewById(R.id.last_name);
        mFirstName = (EditText) findViewById(R.id.first_name);
        mMiddleName = (EditText) findViewById(R.id.middle_name);
        mPhone = (EditText) findViewById(R.id.phone);
        Button mRegistrationButton = (Button) findViewById(R.id.registration);
        mRegistrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doRegistration();
            }
        });
    }

    protected void doRegistration() {
        int minimalLength = 4;
        boolean errors = false;
        View focusView = null;
        mLogin.setError(null);
        mPassword.setError(null);
        String login = mLogin.getText().toString().trim();
        String password = mPassword.getText().toString();
        String passwordConfirm = mPasswordConfirm.getText().toString();

        if (TextUtils.isEmpty(passwordConfirm) || passwordConfirm.length() < minimalLength) {
            mPasswordConfirm.setError("Необходимо заполнить (минимум 4 символа)");
            focusView = mPasswordConfirm;
            errors = true;
        } else if (!password.equals(passwordConfirm)) {
            mPasswordConfirm.setError("Пароли не совпадают");
            focusView = mPasswordConfirm;
            errors = true;
        }

        if (TextUtils.isEmpty(password) || password.length() < minimalLength) {
            mPassword.setError("Необходимо заполнить (минимум 4 символа)");
            focusView = mPassword;
            errors = true;
        }

        if (TextUtils.isEmpty(login) || login.length() < minimalLength) {
            mLogin.setError("Необходимо заполнить (минимум 4 символа)");
            focusView = mLogin;
            errors = true;
        } else if (mDatabase.UserLoginExists(login)) {
            mLogin.setError("Логин уже существует");
            focusView = mLogin;
            errors = true;
        }

        if (errors) {
            focusView.requestFocus();
        } else {
            Calendar calendar = Calendar.getInstance();
            UserItem item = new UserItem();
            item.id_user_type = 2;
            item.registration_date = calendar.getTimeInMillis();
            item.first_name = mFirstName.getText().toString();
            item.last_name = mLastName.getText().toString();
            item.middle_name = mMiddleName.getText().toString();
            item.phone = mPhone.getText().toString();
            item.login = login;
            item.password = password;
            Long id = mDatabase.UserAdd(item);
            this.getIntent().putExtra("id_user", id);
            this.getIntent().putExtra("login", item.login);
            this.getIntent().putExtra("password", item.password);
            setResult(RESULT_OK, this.getIntent());
            finish();
        }
    }

}
