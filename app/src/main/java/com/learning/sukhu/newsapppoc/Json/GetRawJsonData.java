package com.learning.sukhu.newsapppoc.Json;

import android.os.AsyncTask;
import android.util.Log;

import com.learning.sukhu.newsapppoc.DownloadStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by sukhu on 2016-09-12.
 */
public class GetRawJsonData {

    private String LOG_TAG = "Sukh_Tag_GetRawJsonData";
    private String mRawUrl;
    private String mData;
    private DownloadStatus mDownloadStatus;
    private Log log;

    public GetRawJsonData(String mRawUrl) {
        this.mRawUrl = mRawUrl;
        this.mDownloadStatus = DownloadStatus.IDLE;
    }

    public void reset(){
        this.mDownloadStatus = DownloadStatus.IDLE;
        this.mRawUrl = null;
        this.mData = null;
    }

    public String getmRawUrl() {
        return mRawUrl;
    }

    public void setmRawUrl(String mRawUrl) {
        this.mRawUrl = mRawUrl;
    }

    public String getmData() {
        return mData;
    }

    public void setmData(String mData) {
        this.mData = mData;
    }

    public DownloadStatus getmDownloadStatus() {
        return mDownloadStatus;
    }

    public void execute(){
        Log.d(LOG_TAG, "Inside execute of GetRawData");
        this.mDownloadStatus = DownloadStatus.PROCESSING;
        DownloadRawData downloadRowData = new DownloadRawData();
        downloadRowData.execute(mRawUrl);
    }

    public class DownloadRawData extends AsyncTask<String, Void, String>{

        protected void onPostExecute(String webData) {
            Log.v(LOG_TAG, "Inside On Post Execute of DownloadRawData");
            mData = webData;
            Log.v(LOG_TAG, "Data returned was : " + mData);
            if(mData == null) {
                if(mRawUrl ==null){
                    mDownloadStatus = DownloadStatus.NOT_INITIALIZED;
                } else{
                    mDownloadStatus = DownloadStatus.FAILED_OR_EMPTY;
                }
            }else{
                // Success
                mDownloadStatus = DownloadStatus.OK;
            }
        }

        @Override
        protected String doInBackground(String... params) {
            Log.d(LOG_TAG, "Inside doInBackground of GetRawData");
            HttpURLConnection urlConn = null;
            BufferedReader reader = null;

            if(params == null){
                Log.d(LOG_TAG, "param is null");
                return null;
            }

            try{
                URL url = new URL(params[0]);

                urlConn = (HttpURLConnection) url.openConnection();
                urlConn.setRequestMethod("GET");
                urlConn.connect();

                InputStream inputStream = urlConn.getInputStream();
                if(inputStream == null){
                    Log.d(LOG_TAG, "inputStream is null");
                    return null;
                }
                StringBuffer buffer = new StringBuffer();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;

                while((line = reader.readLine()) != null){
                    buffer.append(line + "\n");
                }

                return buffer.toString();

            }catch (IOException e){
                Log.d(LOG_TAG, "Error ", e);
                return null;
            }finally {
                if(urlConn != null){
                    urlConn.disconnect();
                }
                if(reader != null){
                    try {
                        reader.close();
                    } catch (final IOException e){
                        Log.e(LOG_TAG, "Error closing Stream ", e);
                    }
                }
            }
        }
    }
}
