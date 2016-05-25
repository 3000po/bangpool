package kr.popcorn.sharoom.activity.View.Host;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.loopj.android.http.AsyncHttpResponseHandler;
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
import kr.popcorn.sharoom.activity.Fragment.Host.Activity_host_view;
import kr.popcorn.sharoom.helper.Helper_room;
import kr.popcorn.sharoom.helper.Helper_roomData;
import kr.popcorn.sharoom.helper.Helper_server;
import kr.popcorn.sharoom.helper.Helper_userData;

/**
 * Created by parknature on 16. 5. 6..
 */
public class Activity_host_reservation_check extends FragmentActivity {

    private ViewPager viewPager;
    private ViewGroup requestBtn;
    private RelativeLayout sureBtn;
    private Button callbutton;
    private Button smsbutton;
    private ImageAdapter adapter;
    private TextView tvCount, startDate, endDate;
    private int mYear, mMonth, mDay;

    private Button cancel_button;

    private int imgLength;
    private int roomnumber;
    private int idx;
    private int position;
    private Activity_profileView customDialog;

    private TextView tv_guestName;

    private String today;
    private int guestID;
    private String guestName;
    private String guestPhone;
    private String guestEmail;

    private Helper_roomData roomData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_check);

        //imageview(view pager)
        viewPager = (ViewPager)findViewById(R.id.pager);
        tvCount = (TextView) findViewById(R.id.tv_count);
        cancel_button = (Button) findViewById(R.id.cancel_button);
        callbutton = (Button) findViewById(R.id.callbutton);
        smsbutton = (Button) findViewById(R.id.smsbutton);

        tv_guestName = (TextView)findViewById(R.id.hostname);

        position = 1;

        roomnumber = getIntent().getExtras().getInt("roomNumber");  //룸 넘버
        idx = Helper_room.search_index(roomnumber);
        roomData = Helper_room.getInstance().list.get(idx);

        guestID = roomData.rUserID;
        search_user_info(guestID);



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

        adapter = new ImageAdapter(this);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                //tvCount.setText(position + 1 + "/" + imgList.size());
                tvCount.setText(position + 1 + " /" + imgLength);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        today = String.format("%d/%d/%d", mYear, mMonth+1, mDay);
        startDate = (TextView) findViewById(R.id.startDate);
        endDate = (TextView) findViewById(R.id.endDate);

        Calendar cal = new GregorianCalendar();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);
        startDate.setText(roomData.getsDate());
        endDate.setText(roomData.geteDate());

        //달력 입력을 받기 위한 다이얼로그
        startDate.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId()){
                    case R.id.startDate:
                        new DatePickerDialog(Activity_host_reservation_check.this, mDateSetListener1, mYear, mMonth, mDay).show();
                        break;

                }
            }
        });

        endDate.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId()){
                    case R.id.endDate:
                        new DatePickerDialog(Activity_host_reservation_check.this, mDateSetListener2, mYear, mMonth, mDay).show();
                        break;

                }
            }
        });

        sureBtn = (RelativeLayout)findViewById(R.id.sure);

        sureBtn.setOnClickListener(new RelativeLayout.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                switch (arg0.getId()) {
                    case R.id.sure:

                        String sDate = startDate.getText().toString();
                        String eDate = endDate.getText().toString();

                        System.out.println(sDate);

                        System.out.println(eDate);
                        // Log.i("jihyun1", end);)
                        if(sDate == null) {
                            startDate.setText(today);
                            Toast.makeText(Activity_host_reservation_check.this, "시작 날짜를 입력해주세요.", Toast.LENGTH_LONG).show();
                        }
                        else if(eDate == null)
                        {
                            endDate.setText(today);
                            Toast.makeText(Activity_host_reservation_check.this, "종료 날짜를 입력해주세요.", Toast.LENGTH_LONG).show();
                        }
                        else if(sDate.compareTo(eDate)>0){
                            Toast.makeText(Activity_host_reservation_check.this, "입력 날짜를 확인해주세요.", Toast.LENGTH_LONG).show();
                            //startDate.setText(today);
                            //endDate.setText(today);
                        }
                        else finish();
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
                        customDialog = new Activity_profileView(Activity_host_reservation_check.this);
                        customDialog.setProfile(guestName,guestPhone,guestEmail);
                        customDialog.setCanceledOnTouchOutside(true);
                        customDialog.show();

                        break;
                }
            }
        });

        cancel_button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                switch(v.getId()){
                    case R.id.cancel_button:
                        AlertDialog.Builder aDialog = new AlertDialog.Builder(Activity_host_reservation_check.this);
                        aDialog.setTitle("예약 취소 하기"); //타이틀바 제목
                        aDialog.setMessage("정말로 예약을 취소시키겠습니까?");

                        aDialog.setPositiveButton("확인",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        RequestParams params = new RequestParams();
                                        params.put("roomNumber", roomData.roomNumber);

                                        Helper_server.post("data/cancel_reserv.php", params, new AsyncHttpResponseHandler() {
                                            @Override
                                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                                Helper_room.refreshRoomData("refresh",getApplication(), getApplication());
                                                //Intent finishReservIntent = new Intent(Activity_host_reservation_check.this, Activity_host_view.class);
                                                //finishReservIntent.putExtra("roomnumber", roomData.roomNumber);
                                                //startActivity(finishReservIntent);
                                                finish();
                                            }

                                            @Override
                                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                                System.out.println("sibalbalblabl_onFailure");
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
                        break;
                }
            }
        });

        callbutton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId()){
                    case R.id.callbutton:
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+guestPhone));
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
                        Uri uri = Uri.parse("smsto:"+guestPhone);
                        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                        it.putExtra("sms_body", "The SMS text");
                        startActivity(it);
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
            //imageView.setImageResource(imgList[position]);

            Glide.with(context).load(roomData.getImage().get(position)).into(imageView);

            ((ViewPager) container).addView(imageView, 0);

            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((ImageView) object);
        }
    }

    public void search_user_info(int userID) {

        RequestParams params = new RequestParams();
        params.put("userID", userID);

        Helper_server.post("data/getHostProfile.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    guestName = response.get("name").toString();
                    guestPhone = response.get("phoneNumber").toString();
                    guestEmail = response.get("email").toString();
                    Log.d("guestName : ", guestName);

                    tv_guestName.setText(guestName);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("Failed: ", "" + statusCode);
                Log.d("Error : ", "" + throwable);
            }
        });
    }



}
