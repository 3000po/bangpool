package kr.popcorn.sharoom.helper;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ImageView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import kr.popcorn.sharoom.R;

/**
 * Created by user on 16. 3. 2.
 */

//TODO 방정보를 저장할 자료형 클래스
public class Test {

    private static Test roomData = null;
    final static String baseURL = "14.63.227.200/";

    public static ArrayList<Helper_roomData> list = new ArrayList<Helper_roomData>();

    public static int roomCount;
    public static int MAX = 50;
    public static int[] roomNumber = new int[MAX];
    public static int[] userID =new int[MAX];
    public static String[] title = new String[MAX];
    public static String[] address = new String[MAX];
    public static String[] price = new String[MAX];
    public static String[] roomKind = new String[MAX];
    public static String[] roomInfo= new String[MAX];
    public static String[] sDate= new String[MAX];
    public static String[] eDate= new String[MAX];

    public static ArrayList<String>[] image = new ArrayList[MAX];

    public Test(){

    }

    public static void setData(int roomNumber, int userID, String title, String address, String price, String roomKind, String roomInfo, String sDate, String eDate, int index) {
        setRoomNumber(index, roomNumber);
        setUserID(index, userID);
        setTitle(index, title);
        setAddress(index, address);
        setPrice(index, price);
        setRoomKind(index, roomKind);
        setRoomInfo(index, roomInfo);
        setSDate(index, sDate);
        setEDate(index, eDate);
    }

    public static void getRoomData_Login(final String id, final Context mContext) {

            final RequestParams idParams = new RequestParams("userID", id);
            Helper_server.post("data/getRoomData.php", idParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                    roomData = null;
                    roomData = new Test();

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
                            String sDate = response.get("sDate" + i).toString().trim();
                            String eDate = response.get("eDate" + i).toString().trim();

                            image[i] = new ArrayList<String>();
                            for(int j=0; j< 8; j++){
                                String imageUrl = baseURL+response.get("image"+j+"?"+i).toString().trim();
                                System.out.println("ccccc" + imageUrl);
                                addImage(i, imageUrl);
                            }
                            list.add( new Helper_roomData(roomNumber, userID, title, address,price,roomKind,roomInfo,sDate,eDate, image[i]) );
                            roomData.setData(roomNumber, userID, title, address, price, roomKind, roomInfo, sDate, eDate, i);
                            System.out.println("aaaaa " + roomNumber + " " + userID + " " + title + " " + address + " " + price + " " + roomKind + " " + roomInfo + " " + sDate + " " + eDate + " ");
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
                roomData = new Test();

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
                        String sDate = response.get("sDate" + i).toString().trim();
                        String eDate = response.get("eDate" + i).toString().trim();
                        image[i] = new ArrayList<String>();

                        for(int j=0; j< 8; j++){
                            String imageUrl = baseURL+response.get("image"+j+"?"+i).toString().trim();
                            System.out.println("ccccc" + imageUrl);
                            addImage(i, imageUrl);
                        }
                        list.add( new Helper_roomData(roomNumber, userID, title, address,price,roomKind,roomInfo,sDate,eDate, image[i]) );
                        roomData.setData(roomNumber, userID, title, address, price, roomKind, roomInfo, sDate, eDate, i);
                        System.out.println("aaaaa " + roomNumber + " " + userID + " " + title + " " + address + " " + price + " " + roomKind + " " + roomInfo + " " + sDate + " " + eDate + " ");
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
    public static Test getInstance(){
        return roomData;
    }

    public static int getRoomNumber(int index) {
        return roomNumber[index];
    }

    public static void setRoomNumber(int index, int value) {
        roomNumber[index]=value;
    }


    public static int getUserID(int index) {
        return userID[index];
    }

    public static void setUserID(int index, int value) {
        userID[index]=value;
    }
    public static String getTitle(int index) {
        return title[index];
    }

    public static void setTitle(int index, String value) {
        title[index]=value;
    }
    public static String getAddress(int index) {
        return address[index];
    }

    public static void setAddress(int index, String value) {
        address[index]=value;
    }

    public static String getPrice(int index) {
        return price[index];
    }

    public static void setPrice(int index, String value) {
        title[index]=value;
    }

    public static String getRoomKind(int index) {
        return roomKind[index];
    }

    public static void setRoomKind(int index, String value) {
        roomKind[index]=value;
    }
    public static String getRoomInfo(int index) {
        return roomInfo[index];
    }

    public static void setRoomInfo(int index, String value) {
        roomInfo[index]=value;
    }
    public static String getSDate(int index) {
        return sDate[index];
    }

    public static void setSDate(int index, String value) {
        sDate[index]=value;
    }

    public static String getEDate(int index) {
        return eDate[index];
    }

    public static void setEDate(int index, String value) {
        eDate[index]=value;
    }
    public static String getImage(int index, int number) {
        return image[index].get(number);
    }

    public static void addImage(int index, String value) {
        image[index].add(value);
    }
}
