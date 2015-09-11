package com.example.amathew.codechallenge;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by amathew on 9/4/15.
 */
public class IssuesAdapter extends ArrayAdapter<Map<String, String>> {

    public IssuesAdapter(Context context, int textViewResourceId, ArrayList<Map<String, String>> items) {
        super(context, textViewResourceId, items);

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext())
                    .inflate(R.layout.issues_row_item, parent, false);

            viewHolder = new ViewHolder(convertView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Map<String, String> item = getItem(position);
        if (item != null) {
            viewHolder.issueTitle.setText(item.get("title"));
        }

        return convertView;
    }

    protected class ViewHolder {
        public final TextView issueTitle;

        public ViewHolder(View view) {
            this.issueTitle = (TextView) view.findViewById(R.id.issue_title);
        }
    }
}
