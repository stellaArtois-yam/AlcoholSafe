package com.example.alcoholsafe;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AlcoholCal extends AppCompatActivity {

    String TAG;

    String[] gender = {"선택","남자", "여자"};
    double r;
    String[] kind = {"주종 선택","소주", "맥주", "양주", "와인", "막걸리"};
    String[] cup = {"병", "잔"};
    int ml;
    String[] time = {"1.5", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
    double maxAbv;
    double currentAbv;
    int weight;
    double abv;
    int cupCount;
    double numT;
    double adjTime;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alcohol_calculate);
        overridePendingTransition(R.anim.up, R.anim.none);


        Button backButton = findViewById(R.id.calBack);
        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
//                overridePendingTransition(R.anim.none, R.anim.down);
//                startActivity(new Intent(AlcoholCal.this, Menu.class));

                finish();

            }
        });

        // 성별 선택
        Spinner spinGender = findViewById(R.id.gender);

        ArrayAdapter<String>adapterGen= new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, gender);
        adapterGen.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinGender.setAdapter(adapterGen);

        // 주종 선택
        Spinner spinKind = findViewById(R.id.kind);

        ArrayAdapter<String>adapterKind = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, kind);
        adapterKind.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinKind.setAdapter(adapterKind);


        // 병/잔 선택
        Spinner spinCup = findViewById(R.id.cup);

        ArrayAdapter<String>adapterCup = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, cup);
        adapterCup.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinCup.setAdapter(adapterCup);


        // 마지막 음주 시간 선택
        Spinner spinTime = findViewById(R.id.lastTime);

        ArrayAdapter<String>adapterTime = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, time);
        adapterTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinTime.setAdapter(adapterTime);




        // 몸무게 입력
        EditText editWeight = findViewById(R.id.weight);



        // 알코올 도수 입력
        EditText editAbv = findViewById(R.id.abv);


        // 몇 잔, 몇 병 입력
        EditText editVol = findViewById(R.id.volume);



