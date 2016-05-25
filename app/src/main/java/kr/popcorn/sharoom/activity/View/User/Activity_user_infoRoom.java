package kr.popcorn.sharoom.activity.View.User;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import kr.popcorn.sharoom.R;
import kr.popcorn.sharoom.activity.Activity_FacillitiesInfo;
import kr.popcorn.sharoom.activity.Activity_largeMap;
import kr.popcorn.sharoom.helper.GlobalApplication;
import kr.popcorn.sharoom.helper.Helper_room;
import kr.popcorn.sharoom.helper.Helper_roomData;
import me.yokeyword.imagepicker.adapter.GlideFragmentAdapter;


public class Activity_user_infoRoom extends FragmentActivity {

    public static Activity_user_infoRoom rActivity;

    private GoogleMap googleMap;
    private MarkerOptions markerOptions;
    private LatLng latLng;
    public static final int SEARCH_RADIUS = 8000;


    private ViewPager viewPager;
    private Button btn_find;

    private GlideFragmentAdapter listAdapter;

    private TextView tvCount;
    private TextView roomPrice;
    private TextView startDate;
    private TextView endDate;

    private CheckBox type1;
    private CheckBox type2;
    private CheckBox type3;
    private CheckBox type4;

    private TextView address;
    private TextView comment;
    private TextView facilities;

    private int position;
    private int imgLength;
    private int idx;
    private int roomnumber;

    //public ImageView cFacillities;
    private LinearLayout cFacilities;
    private Activity_FacillitiesInfo customDialog;
    private ViewGroup layout;
    private ImageAdapter adapter;
    private Helper_roomData roomData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_info);
        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.mapView);

        // Getting a reference to the map
        googleMap = supportMapFragment.getMap();

        //imageview(view pager)
        viewPager = (ViewPager)findViewById(R.id.pager);
        tvCount = (TextView) findViewById(R.id.tv_count);

        roomPrice = (TextView)findViewById(R.id.roomPrice);
        startDate = (TextView)findViewById(R.id.startDate);
        endDate = (TextView)findViewById(R.id.endDate);

        type1 = (CheckBox)findViewById(R.id.ck_roomtype1);
        type2 = (CheckBox)findViewById(R.id.ck_roomtype2);
        type3 = (CheckBox)findViewById(R.id.ck_roomtype3);
        type4 = (CheckBox)findViewById(R.id.ck_roomtype4);

        address = (TextView)findViewById(R.id.address);
        comment = (TextView)findViewById(R.id.comment);
        facilities = (TextView)findViewById(R.id.facilities);

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

        roomPrice.setText( roomData.price );
        startDate.setText( roomData.sDate );
        endDate.setText( roomData.eDate );

        if( roomData.roomKind.equals("원룸") ){
            type1.setChecked(true);
        }else if( roomData.roomKind.equals("하숙") ){
            type2.setChecked(true);
        }else if( roomData.roomKind.equals("자취") ){
            type3.setChecked(true);
        }else {
            type4.setChecked(true);
        }

        address.setText(roomData.address);

        String location = address.getText().toString();

        Log.d("address", location);
        comment.setText(roomData.roomInfo);
        facilities.setText(roomData.fac);

        GlobalApplication myApp = (GlobalApplication) getApplication();
        myApp.setGlobalString(location);

        if(location!=null && !location.equals("")){
            new GeocoderTask().execute(location);
        }
        //listAdapter = new GlideFragmentAdapter( getSupportFragmentManager(), roomData.image);

        adapter = new ImageAdapter(this);

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);

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



        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //tvCount.setText(position + 1 + "/" + imgList.size());
                tvCount.setText(position + 1 + " /" + imgLength );
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //btn_find.setOnClickListener(findClickListener);
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng arg0) {
                Intent mapIntent = new Intent(Activity_user_infoRoom.this, Activity_largeMap.class);
                startActivity(mapIntent);
            }
        });


        layout = (ViewGroup) findViewById(R.id.reservationBar);
        layout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //Intent intent = new Intent(this, SubActivity.class);
                //startActivity(intent);
                switch (v.getId()) {
                    case R.id.reservationBar:
                        //Toast.makeText(MainActivity.this, "예약버튼이 눌렸습니다.", Toast.LENGTH_SHORT).show();
                        Intent reservationIntent = new Intent(Activity_user_infoRoom.this, Activity_user_reservation.class);
                        reservationIntent.putExtra("index", idx);
                        startActivity(reservationIntent);
                        finish();
                }
            }
        });
    }
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
    public boolean onTouchEvent(MotionEvent event)
    {
        if(event.getAction() == MotionEvent.ACTION_OUTSIDE){
            customDialog.dismiss();
        }

        return false;
    }


    private View.OnClickListener cancelListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.cancelBtn:
                    customDialog.dismiss();
                    break;
            }
        }
    };

    // An AsyncTask class for accessing the GeoCoding Web Service
    private class GeocoderTask extends AsyncTask<String, Void, List<Address>>{

        @Override
        protected List<Address> doInBackground(String... locationName) {
            // Creating an instance of Geocoder class
            Geocoder geocoder = new Geocoder(getBaseContext());
            List<Address> addresses = null;

            try {
                // Getting a maximum of 3 Address that matches the input text
                addresses = geocoder.getFromLocationName(locationName[0], 3);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return addresses;
        }


        @Override
        protected void onPostExecute(List<Address> addresses) {

            if(addresses==null || addresses.size()==0){
                Toast.makeText(getBaseContext(), "No Location found", Toast.LENGTH_SHORT).show();
            }

            // Clears all the existing markers on the map
            googleMap.clear();

            // Adding Markers on Google Map for each matching address
            for(int i=0;i<addresses.size();i++){

                Address address = (Address) addresses.get(i);

                // Creating an instance of GeoPoint, to display in Google Map
                latLng = new LatLng(address.getLatitude(), address.getLongitude());
                String addressText = String.format("%s, %s",
                        address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                        address.getCountryName());

                markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(addressText);
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.mapmarker));

                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                googleMap.addMarker(markerOptions);
                googleMap.addCircle(new CircleOptions()
                        .center(latLng)
                        .radius(SEARCH_RADIUS * 1.2)
                        .strokeColor(Color.RED)
                        .strokeWidth(4));

                // Locate the first location
                if(i==0)
                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            }


        }
    }

    // image resize!!!!!!
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    // image resize!!!!!!
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }
}