package com.example.interviewapp.Activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.interviewapp.Adapter.ImgurAdapter;
import com.example.interviewapp.Model.ImgurData;
import com.example.interviewapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "eee";
    final String BASE_URL = "https://api.imgur.com/3/gallery/search/time/1?q=";

    //This are the components that we need
    RecyclerView ImgurData_recyclerView; //Our collection of data
    ArrayList<ImgurData> imgurDataArrayList; //Wee use this, to hold our data
    ImgurAdapter imgurAdapter; //Our custom adapter
    EditText searchText; //The editText, that we use to search
    TextView message; //if for some reason, we are unable to bring data, we display a message it to the user. This is a good UX practice
    ProgressBar loadingData; //We use this to tell the user that something is happening. This is good UX practice
    ImageButton searchButton; //The button to initiate the query
    Toolbar toolbar; //Provide the name
    RequestQueue queue; //RequestQueue is used to stack your request and handles your cache.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize components
        ImgurData_recyclerView = findViewById(R.id.mainActivity_recyclerView_images);
        ImgurData_recyclerView.setHasFixedSize(true);
        ImgurData_recyclerView.setLayoutManager(new LinearLayoutManager(this));
        queue = Volley.newRequestQueue(this);
        imgurDataArrayList = new ArrayList<>();
        searchButton = findViewById(R.id.mainActivity_searchButton);
        searchText = findViewById(R.id.mainActivity_searchText);
        loadingData = findViewById(R.id.mainActivity_loading_indicator);
        message = findViewById(R.id.mainActivity_message_display);
        toolbar = findViewById(R.id.mainActivity_toolbar);
        setSupportActionBar(toolbar);


        //Here is where I update the the title of the toolbar, and respond to click.

        //Because I know that I am only going to have one button, I can do it this way, but for scalable is better to use a switch
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingData.setVisibility(View.VISIBLE);
                toolbar.setTitle(searchText.getText().toString());
                imgurDataArrayList.clear();
                Search();
            }
        });

        GetJson(searchText.getText().toString());
    }

    /*Function to parse te JSON, using Volley. I could have use async, but Volley was created for this purpose.
    Asynctask is more for long Operations, and has some week points like: no orientation-change support,
     no ability to cancel network calls, as well as no easy way to make API calls in parallel
     */

    private void GetJson(String query){

        StringRequest getRequest = new StringRequest(Request.Method.GET, BASE_URL+query,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loadingData.setVisibility(View.INVISIBLE);
                        // response
                        String title = "";
                        JSONObject imageJSON;
                        String imageURL;
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray data = jsonObject.getJSONArray("data");
                            for(int i = 0; i < data.length(); i++) {
                                title = data.getJSONObject(i).getString("title");
                                imageJSON = data.getJSONObject(i).getJSONArray("images").getJSONObject(0);
                                imageURL = imageJSON.getString("link");

                                imgurDataArrayList.add(new ImgurData(title, imageURL));
                            }

                            imgurAdapter = new ImgurAdapter(MainActivity.this, imgurDataArrayList);
                            ImgurData_recyclerView.setAdapter(imgurAdapter);


                        } catch (JSONException e) { e.printStackTrace(); }

                        imgurAdapter = new ImgurAdapter(MainActivity.this, imgurDataArrayList);
                        ImgurData_recyclerView.setAdapter(imgurAdapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("ERROR", "error => " + error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Client-ID 126701cd8332f32");
                return params;
            }
        };
        queue.add(getRequest);
    }

    //Is very important, to check the network before doing anything
    private boolean NetworkState(Context context){
        boolean isConnected;

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();
        return isConnected;
    }

    private void Search() {
        String query = searchText.getText().toString();
        boolean is_Connected = NetworkState(this);
        if (!is_Connected) {
            message.setText("Failed to load data");
            Toast.makeText(this, "Please make you that you are connected to the iinternet", Toast.LENGTH_SHORT).show();
            ImgurData_recyclerView.setVisibility(View.INVISIBLE);
            message.setVisibility(View.VISIBLE);
            return;
        }

        //If we have connection, we perform the search

        if (query.equals("")) {
            Toast.makeText(this, "Please enter something", Toast.LENGTH_SHORT).show();
        }
        else {
            GetJson(query);
        }
    }
}
