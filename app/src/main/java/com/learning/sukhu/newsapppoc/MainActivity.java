package com.learning.sukhu.newsapppoc;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.learning.sukhu.newsapppoc.Json.GetJsonData;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DataBus{
    private GetJsonData jsonData;
    private String LOG_TAG = "Sukh_Tag_MainActivity";
    private ListView listView;
    private ArrayList<Sources> sourceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.v(LOG_TAG, "onCreate");
    }

    protected void onStart(){
        super.onStart();
        //initializing all the variables
        listView = (ListView) findViewById(R.id.listView);
        sourceList = new ArrayList<>();

        //checking if network is available
        if(isNetworkAvailable()){
            jsonData = new GetJsonData("Hello", this);
            jsonData.execute();
        }else {
            Toast.makeText(this,"Sorry!! I don't have Internet Power Right now !", Toast.LENGTH_LONG).show();
            listView.setVisibility(View.INVISIBLE);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * When AsyncTask completes and onPreExecute method will run
     * this method will invoke
     * @param sources
     */
    @Override
    public void processedData(List<Sources> sources) {
        Log.v(LOG_TAG, "transferring Data");
        //creating sourceList of Sources
        sourceList.addAll(sources);
        //custom adapter
        CustomListAdapter adapter = new CustomListAdapter(
                getApplicationContext(), R.layout.custom_list_layout, sourceList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(view.getContext(), sourceList.get(position).getName(), Toast.LENGTH_LONG).show();
            }
        });
    }

    protected void onStop(){
        super.onStop();
        Log.v(LOG_TAG, "ON_STOP");
        jsonData = null;
        listView = null;
        sourceList = null;
    }
}
