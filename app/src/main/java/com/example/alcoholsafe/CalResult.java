package com.example.alcoholsafe;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CalResult extends AppCompatActivity {

    double maxRound;
    int hour;
    int minute;
    String TAG = "calResult";

    Dialog dlg;

//    AlertDialog.Builder dlg1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cal_result);


        Double abv = getIntent().getDoubleExtra("abvResult", maxRound);
        Log.d("abv", "abv : " + abv);

        String strAbv = Double.toString(abv);

        int getHour = getIntent().getIntExtra("hour", hour);
        Log.d(TAG, "getHour : " + getHour);

        String strHour = Integer.toString(getHour);

        int getMinute = getIntent().getIntExtra("minute", minute);
        Log.d(TAG, "getMinute : " + getMinute);
        String strMinute = Integer.toString(getMinute);


        TextView abvResult = findViewById(R.id.abvResult);
        if(abv>0) {
            abvResult.setText(strAbv);
        }else{
            abvResult.setText("0");
        }

        TextView penalty = findViewById(R.id.penalty);
        if(abv < 0.08 && abv > 0.03){
            penalty.setText("벌점 100점, 면허정지 100일");
        }else if(abv>=0.08){
            penalty.setText("운전면허취소");
        }else{
            penalty.setText("문제 없음");
        }

        TextView hour = findViewById(R.id.hour);
        hour.setText(strHour);

        TextView minute = findViewById(R.id.minute);
        minute.setText(strMinute);


        Button backButton = findViewById(R.id.resultBack);
        backButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CalResult.this, Menu.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });

//        Button alarmButton =  findViewById(R.id.alarm);

//        alarmButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dlg1.setTitle("alerm");
//                dlg1.setMessage("알코올 농도가 0이 될 때 알려드릴가요?");
//                dlg1.setPositiveButton("네", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Intent intent = new Intent(CalResult.this, Timer.class);
//                        intent.putExtra("hour", getHour);
//                        intent.putExtra("minute", getMinute);
//                        startActivity(intent);
//                    }
//                });
//                dlg1.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                    }
//                });
//                dlg1.show();
//            }
//        });



//        alarmButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dlg = new Dialog(CalResult.this);
//                dlg.setContentView(R.layout.popup_alarm);
//                dlg.show();
//
//                Button alarmPopupBack = (Button) dlg.findViewById(R.id.alarmPopupBack);
//                Button alarmPopupStart = (Button) dlg.findViewById(R.id.alarmPopupStart);
//                Button alarmPopupCancel = (Button) dlg.findViewById(R.id.alarmPopupCancel);
//
//                alarmPopupBack.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        dlg.dismiss();
//                    }
//                });

//                alarmPopupCancel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        dlg.dismiss();
//                    }
//                });
//
//                alarmPopupStart.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent intent = new Intent(CalResult.this, Timer.class);
//                        intent.putExtra("hour", getHour);
//                        intent.putExtra("minute", getMinute);
//                        startActivity(intent);
//                        dlg.dismiss();
//                    }
//                });
//
//            }
//        });
    }

}
