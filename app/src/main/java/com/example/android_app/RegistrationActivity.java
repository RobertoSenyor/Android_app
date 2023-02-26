package com.example.android_app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class RegistrationActivity extends AppCompatActivity {

    private EditText NicknameTextField;
    private EditText URLTextField;
    private EditText EMailTextField;
    private EditText PasswordTextField;
    private EditText AgainPasswordTextField;

    private Button InPageRegistrationBtn;
    private Button ExistAccountBtn;
    private Button AcceptAuthBtn;

    private FrameLayout SuccessRegistrationFrameLayout;

    /**
     * короткая вибрация (50мсек)
     */
    private void smallVibration() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            v.vibrate(50);
        }
    }

    /**
     * средняя вибрация (150мсек)
     */
    private void mediumVibration() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            v.vibrate(150);
        }
    }

    /**
     * длинная вибрация (200мсек)
     */
    private void largeVibration() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            v.vibrate(200);
        }
    }

    /**
     * Проверяет успешность введенных жанных для регистрации
     *
     * @param _Username
     * @param _SteamURL
     * @param _EMail
     * @param _Password
     * @param _AgainPassword
     * @return boolean
     */
    private boolean IsSuccessRegistration(String _Username, String _SteamURL, String _EMail, String _Password, String _AgainPassword) {

        // TODO - приравнять к нулю
        AtomicInteger success_instance = new AtomicInteger(0);

        AtomicBoolean isExistUsername = new AtomicBoolean(true);
        AtomicBoolean isExistSteamURL = new AtomicBoolean(false);
//        AtomicBoolean isOkEMail = new AtomicBoolean(false);
        AtomicReference<String> sessionToken = new AtomicReference<>("");

        Runnable task = () -> {

            isExistUsername.set(ClientHTTPRequests.sendGetRequest_isExistUsername(_Username));
            isExistSteamURL.set(ClientHTTPRequests.sendGetRequest_isExistSteamURL(_SteamURL));
            sessionToken.set(ClientHTTPRequests.sendPostRequest_RegistrationUser(_Username, _SteamURL, _Password));

            // Проверка имени пользователя
            // ----------------------------------------------------------------------------------------------------------

            if (isExistUsername.get() == false && !NicknameTextField.getText().toString().isEmpty() && NicknameTextField != null) {
                success_instance.getAndIncrement();
            } else {
                NicknameTextField.setText("");
                NicknameTextField.setHintTextColor(Color.RED);
            }

            // Проверка ссылки на Steam
            // ----------------------------------------------------------------------------------------------------------

            if (isExistSteamURL.get() == true && !URLTextField.getText().toString().isEmpty() && URLTextField != null) {
                success_instance.getAndIncrement();
            } else {
                URLTextField.setText("");
                URLTextField.setHintTextColor(Color.RED);
            }

            // Проверка почты
            // ----------------------------------------------------------------------------------------------------------

            if (!EMailTextField.getText().toString().isEmpty() && URLTextField != null && EMailTextField.getText().toString().contains("@")) {
                success_instance.getAndIncrement();
            } else {
                EMailTextField.setText("");
                EMailTextField.setHintTextColor(Color.RED);
            }

            // Проверка пароля и его повтора
            // ----------------------------------------------------------------------------------------------------------

            if (!_Password.isEmpty() && _Password != null && !_AgainPassword.isEmpty() && _AgainPassword != null && _Password.equals(_AgainPassword)) {
                success_instance.getAndIncrement();
            } else {
                PasswordTextField.setText("");
                PasswordTextField.setHintTextColor(Color.RED);
                AgainPasswordTextField.setText("");
                AgainPasswordTextField.setHintTextColor(Color.RED);
            }

            // Проверк успешности регистрации
            // ----------------------------------------------------------------------------------------------------------

            // TODO - сохранение токена
            if (!sessionToken.get().isEmpty())
                success_instance.getAndIncrement();
        };
        Thread thread = new Thread(task);
        thread.start();

        while (thread.isAlive())
        {}

//        System.out.println("success_instance " + success_instance);

        // ----------------------------------------------------------------------------------------------------------
        return success_instance.get() == 5 ? true : false;
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_page);

        NicknameTextField = (EditText) findViewById(R.id.NicknameTextField);
        URLTextField = (EditText) findViewById(R.id.URLTextField);
        EMailTextField = (EditText) findViewById(R.id.EMailTextField);
        PasswordTextField = (EditText) findViewById(R.id.PasswordTextField);
        AgainPasswordTextField = (EditText) findViewById(R.id.AgainPasswordTextField);

        SuccessRegistrationFrameLayout = (FrameLayout) findViewById(R.id.SuccessRegistrationFrameLayout);

        InPageRegistrationBtn = (Button) findViewById(R.id.InPageRegistrationBtn);
        ExistAccountBtn = (Button) findViewById(R.id.ExistAccountBtn);
        AcceptAuthBtn = (Button) findViewById(R.id.AcceptAuthBtn);

        InPageRegistrationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IsSuccessRegistration(
                        NicknameTextField.getText().toString(),
                        URLTextField.getText().toString(),
                        EMailTextField.getText().toString(),
                        PasswordTextField.getText().toString(),
                        AgainPasswordTextField.getText().toString())) {
                    SuccessRegistrationFrameLayout.setVisibility(View.VISIBLE);

                    ExistAccountBtn.setClickable(false);
                    ExistAccountBtn.setVisibility(View.INVISIBLE);

                    InPageRegistrationBtn.setClickable(false);
                    InPageRegistrationBtn.setVisibility(View.INVISIBLE);
                }
            }
        });

        AcceptAuthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });
    }
}