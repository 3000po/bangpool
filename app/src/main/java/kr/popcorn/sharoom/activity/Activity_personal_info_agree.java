package kr.popcorn.sharoom.activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import kr.popcorn.sharoom.R;

/**
 * Created by parknature on 2016. 5. 24..
 */
public class Activity_personal_info_agree extends Dialog {

    private View.OnClickListener cancelListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams mParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT);

        mParams.gravity = Gravity.CENTER;


        getWindow().setAttributes(mParams);

        setContentView(R.layout.activity_personal_info_agree);
    }

    public Activity_personal_info_agree(Context context) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);

    }

}