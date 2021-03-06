package kr.popcorn.sharoom.helper;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import kr.popcorn.sharoom.R;

/**
 * Created by user on 16. 3. 13.
 */

//Acitivity_editRoom_roomPic에서
//방 내부 사진을 RecyclerView로 출력하기 위한 어댑터
public class Helper_roomPicListAdapter extends RecyclerView.Adapter<Helper_roomPicListAdapter.ViewHolder> {

    private Helper_adapterCommunication mListener; // Acitivity_editRoom_roomPic와 통신을 위한 리스너

    private Context mContext;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<String> list;                 //리스트

    public ArrayList<String> getRoomPicList() {
        return list;
    }

    public Helper_roomPicListAdapter(Context context, ArrayList<String> _dataSet, LinearLayoutManager linearLayoutManager) {
        mContext = context;
        list = _dataSet;
        this.linearLayoutManager = linearLayoutManager;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext)
                .inflate(R.layout.list_item_editroom, parent, false);

        return new ViewHolder(v);
    }


    //현재 보여지는 원소를 출력하기 위한 메소드
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try{
            Bitmap bitmap = decodeUri(mContext,  Uri.fromFile(new File(list.get(position))), 500);
            //Picasso.with(mContext).load(bitmap).into(holder.roompic);
            holder.roompic.setImageBitmap(bitmap);
        }catch( FileNotFoundException e){
            e.printStackTrace();
        }

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

    public void add(String path, int position) {
        list.add(position, path);
        notifyItemInserted(position);
    }
    public void addAll(ArrayList<String> pathList){
        list.addAll(pathList);
    }

    public void setList(ArrayList<String> arrayList){
        list = arrayList;
    }

    //RecyclerView를 출력해주는 액티비티( Acitivity_editRoom_roomPic)와 연결.
    public void setOnClickListener(Helper_adapterCommunication listener){
        mListener = listener;
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView roompic;
        public ImageButton deleteButton;

        public ViewHolder(View itemView) {
            super(itemView);
            roompic = (ImageView) itemView.findViewById(R.id.roompic);
            deleteButton = (ImageButton) itemView.findViewById(R.id.delete);


            itemView.setClickable(true);
            itemView.setOnClickListener(this);
            deleteButton.setOnClickListener(this);

            //원소를 길게 눌렀을떄 처리는 Acitivity_editRoom_roomPic에서 해준다.
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // path is selected
                    mListener.longClickItem(getAdapterPosition());
                    return true;
                }
            });
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.delete:
                    mListener.removeItem(getAdapterPosition());
                    break;
            }
        }
    }

    public static Bitmap decodeUri(Context c, Uri uri, final int requiredSize)
            throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o);

        int width_tmp = o.outWidth
                , height_tmp = o.outHeight;
        int scale = 1;

        while(true) {
            if(width_tmp / 2 < requiredSize || height_tmp / 2 < requiredSize)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o2);
    }
}