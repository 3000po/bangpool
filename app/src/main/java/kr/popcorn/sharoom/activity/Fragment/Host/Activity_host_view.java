package kr.popcorn.sharoom.activity.Fragment.Host;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;

import java.util.ArrayList;

import kr.popcorn.sharoom.R;
import kr.popcorn.sharoom.helper.Helper_room;
import kr.popcorn.sharoom.helper.Helper_roomData;
import kr.popcorn.sharoom.helper.Helper_server;
import kr.popcorn.sharoom.helper.Helper_userData;

public class Activity_host_view extends FragmentActivity {

    H_TestFragmentAdapter mAdapter;
    ViewPager mPager;
    H_PageIndicator mIndicator;
    TextView mToptext;

    private ImageView mapMenu;

    @Override
    protected void onResume() {
        super.onResume();

        int n = mPager.getCurrentItem();
        mToptext.setText("방 리스트");
        mAdapter = new H_TestFragmentAdapter(getSupportFragmentManager());
        mPager.setAdapter(mAdapter);
        mPager.setCurrentItem(n);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_host_view);

        AsyncHttpClient client = Helper_server.getInstance();

        mAdapter = new H_TestFragmentAdapter(getSupportFragmentManager());

        mToptext = (TextView) findViewById(R.id.toptext);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        mPager.setOffscreenPageLimit(3);

        mIndicator = (H_IconPageIndicator) findViewById(R.id.h_indicator);

        mIndicator.setViewPager(mPager);

        mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (mPager.getCurrentItem()){
                    case 0 : mToptext.setText("방 등록 및 관리");
                        break;
                    case 1 : mToptext.setText("예약 관리");
//                        ArrayList<Helper_roomData> roomData;
//
//                        roomData = Helper_room.getInstance().getHostRoomData(""+Helper_userData.getInstance().getUserID());
//                        System.out.println("rooData  : 완료" + Helper_userData.getInstance().getUserID());
//                        int sum=0;
//                        boolean ok = false;
//                        while(!ok){
//                            ok = Helper_room.getInstance().getHostListOK();
//                            System.out.println("kkkkk  :" + sum);
//                            sum++;
//                        }
//                        System.out.println("roomData : " + roomData.get(0).roomNumber);
                        break;
                    case 2 : mToptext.setText("내 정보");
                        break;
                }
            }
        });

//        ImageView email_btn;
//        email_btn = (ImageView) v.findViewById(R.id.confirm1);
//        email_btn.setOnClickListener(new View.OnClickListener(){
//            public void onClick(View v) {
//                if (v.getId() == R.id.confirm1) {
////                    AlertDialog.Builder alert = new AlertDialog.Builder(mContext.get);
////
////                    alert.setTitle("Title");
////                    alert.setMessage("Message");
////
////                    // Set an EditText view to get user input
////                    final EditText input = new EditText(mContext);
////                    alert.setView(input);
////
////                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
////                        public void onClick(DialogInterface dialog, int whichButton) {
////                            String value = input.getText().toString();
////                            value.toString();
////                            // Do something with value!
////                        }
////                    });
////
////
////                    alert.setNegativeButton("Cancel",
////                            new DialogInterface.OnClickListener() {
////                                public void onClick(DialogInterface dialog, int whichButton) {
////                                    // Canceled.
////                                }
////                            });
////
////                    alert.show();
////
////
////
//
//                    Log.i("kisang", "confirm2");
//                    System.out.println("test2");
//                }
//            }});
//        System.out.println("noKisang2");

        init();
    }

    private void init() {
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch(keyCode) {
            case KeyEvent.KEYCODE_BACK:
                new AlertDialog.Builder(this)
                        .setTitle("종료")
                        .setMessage("종료 하시겠어요?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                finish();
                            }
                        })
                        .setNegativeButton("아니오", null).show();
                return false;
            default:
                return false;
        }
    }
}
