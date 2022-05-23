package com.carpark.application;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.carpark.application.R;
import com.carpark.application.database.Database;
import com.carpark.application.database.UserItem;

import java.util.Calendar;

public class UserProfileActivity extends AppCompatActivity {

    private Database mDatabase;
    private EditText mLogin;
    private EditText mPassword;
    private EditText mPasswordConfirm;
    private EditText mLastName;
    private EditText mFirstName;
    private EditText mMiddleName;
    private EditText mPhone;
    private UserItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        mDatabase = new Database(this.getBaseContext());

        item = mDatabase.UserGet(Global.UserID);

        mDatabase = new Database(this.getBaseContext());
        mLogin = (EditText) findViewById(R.id.login);
        mPassword = (EditText) findViewById(R.id.password);
        mPasswordConfirm = (EditText) findViewById(R.id.password_confirm);
        mLastName = (EditText) findViewById(R.id.last_name);
        mFirstName = (EditText) findViewById(R.id.first_name);
        mMiddleName = (EditText) findViewById(R.id.middle_name);
        mPhone = (EditText) findViewById(R.id.phone);

        mLogin.setText(item.login);
        mPhone.setText(item.phone);
        mMiddleName.setText(item.middle_name);
        mFirstName.setText(item.first_name);
        mLastName.setText(item.last_name);
        mPassword.setText(item.password);
        mPasswordConfirm.setText(item.password);

        Button mSaveButton = (Button) findViewById(R.id.save);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doSave();
            }
        });
    }

    protected void doSave() {
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
        } else if (mDatabase.UserLoginExists(login, item.login)) {
            mLogin.setError("Логин уже существует");
            focusView = mLogin;
            errors = true;
        }

        if (errors) {
            focusView.requestFocus();
        } else {
            item.first_name = mFirstName.getText().toString();
            item.last_name = mLastName.getText().toString();
            item.middle_name = mMiddleName.getText().toString();
            item.phone = mPhone.getText().toString();
            item.login = login;
            item.password = password;
            mDatabase.UserUpdate(item);
            finish();
        }
    }
}
