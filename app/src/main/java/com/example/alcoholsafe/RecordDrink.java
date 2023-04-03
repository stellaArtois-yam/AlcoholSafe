package com.example.alcoholsafe;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowAnimationFrameStats;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alcoholsafe.Deco.DateDecorator;
import com.example.alcoholsafe.Deco.RestDayDeco;
import com.example.alcoholsafe.Deco.RestInfo;
import com.example.alcoholsafe.Deco.SaturdayDeco;
import com.example.alcoholsafe.Deco.SundayDeco;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class RecordDrink extends AppCompatActivity {


    private final String TAG = "recordDrinkLog";

    private RecyclerView recyclerView;
    private Adapter adapter;
    private ArrayList<Recycler_item> recordList;
    Dialog dlg1;
    Dialog dlg2;

    MaterialCalendarView calendarView;


    EditText title;
    EditText content;

    SharedPreferences pref;
    SharedPreferences.Editor editor;


    String titleSave = null;
    String contentSave = null;

    String splitTitle[] = null;
    String splitContent[] = null;

    ArrayList<CalendarDay> restInfoList;

    HashSet<CalendarDay> eventDay;



    int selectYear = CalendarDay.today().getYear();
    int selectMonth;
    int selectDay;

    String restDay;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_drink);


        pref = getSharedPreferences("RecordDrink", MODE_PRIVATE);

        editor = pref.edit();



        /**
         *  뒤로 가기
         **/
        Button backButton = findViewById(R.id.calendarBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        /**
         * recyclerView, ArrayList 생성 및 adpater 연결
         */
        recyclerView = findViewById(R.id.recyclerView);

        adapter = new Adapter();

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        recordList = new ArrayList<>();
        adapter.setAdapterItem(recordList);


        /**
         * 날짜 hashset 생성
         */
        eventDay = new HashSet<CalendarDay>();

        /**
         * calendar 연결 및 deco
         */
        calendarView = findViewById(R.id.calendarView);
        calendarView.setSelectedDate(CalendarDay.today());
        calendarView.addDecorators(new SaturdayDeco(), new SundayDeco());



        /**
         * Json Parsing
         */
        restDay = getJsonString();
        jsonParsing(restDay);
        calendarView.addDecorator(new RestDayDeco(restInfoList));


//        new Thread(){
//            @Override
//            public void run() {
//                super.run();
//
//                ApiExplorer apiExplorer = new ApiExplorer(selectYear);
//
//                restInfoList = apiExplorer.getHoliday();
//                Log.d(TAG, "run: restInfo :" + restInfoList);
//
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        calendarView.addDecorator(new RestDayDeco(restInfoList));
//                    }
//                });
//
//
//            }
//        }.start();






















//            }
//        };
//        restInfoList = new ArrayList<>();

//        Thread getDay = new Thread(runnable);
//        getDay.start();

//        for (int i =0; i<restInfoList.size(); i++){
//            Log.d(TAG, "restInfoList : " + restInfoList.indexOf(i));
//        }


//        if(restInfoList.size()>0){
//            getDay.interrupt();
//            Log.d(TAG, "thread state : " + getDay.isInterrupted());
//
//        }












        /**
         *  calendarView가 클릭 되었을 때 해당 날짜에 데이터가 저장....
         */

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                selectMonth = calendarView.getSelectedDate().getMonth();
                selectDay = calendarView.getSelectedDate().getDay();
                Log.d(TAG, "selectDay click: " + selectDay);
                Log.d(TAG, "selectMonth click : " + selectMonth);

                recordList = new ArrayList<>();


                /**
                 * sp 가져옴
                 */

                for(int j=0; j<13;j++){
                    if(selectMonth==j){
                        for(int i=1; i<32;i++){
                            if(selectDay==i){
                                titleSave = pref.getString(Integer.toString(j)+Integer.toString(i)+"title", "");
                                contentSave = pref.getString(Integer.toString(j)+Integer.toString(i)+"content", "");
                                Log.d(TAG, "getTitle : " + titleSave);
                                Log.d(TAG, "getContent : " + contentSave);

                            }
                        }
                    }

                }


                if(titleSave.isEmpty() || contentSave.isEmpty()){

                }else{
                    splitTitle = titleSave.split("/.%_");
                    Log.d(TAG, "splitTitle : " + Arrays.toString(splitTitle));

                    splitContent = contentSave.split("/.%_");
                    Log.d(TAG, "splitContent : " + Arrays.toString(splitContent));


                if(splitContent.length ==0 || splitTitle.length==0){

                }else{
                    for (int i = 0; i < splitTitle.length; i++) {
                        recordList.add(new Recycler_item(splitTitle[i], splitContent[i]));
                    }
                    Log.d(TAG, "recordList size를 함 를 보자 : " +recordList.size());
                }
            }

            adapter.setAdapterItem(recordList);
            Log.d(TAG, "다른 날짜 눌렀을 때 adapter 연결");

            }
        });


        selectMonth = calendarView.getSelectedDate().getMonth();
        selectDay = calendarView.getSelectedDate().getDay();
        Log.d(TAG, "selectDay : " + selectDay);
        Log.d(TAG, "selectMonth: " + selectMonth);


        for(int j=0; j<13;j++){
            if(selectMonth==j){
                for(int i=1; i<32;i++){
                    if(selectDay==i){
                        titleSave = pref.getString(Integer.toString(j)+Integer.toString(i)+"title", "");
                        contentSave = pref.getString(Integer.toString(j)+Integer.toString(i)+"content", "");
//
                        Log.d(TAG, "시작할 때 sp(title): " + titleSave.isEmpty());
                        Log.d(TAG, "시작할 때 sp(content): " + contentSave.isEmpty());

                    }
                }
            }
        }

        for(int i=0; i<13; i++){
            for(int j=1; j<32;j++){
                if(!pref.getString(Integer.toString(i)+Integer.toString(j)+"title", "").isEmpty()){
                    eventDay.add(CalendarDay.from(selectYear, i, j));
                    Log.d(TAG, "시작할 때 eventDay : " + Arrays.toString(eventDay.toArray()));
                }
            }
        }


        calendarView.addDecorator(new DateDecorator(Color.GRAY, eventDay));





        /**
         * sp split으로 쪼개서 나갔다가 들어와도 다시 recyclerView에 보여지게
         */

        if(titleSave.isEmpty() || contentSave.isEmpty()){

        }else{
            Log.d(TAG, "titleSave empty? " + titleSave.isEmpty());
            splitTitle = titleSave.split("/.%_");
            Log.d(TAG, "여기가 문제인데 : " + splitTitle.length);


            splitContent = contentSave.split("/.%_");
            Log.d(TAG, "splitContent : " + splitContent.length);


            if(splitContent.length ==0 || splitTitle.length==0){
                Log.d(TAG, "splitCOntent.length == 0");

            }else{
                for (int i = 0; i < splitTitle.length; i++) {
                    recordList.add(new Recycler_item(splitTitle[i], splitContent[i]));
                }
                Log.d(TAG, "recordList에 추가해서 내용 확인: " +  recordList.indexOf(0));
                Log.d(TAG, "recordList size를 한 를 보자 : " +recordList.size());
            }
        }

        adapter.setAdapterItem(recordList);
        Log.d(TAG, "처음 어댑터 연결");








        /**
         * 추가하기 버튼
         */
        Button addButton = findViewById(R.id.add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 다이얼로그 초기화
                dlg1 = new Dialog(RecordDrink.this);

                // 타이틀 제거
                dlg1.requestWindowFeature(Window.FEATURE_NO_TITLE);

                dlg1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


                // xml 파일과 연결
                dlg1.setContentView(R.layout.popup);

                showDialog();

            }
        });


        /**
         * 아이템뷰 클릭 : textView dialog 보여줌
         */
        adapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d(TAG, "onItemClick: ");
                Log.d(TAG, "recordList toString: " + recordList.get(position).title.toString());

                dlg2 = new Dialog(RecordDrink.this);
                dlg2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dlg2.setContentView(R.layout.popup_save);
                dlg2.show();

                TextView saveTitle = dlg2.findViewById(R.id.saveTitle);
                TextView saveContent = dlg2.findViewById(R.id.saveContent);
                Button saveBack = dlg2.findViewById(R.id.saveBack);

                String titleS = recordList.get(position).title;
                Log.d(TAG, "titleMo : " + titleS);
                String contentS = recordList.get(position).content;
                Log.d(TAG, "contentMo : " + contentS);

                saveTitle.setText(titleS);
                saveContent.setText(contentS);

                saveBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dlg2.dismiss();
                    }
                });
            }


            /**
             * 수정 클릭 : popup dialog 보여줌
             */
            @Override
            public void onEditClick(View view, int position) {
                dlg1 = new Dialog(RecordDrink.this);
                dlg1.setContentView(R.layout.popup);
                dlg1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dlg1.show();

                title = dlg1.findViewById(R.id.title);
                content = dlg1.findViewById(R.id.content);
                Button save = dlg1.findViewById(R.id.save);
                Button backButton = dlg1.findViewById(R.id.dialogBack);
                Button delete = dlg1.findViewById(R.id.delete);


                String titleS = recordList.get(position).title;
                Log.d(TAG, "titleMo : " + titleS);
                String contentS = recordList.get(position).content;
                Log.d(TAG, "contentMo : " + contentS);

                title.setText(titleS);
                content.setText(contentS);

                    save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String afterTitle = title.getText().toString();
                        Log.d(TAG, "afterTitle : " + afterTitle);

                        recordList.get(position).setTitle(afterTitle);
                        Log.d(TAG, "recordList 수정해서 저장 title : " + recordList.get(position).title);

                        String afterContent = content.getText().toString();
                        Log.d(TAG, "onClick: afterContent : " + afterContent);
                        recordList.get(position).setContent(afterContent);
                        Log.d(TAG, "recordList 수정해서 저장 content : " + recordList.get(position).content );


                        adapter.notifyItemChanged(position, title);
                        adapter.notifyItemChanged(position, content);
                        Log.d(TAG, "recordList : " + recordList);

                        titleSave="";
                        contentSave="";

                        for(int i = 0 ; i < recordList.size(); i++){
                            titleSave = titleSave + recordList.get(i).getTitle() +"/.%_";
                            contentSave = contentSave + recordList.get(i).getContent() +"/.%_";
                            Log.d(TAG, "titleSave for문 : " + titleSave);
                            Log.d(TAG, "contentSave for문 : " + contentSave);
                        }



                        editor.putString(Integer.toString(selectMonth)+Integer.toString(selectDay)+"title", titleSave);
                        editor.putString(Integer.toString(selectMonth)+Integer.toString(selectDay)+"content", contentSave);
                        editor.commit();


                        dlg1.dismiss();
                    }
                });

                    backButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dlg1.dismiss();
                        }
                    });


                    delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                            String check = recordList.get(position).toString();
