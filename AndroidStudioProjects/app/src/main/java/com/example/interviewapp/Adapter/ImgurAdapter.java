package com.example.interviewapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.interviewapp.Activities.DetailActivity;
import com.example.interviewapp.Model.ImgurData;
import com.example.interviewapp.R;

import java.util.ArrayList;

public class ImgurAdapter extends RecyclerView.Adapter<ImgurAdapter.HolderClass> {

    private static final String TAG = "lol";
    private Context mContext; // Typically you call it to get information regarding another part of your program (activity and package/application).
    private ArrayList<ImgurData> mArrayData; // This represent a collection of data, that we are going to supply to the RecyclerView
    private RequestOptions mOptions; // This will be use by Glide, that is a library recommended by Google, when you want to download images from an API.

    /*This is the constructor that we will use, to create our RecycleView with our collection, We do not provide the RequestOptions because this is not necessary for creating
    our collection.
    When creating a class, we only need the pieces that are required to instantiate and construct the object
     */

    public ImgurAdapter(Context mContext, ArrayList<ImgurData> mArrayData) {
        this.mContext = mContext;
        this.mArrayData = mArrayData;

        //Request option for Glide
        mOptions = new RequestOptions().centerCrop().placeholder(R.drawable.loading).error(R.drawable.ic_error_black_24dp);
    }
    //Now is time to inflate our custom XML
    @NonNull
    @Override
    public HolderClass onCreateViewHolder(@NonNull final ViewGroup parent, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.data_row, parent, false);
        final HolderClass holderClass = new HolderClass(view);
        holderClass.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Fot this to work the way that we want, we have to get the position of thee element
                Intent intent = new Intent(mContext, DetailActivity.class);
                int pos = holderClass.getAdapterPosition();
                intent.putExtra("img", mArrayData.get(pos).getImage());
                intent.putExtra("title", mArrayData.get(pos).getTitle());
                mContext.startActivity(intent);
                ((AppCompatActivity)mContext).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        return holderClass;
    }

    @Override
    public void onBindViewHolder(@NonNull HolderClass holderClass, int i) {

        //Here we get the data and put it in our recyclerView
        ImgurData imgurData = mArrayData.get(i);
        holderClass.Imgur_tv.setText(imgurData.getTitle());
        Glide.with(mContext).load(mArrayData.get(i).getImage() + ".jpg").apply(mOptions).into(holderClass.Imgur_img);
    }

    //We need to make sure that we have the data correctly in our Array (Collection of Data)
    @Override
    public int getItemCount() {
        if (mArrayData.size() > 0) {
            return mArrayData.size();
        }
        return 0;
    }

    public class HolderClass extends RecyclerView.ViewHolder {

        ImageView Imgur_img;
        TextView Imgur_tv;
        LinearLayout linearLayout;

        public HolderClass(@NonNull View itemView) {
            super(itemView);

            Imgur_img = itemView.findViewById(R.id.data_row_img);
            Imgur_tv = itemView.findViewById(R.id.data_row_title);
            linearLayout = itemView.findViewById(R.id.data_row_container);

        }
    }
}