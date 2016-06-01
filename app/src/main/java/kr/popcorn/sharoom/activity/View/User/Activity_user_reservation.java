package kr.popcorn.sharoom.activity.View.User;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
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
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

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

    private TextView tv_hostName;

    private String today;
    private int hostID;
    private String hostName;
    private String hostPhone;
    private String hostEmail;

    private  int imgLength;
    private Helper_roomData roomData;
    private ImageAdapter adapter;


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
        tv_hostName = (TextView)findViewById(R.id.hostname);

        position = 1; //현재사진의 인덱스

        roomData = Helper_room.getInstance().list.get(idx);
        imgLength = 0;

        for(int i = 0; i< 8; i ++){
            if(roomData.getImage().get(i).equals("http://14.63.227.200/0"))
                break;
            imgLength++;
        }

        if (imgLength > 1) {
            //if(imgList.size() > 1)
            //tvCount.setText(position + "/" + imgList.size());
            tvCount.setText(position + " /" + imgLength);
        } else {
            tvCount.setText("");
        }


        idx = getIntent().getExtras().getInt("index");  //룸 넘버
        roomData = Helper_room.getInstance().list.get(idx);
        imgLength = 0;

        for(int i = 0; i< 8; i ++){
            if(roomData.getImage().get(i).equals("http://14.63.227.200/0"))
                break;
            imgLength++;
        }

        if (imgLength > 1) {
            //if(imgList.size() > 1)
            //tvCount.setText(position + "/" + imgList.size());
            tvCount.setText(position + " /" + imgLength);
        } else {
            tvCount.setText("");
        }

        hostID = roomData.getUserID();
        roomName.setText(roomData.getTitle());
        startDate.setText(roomData.getsDate());
        endDate.setText(roomData.geteDate());

        search_user_info(hostID);


        listAdapter = new GlideFragmentAdapter( getSupportFragmentManager(), roomData.image);

        viewPager.setAdapter(listAdapter);
        viewPager.setCurrentItem(0);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tvCount.setText(position + 1 + " /" +  imgLength);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        adapter = new ImageAdapter(this);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);

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
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+hostPhone));
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
                        Uri uri = Uri.parse("smsto:"+hostPhone);
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
                        final String sDate = startDate.getText().toString();
                        final String eDate = endDate.getText().toString();

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
                            aDialog.setMessage("서로 연락이 닿았고 예약 하기로 하셨습니까?\n\n ※무책임하게 예약을 하실 경우 방 리스트에서 사라지게 되어 주인의 영업에 손실에 대한 책임을 질 수도 있습니다." +
                                    "\n 이 방이 맞는지 확인해 주세요!");


                            aDialog.setPositiveButton("확인",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Log.e("reserv !!","요상하구만");

                                            RequestParams params = new RequestParams();
                                            params.put("roomNumber", roomData.roomNumber);
                                            params.put("userID", Helper_userData.getInstance().getUserID());
                                            params.put("rsDate", sDate);
                                            params.put("reDate", eDate);

                                            //TODO

                                            params.put("rsdate", roomData.rsDate);
                                            params.put("redate", roomData.reDate);

                                            //TODO
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
                                                    Log.e("reserv !!",""+reserv);
                                                    if(reserv == true) {

                                                        Helper_room.refreshRoomData("refresh", getApplicationContext());
                                                        Intent finishReservIntent = new Intent(Activity_user_reservation.this, Activity_FinishReserv.class);
                                                        finishReservIntent.putExtra("roomnumber", roomData.roomNumber);
                                                        startActivity(finishReservIntent);

                                                        Activity_user_reservation.this.finish();
                                                    }
                                                    else{
                                                        //이미 예약되었습니다.
                                                        Toast.makeText(Activity_user_reservation.this, "죄송합니다. 이미 예약되었습니다.", Toast.LENGTH_SHORT).show();
                                                    }
                                                }

                                                @Override
                                                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                                    super.onFailure(statusCode, headers, responseString, throwable);
                                                    Log.d("reservation_Failed: ", ""+statusCode);
                                                    Log.d("reservation_Error : ", "" + throwable);
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
                        customDialog.setProfile(hostName,hostPhone,hostEmail);
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

    public class ImageAdapter extends PagerAdapter {
        Context context;

        ImageAdapter(Context context){
            this.context=context;
        }
        @Override
        public int getCount() {
            return imgLength;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((ImageView) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            ImageView imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            Glide.with(context).load(roomData.getImage().get(position)).into(imageView);

            ((ViewPager) container).addView(imageView, 0);

            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((ImageView) object);
        }
    }

    public void search_user_info(int userID){

        RequestParams params = new RequestParams();
        params.put("userID", userID);

        Helper_server.post("data/getHostProfile.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try{
                    hostName = response.get("name").toString();
                    hostPhone = response.get("phoneNumber").toString();
                    hostEmail = response.get("email").toString();
                    Log.d("hostName : ",hostName);

                    tv_hostName.setText(hostName);

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