//                            Log.d(TAG, "check : " + check);

                            recordList.remove(position);

//                            editor.putString("titleSave", titleSave);

                            adapter.notifyItemRemoved(position);
                            adapter.notifyItemRangeChanged(position,recordList.size());

                            titleSave ="";
                            contentSave ="";

                            for(int i = 0 ; i < recordList.size(); i++){
                                titleSave = titleSave + recordList.get(i).getTitle() +"/.%_";
                                contentSave = contentSave + recordList.get(i).getContent() +"/.%_";
                                Log.d(TAG, "titleSave for문 : " + titleSave);
                                Log.d(TAG, "contentSave for문 : " + contentSave);
                            }


                            editor.putString(Integer.toString(selectMonth)+Integer.toString(selectDay)+"title", titleSave);
                            editor.putString(Integer.toString(selectMonth)+Integer.toString(selectDay)+"content", contentSave);
                            editor.commit();

                            /**
                             * 삭제하면 도트 없애기
                             */
                            for(int i=0; i<13; i++){
                                for(int j=1; j<32;j++){
                                    if(pref.getString(Integer.toString(i)+Integer.toString(j)+"title", "").isEmpty()){
                                        eventDay.remove(CalendarDay.from(selectYear, i, j));
                                    }
                                }
                            }

                            calendarView.removeDecorators();
                            calendarView.addDecorators(new DateDecorator(Color.GRAY, eventDay),
                                    new SaturdayDeco(), new SundayDeco(), new RestDayDeco(restInfoList));


                            dlg1.dismiss();
                        }
                    });
                }


            /**
             * 삭제 클릭
             */
                @Override
                 public void onDeleteclick(View view, int position) {

                    recordList.remove(position);
                    
                    // 여기서 지금 recordlist가 변한거니까 이 상태로 다시 for문으로 넣어서 putString을 업로드 하는 것이지.

                    titleSave=null;
                    contentSave=null;

                    for(int i = 0 ; i < recordList.size(); i++){
                        titleSave = titleSave + recordList.get(i).getTitle() +"/.%_";
                        contentSave = contentSave + recordList.get(i).getContent()  +"/.%_";
                        Log.d(TAG, "titleSave for문 : " + titleSave);
                        Log.d(TAG, "contentSave for문 : " + contentSave);
                    }
                    Log.d(TAG, "onDeleteclick: for문");


                    editor.putString(Integer.toString(selectMonth)+Integer.toString(selectDay)+"title", titleSave);
                    editor.putString(Integer.toString(selectMonth)+Integer.toString(selectDay)+"content", contentSave);
                    editor.commit();

                    Log.d(TAG, "onDeleteclick: sp저장");

                    adapter.notifyItemRemoved(position);
                    adapter.notifyItemRangeChanged(position,recordList.size());
                    Log.d(TAG, "onDeleteclick: adpater 적용");


                    /**
                     * 삭제하면 도트 없애기
                     */
                    for(int i=0; i<13; i++){
                        for(int j=1; j<32;j++){
                            if(pref.getString(Integer.toString(i)+Integer.toString(j)+"title", "").isEmpty()){
                                eventDay.remove(CalendarDay.from(selectYear, i, j));
                            }
                        }
                    }

                    calendarView.removeDecorators();
                    calendarView.addDecorators(new DateDecorator(Color.GRAY, eventDay),
                            new SaturdayDeco(), new SundayDeco(), new RestDayDeco(restInfoList));




            }


        });


    }


    /**
     *  다이얼로그를 보여주는 메소드
     */

    public void showDialog(){



        Button dialogBack = dlg1.findViewById(R.id.dialogBack);
        title = dlg1.findViewById(R.id.title);
        content =  dlg1.findViewById(R.id.content);
        Button save = dlg1.findViewById(R.id.save);
        Button delete = dlg1.findViewById(R.id.delete);

        dlg1.show();


        editor = pref.edit();


        /**
         * back 버튼
         */

        dialogBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg1.dismiss();
            }
        });




        /**
         * 저장하기 버튼을 누르면
         */

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //제목이 recyclerView로 올라감
                if(title.getText().toString().equals("") || content.getText().toString().equals("")){

                }else {
                    recordList.add(new Recycler_item(title.getText().toString(), content.getText().toString()));
                    Log.d(TAG, "여기가 추가?");
                }



                    titleSave = pref.getString(Integer.toString(selectMonth) + Integer.toString(selectDay) + "title", "");
                    Log.d(TAG, "dlgShow title : " + titleSave);
                    contentSave = pref.getString(Integer.toString(selectMonth) + Integer.toString(selectDay) + "content", "");
                    Log.d(TAG, "dlgShow content : " + contentSave);





                if(recordList.size()==0){
                    for(int i = 0 ; i < recordList.size(); i++){
                        titleSave = titleSave + recordList.get(0).getTitle() + "/.%_";
                        contentSave = contentSave + recordList.get(0).getContent() + "/.%_";
//                    Toast.makeText(RecordDrink.this, "제목과 내용을 모두 입력해주세요.", Toast.LENGTH_LONG).show();
                        Log.d(TAG, "titleSave for문_0 : " + titleSave);
                        Log.d(TAG, "contentSave for문_0 : " + contentSave);
                    }
                }else{

                    for(int i = recordList.size()-1 ; i < recordList.size(); i++){
                        titleSave = titleSave + recordList.get(i).getTitle()  +"/.%_";
                        contentSave = contentSave + recordList.get(i).getContent() +"/.%_";
                        Log.d(TAG, "titleSave for문 : " + titleSave);
                        Log.d(TAG, "contentSave for문 : " + contentSave);
                    }
                }


                adapter.setAdapterItem(recordList);

                eventDay.add(CalendarDay.from(selectYear, selectMonth, selectDay));
                Log.d(TAG, "dot 추가하기 eventDay : " + eventDay);
                calendarView.addDecorator(new DateDecorator(Color.GRAY, eventDay));
                Log.d(TAG, "추가하기 dot : " );




