package kr.popcorn.sharoom.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import kr.popcorn.sharoom.activity.Fragment.User.Activity_user_view;

/**
 * Created by Administrator on 2016-03-19.
 */

//TODO 유저정보 저장하는 클래스

public class Helper_userData {

    private static Helper_userData user;

    public int userID;
    public String id;
    public String name;
    public String phoneNumber;
    public String email;
    public String facebook;
    public String kakaotalk;
    public String sessionKey;
    public int isHost;

    public Helper_userData(int userID, String name, String phoneNumber, String email, String facebook, String kakaotalk, int isHost) {
        this.userID = userID;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.facebook = facebook;
        this.kakaotalk = kakaotalk;
        this.isHost = isHost;
    }

    public Helper_userData(int userID, String id, String name, String phoneNumber, String email, String facebook, String kakaotalk, int isHost) {
        this.userID = userID;
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.facebook = facebook;
        this.kakaotalk = kakaotalk;
        this.isHost = isHost;
    }

    public Helper_userData(int userID, String id, String name, String phoneNumber, String email, String facebook, String kakaotalk, String sessionKey, int isHost) {
        this.userID = userID;
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.facebook = facebook;
        this.kakaotalk = kakaotalk;
        this.sessionKey = sessionKey;
        this.isHost = isHost;
    }

    public Helper_userData(){

    }
    public static Helper_userData getInstance(Context mContext) {
        return user;
    }

    public static void login_GetData(String id, final Context mContext, int num) {
        if( user == null ) {
            final RequestParams idParams = new RequestParams("id", id);

            //kakaotalk
            if( num == 0 ) {
                Helper_server.post("data/getProfile_kt.php", idParams, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.i("myself", "success");
                        int userID;
                        String name;
                        String phoneNumber;
                        String email;
                        String facebook;
                        String kakaotalk;
                        int isHost;

                        try {
                            userID = isNull_Int(response.get("userID"));
                            name = isNull_String(response.get("name"));
                            phoneNumber = isNull_String(response.get("phoneNumber"));
                            email = isNull_String(response.get("email"));
                            facebook = isNull_String(response.get("facebook"));
                            kakaotalk = isNull_String(response.get("kakaotalk"));
                            isHost = isNull_Int(response.get("isHost"));

                            user = new Helper_userData(userID, name, phoneNumber, email, facebook, kakaotalk, isHost);

                            Intent intent = new Intent(mContext, Activity_user_view.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mContext.startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        Log.d("Failed: ", "myself " + statusCode);
                        Log.d("Error : ", "myself " + throwable);
                    }
                });
            }else if( num == 1 ){  // facebook
                Helper_server.post("data/getProfile_fb.php", idParams, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.i("myself", "success");
                        int userID;
                        String name;
                        String phoneNumber;
                        String email;
                        String facebook;
                        String kakaotalk;
                        int isHost;

                        try {
                            userID = isNull_Int(response.get("userID"));
                            name = isNull_String(response.get("name"));
                            phoneNumber = isNull_String(response.get("phoneNumber"));
                            email = isNull_String(response.get("email"));
                            facebook = isNull_String(response.get("facebook"));
                            kakaotalk = isNull_String(response.get("kakaotalk"));
                            isHost = isNull_Int(response.get("isHost"));

                            user = new Helper_userData(userID, name, phoneNumber, email, facebook, kakaotalk, isHost);

                            Intent intent = new Intent(mContext, Activity_user_view.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mContext.startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        Log.d("Failed: ", "myself " + statusCode);
                        Log.d("Error : ", "myself " + throwable);
                    }
                });
            }
        }
        else{
            Intent intent = new Intent(mContext, Activity_user_view.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }
    }


    public static void login_GetData(String id, final Context mContext) {
        if( user == null ) {
            final RequestParams idParams = new RequestParams("id", id);

            Helper_server.post("data/getProfile_Id.php", idParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.i("myself", "success");
                    int userID;
                    String id;
                    String name;
                    String phoneNumber;
                    String email;
                    String facebook;
                    String kakaotalk;
                    String sessionkey;
                    int isHost;

                    try {
                        userID = isNull_Int(response.get("userID"));
                        id = isNull_String(response.get("id"));
                        name = isNull_String(response.get("name"));
                        phoneNumber = isNull_String(response.get("phoneNumber"));
                        email = isNull_String(response.get("email"));
                        facebook = isNull_String(response.get("facebook"));
                        kakaotalk = isNull_String(response.get("kakaotalk"));
                        sessionkey = isNull_String(response.get("sessionkey"));
                        isHost = isNull_Int(response.get("isHost"));

                        Log.d("userData", id);
                        Log.d("userData", name);
                        Log.d("userData", email);

                        user = new Helper_userData(userID, id, name, phoneNumber, email, facebook, kakaotalk, sessionkey, isHost);

                        Intent intent = new Intent(mContext, Activity_user_view.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    Log.d("Failed: ", "myself " + statusCode);
                    Log.d("Error : ", "myself " + throwable);
                }
            });
        }
        else{
            Intent intent = new Intent(mContext, Activity_user_view.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }
    }

    public static Helper_userData getInstance(){return user;}

   public static Helper_userData getInstance(String id, Context applicationContext){
       return user;
   }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getKakaotalk() {
        return kakaotalk;
    }

    public void setKakaotalk(String kakaotalk) {
        this.kakaotalk = kakaotalk;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public static String isNull_String(Object response){
        if(response == null | response.equals(null)) return "";
        else return response.toString().trim();
    }
    public static int isNull_Int(Object response){
        if(response == null || response.equals(null)) return -1;
        else return Integer.parseInt(response.toString().trim());
    }
    public static void setUserNull(){
        user = null;
    }

}
