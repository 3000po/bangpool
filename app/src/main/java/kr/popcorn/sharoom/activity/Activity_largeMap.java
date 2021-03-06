package kr.popcorn.sharoom.activity;

import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import kr.popcorn.sharoom.R;
import kr.popcorn.sharoom.helper.GlobalApplication;
import kr.popcorn.sharoom.helper.Helper_room;
import kr.popcorn.sharoom.helper.Helper_roomData;

public class Activity_largeMap extends FragmentActivity {

    private GoogleMap googleMap = null;
    LatLng latLng;
    MarkerOptions markerOptions;
    public static final int SEARCH_RADIUS = 8000;
    private int idx;


    //static final LatLng SEOUL = new LatLng(37.56, 126.97);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_largemap);

        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.mapView);

        // Getting a reference to the map
        googleMap = supportMapFragment.getMap();
        idx = getIntent().getIntExtra("roomNumber",0);  //방리스트 인덱스
        Helper_roomData roomData = Helper_room.getInstance().list.get(idx);

        String location = ((GlobalApplication)this.getApplicationContext()).getGlobalString();

        if(location!=null && !location.equals("")){
            new GeocoderTask().execute(location);
        }
    }

    private class GeocoderTask extends AsyncTask<String, Void, List<Address>> {

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
}
