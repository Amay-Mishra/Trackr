package com.example.root.trackr;

import android.content.Context;
import android.support.annotation.IdRes;
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

public class OnlineFriendListAdapter extends ArrayAdapter<OnlineFriend> implements View.OnClickListener{

    private ArrayList<OnlineFriend> onlineFriends;
    Context mContext;

    @Override
    public void onClick(View v) {

    }

    // View lookup cache
    private static class ViewHolder {
        TextView textViewOnlineFriendId;
        TextView textViewOnlineFriendName;
    }

    public OnlineFriendListAdapter(@NonNull Context context, ArrayList<OnlineFriend> onlineFriend) {
        super(context, R.layout.list_online_friends, onlineFriend);
        this.onlineFriends = onlineFriend;
        this.mContext = context;
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        OnlineFriend onlineFriend = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_online_friends, parent, false);
            viewHolder.textViewOnlineFriendName = (TextView) convertView.findViewById(R.id.textViewOnlineFriendName);
            viewHolder.textViewOnlineFriendId= (TextView) convertView.findViewById(R.id.textViewOnlineFriendId);


            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.textViewOnlineFriendId.setText(String.valueOf(onlineFriend.getId()));
        viewHolder.textViewOnlineFriendName.setText(onlineFriend.getName());
        // Return the completed view to render on screen
        return convertView;
    }
}
