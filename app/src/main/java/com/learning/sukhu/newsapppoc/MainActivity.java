package com.learning.sukhu.newsapppoc;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.learning.sukhu.newsapppoc.Json.GetJsonData;

public class MainActivity extends AppCompatActivity {
    private GetJsonData jsonData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*GetRawJsonData theRawData = new GetRawJsonData("https://newsapi.org/v1/sources/");
        theRawData.execute();*/

    }

    public void onStart(){
        super.onStart();
        if(isNetworkAvailable()){
            jsonData = new GetJsonData("Hello");
            jsonData.execute();
        }else {
            Toast.makeText(this,"Sorry!! I don't have Internet Power Right now !", Toast.LENGTH_LONG).show();
        }
    }

    public void onTouch(View v){
        Toast.makeText(this,"Hello", Toast.LENGTH_LONG).show();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void onPause(){
        super.onPause();
        jsonData = null;
    }
}
