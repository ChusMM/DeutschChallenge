package com.deutchall.activities;

import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.deutchall.exceptions.ExistingUserException;
import com.deutchall.identification.PFUser;

public class RegisterActivity extends Activity {
	
	public static final String TAG = "com.deutchall.RegisterActivity";
	
	private final String SUBJECT = "Deutsch Challenge application registration";
	private final String MESSAGE_BODY = "Welcome to Deutsch Challenge.\nThank you for registering in german learning app!\n" +
									      "We are grateful to have you as a customer of our application and we expect it will be useful for your german learning proccess.\n" + 
									      "We strongly recommend you read our privacy policy along with the using terms and conditions";	
	private TextView txUser;
	private TextView txEmail;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        
		this.txUser = (TextView)findViewById(R.id.txName);
		this.txEmail = (TextView)findViewById(R.id.txMail);
    }
	
	@Override
    public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		
		if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) { } 
		else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) { }
    }
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	    	terminate();
	    }
	    return super.onKeyDown(keyCode, event);
	}
		
	public void register(View view)  {
		boolean checkPointOk = false;
		String user = txUser.getText().toString();
		String email = txEmail.getText().toString();
			
		try {
			checkPointOk = this.checkFields(user, this.txEmail.getText()) & !this.existingEmail(email);
			if (checkPointOk) {
				com.deutchall.utilities.MailSender sender = new com.deutchall.utilities.MailSender(email, "123", this);
				sender.sendMail(SUBJECT, MESSAGE_BODY, "noreply@deutschallenge.com", email);
			
				PFUser.insert(this, user, email);
				this.terminate();		
			}
		} catch (SQLiteException e) {
			Log.e(TAG, e.toString());
		} catch (IndexOutOfBoundsException e) {
			Log.e(TAG, e.toString());
		} catch (IOException e) {
			Log.e(TAG, e.toString());
			e.printStackTrace();
		} catch (ExistingUserException e) {
			Log.e(TAG, e.toString());
		}
	}
	
	public void menu(View view) {
		terminate();
	}
	
	private boolean checkFields(String user, CharSequence email) {
		if (user.length() == 0) {
			alertMsg("Please, enter an username");
			return false;
		} else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
			alertMsg("Please, enter a valid email address");
			return false;
		} else {
			return true;
		}
	}
	
	private boolean existingEmail(String email) throws SQLiteException, IndexOutOfBoundsException, IOException {
		if (PFUser.existsEmail(this, email)) {
			alertMsg("E-Mail " + email + " already exists");
			return true;
		} else {
			return false;
		}
	}

	private void alertMsg(String msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Attention!");
        builder.setMessage(msg);
        builder.setPositiveButton("OK", null);
        builder.create();
        builder.show();
	}
	
	private void terminate() {
		RegisterActivity.this.finish();
	}
}
