package com.example.android_app.ui.Activities;

import static android.app.Notification.DEFAULT_SOUND;
import static android.app.Notification.DEFAULT_VIBRATE;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.android_app.CacheInteraction.PlayMateCache;
import com.example.android_app.HTTPInteraction.ClientHTTPRequests ;
import com.example.android_app.R;

import org.json.JSONException;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;


public class LoginActivity extends AppCompatActivity {

    private EditText NicknameTextField;
    private EditText PasswordTextField;

    private Button InPageAuthBtn;
    private Button GoToRegBtn;

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
     * Проверяет успешность введенных данных для авторизации
     *
     * @param _Username
     * @param _Password
     * @return boolean
     */
    private boolean IsSuccessAuthorisation(String _Username, String _Password) {

        AtomicInteger success_instance = new AtomicInteger(0);
        AtomicReference<String> sessionToken = new AtomicReference<>("");

        Runnable task = () -> {

            // Проверка имени пользователя
            // ----------------------------------------------------------------------------------------------------------

            if (!NicknameTextField.getText().toString().isEmpty() && NicknameTextField != null) {
                success_instance.getAndIncrement();
            } else {
                NicknameTextField.setText("");
                NicknameTextField.setHintTextColor(Color.RED);
            }

            // Проверка пароля
            // ----------------------------------------------------------------------------------------------------------

            if (!_Password.isEmpty() && _Password != null) {
                success_instance.getAndIncrement();
            } else {
                PasswordTextField.setText("");
                PasswordTextField.setHintTextColor(Color.RED);
            }

            // Проверк успешности регистрации
            // ----------------------------------------------------------------------------------------------------------


                sessionToken.set(ClientHTTPRequests.sendPostRequest_LoginUser(_Username, _Password));


            if (sessionToken.get() != null && success_instance.get() == 2)
            {
//                System.out.println("BBBBBBBBBBBBBBB\n"+sessionToken.get()+"\nBBBBBBBBBBBBBB");

                    try { // кэширование токена
                        PlayMateCache.getInstance().setToken(sessionToken.get(), LoginActivity.this.getApplicationContext());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    success_instance.getAndIncrement();
            }
        };
        Thread thread = new Thread(task);
        thread.start();

        while (thread.isAlive())
        {}

//        System.out.println("success_instance " + success_instance);

        // ----------------------------------------------------------------------------------------------------------
        return success_instance.get() == 3;
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginactivity_page);

        NicknameTextField = (EditText) findViewById(R.id.NicknameTextField);
        PasswordTextField = (EditText) findViewById(R.id.PasswordTextField);

        InPageAuthBtn = (Button) findViewById(R.id.InPageAuthBtn);
        GoToRegBtn = (Button) findViewById(R.id.GoToRegBtn);

        InPageAuthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IsSuccessAuthorisation(
                        NicknameTextField.getText().toString(),
                        PasswordTextField.getText().toString())) {

                    try {
                        // TODO - переход к нужному окну
                        Intent intent_newView = new Intent(LoginActivity.this, MainActivity.class);
                        intent_newView.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent_newView);
                        finish();
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }
            }
        });

        GoToRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mediumVibration();

                try {
                    Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
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