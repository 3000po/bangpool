package kr.popcorn.sharoom.helper;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import kr.popcorn.sharoom.activity.Fragment.Host.Activity_host_view;
import kr.popcorn.sharoom.activity.TabView.Activity_roading;
import kr.popcorn.sharoom.activity.TabView.Activity_server_roading;

/**
 * Created by user on 16. 3. 2.
 */

//TODO 방정보를 저장할 자료형 클래스
public class Helper_room {

    private static Helper_room roomData = null;
    final static String baseURL = "http://14.63.227.200/";

    public static ArrayList<Helper_roomData> list = new ArrayList<Helper_roomData>();

    public static int roomCount;
    public static int MAX = 50;


    public static ArrayList<String>[] image = new ArrayList[MAX];

    public Helper_room(){

    }

    public static void getRoomData_Login(final String id, final Context mContext) {

            final RequestParams idParams = new RequestParams("userID", id);
            Helper_server.post("data/getRoomData.php", idParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                    roomData = null;
                    roomData = new Helper_room();

                    try {
                        roomCount = Integer.parseInt(response.get("num").toString().trim());

                        for (int i = 0; i < roomCount; i++) {
                            int roomNumber = Integer.parseInt(response.get("roomNumber" + i).toString().trim());
                            int userID = Integer.parseInt(response.get("userID" + i).toString().trim());
                            String title = response.get("title" + i).toString().trim();
                            String address = response.get("address" + i).toString().trim();
                            String price = response.get("price" + i).toString().trim();
                            String roomKind = response.get("roomKind" + i).toString().trim();
                            String roomInfo = response.get("roomInfo" + i).toString().trim();
                            String fac = response.get("fac" + i).toString().trim();
                            double lat = Double.parseDouble(response.get("lat" + i).toString().trim());
                            double lng = Double.parseDouble(response.get("lng" + i).toString().trim());
                            String sDate = response.get("sDate" + i).toString().trim();
                            String eDate = response.get("eDate" + i).toString().trim();
                            int isClosed = Integer.parseInt(response.get("isClosed"+i).toString().trim());
                            int rUserID = Integer.parseInt(response.get("rUserID" + i).toString().trim());

                            image[i] = new ArrayList<String>();
                            for(int j=0; j< 8; j++){
                                String str=response.get("image"+j+"?"+i).toString().trim();
                                String imageUrl = baseURL+str;
                                System.out.println("ccccc" + imageUrl);
                                addImage(i, imageUrl);
                            }
                            list.add(i, new Helper_roomData(roomNumber, userID, title, address, price, roomKind, roomInfo, fac, lat, lng, sDate, eDate, image[i], isClosed, rUserID));
                        }
                        System.out.println("ListData finish");
                        Helper_userData.login_GetData(id, mContext);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    Log.d("Failed: ", "myself " + statusCode);
                    Log.d("Error : ", "myself " + throwable);
                }
            });

    }
    public static void getRoomData_Login(final String id, final Context mContext, final int num) {

        final RequestParams idParams = new RequestParams("userID", id);
        Helper_server.post("data/getRoomData.php", idParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                roomData = null;
                roomData = new Helper_room();

                try {
                    roomCount = Integer.parseInt(response.get("num").toString().trim());

                    for (int i = 0; i < roomCount; i++) {
                        int roomNumber = Integer.parseInt(response.get("roomNumber" + i).toString().trim());
                        int userID = Integer.parseInt(response.get("userID" + i).toString().trim());
                        String title = response.get("title" + i).toString().trim();
                        String address = response.get("address" + i).toString().trim();
                        String price = response.get("price" + i).toString().trim();
                        String roomKind = response.get("roomKind" + i).toString().trim();
                        String roomInfo = response.get("roomInfo" + i).toString().trim();
                        String fac = response.get("fac" + i).toString().trim();
                        double lat = Double.parseDouble(response.get("lat" + i).toString().trim());
                        double lng = Double.parseDouble(response.get("lng" + i).toString().trim());
                        String sDate = response.get("sDate" + i).toString().trim();
                        String eDate = response.get("eDate" + i).toString().trim();
                        int isClosed = Integer.parseInt(response.get("isClosed"+i).toString().trim());
                        int rUserID = Integer.parseInt(response.get("rUserID" + i).toString().trim());

                        image[i] = new ArrayList<String>();

                        for(int j=0; j< 8; j++){
                            String str=response.get("image"+j+"?"+i).toString().trim();
                            String imageUrl = baseURL+str;
                            System.out.println("ccccc" + imageUrl);
                            addImage(i, imageUrl);
                        }
                        list.add(i,new Helper_roomData(roomNumber, userID, title, address, price, roomKind, roomInfo, fac, lat, lng, sDate, eDate, image[i], isClosed, rUserID));
                    }
                    System.out.println("ListData finish");
                    Helper_userData.login_GetData(id, mContext, num);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("Failed: ", "myself " + statusCode);
                Log.d("Error : ", "myself " + throwable);
            }
        });
    }

    public static void refreshRoomData(final String id, final Context mContext) {

        Intent intent = new Intent(mContext, Activity_server_roading.class);
        intent.putExtra("main","서버와 연결 중입니다.");
        mContext.startActivity(intent); // 서버 정보 받을 동안 보여줄 activity

        final Activity_server_roading activity_server_roading = (Activity_server_roading) Activity_server_roading.activity_server_roading;

        final RequestParams idParams = new RequestParams("userID", id);
        Helper_server.post("data/getRoomData.php", idParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                roomData = null;
                roomData = new Helper_room();

                try {
                    roomCount = Integer.parseInt(response.get("num").toString().trim());

                    for (int i = 0; i < roomCount; i++) {
                        int roomNumber = Integer.parseInt(response.get("roomNumber" + i).toString().trim());
                        int userID = Integer.parseInt(response.get("userID" + i).toString().trim());
                        String title = response.get("title" + i).toString().trim();
                        String address = response.get("address" + i).toString().trim();
                        String price = response.get("price" + i).toString().trim();
                        String roomKind = response.get("roomKind" + i).toString().trim();
                        String roomInfo = response.get("roomInfo" + i).toString().trim();
                        String fac = response.get("fac" + i).toString().trim();
                        double lat = Double.parseDouble(response.get("lat" + i).toString().trim());
                        double lng = Double.parseDouble(response.get("lng" + i).toString().trim());
                        String sDate = response.get("sDate" + i).toString().trim();
                        String eDate = response.get("eDate" + i).toString().trim();
                        int isClosed = Integer.parseInt(response.get("isClosed"+i).toString().trim());
                        int rUserID = Integer.parseInt(response.get("rUserID" + i).toString().trim());

                        image[i] = new ArrayList<String>();
                        for(int j=0; j< 8; j++){
                            String str=response.get("image"+j+"?"+i).toString().trim();
                                String imageUrl = baseURL+str;
                                System.out.println("ccccc" + imageUrl);
                                addImage(i, imageUrl);
                        }
                        list.add(i, new Helper_roomData(roomNumber, userID, title, address,price,roomKind,roomInfo, fac, lat, lng, sDate,eDate, image[i], isClosed, rUserID) );
                    }
                    ((Activity)Activity_server_roading.activity_server_roading).finish();
                    System.out.println("ListData finish");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("Failed: ", "myself " + statusCode);
                Log.d("Error : ", "myself " + throwable);
                ((Activity)Activity_server_roading.activity_server_roading).finish();
                Toast.makeText(mContext, "서버에 연결을 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    //룸 넘버의 list index 찾기
    public static int search_index(int rn){
        for(int i = 0 ; i < roomCount ; i++){
            if(list.get(i).getRoomNumber() == rn)
                return i;
        }
        Log.e("인덱스를 찾을 수 없습니다. ", "<search_index> "+ rn);
        return -1;
    }

    //룸 넘버의 주인 찾기
    public static int search_host(int rn){
        for(int i = 0 ; i < roomCount ; i++){
            if(list.get(i).getRoomNumber() == rn)
                return list.get(i).getUserID();
        }
        Log.e("인덱스를 찾을 수 없습니다. ", "<search_host> "+ rn);
        return -1;
    }

    public static Helper_room getInstance(){
        return roomData;
    }

    public static String getImage(int index, int number) {
        return image[index].get(number);
    }

    public static void addImage(int index, String value) {
        image[index].add(value);
    }

}
