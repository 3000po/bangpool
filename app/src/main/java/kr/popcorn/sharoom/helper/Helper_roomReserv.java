package kr.popcorn.sharoom.helper;

import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Administrator on 2016-05-24.
 */
public class Helper_roomReserv {

    public static ArrayList<Helper_roomReservData> reservRoomList = new ArrayList<Helper_roomReservData>(); ;

    public static int userID;
    public static int roomNumber;
    public static int roomCount;

    public Helper_roomReserv() {
    }

    public static void getReservRoomData(final int userID) {

        final RequestParams reservParams = new RequestParams("userID", userID);

        Helper_server.post("data/getReservation.php", reservParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    roomCount = Integer.parseInt(response.get("num").toString().trim());

                    for (int i = 0; i < roomCount; i++) {
                        int roomNumber = Integer.parseInt(response.get("roomNumber" + i).toString().trim());
                        int userID = Integer.parseInt(response.get("userID" + i).toString().trim());

                        reservRoomList.add(i, new Helper_roomReservData(roomNumber, userID));
                    }
                    System.out.println("ListData finish2");
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


    public static ArrayList<Helper_roomReservData> getInstance(){
        return reservRoomList;
    }


}
