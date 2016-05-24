package kr.popcorn.sharoom.activity.View.User;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import cz.msebera.android.httpclient.Header;
import kr.popcorn.sharoom.R;
import kr.popcorn.sharoom.activity.Activity_FinishReserv;
import kr.popcorn.sharoom.activity.Activity_profileView;
import kr.popcorn.sharoom.helper.Helper_room;
import kr.popcorn.sharoom.helper.Helper_roomData;
import kr.popcorn.sharoom.helper.Helper_server;
import kr.popcorn.sharoom.helper.Helper_userData;
import me.yokeyword.imagepicker.adapter.GlideFragmentAdapter;

/**
 * Created by parknature on 16. 5. 6..
 */
public class Activity_user_reservation extends FragmentActivity {
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private ViewGroup requestBtn;
    private RelativeLayout reservationBtn;

    private GlideFragmentAdapter listAdapter;

    private TextView tvCount, startDate, endDate;
    private  TextView roomName;
    private int mYear, mMonth, mDay;

    private String url = "http://i.imgur.com/DvpvklR.png";

    private int idx;

    private int position;
    private Paint p;
    private Spinner peopleNum;
    private Activity_profileView customDialog;
    public static Activity_user_reservation rActivity;
    public boolean reserv = false;

    private Button callbutton;
    private Button smsbutton;
    private String today;

    private Helper_roomData roomData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        rActivity = Activity_user_reservation.this;

        //imageview(view pager)
        viewPager = (ViewPager)findViewById(R.id.pager);
        tvCount = (TextView) findViewById(R.id.tv_count);

        startDate = (TextView) findViewById(R.id.startDate);
        endDate = (TextView) findViewById(R.id.endDate);
        reservationBtn = (RelativeLayout)findViewById(R.id.reservationBtn);


        callbutton = (Button) findViewById(R.id.callbutton);
        smsbutton = (Button) findViewById(R.id.smsbutton);

        roomName = (TextView)findViewById(R.id.roomName);

        position = 1; //현재사진의 인덱스



        idx = getIntent().getExtras().getInt("index");  //룸 넘버
        roomData = Helper_room.getInstance().list.get(idx);

        if ( roomData.image.size() > 1) {
            tvCount.setText(position + " /" + roomData.image.size());
        } else {
            tvCount.setText("");
        }

        roomName.setText(roomData.getTitle());
        startDate.setText(roomData.getsDate());
        endDate.setText(roomData.geteDate());



        listAdapter = new GlideFragmentAdapter( getSupportFragmentManager(), roomData.image);

        viewPager.setAdapter(listAdapter);
        viewPager.setCurrentItem(0);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tvCount.setText(position + 1 + " /" + roomData.image.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        p = new Paint();
        p.setColor(Color.rgb(32, 197, 137));

        //SpannableString content = new SpannableString("2016/2/14");
        //content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        //startDate.setHint("년/월/일");


//      startDate.setText("2016/2/14");



        Calendar cal = new GregorianCalendar();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);
        startDate.setText(String.format("%d/%d/%d", mYear, mMonth+1, mDay));
        endDate.setText(String.format("%d/%d/%d", mYear, mMonth + 1, mDay));

