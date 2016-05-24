package kr.popcorn.sharoom.activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import org.w3c.dom.Text;

import kr.popcorn.sharoom.R;

/**
 * Created by parknature on 16. 5. 6..
 */
public class Activity_profileView extends Dialog {
    private WindowManager mManager;
    private View mView;
    private View.OnClickListener cancelListener;

    private TextView tv_name;
    private TextView tv_phonenumber;
    private TextView tv_email;

    String name;
    String phone;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams mParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT);

        getWindow().setAttributes(mParams);
        setContentView(R.layout.activity_profileview);

        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_phonenumber = (TextView) findViewById(R.id.tv_phonenumber);
        tv_email = (TextView) findViewById(R.id.tv_email);

        tv_name.setText(name);
        tv_phonenumber.setText(phone);
        tv_email.setText(email);
    }

    public Activity_profileView(Context context) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
    }
    public void setProfile(String name, String phone, String email){
        this.name = name;
        this.phone = phone;
        this.email = email;
    }
}