//                for(int i = 0; i < eventDayTemp.size(); i++){
//                    calendarView.addDecorator(new DateDecorator(Color.GRAY, Collections.singleton(eventDayTemp.get(i))));
//                }

                if(title.getText().toString().equals("") || content.getText().toString().equals("")){
                    Toast.makeText(RecordDrink.this, "제목과 내용을 모두 입력해주세요.", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "되는거냐");
                }else {
                    editor.putString(Integer.toString(selectMonth) + Integer.toString(selectDay) + "title", titleSave);
                    Log.d(TAG, "추가하기 title : ");
                    editor.putString(Integer.toString(selectMonth) + Integer.toString(selectDay) + "content", contentSave);
                    Log.d(TAG, "추가하기 content : ");
                    editor.commit();


                    dlg1.dismiss();
                }






            }
        });





        /**
         * 취소 버튼
         */

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dlg1.dismiss();
            }
        });
    }


    public String getJsonString()  {
        String json = "";

        try{
            InputStream is = getAssets().open("RestInfo.json");
            int fileSize = is.available();

            byte[] buffer = new byte[fileSize];
            is.read(buffer);
            is.close();

            json = new String(buffer, "UTF-8");
        }catch (IOException ex){
            ex.printStackTrace();
        }

        return json;

    }

    public void jsonParsing(String json){
        String temp;
        int temp_year;
        int month;
        int day;
        restInfoList = new ArrayList<>();

        try {

            JSONObject jsonObject = new JSONObject(json);
            Log.d("jsonParsing", "jsonParsing: 1");
            JSONObject response =jsonObject.getJSONObject("response");
            Log.d("jsonParsing", "response : " + response);
            JSONObject body = response.getJSONObject("body");
            Log.d("jsonParsing", "body : " + body);
            JSONObject items = body.getJSONObject("items");
            Log.d("jsonParsing", "items : " + items);

            JSONArray jsonArray = items.getJSONArray("item");
            Log.d("getJson", "ArrayLength : " + jsonArray.length());

            for(int i = 0; i<jsonArray.length(); i++){
                JSONObject restObject = jsonArray.getJSONObject(i);
                Log.d("getJson", "restObject : " + restObject);


                temp = restObject.getString("locdate");

                temp_year = Integer.parseInt(temp.substring(0, 4));
                month = Integer.parseInt(temp.substring(4, 6)) -1;
                day = Integer.parseInt(temp.substring(6));

                restInfoList.add(CalendarDay.from(temp_year, month, day));
            }


        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "jsonParsing: e :" + e);
        }




    }





    
}