        //달력 입력을 받기 위한 다이얼로그
        startDate.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId()){
                    case R.id.startDate:
                        new DatePickerDialog(Activity_user_reservation.this, mDateSetListener1, mYear, mMonth, mDay).show();
                        break;

                }
            }
        });

        endDate.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId()){
                    case R.id.endDate:
                        new DatePickerDialog(Activity_user_reservation.this, mDateSetListener2, mYear, mMonth, mDay).show();
                        break;

                }
            }
        });

        callbutton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId()){
                    case R.id.callbutton:
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:010-1111-2222"));
                        startActivity(intent);
                        break;

                }
            }
        });

        smsbutton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId()){
                    case R.id.smsbutton:
                        Uri uri = Uri.parse("smsto:01064207202");
                        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                        it.putExtra("sms_body", "The SMS text");
                        startActivity(it);
                        break;
                }
            }
        });

        reservationBtn.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                switch (arg0.getId()) {
                    case R.id.reservationBtn:
                        String sDate = startDate.getText().toString();
                        String eDate = endDate.getText().toString();

                        if(sDate == null) {
                            startDate.setText(today);
                            Toast.makeText(Activity_user_reservation.this, "시작 날짜를 입력해주세요.", Toast.LENGTH_LONG).show();
                        }
                        else if(eDate == null)
                        {
                            endDate.setText(today);
                            Toast.makeText(Activity_user_reservation.this, "종료 날짜를 입력해주세요.", Toast.LENGTH_LONG).show();
                        }
                        else if(sDate.compareTo(eDate)>0){
                            Toast.makeText(Activity_user_reservation.this, "입력 날짜를 확인해주세요.", Toast.LENGTH_LONG).show();
                            //startDate.setText(today);
                            //endDate.setText(today);
                        }
                        else{
                            AlertDialog.Builder aDialog = new AlertDialog.Builder(Activity_user_reservation.this);
                            aDialog.setTitle("예약 체크 하기"); //타이틀바 제목
                            aDialog.setMessage("서로 연락이 닿았고 예약 하기로 하셨습니까?");

                            aDialog.setPositiveButton("확인",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            RequestParams params = new RequestParams();
                                            params.put("roomNumber", roomData.roomNumber);
                                            params.put("userID", Helper_userData.getInstance().getUserID());
                                            System.out.println("position" + roomData.roomNumber);
                                            System.out.println("position" + roomData.isClosed);
                                            System.out.println("position" + Helper_userData.getInstance().getUserID());

                                            Helper_server.post("data/reserv_room.php", params, new JsonHttpResponseHandler() {
                                                @Override
                                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                                                    try{
                                                        String ok = response.get("ok").toString();
                                                        if(ok.equals("true")) reserv =  true;
                                                        else reserv = false;
                                                    } catch(JSONException e){
                                                        e.printStackTrace();
                                                    }
                                                    if(reserv == true) {

                                                        Helper_room.refreshRoomData("refresh",Activity_user_reservation.this);
                                                        Intent finishReservIntent = new Intent(Activity_user_reservation.this, Activity_FinishReserv.class);
                                                        startActivity(finishReservIntent);
                                                    }
                                                    else{
                                                        //이미 예약되었습니다.
                                                    }
                                                }

                                                @Override
                                                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                                    super.onFailure(statusCode, headers, responseString, throwable);
                                                    Log.d("Failed: ", ""+statusCode);
                                                    Log.d("Error : ", "" + throwable);
                                                }
                                            });
                                        }
                                    }).setNegativeButton("취소",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // 'No'
                                            return;
                                        }
                                    });
                            aDialog.show();

                        }

                        break;

                }

            }

        });

        requestBtn = (ViewGroup) findViewById(R.id.requestInfo);
        requestBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId())
                {
                    case R.id.requestInfo:
                        //Toast.makeText(Activity_Reservation.this, "문의요청버튼 누름.", Toast.LENGTH_LONG).show();
                        customDialog = new Activity_profileView(Activity_user_reservation.this);
                        customDialog.setCanceledOnTouchOutside(true);
                        customDialog.show();

                        break;
                }
            }
        });

    }

    DatePickerDialog.OnDateSetListener mDateSetListener1 =
            new DatePickerDialog.OnDateSetListener(){

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;

                    startDate.setText(String.format("%d/%d/%d", mYear, mMonth+1, mDay));
                }
            };

    DatePickerDialog.OnDateSetListener mDateSetListener2 =
            new DatePickerDialog.OnDateSetListener(){

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;

                    endDate.setText(String.format("%d/%d/%d", mYear, mMonth+1, mDay));
                }
            };

    public class ViewPagerAdapter extends PagerAdapter {
        // Declare Variables
        Context context;
        ArrayList<String> list;
        LayoutInflater inflater;

        public ViewPagerAdapter(Context context, ArrayList<String> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((RelativeLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            // Declare Variables
            ImageView imgflag;

            ImageView imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            Glide.with(context).load(list.get(position)).into(imageView);
            Log.i("baba",list.get(position));

            // Add viewpager_item.xml to ViewPager
            ((ViewPager) container).addView(imageView);

            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // Remove viewpager_item.xml from ViewPager
            ((ViewPager) container).removeView((RelativeLayout) object);

        }
    }

    public void test(){

        String userID = "find user id";
        RequestParams params = new RequestParams();
        params.put("userID", userID);

        Helper_server.post("data/getHostProfile.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try{
                    String name = response.get("name").toString();
                    String phoneNumber = response.get("phoneNumber").toString();
                    String email = response.get("email").toString();

                } catch(JSONException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("Failed: ", ""+statusCode);
                Log.d("Error : ", "" + throwable);
            }
        });




    }


}