//         계산하기 버튼
        Button calButton = (Button) findViewById(R.id.calButton);
        calButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                // 성별 계수
                if(spinGender.getSelectedItem().toString().equals("남자")){
                   r =  0.86;
                    Log.d("spinGenM", "spinGenM : " + r);
                }else if(spinGender.getSelectedItem().toString().equals("여자")){
                    r = 0.64;
                    Log.d("spinGenW", "spinGenW : " + r);
                }


                //몸무게 받아오기

                String strW = editWeight.getText().toString();
                if(strW.equals("")){
                    weight = 0;
                }else{
                    weight = Integer.parseInt(strW);
                    Log.d("weight", "weight : " + weight);
                }




                // 주종 별 용량
                if(spinKind.getSelectedItem().toString().equals("소주")){
                    if(spinCup.getSelectedItem().toString().equals("병")){
                        ml = 360;
                        Log.d("spinSB", "spinSB : " + ml);
                    }else if(spinCup.getSelectedItem().toString().equals("잔")){
                        ml = 48;
                        Log.d("spinSC", "spinSC : " + ml);
                    }
                }else if(spinKind.getSelectedItem().toString().equals("맥주")){
                    if(spinCup.getSelectedItem().toString().equals("병")){
                        ml = 500;
                        Log.d("spinBB", "spinBB : " + ml);
                    }else if(spinCup.getSelectedItem().toString().equals("잔")){
                        ml = 200;
                        Log.d("spinSC", "spinSC : " + ml);
                    }
                }else if(spinKind.getSelectedItem().toString().equals("양주")){
                    if(spinCup.getSelectedItem().toString().equals("병")){
                         ml = 750;
                        Log.d("spinWHB", "spinWHB : " + ml);
                    }else if(spinCup.getSelectedItem().toString().equals("잔")){
                        ml =30;
                        Log.d("spinWHC", "spinWHC : " + ml);
                    }
                }else if(spinKind.getSelectedItem().toString().equals("와인")){
                    if(spinCup.getSelectedItem().toString().equals("병")){
                        ml = 750;
                        Log.d("spinWB", "spinWB : " + ml);
                    }else if(spinCup.getSelectedItem().toString().equals("잔")){
                        ml = 125;
                        Log.d("spinWC", "spinWC : " + ml);
                    }
                }else if(spinKind.getSelectedItem().toString().equals("막걸리")){
                    if(spinCup.getSelectedItem().toString().equals("병")){
                        ml = 750;
                        Log.d("spinMB", "spinMB : " + ml);
                    }else if(spinCup.getSelectedItem().toString().equals("잔")){
                        ml = 300;
                        Log.d("spinMC", "spinMC : " + ml);
                    }
                }

               // 병, 잔 개수 입력
                String strV = editVol.getText().toString().trim();
                if(strV.equals("")){
                    cupCount=0;
                }else{
                    cupCount = Integer.parseInt((strV));
                    Log.d("cupCount", "cupCount : " + cupCount);
                }


                //알코올 도수 입력
                String strA = editAbv.getText().toString().trim();
                if(strA.equals("")){
                    abv=0;
                }else{
                    abv = Double.parseDouble(strA);
                    Log.d("abv", "abv : " + abv);
                }


                // 시간 입력
                String strT = spinTime.getSelectedItem().toString();
                numT = Double.parseDouble(strT);
                Log.d("numT", "onClick: numT : " + adjTime);


                if(spinTime.getSelectedItem().equals("1.5") ){
                    adjTime = (numT -1.5);
                }else{
                    adjTime = (numT/2);
                }


                double ABV = abv * 0.01;
                Log.d("ABV", "ABV : " + ABV);

                maxAbv = ((ml * cupCount * ABV * 0.7894 * 0.7)/(weight * r * 10));
                Log.d("maxAbv", "maxAbv : " + maxAbv);

                // 시간 계산 : hour 부분
                
                double roundAbv = 0;
                
                if(spinGender.getSelectedItem().equals("남자")){
                    roundAbv = Math.round(maxAbv*1000)/1000.0;
                    Log.d(TAG, "roundAbv : " + roundAbv);
                }else if(spinGender.getSelectedItem().equals("여자")){
                    roundAbv = Math.round(maxAbv*1000)/1000.0 - 0.001;
                    Log.d(TAG, "roundAbv : " + roundAbv);
                }

                double hourDouble = roundAbv/0.015;
                Log.d(TAG, "hourDouble : " + hourDouble);
                int hour = (int)hourDouble;
                Log.d(TAG, "hour : " + hour);

                // 시간 계산 : minute 부분
                double round = hourDouble - hour;
                Log.d(TAG, "round : " + round);
                int minute =(int) (round * 60);
                Log.d(TAG, "minute : " + minute);


                if(spinGender.getSelectedItem().equals("남자")){
                    currentAbv = maxAbv - (0.03 * adjTime);
                }else{
                    currentAbv = maxAbv - (0.03 * adjTime) -0.001;
                }

                Log.d("currentAbv", "currentAbv : " + currentAbv);



                double maxRound = Math.round(currentAbv*1000)/1000.0;
                Log.d("maxRound", "maxRound : " + maxRound);




                if(spinGender.getSelectedItem().toString().equals("선택")){
                    Toast.makeText(AlcoholCal.this, "성별을 선택해주세요.",Toast.LENGTH_LONG).show();
                }else if(weight==0){
                    Toast.makeText(AlcoholCal.this, "몸무게를 입력해주세요.",Toast.LENGTH_LONG).show();
                }else if(spinKind.getSelectedItem().toString().equals("주종 선택")){
                    Toast.makeText(AlcoholCal.this, "주종을 선택해주세요.",Toast.LENGTH_LONG).show();
                }else if(abv==0){
                    Toast.makeText(AlcoholCal.this, "알코올 도수를 입력해주세요.",Toast.LENGTH_LONG).show();
                }else if(cupCount==0){
                    Toast.makeText(AlcoholCal.this, "용량을 입력해주세요.",Toast.LENGTH_LONG).show();
                }else {
                    Intent intent = new Intent(AlcoholCal.this, CalResult.class);
                    intent.putExtra("abvResult", maxRound);
                    intent.putExtra("hour", hour);
                    intent.putExtra("minute", minute);
                    startActivity(intent);
                }
            }
        });

// 혈중 알콜농도 최고치 = (음주량(ml) x 술의 농도(%) x 0.7894) / (wei치ht x 성별 계수(R))

        }
}



