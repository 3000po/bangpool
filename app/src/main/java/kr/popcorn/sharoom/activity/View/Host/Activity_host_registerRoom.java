package kr.popcorn.sharoom.activity.View.Host;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import kr.popcorn.sharoom.R;
import kr.popcorn.sharoom.helper.Helper_server;
import kr.popcorn.sharoom.helper.Helper_userData;
import me.yokeyword.imagepicker.ImagePicker;
import me.yokeyword.imagepicker.callback.CallbackForCamera;
import me.yokeyword.imagepicker.callback.CallbackForImagePicker;

/**
 * Created by user on 16. 3. 12.
 */

//방을 등록하기 위한 액티비티
public class Activity_host_registerRoom extends Activity  implements View.OnClickListener{
    public final int MAX_SIZE=7;
    public final int PICK_THE_ALBUM=1;

    private ArrayList<String> list;
    private ImagePicker mImagePicker;

    private Dialog dialog;

    private ImageButton picButton;
    private ImageButton dialogCam;
    private ImageButton dialogGal;

    private EditText et_title;
    private EditText et_address;
    private EditText et_price;
    private EditText et_roomKind;
    private EditText et_roomInfo;

    public TextView tv_register;
    private int mYear, mMonth, mDay;
    private TextView startDate, endDate;
    private String today;
    private String start, end;

