package com.example.amathew.codechallenge;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by amathew on 9/4/15.
 */
public class IssuesAdapter extends CursorAdapter{

    public IssuesAdapter(Context context) {
        super(context, null,  android.widget.CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        final View view;
        view = LayoutInflater.from(context).inflate(R.layout.issues_row_item, parent, false);
        if (view != null) {
            final ViewHolder viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final ViewHolder viewHolder = (ViewHolder) view.getTag();
        viewHolder.issueTitle.setText("issue title...");
    }

    protected class ViewHolder {
        public final TextView issueTitle;

        public ViewHolder(View view) {
            this.issueTitle = (TextView) view.findViewById(R.id.issue_title);
        }
    }
}
