package com.example.amathew.codechallenge;

import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.content.Loader;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.amathew.codechallenge.util.CommonUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity{

    private ArrayAdapter<Map<String,String>> mIssuesAdapter;
    private ProgressDialog progress;

    public static final String TAG = "debug_log";
    private static final String ISSUES_URL = "https://api.github.com/repos/rails/rails/issues";

    private static final String ISSUE_TITLE_KEY ="title";
    private static final String COMMENTS_URL_KEY ="comments_url";

    public static final String EXTRA_COMMENTS_URL = "cu";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showProgressSpinner();

        FetchIssueTask issueFetchTask = new FetchIssueTask();
        issueFetchTask.execute();

        mIssuesAdapter = new ArrayAdapter<Map<String,String>>(
                this,
                R.layout.issues_row_item,
                R.id.issue_title,
                new ArrayList<Map<String,String>>());

        ListView listView = (ListView) findViewById(R.id.issue_list);
        listView.setAdapter(mIssuesAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Map<String,String> issueMap = mIssuesAdapter.getItem(position);
                Intent intent = new Intent(MainActivity.this, IssueDetailActivity.class)
                        .putExtra(EXTRA_COMMENTS_URL, issueMap.get(COMMENTS_URL_KEY));
                startActivity(intent);
            }
        });
    }

    private void showProgressSpinner() {
        if(progress == null){
            progress = new ProgressDialog(this);
            progress.setTitle("Loading");
            progress.setMessage("Loading issues from Github...");
        }
        progress.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class FetchIssueTask extends AsyncTask<Void, Void, JSONArray> {

        @Override
        protected JSONArray doInBackground(Void... params) {
            String forecastJsonStr = CommonUtil.placeNetworkRequest(ISSUES_URL,
                    CommonUtil.HttpMethod.GET);
            if(forecastJsonStr != null){
                try {
                    return getArrayFromJSONArrayString(forecastJsonStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONArray result) {
            progress.dismiss();
            if (result != null) {
                mIssuesAdapter.clear();
                for(int i=0; i < result.length(); i++) {
                    try {
                        JSONObject issueItemJSON = new JSONObject(result.getString(i));
                        Map<String,String> issuesMap = new HashMap<>();
                        issuesMap.put(ISSUE_TITLE_KEY, issueItemJSON.getString("title"));
                        issuesMap.put(COMMENTS_URL_KEY, issueItemJSON.getString("comments_url"));
                        mIssuesAdapter.add(issuesMap);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }else{
                Log.e(TAG, "Empty or Invalid response");
            }
        }

        private JSONArray getArrayFromJSONArrayString(String jsonArrayString) throws JSONException {
            return new JSONArray(jsonArrayString);
        }
    }

}
