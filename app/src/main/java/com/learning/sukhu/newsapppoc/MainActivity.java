package com.learning.sukhu.newsapppoc;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.learning.sukhu.newsapppoc.Json.GetJsonData;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DataBus{
    private GetJsonData jsonData;
    private String LOG_TAG = "Sukh_Tag_MainActivity";
    private ListView listView;
    private GridView gridView;
    private ArrayList<Sources> sourceList;
    private int USER_PREFRENCE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(LOG_TAG, "onCreate");
        setContentView(R.layout.activity_main);
    }

    protected void onStart(){
        super.onStart();
        //initializing all the variables
        sourceList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.listView);
        gridView = (GridView) findViewById(R.id.gridView);

        getUserPref();

        //checking if network is available
        if(isNetworkAvailable()){
            jsonData = new GetJsonData("Hello", this);
            jsonData.execute();
        }else {
            Toast.makeText(this,"Sorry!! I don't have Internet Power Right now !", Toast.LENGTH_LONG).show();
            listView.setVisibility(View.INVISIBLE);
        }
    }

    protected void onStop(){
        super.onStop();
        Log.v(LOG_TAG, "ON_STOP");
        jsonData = null;
        listView = null;
        sourceList = null;
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
        if (USER_PREFRENCE % 2 == 0) {
            // even
            Log.v(LOG_TAG, "EVEN");
            listView.setVisibility(View.INVISIBLE);
            gridView.setVisibility(View.VISIBLE);
            CustomListAdapter adapter = new CustomListAdapter(
                    getApplicationContext(), R.layout.custome_grid_layout, sourceList, "GRID");
            gridView.setAdapter(adapter);
        } else {
            // odd
            gridView.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.VISIBLE);
            Log.v(LOG_TAG, "ODD");
            CustomListAdapter adapter = new CustomListAdapter(
                    getApplicationContext(), R.layout.custom_list_layout, sourceList, "LIST");
            listView.setAdapter(adapter);
        }


        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(view.getContext(), sourceList.get(position).getName(), Toast.LENGTH_LONG).show();
            }
        });*/
    }

    private void getUserPref(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        USER_PREFRENCE = sharedPreferences.getInt("Key", 0);
        Log.v(LOG_TAG, "PREF - " + USER_PREFRENCE);
        if(USER_PREFRENCE==0){
            Log.v(LOG_TAG, "FIRST TIME");
            updatePref(sharedPreferences);
        }else{
            Log.v(LOG_TAG, "NOT FIRST TIME");
            updatePref(sharedPreferences);
        }
    }

    private void updatePref(SharedPreferences sharedPreferences){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        USER_PREFRENCE++;
        editor.putInt("Key", USER_PREFRENCE);
        editor.commit();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