    private int flag1=0, flag2=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editroom);


        //사진 클릭시 카메라와 갤러리에서 고를수있게 지원해주는 다이얼로그
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_editroom_dialog);

        picButton = (ImageButton) findViewById(R.id.picture);

        //다이얼로그의 카메라와 갤러리 버튼
        dialogCam = (ImageButton) dialog.findViewById(R.id.camera);
        dialogGal = (ImageButton) dialog.findViewById(R.id.gallery);

        picButton.setOnClickListener(this);
        dialogCam.setOnClickListener(this);
        dialogGal.setOnClickListener(this);

        mImagePicker = new ImagePicker(this);

        loadData();

        et_title = (EditText)findViewById(R.id.et_title);
        et_address = (EditText)findViewById(R.id.et_address);
        et_price = (EditText)findViewById(R.id.et_price);
        et_roomKind = (EditText)findViewById(R.id.et_roomKind);
        et_roomInfo = (EditText) findViewById(R.id.et_roominfo);

        et_title.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                              @Override
                                              public void onFocusChange(View v, boolean hasFocus) {
                                                  if (hasFocus) {
                                                      et_title.setHint("");
                                                      InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                      imm.showSoftInput(et_title, InputMethodManager.SHOW_IMPLICIT);
                                                  } else
                                                      et_title.setHint("주말 빌려드립니다! 연락주세요!");
                                              }
                                          }
        );

        et_address.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    et_address.setHint("");
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(et_address, InputMethodManager.SHOW_IMPLICIT);

                } else
                    et_address.setHint("서울시 성북구 정릉동 11-12");
            }
        });

        et_price.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    et_price.setHint("");
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(et_price, InputMethodManager.SHOW_IMPLICIT);

                } else
                    et_price.setHint("300,000");
            }
        });
        et_roomKind.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    et_roomKind.setHint("");
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(et_roomKind, InputMethodManager.SHOW_IMPLICIT);

                } else
                    et_roomKind.setHint("원룸, 자취방, 하숙집...etc");
            }
        });
        et_roomInfo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    et_roomInfo.setHint("");
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(et_roomInfo, InputMethodManager.SHOW_IMPLICIT);

                } else
                    et_roomInfo.setHint("침대 1인용, 컴퓨터 책상, 옷걸이...etc");
            }
        });

        et_title.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(et_title.getWindowToken(), 0);    //hide keyboard
                    return true;
                }
                return false;
            }
        });
        et_address.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(et_address.getWindowToken(), 0);    //hide keyboard
                    return true;
                }
                return false;
            }
        });
        et_price.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(et_price.getWindowToken(), 0);    //hide keyboard
                    return true;
                }
                return false;
            }
        });

        et_roomKind.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(et_roomKind.getWindowToken(), 0);    //hide keyboard
                    return true;
                }
                return false;
            }
        });

        et_roomInfo.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(et_roomInfo.getWindowToken(), 0);    //hide keyboard
                    return true;
                }
                return false;
            }
        });

        startDate = (TextView) findViewById(R.id.startDate);
        endDate = (TextView) findViewById(R.id.endDate);

        Calendar cal = new GregorianCalendar();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);
        //날짜 초기값 현재날짜로 세팅
        today = String.format("%d/%d/%d", mYear, mMonth+1, mDay);
        startDate.setText(String.format("%d/%d/%d", mYear, mMonth+1, mDay));
        endDate.setText(String.format("%d/%d/%d", mYear, mMonth + 1, mDay));

        //달력 입력을 받기 위한 다이얼로그
        startDate.setOnClickListener(new TextView.OnClickListener() { //시작날짜
            @Override
            public void onClick(View v) {
                switch(v.getId()){
                    case R.id.startDate:
                        new DatePickerDialog(Activity_host_registerRoom.this, mDateSetListener1, mYear, mMonth, mDay).show();
                        flag1=1;
                        break;

                }
            }
        });

        endDate.setOnClickListener(new TextView.OnClickListener() { //끝 날짜
            @Override
            public void onClick(View v) {
                switch(v.getId()){
                    case R.id.endDate:
                        new DatePickerDialog(Activity_host_registerRoom.this, mDateSetListener2, mYear, mMonth, mDay).show();
                        flag2=1;
                        break;

                }
            }
        });

        /*
        //시작날짜 끝날짜를 모두 입력 받은 후에 끝날짜가 시작날짜보다 빠르면 알림
        if(flag1==1 && flag2==1 &&(startDate.toString().compareTo(endDate.toString())<0)){
            AlertDialog.Builder alert = new AlertDialog.Builder(Activity_host_registerRoom.this);
            alert.setTitle("달력");
            alert.setMessage("입력 날짜를 확인해주세요.");
            alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    startDate.setText(String.format("%d/%d/%d", mYear, mMonth+1, mDay));
                    endDate.setText(String.format("%d/%d/%d", mYear, mMonth+1, mDay));
                }
            });
            alert.show();
        }*/

        tv_register = (TextView) findViewById(R.id.bottomtext);
        tv_register.setOnClickListener(new TextView.OnClickListener(){
            public void onClick(View v) {
                Log.d("buttonClick", "okokokokok");
                for(int i=0; i<list.size(); i++){
                    Log.d("buttonList", list.get(i));
                }
                final String title = et_title.getText().toString();
                final String address = et_address.getText().toString();
                final String price = et_price.getText().toString();
                final String roomKind = et_roomKind.getText().toString();
                final String roomInfo = et_roomInfo.getText().toString();
                /*final String year1 = et_year1.getText().toString();
                final String month1 = et_month1.getText().toString();
                final String day1 = et_day1.getText().toString();
                final String year2 = et_year2.getText().toString();
                final String month2 = et_month2.getText().toString();
                final String day2 = et_day2.getText().toString();
                final String comment = et_commnet.getText().toString();
                */
                String sDate = startDate.toString();
                String eDate = endDate.toString();

                System.out.println(sDate);

                System.out.println(eDate);

                //시작날짜 끝날짜를 모두 입력 받은 후에 끝날짜가 시작날짜보다 빠르면 알림
                if(sDate!=null && eDate != null && sDate.compareTo(eDate)>0){
                    Toast.makeText(Activity_host_registerRoom.this, "입력 날짜를 확인해주세요.", Toast.LENGTH_LONG).show();
                    //startDate.setText(today);
                    //endDate.setText(today);
                }
                postImage(list, title, address, price, roomKind, roomInfo, sDate, eDate);
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
                    start = new String(String.valueOf(mYear)+"/"+String.valueOf(mMonth+1)+"/"+String.valueOf(mDay));
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
                    end = new String(String.valueOf(mYear)+"/"+String.valueOf(mMonth+1)+"/"+String.valueOf(mDay));

                }
            };
    @Override
    public void onClick(View v) {
        switch (v.getId()){

            //방 사진을 눌렀을때 처리
            case R.id.picture:
                //방에 사진이 하나도 없을경우 다이얼로그를 띄워서 카메라와 갤러리를 고를수있게해줌
                if(list.size() == 0 ) dialog.show();
                else{   //사진이 하나이상 있을경우 사진편집 액티비티를 띄워서 그곳에서 방사진을 편집하게끔한다.
                    openActivity();
                }
                break;
            case R.id.camera:
                dialog.dismiss();
                mImagePicker.openCamera(new CallbackForCamera() {
                    @Override
                    public void onError(Exception error) {

                    }
                    @Override
                    public void onComplete(String imagePath) {
                        list.add(imagePath);
                        openActivity();
                    }

                    @Override
                    public void onCancel(String imagePath) {
                        Toast.makeText(getApplicationContext(), "실패..", Toast.LENGTH_SHORT).show();

                        File tempFile = new File(imagePath);
                        if (tempFile.exists()) {
                            tempFile.delete();
                        }
                    }
                });
                break;
            case R.id.gallery:
                dialog.dismiss();
                mImagePicker.openImagePiker(true, new CallbackForImagePicker() {
                    @Override
                    public void onError(Exception error) {
                        Log.i("aab", "error");
                    }
                    @Override
                    public void onComplete(List<String> imagePath) {
                        list.addAll(imagePath);
                        for(int i=MAX_SIZE; i<list.size(); i++){
                            list.remove(i);
                        }
                        openActivity();
                    }
                });
                break;
        }
    }
    public void openActivity(){
        Intent it = new Intent(this, Activity_host_registerRoom_roomPic.class);
        it.putExtra("list", list);
        startActivityForResult(it, PICK_THE_ALBUM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode){
            case PICK_THE_ALBUM:
                list = data.getStringArrayListExtra("list");
                saveData();
                break;
        }

        mImagePicker.delegateActivityResult(requestCode, resultCode, data);
    }



    @Override
    public void onResume(){
        super.onResume();
        loadData();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveData();
    }

    private void saveData(){
        // 특정번호의 공유저장소를 편집가능 상태로 불러온다.
        SharedPreferences.Editor edt = getSharedPreferences("room",0).edit();

        // 저장
        edt.putInt("picCount", list.size());
        for(int i=0; i<list.size(); i++){
            edt.putString("pic" + i, list.get(i));
        }

        // 수행
        edt.commit();
    }

    private void loadData(){
        // 저장소 객체를 생성
        SharedPreferences prefs = getSharedPreferences("room", 0);

        // 로드
        int size = prefs.getInt("picCount",0);
        ArrayList<String> arrayList = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            arrayList.add(prefs.getString("pic" + i, null));
        }

        list = arrayList;

        if( list.size() > 0 ) {
            picButton.setImageURI( Uri.fromFile( new File(list.get(0))));
        }else{
            picButton.setImageResource(R.drawable.roompicture);
        }
    }


    public static void postImage(ArrayList<String> list, String title, String address, String price, String roomKind, String roomInfo, String sDate, String eDate){

        //아이디 가져옴.
        String id = Helper_userData.getInstance().getId();

        RequestParams params = new RequestParams();
        params.put("id",id);
        params.put("size", list.size()); //이미지 크기.

        for (int i = 0; i < list.size(); i++) {
            System.out.println("sibalbalblabl_imageLink : " + list.get(i));
            String imagePath =list.get(i);
            File f = new File(imagePath);
            System.out.println("sibalbalImagePath : " + imagePath);

            try{
                params.put("file" + i, f);
                //params.put("path", "aaa");
            }
            catch(FileNotFoundException e){
                System.out.println("sibalbal fileNotFound");
            }
        }
        params.put("title", title);
        params.put("address", address);
        params.put("price", price);
        params.put("roomKind", roomKind);
        params.put("roomInfo", roomInfo);
        params.put("sDate", sDate);
        params.put("eDate", eDate);

        Helper_server.post("data/insert_Roomdata.php", params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                System.out.println("statusCode "+statusCode);//statusCode 200
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                System.out.println("sibalbalblabl_onFailure");
            }
        });
    }

    public static Bitmap decodeUri(Context c, String path, final int requiredSize)
            throws FileNotFoundException {

        Uri uri = Uri.fromFile(new File(path));
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o);

        int width_tmp = o.outWidth
                , height_tmp = o.outHeight;
        int scale = 1;

        while(true) {
            if(width_tmp / 2 < requiredSize || height_tmp / 2 < requiredSize)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o2);
    }
}
