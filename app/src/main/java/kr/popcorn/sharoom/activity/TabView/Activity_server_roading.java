package kr.popcorn.sharoom.activity.TabView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import kr.popcorn.sharoom.R;
import kr.popcorn.sharoom.activity.Fragment.User.Activity_user_view;
import kr.popcorn.sharoom.helper.GlobalApplication;
import kr.popcorn.sharoom.helper.Helper_room;

import static android.support.v4.app.ActivityCompat.startActivity;

public class Activity_server_roading extends Activity {


    ProgressBar progressBar;
    TextView main_text;
    public static Activity_server_roading activity_server_roading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_roading);

        activity_server_roading = Activity_server_roading.this;

        String main = getIntent().getExtras().getString("main");

        progressBar = (ProgressBar)findViewById(R.id.progress_bar);
        main_text = (TextView)findViewById(R.id.main);

        progressBar.incrementProgressBy(1);
        main_text.setText(main);

        GlobalApplication serverInfo = (GlobalApplication)getApplication();

        if (serverInfo.getServer_info() == 1) {
            serverInfo.setServer_info(0);
            Activity_server_roading.this.finish(); // 로딩페이지 Activity Stack에서 제거
        }

        Handler handler = new Handler();
        handler.postDelayed(new splashhandler(), 30000);
    }

    private class splashhandler implements Runnable{
        public void run() {

            Activity_server_roading.this.finish(); // 로딩페이지 Activity Stack에서 제거

        }
    }
}


