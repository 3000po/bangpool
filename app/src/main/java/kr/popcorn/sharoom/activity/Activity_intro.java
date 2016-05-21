package kr.popcorn.sharoom.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.kakao.auth.Session;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import kr.popcorn.sharoom.R;
import kr.popcorn.sharoom.helper.Helper_server;
import kr.popcorn.sharoom.helper.Test;

public class Activity_intro extends Activity {

    ImageView loading_img;
    AnimationDrawable mAnimationDrawable_1;

    public void open_UserView_Activity(String id, Context mContext){
        Test.getRoomData_Login(id, mContext);
    }
    public void open_UserView_Activity(String id, Context mContext, int num){
        Test.getRoomData_Login(id, mContext, num);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        getAppKeyHash();

        boolean isLogined = false;

        AsyncHttpClient client = Helper_server.getInstance();
        final PersistentCookieStore myCookieStore = new PersistentCookieStore(this);
        client.setCookieStore(myCookieStore);

        //자동 로그인 파트.
        //배치 변경 필요.
        if (Helper_server.login(myCookieStore)) {
            Log.i("abde", "what the!! ");
            String id = Helper_server.getCookieValue(myCookieStore,"id");
            open_UserView_Activity(id, getApplicationContext());
            isLogined = true;
        }else if( Session.getCurrentSession().isOpened() ) {
            String id = Helper_server.getCookieValue(myCookieStore,"id");
            open_UserView_Activity(id, getApplicationContext(),0);
            isLogined = true;
        }else
        { //페이스북 자동로그인 파트
            FacebookSdk.sdkInitialize(getApplicationContext());
            AccessToken accessToken = AccessToken.getCurrentAccessToken();
            if (accessToken == null) {
                Log.d("abde", ">>>" + "Signed Out");
            } else {
                Log.d("abde", ">>>" + "Signed In");
                String id = Helper_server.getCookieValue(myCookieStore,"id");
                open_UserView_Activity(id, getApplicationContext(),1);
                isLogined = true;
            }
        }

        if( !isLogined ) {
            // 주 쓰레드를 실행
            start_thread();
            init(); // 디자인초기화
        }else{
            Log.d("what","thefuck");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Logs 'app deactivate' App Event.
        //AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onResume() {

        super.onResume();
        // Logs 'install' and 'app activate' App Events.
        //AppEventsLogger.activateApp(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.activity_intro, menu);
        return true;
    }

    Handler DisplayHandler = new Handler();

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                moveTaskToBack(true);

                Intent setIntent = new Intent(Intent.ACTION_MAIN);
                setIntent.addCategory(Intent.CATEGORY_HOME);
                setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(setIntent);
            default:
                return super.onKeyDown(keyCode, event);
        }
    }

    private void init() {

    }

    private void start_thread() {

        Toast.makeText(this, "방풀에 오신 것을 환영합니다.", Toast.LENGTH_SHORT).show();

        DisplayHandler.postDelayed(new Runnable() {

            public void run() {

                Intent intent = new Intent(Activity_intro.this, Activity_mainIntro.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                finish();
            }
        }, 5000); // 시간지정

    }

    // ---------------- 사용자이벤트핸들러 --------------------

    public void buttonClicked(View v) {

        Toast.makeText(this, "onclick 사용자 버튼", Toast.LENGTH_SHORT).show();

        switch (v.getId()) {

		/*
		 * case R.id.reconnect:
		 *

		 *
		 * DisplayHandler.postDelayed(Decision, 0);
		 *
		 * break;
		 */

        }

    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        loading_img = (ImageView)findViewById(R.id.loading_img);
        loading_img.setBackgroundResource(R.drawable.roading_animation);
        mAnimationDrawable_1 = (AnimationDrawable)loading_img.getBackground();
        mAnimationDrawable_1.run();
    }


    //get App hash key
    private void getAppKeyHash() {
        String something="";
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                something = new String(Base64.encode(md.digest(), 0));
                //Log.d("Hash key", something);
            }
            Log.d("lol key : ", something);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("name not found", e.toString());
        }
    }

}