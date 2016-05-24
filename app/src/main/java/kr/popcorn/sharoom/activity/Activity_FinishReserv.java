package kr.popcorn.sharoom.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import kr.popcorn.sharoom.R;
import kr.popcorn.sharoom.activity.View.User.Activity_user_infoRoom;
import kr.popcorn.sharoom.activity.View.User.Activity_user_reservation;
import kr.popcorn.sharoom.activity.View.User.Activity_user_reservation_check;

public class Activity_FinishReserv extends Activity {
    private TextView checkReserv;
    private ImageView home;

    Activity_user_reservation activity = (Activity_user_reservation) Activity_user_reservation.rActivity;
    Activity_user_infoRoom rActivity = (Activity_user_infoRoom) Activity_user_infoRoom.rActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_reserv);

        //activity.finish();
        checkReserv = (TextView)findViewById(R.id.CheckReservation);
        checkReserv.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(Activity_FinishReserv.this, "예약확방확인.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Activity_FinishReserv.this, Activity_user_reservation_check.class);
                intent.putExtra("roomNumber", getIntent().getExtras().getInt("roomnumber"));

                startActivity(intent);
                finish();
            }
        });

        home = (ImageView) findViewById(R.id.iv_homebtn);
        home.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
