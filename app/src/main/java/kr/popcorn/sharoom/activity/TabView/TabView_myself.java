package kr.popcorn.sharoom.activity.TabView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;
import kr.popcorn.sharoom.R;
import kr.popcorn.sharoom.activity.Activity_login;
import kr.popcorn.sharoom.helper.Helper_server;
import kr.popcorn.sharoom.helper.Helper_userData;

/**
 * Created by Administrator on 2016-05-11.
 */
public class TabView_myself extends LinearLayout {

    Context mContext;

    ImageView myFace;
    Button logout_btn;
    Button chage_btn;
    Button edit_btn;

    EditText edit_phone;
    EditText edit_email;
    EditText edit_facebook;
    EditText edit_kakaotalk;

    TextView myname;

    TextView text_phone;
    TextView text_email;
    TextView text_facebook;
    TextView text_kakaotalk;


    int check_host_user = 0;

    public TabView_myself(Context context) {
        super(context);
        mContext = context;
        init();
    }

    private void init(){

  //      final Activity_user_view aActivity = (Activity_user_view) Activity_user_view.AActivty;

        Helper_server.userData = Helper_userData.getInstance(getContext().getApplicationContext());

        Bitmap face = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.myself_50x50);

        final View view = LayoutInflater.from(getContext()).inflate(R.layout.activity_myself,null);
        view.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

        myFace = (ImageView) view.findViewById(R.id.myface);

        logout_btn = (Button) view.findViewById(R.id.logout);
        chage_btn = (Button) view.findViewById(R.id.chage_btn);
        edit_btn = (Button) view.findViewById(R.id.editbutton);

        myname = (TextView) view.findViewById(R.id.myname);

        text_phone = (TextView) view.findViewById(R.id.text_phone);
        text_email = (TextView) view.findViewById(R.id.text_email);
        text_facebook = (TextView) view.findViewById(R.id.text_facebook);
        text_kakaotalk = (TextView) view.findViewById(R.id.text_kakao);

        myFace.setImageBitmap(getCircleBitmap(face));


        //Log.e("check :", "0=" + Helper_server.userData.getName());

        if(Helper_server.userData.getId() != null )
            myname.setText(Helper_server.userData.getId() + "(" + Helper_server.userData.getName() + ")" );
        else
            myname.setText("(" + Helper_server.userData.getName() + ")" );

        text_phone.setText(Helper_server.userData.getPhoneNumber());

        if( Helper_server.userData.getEmail() == null ) text_email.setText("없음");
        else                                            text_email.setText(Helper_server.userData.getEmail());

        if( Helper_server.userData.getFacebook() == null ) text_facebook.setText("없음");
        else                                               text_facebook.setText(Helper_server.userData.getFacebook());

        if( Helper_server.userData.getKakaotalk() == null ) text_kakaotalk.setText("없음");
        else                                                text_kakaotalk.setText(Helper_server.userData.getKakaotalk());

        String str = "" + getContext().getClass();

        if (str.contains("Activity_user_view")) chage_btn.setText("  호스트 모드로 변환  ");
        else chage_btn.setText("  사용자 모드로 변환  ");


        logout_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (v.getId() == R.id.logout) {
                    // Set an EditText view to get user input
                    //로그아웃파트.
                    logoutAlert();
                    //여기에 로그아웃 됬다는 말과 함께 로그인 화면으로 이동시켜 주어야 함.
                }
            }
        });

        chage_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (v.getId() == R.id.chage_btn) {
                    String str = "" + getContext().getClass();
                    if (str.contains("Activity_user_view")) {
                        Intent intent = new Intent(getContext(), Activity_User_to_Host_animation.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getContext().startActivity(intent);
                    } else if (str.contains("Activity_host_view")) {
                        Intent intent = new Intent(getContext(), Activity_Host_to_User_animation.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getContext().startActivity(intent);
                    }
                }
            }
        });

        edit_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (v.getId() == R.id.editbutton) {
                    Context mContext = getContext().getApplicationContext();
                    LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(getContext().LAYOUT_INFLATER_SERVICE);

                    //R.layout.dialog는 xml 파일명이고  R.id.popup은 보여줄 레이아웃 아이디
                    View layout = inflater.inflate(R.layout.dialog_edit,(ViewGroup) findViewById(R.id.popup));
                    AlertDialog.Builder aDialog = new AlertDialog.Builder(getContext());

                    aDialog.setTitle("내 정보 수정하기"); //타이틀바 제목
                    aDialog.setView(layout); //dialog.xml 파일을 뷰로 셋팅


                    edit_phone = (EditText) layout.findViewById(R.id.edit_phone);
                    edit_email = (EditText) layout.findViewById(R.id.edit_email);
                    edit_facebook = (EditText) layout.findViewById(R.id.edit_facebook);
                    edit_kakaotalk = (EditText) layout.findViewById(R.id.edit_kakao);

                    edit_phone.setHint(text_phone.getText());
                    edit_email.setHint(text_email.getText());
                    edit_facebook.setHint(text_facebook.getText());
                    edit_kakaotalk.setHint(text_kakaotalk.getText());

                    aDialog.setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    final RequestParams idParams = new RequestParams("id", Helper_server.userData.getUserID());
                                    int userID = Helper_userData.getInstance().getUserID();
                                    final String phone = edit_phone.getText().toString();
                                    final String email = edit_email.getText().toString();
                                    Log.d("aaaaaa",""+userID);
                                    Log.d("aaaaaa",email);

                                    //알림 뜨게 추가.
                                    if(phone.equals(""))
                                        return;
                                    if(email.equals(""))
                                        return;
                                    idParams.put("userID", userID);
                                    idParams.put("phone", phone);
                                    idParams.put("email", email);
                                    //TODO 카톡이랑 페이스북 아이디 넣기

                                    Helper_server.post("editmyself.php", idParams, new AsyncHttpResponseHandler() {
                                        @Override
                                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                            Log.i("abde", "why");
                                            text_phone.setText(edit_phone.getText());
                                            text_email.setText(edit_email.getText());
                                            Helper_userData.getInstance().setPhoneNumber(phone);
                                            Helper_userData.getInstance().setEmail(email);
                                        }

                                        @Override
                                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                            Log.i("abde", "fail");
                                        }
                                    });
                                    // 'YES'
                                    //Log.e("edit_phone.getText()",""+edit_phone.getText().toString());


                                }
                            }).setNegativeButton("취소",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // 'No'
                                    return;
                                }
                            });
                    AlertDialog ad = aDialog.create();
                    //ad.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                    //ad.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    ad.show();//보여줌!


                }
            }
        });
        this.addView(view);
    }

    public Bitmap getCircleBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        int size = (bitmap.getWidth()/2);
        canvas.drawCircle(size, size, size, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public void logoutAlert() {
        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(getContext());
        alert.setTitle("로그아웃");
        alert.setMessage("로그아웃 하겠습니까?");
        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                AsyncHttpClient client = Helper_server.getInstance();
                final PersistentCookieStore myCookieStore = new PersistentCookieStore(getContext()); //이부분 Context 확인해야함. Activity context로.
                Helper_server.logout(myCookieStore, getContext());
                client.setCookieStore(myCookieStore);

                Intent intent = new Intent(getContext(), Activity_login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                getContext().startActivity(intent);
            }
        });
        alert.show();

    }
    public void setCheck_host_user(int n){
        check_host_user = n;
    }
}
