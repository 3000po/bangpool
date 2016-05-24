package kr.popcorn.sharoom.activity.Fragment.User;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import java.util.ArrayList;

import kr.popcorn.sharoom.R;

import kr.popcorn.sharoom.activity.TabView.TabView_myself;
import kr.popcorn.sharoom.activity.TabView.TabView_registerAdapter;
import kr.popcorn.sharoom.activity.TabView.TabView_rentListAdapter;
import kr.popcorn.sharoom.activity.TabView.TabView_reservationAdapter;
import kr.popcorn.sharoom.helper.Helper_roomData;
import kr.popcorn.sharoom.helper.Helper_room;
import kr.popcorn.sharoom.helper.Helper_userData;

public final class TestFragment extends Fragment {
    private static final String KEY_CONTENT = "TestFragment:Content";

    final int ROOMLIST = 0;
    final int RESERVATONROOM = 1;
    final int MyInformation = 2;

    private View view;
    private View view_register;
    private View info;

    public RecyclerView recyclerView;
    public RecyclerView recyclerView_register;

    private TabView_rentListAdapter rentListAdapter;
    private TabView_reservationAdapter reservationAdapter;
    private TabView_registerAdapter registerAdapter;

    public static TestFragment newInstance(String content) {
        TestFragment fragment = new TestFragment();

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            builder.append(content).append(" ");
        }
        builder.deleteCharAt(builder.length() - 1);
        fragment.mContent = content;

        return fragment;
    }

    private String mContent = "???";

    private void setAdapterView(LayoutInflater inflater, ViewGroup container, int cases){
        view = inflater.inflate(R.layout.activity_list, container, false);
        info = inflater.inflate(R.layout.activity_myself,container,false);

        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        ArrayList<Helper_roomData> roomList = new ArrayList<Helper_roomData>();
        ArrayList<Helper_roomData> reservList = new ArrayList<Helper_roomData>();

        Log.i("roomCount",""+ Helper_room.roomCount);
        for(int i=0; i< Helper_room.roomCount; i++){
            //roomList = Helper_room.getInstance().list;
            if( Helper_room.getInstance().list.get(i).isClosed == 0 && Helper_room.getInstance().list.get(i).getUserID() != Helper_userData.getInstance().getUserID()) {
                roomList.add( Helper_room.getInstance().list.get(i) );
            }else if( Helper_room.getInstance().list.get(i).rUserID == Helper_userData.getInstance().getUserID() ){
                reservList.add( Helper_room.getInstance().list.get(i) );
            }
        }

        switch (cases){
            case ROOMLIST:
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setItemAnimator(new DefaultItemAnimator());

                rentListAdapter = new TabView_rentListAdapter(getActivity(),
                        roomList,
                        (LinearLayoutManager) recyclerView.getLayoutManager());
                recyclerView.setAdapter(rentListAdapter);

                break;

            case RESERVATONROOM:
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setItemAnimator(new DefaultItemAnimator());

                reservationAdapter = new TabView_reservationAdapter(getActivity(),
                        reservList,
                        (LinearLayoutManager) recyclerView.getLayoutManager());
                recyclerView.setAdapter(reservationAdapter);
                break;
        }
        return ;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
            mContent = savedInstanceState.getString(KEY_CONTENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(mContent.equalsIgnoreCase("a")){ //Ignore Lower Upper case
            //setAdapterView(inflater, container, ROOMLIST);
          //  LinearLayout layout = new LinearLayout(getActivity());
          //  layout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
         //   layout.setGravity(Gravity.CENTER);

            //Activity l =  (Activity) new Activity_roomList();
            //LinearLayout layout = (LinearLayout) l.findViewById(R.id.roomlist);

            //return inflater.inflate(R.layout.activity_list, container, false);
            setAdapterView(inflater, container, ROOMLIST);
            return view;
        }
        else if(mContent.equalsIgnoreCase("b")){
            setAdapterView(inflater, container, RESERVATONROOM);
            return view;
        }
        else if(mContent.equalsIgnoreCase("c")){
            TabView_myself tabView_myself = new TabView_myself(getContext());
            return tabView_myself;

        }else {

            TextView text = new TextView(getActivity());
            text.setGravity(Gravity.CENTER);
            text.setText(mContent);
            text.setTextSize(20 * getResources().getDisplayMetrics().density);
            text.setPadding(20, 20, 20, 20);

            LinearLayout layout = new LinearLayout(getActivity());
            layout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
            layout.setGravity(Gravity.CENTER);
            layout.addView(text);

            return layout;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_CONTENT, mContent);

    }

    public void setDate(ArrayList<Helper_roomData> list){

    }

}
