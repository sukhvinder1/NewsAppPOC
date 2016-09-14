package com.learning.sukhu.newsapppoc;

import android.app.ListActivity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.learning.sukhu.newsapppoc.Json.GetJsonData;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DataBus{
    private GetJsonData jsonData;
    private List<Sources> sourcesData;
    private String LOG_TAG = "Sukh_Tag_MainActivity";
    private ListView listView;
    private String[] sourcesList;
    private ArrayList<Sources> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.v(LOG_TAG, "onCreate");
        listView = (ListView) findViewById(R.id.listView);

        sourcesData = new ArrayList<Sources>();
        arrayList = new ArrayList<>();

        if(isNetworkAvailable()){
            jsonData = new GetJsonData("Hello", this);
            jsonData.execute();
        }else {
            Toast.makeText(this,"Sorry!! I don't have Internet Power Right now !", Toast.LENGTH_LONG).show();
            listView.setVisibility(View.INVISIBLE);
        }
        /*GetRawJsonData theRawData = new GetRawJsonData("https://newsapi.org/v1/sources/");
        theRawData.execute();*/
    }

    public void onStart(){
        super.onStart();
        Log.v(LOG_TAG, "onStart");
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void onPause(){
        super.onPause();
        //jsonData = null;
        Log.v(LOG_TAG, "onPause");
    }

    @Override
    public void processedData(List<Sources> sources) {
        Log.v(LOG_TAG, "transferring Data");
        this.sourcesData = sources;
        sourcesList = new String[sourcesData.size()];
        int i=0;
        for(Sources source : sourcesData){
            Log.v(LOG_TAG, source.getName());
            sourcesList[i] = source.getName();
            arrayList.add(source);
            i++;
        }
        //ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, sourcesList);
        CustomListAdapter adapter = new CustomListAdapter(getApplicationContext(), R.layout.custom_list_layout, arrayList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(view.getContext(), sourcesList[position], Toast.LENGTH_LONG).show();
            }
        });
    }
}
