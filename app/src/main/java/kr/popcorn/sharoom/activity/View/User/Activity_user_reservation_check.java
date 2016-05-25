package kr.popcorn.sharoom.activity.View.User;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

import com.bumptech.glide.Glide;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.GregorianCalendar;

import cz.msebera.android.httpclient.Header;
import kr.popcorn.sharoom.R;
import kr.popcorn.sharoom.activity.Activity_profileView;
import kr.popcorn.sharoom.helper.Helper_room;
import kr.popcorn.sharoom.helper.Helper_roomData;
import kr.popcorn.sharoom.helper.Helper_server;

/**
 * Created by parknature on 16. 5. 6..
 */
public class Activity_user_reservation_check extends FragmentActivity {

    private ViewPager viewPager;
    private ViewGroup requestBtn;
    private RelativeLayout sureBtn;
    private ImageAdapter adapter;
    private TextView tvCount, startDate, endDate;
    private int mYear, mMonth, mDay;
    private TextView roomName;

    private String url = "http://i.imgur.com/DvpvklR.png";

    private int idx;

    private int position;
    private Paint p;
    private Spinner peopleNum;
    private Activity_profileView customDialog;

    private Button callbutton;
    private Button smsbutton;
    private Button cancel_button;

    private int roomnumber;
    private  int imgLength;

    private TextView tv_hostName;

    private int hostID;
    private String hostName;
    private String hostPhone;
    private String hostEmail;


    private Helper_roomData roomData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_check);

        //imageview(view pager)
        viewPager = (ViewPager)findViewById(R.id.pager);
        tvCount = (TextView) findViewById(R.id.tv_count);
        position = getIntent().getIntExtra("idx",1);

        callbutton = (Button) findViewById(R.id.callbutton);
        smsbutton = (Button) findViewById(R.id.smsbutton);
        cancel_button = (Button) findViewById(R.id.cancel_button);

        roomName = (TextView)findViewById(R.id.roomName);
        startDate = (TextView) findViewById(R.id.startDate);
        endDate = (TextView) findViewById(R.id.endDate);

        cancel_button.setVisibility(View.GONE);

        tv_hostName = (TextView)findViewById(R.id.hostname);

        roomnumber = getIntent().getExtras().getInt("roomNumber");  //룸 넘버
        idx = Helper_room.search_index(roomnumber);

        position=1; //현재 사진의 인덱스

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
        p = new Paint();
        p.setColor(Color.rgb(32, 197, 137));


        callbutton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
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
        Calendar cal = new GregorianCalendar();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);
        startDate.setText(String.format("%d/%d/%d", mYear, mMonth+1, mDay));
        endDate.setText(String.format("%d/%d/%d", mYear, mMonth+1, mDay));

  /*peopleNum = (Spinner)findViewById(R.id.peopleNum);
        List<String> list = new ArrayList<String>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");

        ArrayAdapter<String> mMyadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, list);
        mMyadapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        peopleNum.setAdapter(mMyadapter);
        */
        sureBtn = (RelativeLayout)findViewById(R.id.sure);

        sureBtn.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                switch (arg0.getId()) {
                    case R.id.sure:
                        finish();
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
                        customDialog = new Activity_profileView(Activity_user_reservation_check.this);
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
