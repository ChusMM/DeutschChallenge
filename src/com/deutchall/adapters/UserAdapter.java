package com.deutchall.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.deutchall.activities.R;
import com.deutchall.identification.User;

public class UserAdapter extends ArrayAdapter<User> implements OnItemClickListener {
	
	private final Context context;
    private final List<User> users;
    private String selected = null;
    
    public UserAdapter(Context context, List<User> users) {
        super(context, R.layout.rowuser, users);
        this.context = context;
        this.users = users;
    }
    
    public String getSelected() {
    	return this.selected;
    }
    
    public void setSelected(String selected) {
    	this.selected = selected;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	View rowView = inflater.inflate(R.layout.rowuser, parent, false);
    	
    	TextView txRowUser = (TextView) rowView.findViewById(R.id.userNameRow);
    	txRowUser.setText(users.get(position).getName());
    	if(txRowUser.getText().toString().equals(selected)) {
    		txRowUser.setBackgroundColor(context.getResources().getColor(R.color.selected));
    	} else {
    		txRowUser.setBackgroundColor(context.getResources().getColor(R.color.shadow));
    	}
    	return rowView;
    }
    
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
    	if (parent.getId() == R.id.listUsers) {
			selected = ((TextView) view).getText().toString();
		}
    }
}
