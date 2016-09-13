package com.learning.sukhu.newsapppoc.Json;

import android.net.Uri;
import android.util.Log;

import com.learning.sukhu.newsapppoc.DataBus;
import com.learning.sukhu.newsapppoc.DownloadStatus;
import com.learning.sukhu.newsapppoc.Sources;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sukhu on 2016-09-12.
 */
public class GetJsonData extends GetRawJsonData {
    private String LOG_TAG = "Sukh_tag_GetJsonData";
    private List<Sources> sourcesList;
    private Uri destinationUri;
    private DataBus dataBus;

    public GetJsonData(String url, DataBus dataBus){
        super(null);
        sourcesList = new ArrayList<Sources>();
        this.dataBus = dataBus;
        createUri(url);
    }

    public void execute(){
        super.setmRawUrl(destinationUri.toString());
        DownloadJsonData downloadJsonData = new DownloadJsonData();
        Log.v(LOG_TAG, "Built URI = " + destinationUri.toString());
        downloadJsonData.execute(destinationUri.toString());
    }

    public void createUri(String url){
        Log.v(LOG_TAG, url);
        final String BASE_URL = "https://newsapi.org/v1/sources/";
        destinationUri = Uri.parse(BASE_URL).buildUpon().build();
        Log.v(LOG_TAG, "Prepare URI - " + destinationUri.toString());
    }

    public List<Sources> processData(){
        if(getmDownloadStatus() != DownloadStatus.OK){
            Log.e(LOG_TAG, "Error Downloading raw file");
        }

        final String SOURCES = "sources";
        final String ID = "id";
        final String NAME = "name";
        final String CATEGORY = "category";
        final String URLS_TO_LOGOS = "urlsToLogos";
        final String URL_SIZE = "small";

        try{
            JSONObject jsonObject = new JSONObject(getmData());
            JSONArray jsonArray = jsonObject.getJSONArray(SOURCES);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonSource = jsonArray.getJSONObject(i);
                String id = jsonSource.getString(ID);
                String name = jsonSource.getString(NAME);
                String categoty = jsonSource.getString(CATEGORY);

                JSONObject urls = jsonSource.getJSONObject(URLS_TO_LOGOS);
                String urlSmall = urls.getString(URL_SIZE);
                
                Sources sourcesObject = new Sources(id, name, categoty, urlSmall);
                
                this.sourcesList.add(sourcesObject);
            }
        }catch (JSONException jsone){
            jsone.printStackTrace();
            Log.e(LOG_TAG, "Error process json data");
        }

        return sourcesList;
    }

    public class DownloadJsonData extends DownloadRawData{

        protected void onPostExecute(String webData) {
            super.onPostExecute(webData);
            dataBus.processedData(processData());
        }

        protected String doInBackground(String... params) {
            return super.doInBackground(params);
        }
    }


}
