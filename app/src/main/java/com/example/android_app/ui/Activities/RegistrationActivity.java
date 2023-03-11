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
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.android_app.CacheInteraction.PlayMateCache;
import com.example.android_app.HTTPInteraction.ClientHTTPRequests;
import com.example.android_app.R;

import org.json.JSONException;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class RegistrationActivity extends AppCompatActivity {

    // для всплыващих уведомлений
    private NotificationManager notificationManager;
    private static final int NOTIFY_ID = 1;
    private static final String CHANNEL_ID = "CHANNEL_ID";

    private EditText NicknameTextField;
    private EditText URLTextField;
    private EditText EMailTextField;
    private EditText PasswordTextField;
    private EditText AgainPasswordTextField;

    private Button InPageRegistrationBtn;
    private Button ExistAccountBtn;
    private Button SocialPolicyBtn;

    private ProgressBar registrationProgressBar;

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

    private void twiceVibrate(){

        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        Runnable task = () -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(75, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                v.vibrate(75);
            }

            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(75, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                v.vibrate(75);
            }
        };

        Thread thread = new Thread(task);
        thread.start();
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

            // Проверка имени пользователя
            // ----------------------------------------------------------------------------------------------------------

            if (isExistUsername.get() == false && !_Username.isEmpty() && NicknameTextField != null) {
                success_instance.getAndIncrement();
            } else {
                NicknameTextField.setText("");
                NicknameTextField.setHintTextColor(Color.RED);
            }

            // Проверка ссылки на Steam
            // ----------------------------------------------------------------------------------------------------------

            if (isExistSteamURL.get() == false && !_SteamURL.isEmpty() && URLTextField != null) {
                success_instance.getAndIncrement();
            } else {
                URLTextField.setText("");
                URLTextField.setHintTextColor(Color.RED);
            }

            // Проверка почты
            // ----------------------------------------------------------------------------------------------------------

            if (!_EMail.isEmpty() && _EMail.contains("@") && EMailTextField != null) {
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

            if (success_instance.get() == 4) {
                sessionToken.set(ClientHTTPRequests.sendPostRequest_RegistrationUser(_Username, _SteamURL, _EMail, _Password));
            }

            // Проверк успешности регистрации
            // ----------------------------------------------------------------------------------------------------------

            if (!sessionToken.get().isEmpty())
            {
                try { // кэширование токена
                    PlayMateCache.getInstance().setToken(sessionToken.get(),RegistrationActivity.this.getApplicationContext());
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                success_instance.getAndIncrement();
                Thread.currentThread().interrupt();
            }
        };
        Thread thread = new Thread(task);
        thread.start();

        registrationProgressBar.setVisibility(View.VISIBLE);
        InPageRegistrationBtn.setVisibility(View.INVISIBLE);

        while (true) {
            if (!thread.isAlive()) {
                registrationProgressBar.setVisibility(View.INVISIBLE);
                InPageRegistrationBtn.setVisibility(View.VISIBLE);
                return success_instance.get() == 5 ? true : false;
            }
        }
    }

    public static void createChannelIfNeeded(NotificationManager manager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_ID, NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(notificationChannel);
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Context appContext = getApplicationContext();

        try
        {
            String token = PlayMateCache.getInstance().getToken(appContext);

            if (!token.equals(""))
            {
                try {
                    // TODO - переход к нужному окну
                    Intent intent_newView = new Intent(RegistrationActivity.this, MainActivity.class);
                    intent_newView.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent_newView);
                    finish();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_page);

        notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        NicknameTextField = (EditText) findViewById(R.id.NicknameTextField);
        URLTextField = (EditText) findViewById(R.id.URLTextField);
        EMailTextField = (EditText) findViewById(R.id.EMailTextField);
        PasswordTextField = (EditText) findViewById(R.id.PasswordTextField);
        AgainPasswordTextField = (EditText) findViewById(R.id.AgainPasswordTextField);

        InPageRegistrationBtn = (Button) findViewById(R.id.InPageRegistrationBtn);
        ExistAccountBtn = (Button) findViewById(R.id.ExistAccountBtn);

        registrationProgressBar = (ProgressBar) findViewById(R.id.registrationProgressBar);

        ExistAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mediumVibration();

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


        InPageRegistrationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO - перейти к странице логина
                if (IsSuccessRegistration(
                        NicknameTextField.getText().toString(),
                        URLTextField.getText().toString(),
                        EMailTextField.getText().toString(),
                        PasswordTextField.getText().toString(),
                        AgainPasswordTextField.getText().toString())) {

                    twiceVibrate();

                    InPageRegistrationBtn.setClickable(false);

                    Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    NotificationCompat.Builder notificationBuilder =
                            new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                                    .setAutoCancel(false)
                                    .setSmallIcon(android.R.drawable.ic_dialog_email)
                                    .setWhen(System.currentTimeMillis())
                                    .setContentIntent(pendingIntent)
                                    .setContentTitle("Ура")
                                    .setContentText("Вы успешно зарегестрировались!")
                                    .setDefaults(DEFAULT_SOUND | DEFAULT_VIBRATE)
                                    .setPriority(NotificationCompat.PRIORITY_MAX);


                    createChannelIfNeeded(notificationManager);
                    notificationManager.notify(NOTIFY_ID, notificationBuilder.build());

                    try {
                        // TODO - переход к нужному окну
                        Intent intent_newView = new Intent(RegistrationActivity.this, MainActivity.class);
                        intent_newView.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent_newView);
                        finish();
                    } catch (Exception e) {
                        System.out.println(e);
                    }

                }
                else {
                    twiceVibrate();

                    Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    NotificationCompat.Builder notificationBuilder =
                            new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                                    .setAutoCancel(false)
                                    .setSmallIcon(android.R.drawable.ic_dialog_alert)
                                    .setWhen(System.currentTimeMillis())
                                    .setContentIntent(pendingIntent)
                                    .setContentTitle("Упс...")
                                    .setContentText("Регистрация не произошла, попробуйте снова.")
                                    .setDefaults(DEFAULT_SOUND | DEFAULT_VIBRATE)
                                    .setPriority(NotificationCompat.PRIORITY_MAX);


                    createChannelIfNeeded(notificationManager);
                    notificationManager.notify(NOTIFY_ID, notificationBuilder.build());
                }
            }
        });
    }
}