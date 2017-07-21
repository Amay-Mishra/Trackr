package com.example.root.trackr;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Debargha on 20/7/17.
 */

public class FriendListListAdapter extends ArrayAdapter<FriendList> implements View.OnClickListener{

    private ArrayList<FriendList> friendLists;
    Context mContext;


    @Override
    public void onClick(View v) {

    }

    // View lookup cache
    private static class ViewHolder {
        TextView textViewFriendListFriendId;
        TextView textViewFriendListFriendName;
        TextView textViewPermissionStatus;
    }

    public FriendListListAdapter(@NonNull Context context, ArrayList<FriendList> friendLists) {
        super(context, R.layout.list_friend_list, friendLists);
        this.friendLists = friendLists;
        this.mContext = context;
    }


    private int lastPosition = -1;



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        FriendList friendList;
        // Check if an existing view is being reused, otherwise inflate the view
        final ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_friend_list, parent, false);

            viewHolder.textViewFriendListFriendId = (TextView) convertView.findViewById(R.id.textViewFriendListId);
            viewHolder.textViewFriendListFriendName = (TextView) convertView.findViewById(R.id.textViewFriendListName);
            viewHolder.textViewPermissionStatus = (TextView) convertView.findViewById(R.id.textViewPermissionStatus);

            result=convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        friendList = friendLists.get(position);

        viewHolder.textViewFriendListFriendId.setText(String.valueOf(friendList.getId()));
        viewHolder.textViewFriendListFriendName.setText(friendList.getName());
        if(friendList.getPermissionStatus() == 0) {
            viewHolder.textViewPermissionStatus.setText("DENIED");
        } else {
            viewHolder.textViewPermissionStatus.setText("ALLOWED");
        }

        viewHolder.textViewPermissionStatus.setTag(friendList);

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        return convertView;
    }



}
