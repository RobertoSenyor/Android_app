package com.example.android_app.ui.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.view.View;
import android.widget.Button;
import android.os.Vibrator;

import com.example.android_app.CacheInteraction.PlayMateCache;
import com.example.android_app.R;

import org.json.JSONException;
import org.json.simple.parser.ParseException;

import java.io.IOException;

/**
 * Запускаемый класс
 */
public class WelcomeActivity extends AppCompatActivity {

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Button acceptIntType;
    private Button acceptPoint2DType;
    private Button closeBtn;

    private Button AboutUsBtn;
    private Button LoginBtn;
    private Button RegistrationBtn;

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
     * Диалоговое окно
     */
    private void createNewUserTypeDialog() {
//        dialogBuilder = new AlertDialog.Builder(this);
//        final View createNewUserTypePopupView = getLayoutInflater().inflate(R.layout.selectusertype_popup, null);
//        acceptIntType = (Button) createNewUserTypePopupView.findViewById(R.id.selectIntPopup);
//        acceptPoint2DType = (Button) createNewUserTypePopupView.findViewById(R.id.selectPoint2DPopup);
//        closeBtn = (Button) createNewUserTypePopupView.findViewById(R.id.closePopup);
//
//        dialogBuilder.setView(createNewUserTypePopupView);
//        dialog = dialogBuilder.create();
//        dialog.show();
//
//        acceptIntType.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    v.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
//                } else {
//                    v.vibrate(50);
//                }
//
//                dialog.dismiss();
//            }
//        });
//
//        acceptPoint2DType.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    v.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
//                } else {
//                    v.vibrate(50);
//                }
//
//                dialog.dismiss();
//            }
//        });
//
//        closeBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Context appContext = getApplicationContext();

        try {
            if(PlayMateCache.getInstance().IsFirstBoot(appContext))
            {
                Runnable task = () -> {
                    try {
                        PlayMateCache.getInstance().setIsFirstBoot(false, appContext);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                };

                Thread thread = new Thread(task);
                thread.start();
            }
            else
            {
                try {
                    Intent intent = new Intent(WelcomeActivity.this, RegistrationActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page);

        AboutUsBtn = (Button) findViewById(R.id.AboutUsBtn);
        AboutUsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                smallVibration();
            }
        });

        LoginBtn = (Button) findViewById(R.id.LoginBtn);

        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mediumVibration();

                try {
                    Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });

        RegistrationBtn = (Button) findViewById(R.id.RegistarationBtn);

        RegistrationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mediumVibration();

                /*
                Intent.FLAG_ACTIVITY_REORDER_TO_FRONT - перемещает activity,
                к которой осуществляется переход на вершину стека, если она ужее есть в стеке.

                Intent.FLAG_ACTIVITY_CLEAR_TOP - очищает все activity кроме
                той, которая запускается (если она уже есть в стеке).

                Intent.FLAG_ACTIVITY_SINGLE_TOP указывает, что если в вершине стеке уже есть
                activity, которую надо запустить, то она НЕ запускается
                (то она может существовать в стеке только в единичном виде).

                Intent.FLAG_ACTIVITY_NO_HISTORY - позволит не сохранять в стеке запускаемую activity.
                */

                try {
                    Intent intent = new Intent(WelcomeActivity.this, RegistrationActivity.class);
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