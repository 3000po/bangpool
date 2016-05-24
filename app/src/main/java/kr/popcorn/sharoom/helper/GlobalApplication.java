package kr.popcorn.sharoom.helper;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import com.kakao.auth.KakaoSDK;

/**
 * Created by user on 16. 5. 13.
 */


public class GlobalApplication extends Application {
    private static GlobalApplication mInstance;
    private static volatile Activity currentActivity = null;

    /*Typekit.getInstance()
            .addNormal(Typekit.createFromAsset(this, "fonts/NanumBarunGothic-Regular.otf"))
            .addBold(Typekit.createFromAsset(this, "fonts/NanumBarunGothic-Bold.otf"));*/
    public static Activity getCurrentActivity() {
        Log.d("TAG", "++ currentActivity : " + (currentActivity != null ? currentActivity.getClass().getSimpleName() : ""));
        return currentActivity;
    }

    public static void setCurrentActivity(Activity currentActivity) {
        GlobalApplication.currentActivity = currentActivity;
    }

    /**
     * singleton
     * @return singleton
     */
    public static GlobalApplication getGlobalApplicationContext() {
        if(mInstance == null)
            throw new IllegalStateException("this application does not inherit GlobalApplication");
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        KakaoSDK.init(new KakaoSDKAdapter());
        server_info = 0;
    }

    private String mGlobalString;
    private int server_info;

    public void setServer_info(int n){
        server_info = n;
    }

    public int getServer_info(){
        return  server_info;
    }

    public String getGlobalString()
    {
        return mGlobalString;
    }

    public void setGlobalString(String globalString)
    {
        this.mGlobalString = globalString;
    }


}
