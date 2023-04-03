package com.example.alcoholsafe;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.TestLooperManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import java.util.TimerTask;

public class Timer extends AppCompatActivity {

    int hour;
    int minute;

    int getHour;
    int getMinute;

    String TAG = "TimerClass";

    TimerHandler timerHandler;

    TextView timerText;

    Button timerStop;

    Ringtone rt;

    boolean intentCk = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer);

        /**
         * intent로 시간 받아옴
          */
        getHour = getIntent().getIntExtra("hour", hour);
        getMinute = getIntent().getIntExtra("minute", minute);
        Log.d(TAG, "시작 눌렀을 때 시간 받아오나 : " + getHour);
        Log.d(TAG, "시작 눌렀을 때 분 받아오나 :  " + getMinute);


        /**
         * intent로 받아온 시간/분이 모두 0일 때는 아무것도 읎서
         */
        if(getMinute==0 && getHour ==0){
            intentCk = false;
        }else{
            intentCk = true;
        }

        Log.d(TAG, "intentCK : " + intentCk);

        /**
         * textView 연결하고
         */
        timerText = findViewById(R.id.timerText);

        NotificationHelper notify_channel = new NotificationHelper(getApplicationContext());


        /**
         * 알람 소리
         */
        Uri alarm = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        Log.d(TAG, "alarm");
        rt = RingtoneManager.getRingtone(getApplicationContext(), alarm);
        Log.d(TAG, "rt");


//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                for (i = getMinute; i >= 0; i--) {
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            timerText.setText("" + i);
//                        }
//                    });
//
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//
//                    if(i==0){
//                        getMinute = 0;
//                    }
//
//                }
//            }
//        }).start();

        /**
         * 핸들러 객체 생성
         */

        timerHandler = new TimerHandler();



        if(intentCk) {
            timerHandler.sendEmptyMessage(0);
            Log.d(TAG, "intent로 들어옴");

        }else{
            timerHandler.removeMessages(0);
            Log.d(TAG, "첨 들어옴");
        }



        /**
         * 뒤로 가기 버튼
         */

        Button timerBack = (Button) findViewById(R.id.timerBack);
        timerBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



//                if(getMinute==60){
//                    finish();
//                }else{
                    Intent intent = new Intent(Timer.this, Menu.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
//                }


            }
        });



        /**
         * 타이머 정지버튼
         */

        timerStop = findViewById(R.id.timerStop);
        timerStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder dlg = new AlertDialog.Builder(Timer.this);
                dlg.setIcon(R.drawable.smile);
                dlg.setTitle("AlcoholSafe");
                dlg.setMessage("타이머를 중지하시겠습니까?");
                dlg.setCancelable(false);
                dlg.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dlg.setPositiveButton("OK",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which) {
                        timerHandler.removeMessages(1);
                        timerStop.setText("다시 시작");
                    }
                });

                dlg.show();
            }
        });


    }

    class TimerHandler extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case 0: //시작하기
                    if(intentCk = true && getMinute == 0){
                        Log.d(TAG, "handleMessage: intentCK " + intentCk);
                        timerText.setText("Timter : " + getMinute);
                        removeMessages(0);
                        rt.play();

                        AlertDialog.Builder dlg = new AlertDialog.Builder(Timer.this);
                        dlg.setIcon(R.drawable.smile);
                        dlg.setTitle("AlcoholSafe");
                        dlg.setMessage("알람이 종료되었습니다.");
                        dlg.setCancelable(false);
                        dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                rt.stop();
                            }
                        });
                        dlg.show();
                        break;

                    }

                    timerText.setText("Timer : " + getMinute--);
                    sendEmptyMessageDelayed(0,1000);

                    break;


                    case 1: //일시정지
                        removeMessages(0);
                        timerText.setText("Timer : " + getMinute);
                        break;
                    }
            }
        }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: ");
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if(timerHandler != null){
//            timerHandler.removeMessages(0);
            Log.d(TAG, "onDestroy: ");
//        }
    }
}
