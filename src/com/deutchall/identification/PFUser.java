package com.deutchall.identification;

import java.io.IOException;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteException;

import com.deutchall.exceptions.ExistingUserException;
import com.deutchall.exceptions.PkDuplicatedException;
import com.deutchall.exceptions.RegisterNotFoundException;
import com.deutchall.persistence.DBAgent;
import com.deutchall.persistence.Sql;

public class PFUser {
	
	public static List<User> select(Context context) throws SQLiteException, IndexOutOfBoundsException, IOException {
		String[] columns = new String[] {Sql.USERNAME, Sql.EMAIL};
		List<User> users = DBAgent.getInstance(context).selectUser(columns, null, null, null, null, null);
		return users;
	}
	
	public static User select(Context context, String name) throws PkDuplicatedException, RegisterNotFoundException, SQLiteException, IndexOutOfBoundsException, IOException {	
		String[] columns = new String[] {Sql.USERNAME, Sql.EMAIL};
		List<User> users = DBAgent.getInstance(context).selectUser(columns,  Sql.USERNAME + " =  ?", 
				new String[] {name}, null, null, null);
		if (users.size() == 1) {
			return users.get(0);
		} else if (users.size() > 1) {
			throw new PkDuplicatedException("Found a duplicated primary key in users table");
		} else {
			throw new RegisterNotFoundException("User: " + name + " not found");
		}
	}
	
	public static User select(Context context, User u) throws RegisterNotFoundException, PkDuplicatedException, SQLiteException, IndexOutOfBoundsException, IOException {	
		String[] columns = new String[] {Sql.USERNAME, Sql.EMAIL};
		List<User> users = DBAgent.getInstance(context).selectUser(columns,  Sql.USERNAME + " =  ?", 
				new String[] {u.getName()}, null, null, null);
		if (users.size() == 1) {
			return users.get(0);
		} else if (users.size() > 1) {
			 throw new PkDuplicatedException("Found a duplicated primary key in users table");
		} else {
			throw new RegisterNotFoundException("User: " + u.getName() + " not found");
		}
	}
	
	public static boolean exists(Context context, String name) throws SQLiteException, IndexOutOfBoundsException, IOException {
		String[] columns = new String[] {Sql.USERNAME, Sql.EMAIL};
		List<User> users = DBAgent.getInstance(context).selectUser(columns,  Sql.USERNAME + " =  ?", 
				new String[] {name}, null, null, null);
		return users.size() > 0;
	}
	
	public static boolean exists(Context context, User u) throws SQLiteException, IndexOutOfBoundsException, IOException {
		String[] columns = new String[] {Sql.USERNAME, Sql.EMAIL};
		List<User> users = DBAgent.getInstance(context).selectUser(columns,  Sql.USERNAME + " =  ?", 
				new String[] {u.getName()}, null, null, null);
		return users.size() > 0;
	}
	
	public static boolean existsEmail(Context context, String email) throws SQLiteException, IndexOutOfBoundsException, IOException {
		String[] columns = new String[] {Sql.USERNAME, Sql.EMAIL};
		List<User> users = DBAgent.getInstance(context).selectUser(columns, Sql.EMAIL + " =  ?", 
				new String[] {email}, null, null, null);
		return users.size() > 0;
	}
	
	public static void insert(Context context, String name, String email) throws ExistingUserException, SQLiteException, IndexOutOfBoundsException, IOException {
		ContentValues contentValues = new ContentValues();
		contentValues.put(Sql.USERNAME, name);
		contentValues.put(Sql.EMAIL, email);
		if (!exists(context, name)) {
			DBAgent.getInstance(context).insert(Sql.USERS, contentValues);
		} else {
			throw new ExistingUserException();
		}
	}
	
	public static void insert(Context context, User u) throws ExistingUserException, SQLiteException, IndexOutOfBoundsException, IOException {
		ContentValues contentValues = new ContentValues();
		contentValues.put(Sql.USERNAME, u.getName());
		contentValues.put(Sql.EMAIL, u.getEmail());
		if (!exists(context, u.getName())) {
			DBAgent.getInstance(context).insert(Sql.USERS, contentValues);
		} else {
			throw new ExistingUserException();
		}
	}
}
