package com.deutchall.identification;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;

import com.deutchall.persistence.DBAgent;

public class PFUser {
	
	public static List<User> select(Context context) {
		
		String[] columns = new String[] {DBAgent.KEY_USER, DBAgent.USERNAME, DBAgent.EMAIL};
		List<User> users = DBAgent.getInstance(context).selectUser(columns, null, null, null, null, null);
		return users;
	}
	
	public static User select(Context context, String name) {	
		String[] columns = new String[] {DBAgent.KEY_USER, DBAgent.USERNAME, DBAgent.EMAIL};
		List<User> users = DBAgent.getInstance(context).selectUser(columns,  DBAgent.USERNAME + " =  ?", 
				new String[] {name}, null, null, null);
		if (users.size() == 1) {
			return users.get(0);
		} else {
			return null;
		}
	}
	
	public static User select(Context context, User u) {	
		String[] columns = new String[] {DBAgent.KEY_USER, DBAgent.USERNAME, DBAgent.EMAIL};
		List<User> users = DBAgent.getInstance(context).selectUser(columns,  DBAgent.USERNAME + " =  ?", 
				new String[] {u.getName()}, null, null, null);
		if (users.size() == 1) {
			return users.get(0);
		} else if (users.size() > 1) {
			return null;  //throw new DBCorrectnessException();
		} else {
			return null;
		}
	}
	
	public static boolean exists(Context context, String name) {
		String[] columns = new String[] {DBAgent.KEY_USER, DBAgent.USERNAME, DBAgent.EMAIL};
		List<User> users = DBAgent.getInstance(context).selectUser(columns,  DBAgent.USERNAME + " =  ?", 
				new String[] {name}, null, null, null);
		return users.size() > 0;
	}
	
	public static boolean exists(Context context, User u) {
		String[] columns = new String[] {DBAgent.KEY_USER, DBAgent.USERNAME, DBAgent.EMAIL};
		List<User> users = DBAgent.getInstance(context).selectUser(columns,  DBAgent.USERNAME + " =  ?", 
				new String[] {u.getName()}, null, null, null);
		return users.size() > 0;
	}
	
	public static boolean existsEmail(Context context, String email) {
		
		String[] columns = new String[] {DBAgent.KEY_USER, DBAgent.USERNAME, DBAgent.EMAIL};
		List<User> users = DBAgent.getInstance(context).selectUser(columns, DBAgent.EMAIL + " =  ?", 
				new String[] {email}, null, null, null);
		return users.size() > 0;
	}
	
	public static void insert(Context context, String name, String email) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(DBAgent.USERNAME, name);
		contentValues.put(DBAgent.EMAIL, email);
		if (!exists(context, name)) {
			DBAgent.getInstance(context).insert(DBAgent.USERS, contentValues);
		}
	}
	
	public static void insert(Context context, User u) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(DBAgent.USERNAME, u.getName());
		contentValues.put(DBAgent.EMAIL, u.getEmail());
		if (!exists(context, u.getName())) {
			DBAgent.getInstance(context).insert(DBAgent.USERS, contentValues);
		}
	}
}
