package com.example.alcoholsafe;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.RealResponseBody;

public class ApiExplorer {
    String TAG = "apiExplorer";

    int thisYear;
    String holiday;


    public ApiExplorer(int thisYear){
        this.thisYear = thisYear;
    }


    public ArrayList<CalendarDay> getHoliday() {



        String year = String.valueOf(thisYear);


        /**
         * String Builder
         * : String은 수정 작업 시 기존 참조 데이터에 추가/삭제하는 것이 아니라 수정된 문자열을 새로 갖는 String을 생성 후 참조를 바꾸는 형식
         *   그러면 연산이 끝나고 남는 자원이 생성되기 때문에 비효율적
         *   이를 개선하기 위해서 나온 것이 String Builder!!!!1
         *   String Builder는 참조를 바꾸는 것이 아니라 참조하는 값을 바꾸는 방법
         *   따라서 문자열 수정 작업이 많을 경우 String Builder를 사용하는 것이 좋음
      음  */
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/getRestDeInfo"); /*URL*/

        try {
            urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=1A2Z3QTBxCdQdgUqudAf%2BhOmfds7wGNG9s1Y365T2aa%2FcbaivVypAscNWkOt00y7JZMF214QBZJWCfee%2Btd1GQ%3D%3D"); /*Service Key*/
            urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
            urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("50", "UTF-8")); /*한 페이지 결과 수*/
            urlBuilder.append("&" + URLEncoder.encode("solYear","UTF-8") + "=" + URLEncoder.encode(year, "UTF-8")); /*연*/
            urlBuilder.append("&" + URLEncoder.encode("_type","UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /*제이슨*/


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.d(TAG, "append e : " + e);
        }


        /**
         * okhttp로 만들어봅시다
         */

        URL url = null;
        try {
            url = new URL(urlBuilder.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.d(TAG, "url e :" + e);
        }

        // 요청 만들기
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

//        콜백

        try {
            Response response = client.newCall(request).execute();


            if(response.isSuccessful()){
                Log.d(TAG, "연결 성공 : ");
                ResponseBody body = response.body();

                if(body!=null) {
//                    BufferedReader rd = new BufferedReader(new InputStreamReader(response.body().byteStream()));
//
//                    StringBuilder sb = new StringBuilder();
//                    String line;
//                    while ((line = rd.readLine()) != null) {
//                        sb.append(line);
//                    }
//                    rd.close();

                    holiday = body.string();
                    Log.d(TAG, "holiday : " + holiday.length());
                }
            }else{
                Log.d(TAG, "연결 실패");
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "isSuccessful e :" + e);
        }
        Log.d(TAG, "holiday null? :  " + holiday);



        String temp;
        int temp_year;
        int month;
        int day;
        ArrayList<CalendarDay> restInfoList = new ArrayList<CalendarDay>();

        try {

            JSONObject jsonObject = new JSONObject(holiday);
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

        return restInfoList;

    }


}

