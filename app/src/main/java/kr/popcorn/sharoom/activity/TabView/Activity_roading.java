package kr.popcorn.sharoom.activity.TabView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import kr.popcorn.sharoom.R;
import kr.popcorn.sharoom.activity.Fragment.User.Activity_user_view;

public class Activity_roading extends Activity {

    TextView text;
    ImageView loading_img;
    AnimationDrawable mAnimationDrawable_1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chage_mode);

        text = (TextView)findViewById(R.id.maintext);
        loading_img = (ImageView)findViewById(R.id.loading_img);

        text.setText("로딩 중입니다.");

        Handler handler = new Handler();
        handler.postDelayed(new splashhandler(), 5000);
    }


    private class splashhandler implements Runnable{
        public void run() {
            Activity_roading.this.finish(); // 로딩페이지 Activity Stack에서 제거

        }
    }
}
