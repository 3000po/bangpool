package kr.popcorn.sharoom.activity.TabView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import kr.popcorn.sharoom.R;
import kr.popcorn.sharoom.activity.View.Host.Activity_host_reservation_check;
import kr.popcorn.sharoom.activity.View.User.Activity_user_reservation_check;
import kr.popcorn.sharoom.helper.Helper_roomData;


/**
 * Created by Administrator on 2016-03-25.
 */
public class TabView_reservationAdapter extends RecyclerView.Adapter<TabView_reservationAdapter.ViewHolder> {

    ImageView myface;

    private Context mContext;
    public ArrayList<Helper_roomData> list;
    private LinearLayoutManager linearLayoutManager;

    public ArrayList<Helper_roomData> getContactsList() {
        return list;
    }

    public TabView_reservationAdapter(Context context, ArrayList<Helper_roomData> _dataSet, LinearLayoutManager linearLayoutManager) {
        mContext = context;
        list = _dataSet;
        this.linearLayoutManager = linearLayoutManager;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext)
                .inflate(R.layout.list_item_reservation_room, parent, false);

        return new ViewHolder(v);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {

        //myface.setImageBitmap(getCircleBitmap(face));

        //holder.myface.setImageResource(R.drawable.ic_action_mapview_m);
       // Glide.with(mContext).load(list.get(position).image.get(0)).override(800,600).into(holder.roomimage);
        Glide.with(mContext).load(list.get(position).image.get(0)).into(holder.roomimage);

        holder.roomname.setText(list.get(position).title);
        holder.sDate.setText(list.get(position).rsDate);
        holder.sDate.setText(list.get(position).reDate);
    }

    @Override
    public int getItemCount() {
        if (list == null) return 0;
        return list.size();
    }

    public void add(Helper_roomData data, int position) {
        list.add(position, data);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView roomimage;
        public TextView roomname;

        TextView sDate;
        TextView eDate;

        public ViewHolder(View itemView) {
            super(itemView);
            //album = (ImageView) itemView.findViewById(R.id.album_art1);
            //text = (TextView) itemView.findViewById(R.id.year);

            roomimage = (ImageView) itemView.findViewById(R.id.roomimage);
            roomname = (TextView) itemView.findViewById(R.id.r_roomname);

            sDate = (TextView) itemView.findViewById(R.id.s_date);
            eDate = (TextView) itemView.findViewById(R.id.e_date);

            itemView.setClickable(true);
            itemView.setOnClickListener(this);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // song is selected
                    return true;
                }
            });
        }

        @Override
        public void onClick(View v) {
            String str = "" + mContext.getClass();
            if (str.contains("Activity_user_view")) {
                Intent intent = new Intent(mContext, Activity_user_reservation_check.class);
                intent.putExtra("roomNumber", list.get(getAdapterPosition()).getRoomNumber());
                mContext.startActivity(intent);

            } else if (str.contains("Activity_host_view")) {
                //방 상태에 따라 바로 예약확인 이냐 예약완료냐
                Intent intent = new Intent(mContext, Activity_host_reservation_check.class);  // <- 예약 확인
                intent.putExtra("roomNumber", list.get(getAdapterPosition()).getRoomNumber());
                mContext.startActivity(intent);
            }
        }

    }
}
