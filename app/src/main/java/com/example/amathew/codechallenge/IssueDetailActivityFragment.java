package com.example.amathew.codechallenge;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.amathew.codechallenge.util.CommonUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class IssueDetailActivityFragment extends Fragment {

    private ProgressDialog progress;
    private View fragmentRootView;

    private ArrayAdapter<String> mCommentsAdapter;
    public IssueDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Activity activity = getActivity();
        Intent intent = activity.getIntent();
        showProgressSpinner();

        FetchCommentsTask issueFetchTask = new FetchCommentsTask();
        String commentsUrl = intent.getStringExtra(MainActivity.EXTRA_COMMENTS_URL);
        issueFetchTask.execute(commentsUrl);

        fragmentRootView = inflater.inflate(R.layout.fragment_issue_detail, container, false);;
        mCommentsAdapter = new ArrayAdapter<String>(
                activity,
                R.layout.comments_row_item,
                R.id.comment_title,
                new ArrayList<String>());

        ListView listView = (ListView) fragmentRootView.findViewById(R.id.comment_list);
        listView.setAdapter(mCommentsAdapter);

        View emptyView = fragmentRootView.findViewById(R.id.empty_list);
        listView.setEmptyView(emptyView);
        return fragmentRootView;
    }

    private void showProgressSpinner() {
        if(progress == null){
            progress = new ProgressDialog(getActivity());
            progress.setTitle("Loading");
            progress.setMessage("Loading comments...");
        }
        progress.show();
    }

    public class FetchCommentsTask extends AsyncTask<String, Void, JSONArray> {

        @Override
        protected JSONArray doInBackground(String... params) {
            String commentsJsonStr = CommonUtil.placeNetworkRequest(params[0],
                    CommonUtil.HttpMethod.GET);
            if(commentsJsonStr != null){
                try {
                    return new JSONArray(commentsJsonStr);
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
                mCommentsAdapter.clear();
                for(int i=0; i < result.length(); i++) {
                    try {
                        JSONObject issueItemJSON = new JSONObject(result.getString(i));
                        mCommentsAdapter.add(issueItemJSON.getString("body"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }else{
                Log.e(MainActivity.TAG, "Empty or Invalid response");
            }
        }

    }
}
