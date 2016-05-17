package kr.popcorn.sharoom.activity.View.Host;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import me.yokeyword.imagepicker.ImagePicker;
import me.yokeyword.imagepicker.callback.CallbackForCamera;
import me.yokeyword.imagepicker.callback.CallbackForImagePicker;

/**
 * Created by user on 16. 3. 12.
 */


//방을 수정하기 위한 액티비티
public class Activity_host_editRoom extends Activity  implements View.OnClickListener{
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
    private EditText et_facilities;

    public TextView tv_register;
    private int mYear, mMonth, mDay;
    private TextView startDate, endDate;
    private String start, end;
    private String today;
    private TextView toptext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editroom);


        //사진 클릭시 카메라와 갤러리에서 고를수있게 지원해주는 다이얼로그
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_editroom_dialog);

        picButton = (ImageButton) findViewById(R.id.picture);

        toptext = (TextView) findViewById(R.id.toptext);

        //다이얼로그의 카메라와 갤러리 버튼
        dialogCam = (ImageButton) dialog.findViewById(R.id.camera);
        dialogGal = (ImageButton) dialog.findViewById(R.id.gallery);


        picButton.setOnClickListener(this);
        dialogCam.setOnClickListener(this);
        dialogGal.setOnClickListener(this);

        toptext.setText("방 수정하기");


        mImagePicker = new ImagePicker(this);
        loadData();

        et_title = (EditText)findViewById(R.id.et_title);
        et_address = (EditText)findViewById(R.id.et_address);
        et_price = (EditText)findViewById(R.id.et_price);
        et_roomKind = (EditText)findViewById(R.id.et_roomKind);
        et_roomInfo = (EditText) findViewById(R.id.et_roominfo);
        et_facilities = (EditText)findViewById(R.id.et_facilities);

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


        et_facilities.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    et_facilities.setHint("");
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(et_facilities, InputMethodManager.SHOW_IMPLICIT);

                } else
                    et_address.setHint("침대 1인용, 컴퓨터 책상, 옷걸이...etc");
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
                    et_roomInfo.setHint("국민대학교 정문에서 걸어서 10분정도 걸리는 거리에 위치한 원룸입니다. 2달 정도 방을 비워야 할일이 생겨서 같은 학교 학생분께 저렴한 가격에 대여해드리고 싶습니다. 연락주세요.");
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
        et_facilities.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(et_facilities.getWindowToken(), 0);    //hide keyboard
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
                        new DatePickerDialog(Activity_host_editRoom.this, mDateSetListener1, mYear, mMonth, mDay).show();
                        break;

                }
            }
        });

        endDate.setOnClickListener(new TextView.OnClickListener() { //끝 날짜
            @Override
            public void onClick(View v) {
                switch(v.getId()){
                    case R.id.endDate:
                        new DatePickerDialog(Activity_host_editRoom.this, mDateSetListener2, mYear, mMonth, mDay).show();
                        break;

                }
            }
        });

        tv_register = (TextView) findViewById(R.id.bottomtext);
        tv_register.setOnClickListener(new TextView.OnClickListener(){
            public void onClick(View v) {
                Log.d("buttonClick", "okokokokok");
                for(int i=0; i<list.size(); i++){
                    Log.d("buttonList", list.get(i));
                }
                postImage(list);
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


    public static void postImage(ArrayList<String> list){
        RequestParams params = new RequestParams();
        params.put("size", list.size());
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

        Helper_server.post("image/save1.php", params, new AsyncHttpResponseHandler() {

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
}

