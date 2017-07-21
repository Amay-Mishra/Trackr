package com.example.root.trackr;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Debargha on 20/7/17.
 */

public class SearchFriendListAdapter extends ArrayAdapter<User> implements View.OnClickListener{

    private ArrayList<User> users;
    Context mContext;

    @Override
    public void onClick(View v) {

    }

    // View lookup cache
    private static class ViewHolder {
        TextView textViewSearchFriendId;
        TextView textViewSearchFriendName;
    }

    public SearchFriendListAdapter(@NonNull Context context, ArrayList<User> users) {
        super(context, R.layout.list_search_friend, users);
        this.users = users;
        this.mContext = context;
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        User user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_search_friend, parent, false);
            viewHolder.textViewSearchFriendName = (TextView) convertView.findViewById(R.id.textViewSearchFriendName);
            viewHolder.textViewSearchFriendId= (TextView) convertView.findViewById(R.id.textViewSearchFriendId);


            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.textViewSearchFriendId.setText(String.valueOf(user.getId()));
        viewHolder.textViewSearchFriendName.setText(user.getFname() + " " + user.getLname());
        // Return the completed view to render on screen
        return convertView;
    }
}
