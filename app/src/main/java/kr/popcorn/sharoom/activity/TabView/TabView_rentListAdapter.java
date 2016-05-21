package kr.popcorn.sharoom.activity.TabView;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import kr.popcorn.sharoom.R;
import kr.popcorn.sharoom.activity.View.User.Activity_user_infoRoom;
import kr.popcorn.sharoom.helper.Helper_roomData;

/**
 * Created by user on 16. 3. 2.
 */


//예약가능한 방목록을 보여주기위한 리스트 어댑터
public class TabView_rentListAdapter extends RecyclerView.Adapter<TabView_rentListAdapter.ViewHolder> {

    private Context mContext;
    public ArrayList<Helper_roomData> list;
    private LinearLayoutManager linearLayoutManager;

    public ArrayList<Helper_roomData> getContactsList() {
        return list;
    }

    public TabView_rentListAdapter(Context context, ArrayList<Helper_roomData> _dataSet, LinearLayoutManager linearLayoutManager) {
        mContext = context;
        list = _dataSet;
        this.linearLayoutManager = linearLayoutManager;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext)
                .inflate(R.layout.list_item_room, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.i("whatthefuck", list.get(0).image.get(0));
        Glide.with(mContext).load(list.get(0).image.get(0)).into(holder.roomimage);

        //holder.roomimage.setImageResource(list.get(position).roomimage);
        holder.rating.setText(list.get(position).title);
        //holder.text.setText(tmp.substring(0,4));
    }

    @Override
    public int getItemCount() {
        if (list == null) return 0;
        return list.size();
    }

    public void remove(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

    public void add(Helper_roomData song, int position) {
        list.add(position, song);
        notifyItemInserted(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView roomimage;
        public TextView rating;
        public ImageView mapMenu;

        public ViewHolder(View itemView) {
            super(itemView);
            roomimage = (ImageView) itemView.findViewById(R.id.roomimage);
            rating = (TextView) itemView.findViewById(R.id.roomrating);

            itemView.setClickable(true);
            itemView.setOnClickListener(this);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // song is selected
                    Log.i("", "index : " + getAdapterPosition());
                    return true;
                }
            });
        }

        //방을 클릭했을때 방에대한 정보를 넣어 상세정보 액티비티를 호출한다.
        @Override
        public void onClick(View v) {
            //when user click the roomlist, it show the room information about index!!!
            Intent intent = new Intent(mContext, Activity_user_infoRoom.class);
            intent.putExtra("roomNumber", list.get(getAdapterPosition()).roomNumber);
            mContext.startActivity(intent);


            Log.e("number", "index : " + list.get(0).title);
            Log.e("number", "index : " + getAdapterPosition());
        }

    }
}