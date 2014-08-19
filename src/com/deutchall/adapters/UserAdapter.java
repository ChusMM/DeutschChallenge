package com.deutchall.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.deutchall.activities.R;
import com.deutchall.identification.User;

public class UserAdapter extends ArrayAdapter<User> {
	
	private final Context context;
    private final List<User> users;
    
    public UserAdapter(Context context, List<User> users) {
        super(context, R.layout.rowuser, users);
        this.context = context;
        this.users = users;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	View rowView = inflater.inflate(R.layout.rowuser, parent, false);
    	
    	TextView txRowUser = (TextView) rowView.findViewById(R.id.userNameRow);
    	txRowUser.setText(users.get(position).getName());
    	return rowView;
    }
}
