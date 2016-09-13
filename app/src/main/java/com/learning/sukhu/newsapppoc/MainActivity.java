package com.learning.sukhu.newsapppoc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.learning.sukhu.newsapppoc.Json.GetJsonData;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*GetRawJsonData theRawData = new GetRawJsonData("https://newsapi.org/v1/sources/");
        theRawData.execute();*/

        GetJsonData jsonData = new GetJsonData("Hello");
        jsonData.execute();
    }
}
