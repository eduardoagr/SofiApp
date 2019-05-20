package com.example.interviewapp.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.interviewapp.R;

public class DetailActivity extends AppCompatActivity {

    ImageView imgData;
    TextView titleData;

    //This will be use for Glide, to provide a placeholder or error
    RequestOptions requestOptions;

    //Is never a good idea to manipulate data, coming directly from another activity
    String imgPlaceholder;
    String titlePlaceHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_details);

        imgData = findViewById(R.id.detailActivity_img);
        titleData = findViewById(R.id.detailActivity_title);

        //We want to always check to see if we are passing the data correctly

        Bundle data = getIntent().getExtras();

        if (data != null) {
            imgPlaceholder = data.getString("img");
            titlePlaceHolder = data.getString("title");
        }

        titleData.setText(titlePlaceHolder);

         requestOptions = new RequestOptions().centerCrop().placeholder(R.drawable.loading).error(R.drawable.ic_error_black_24dp);

        Glide.with(this).load(imgPlaceholder).centerCrop().apply(requestOptions).into(imgData);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }
}
