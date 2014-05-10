package com.deutchall.activities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.deutchall.identification.PFQuestion;
import com.deutchall.identification.Question;
import com.deutchall.persistence.DBAgent;
import com.deutchall.activities.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.graphics.Color;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class GameActivity extends Activity {
	
	private final int timeOutValue = 5000;
	private final int second = 1000;
	private final int baseTimeAnswering = 700;
	private final int baseTimeTimeOut = 0;
	private final int multip = 10;
	
	private String name = null;
	private int gameId;
	private int score = 0;
	private int nQuestion = 0;
	private String result = "";
	private ArrayList<Question> questions;
	private int checkedRadioButton = -1;
	private int correctAns = -1;
	private Timer updateTimeout;
	private int remainingTime;
	private boolean called = false;
	
	private final Lock lock = new ReentrantLock();
	private final Handler timeOutHandler = new Handler();
	private final Handler RemainingTimeHandler = new Handler();
	private final Handler timeHandler = new Handler();
	private final Handler animTimeHandler = new Handler();
	
	private TextView txName;
	private TextView txScore;
	private TextView txNQuestion;
	private TextView txQuestion;
	private TextView txTime;
	private Button rad0;
	private Button rad1;
	private Button rad2;
	private TextView txResult;
		
	@Override
    public void onCreate(Bundle savedInstanceState) {
		
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.game);
        
        Intent intent = getIntent();
        String[] args = intent.getStringArrayExtra(SelectGameActivity.EXTRA_MESSAGE);
        
        this.name = args[0];
        this.gameId = Integer.parseInt(args[1]);
        this.questions = PFQuestion.retrieveQuestions(this, gameId);
        
        this.txName = (TextView)findViewById(R.id.txName);
        this.txScore = (TextView)findViewById(R.id.txScore);
        this.txNQuestion = (TextView)findViewById(R.id.txNQuestion);
        this.txQuestion = (TextView)findViewById(R.id.txQuestion);
        this.txTime = (TextView)findViewById(R.id.txTime);
        this.rad0 = (Button)findViewById(R.id.radio0);
        this.rad1 = (Button)findViewById(R.id.radio1);
        this.rad2 = (Button)findViewById(R.id.radio2);
        this.txResult = (TextView)findViewById(R.id.txResult);
        
        this.txName.setText(this.name);
        this.setFullQuestionVisibility(View.GONE);
        
        this.setEnableAnswers(false);
        this.updateScore();
        this.launchNextQuestion();
    }
	
	@Override
    public void onConfigurationChanged(Configuration newConfig) {
		
		super.onConfigurationChanged(newConfig);
		
		if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) { } 
		else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) { }
    }
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
	    if((keyCode == KeyEvent.KEYCODE_BACK)) {
	    	this.back();
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	public void resp0(View view) throws InterruptedException {
		
		((Button)findViewById(R.id.radio0)).setSelected(true);
		((Button)findViewById(R.id.radio1)).setSelected(false);
		((Button)findViewById(R.id.radio2)).setSelected(false);
		this.answer();
	}
	
	public void resp1(View view) throws InterruptedException {
		
		((Button)findViewById(R.id.radio0)).setSelected(false);
		((Button)findViewById(R.id.radio1)).setSelected(true);
		((Button)findViewById(R.id.radio2)).setSelected(false);
		this.answer();
	}
	
	public void resp2(View view) throws InterruptedException {
		
		((Button)findViewById(R.id.radio0)).setSelected(false);
		((Button)findViewById(R.id.radio1)).setSelected(false);
		((Button)findViewById(R.id.radio2)).setSelected(true);
		this.answer();
	}
	
	public void menu(View view) {
		this.back();
	}
			
	private void answer() throws InterruptedException {

		this.setEnableAnswers(false);
		this.cancelUpdateTime();
		this.checkedRadioButton = this.radChecked();
		
		this.launchCheckAnswer();
    }
			
	private void launchUpdateTimeout() {
		this.RemainingTimeHandler.post(updateTimeoutRunnable);
	}
	
	private void launchCheckAnswer() {
		lock.lock();
		
		try {
			
			if(!this.called) {
				this.setCalled(true);
				this.timeOutHandler.post(checkAnswerRunnable);
			}
			
		} finally {
			lock.unlock();
		}
	}
	
	private void launchEval() {
		this.timeHandler.post(evalRunnable);
	}
	
	private void launchHide() {
		this.timeHandler.post(hideButtonsRunnable);
	}
	
	private void launchGoToNext() {
		this.timeHandler.post(nextRunnable);
	}
		
	private void launchShowQuestionView(View v) {
		
		final ShowQuestionViewRunnable showRunnable = new ShowQuestionViewRunnable(v);
		this.animTimeHandler.post(showRunnable);
	}
	
	private void launchHideQuestionView(View v) {
		
		final HideQuestionViewRunnable hideRunnable = new HideQuestionViewRunnable(v);
		this.animTimeHandler.post(hideRunnable);
	}
	
	final Runnable updateTimeoutRunnable = new Runnable() {
		public void run() {
			
			txTime.setVisibility(View.GONE);
			if(remainingTime >= 0) animTxTime(txTime, remainingTime);
			
			if(remainingTime < 0) {
				
				setEnableAnswers(false);
				remainingTime = 0;
				cancelUpdateTime();
				launchCheckAnswer();
				
			} else {
				remainingTime --;
			}
		}
	};
	
	final Runnable evalRunnable = new Runnable() {
		public void run() {
			evaluate();
		}
	};
	
	final Runnable checkAnswerRunnable = new Runnable() {
		public void run() {
			setCorrectAns();
			timerEvaluationSequence();
		}
	};

	final Runnable hideButtonsRunnable = new Runnable() {
		public void run() {
			clearTxResult();
			hideFullQuestion();
		}
	};
	
	final Runnable nextRunnable = new Runnable() {
		public void run() {
			goToNext();
		}
	};

	private class ShowQuestionViewRunnable implements Runnable {
		View v;
		public ShowQuestionViewRunnable(View v) {this.v = v; }
		public void run() {
			animShowQuestionView(v);
		}
	}
	
	private class HideQuestionViewRunnable implements Runnable {
		View v;
		public HideQuestionViewRunnable(View v) {this.v = v; }
		public void run() {
			animHideQuestionView(v);
		}
	}
	
	private void timerUpdateTimeout() {
		
		this.updateTimeout = new Timer();

		this.updateTimeout.schedule(new TimerTask() {
			@Override
			public void run () {
				launchUpdateTimeout();
			}
		}, 0, second);
	}
	
	private void timerShowQuestionView(final View v, int miliseconds) {
		
		Timer showTimer = new Timer();
		
		showTimer.schedule(new TimerTask() {
			@Override
			public void run() { launchShowQuestionView(v); } 
		}, miliseconds);		
	}
	
	private void timerHideQuestionView(final View v, int miliseconds) {
		
		Timer showTimer = new Timer();
		
		showTimer.schedule(new TimerTask() {
			@Override
			public void run() { launchHideQuestionView(v); } 
		}, miliseconds);		
	}

	private void timerEvaluationSequence() {
		
		Timer ansTimer = new Timer();
		
		int baseTime = this.getBaseTime();
		
		ansTimer.schedule(new TimerTask() {
			@Override
			public void run() { launchEval(); } 
		}, baseTime);
		
		ansTimer.schedule(new TimerTask() {
			@Override
			public void run() { launchHide(); } 
		}, baseTime + 1000);
		
		ansTimer.schedule(new TimerTask() {
			@Override
			public void run() { launchGoToNext(); } 
		}, baseTime + 1600);
	}
		
	private void cancelUpdateTime() {
		this.updateTimeout.cancel();
		this.updateTimeout.purge();
	}
	
	
	private void setCorrectAns() {
		this.correctAns  = this.questions.get(this.nQuestion).getCorrect();
	}
	
	private void evaluate() {
		
		boolean success = this.evaluateAnswer();
			
		this.showResult(success);
		if(success) {
			this.incScore();
		}
		this.incQuestionIndex();
	}
	
	private boolean evaluateAnswer() {

		boolean success = this.checkedRadioButton == this.correctAns;
				
		return success;
	}
	
	private void showResult(boolean success) {
		
		this.result = this.getResult(success);
		
		this.txTime.setVisibility(View.VISIBLE);
		this.drawCorrectAnswer(); 
		this.txResult.setText(this.result);
		
		if(success) {
			this.txResult.setTextColor(Color.GREEN);
		}
		else {
			this.drawWrongAnswer();
			this.txResult.setTextColor(Color.RED);
		}
		this.animScaleView(this.txResult);
	}

	private void animShowQuestionView(View animationTarget) {
		
		animationTarget.setVisibility(View.VISIBLE);
		Animation animation = AnimationUtils.loadAnimation(this, R.anim.button_appearance);
		animationTarget.startAnimation(animation);
	}
	
	private void animHideQuestionView(View animationTarget) {
		
		Animation animation = AnimationUtils.loadAnimation(this, R.anim.button_disappearance);
		animationTarget.startAnimation(animation);
		animationTarget.setVisibility(View.GONE);
	}
	
	private void animScaleView(View animationTarget) {
		
		animationTarget.setVisibility(View.VISIBLE);
		Animation animation = AnimationUtils.loadAnimation(this, R.anim.result_animation);
		animationTarget.startAnimation(animation);
	}
	
	private void animTxTime(TextView animationTarget, int rem) {
		
		animationTarget.setText(String.valueOf(rem));
		//animationTarget.setVisibility(View.VISIBLE);
		Animation animation = AnimationUtils.loadAnimation(this, R.anim.time_animation);
		animationTarget.startAnimation(animation);
	}

	private void incScore() {
		int rem = Integer.parseInt(this.txTime.getText().toString());
		this.score = this.score + (this.multip * (++ rem));
		this.updateScore();
	}

	private void incQuestionIndex() {
		
		this.nQuestion ++;
	}

	private String getResult(boolean success) {
		
		if(success) {
			return "Richtig!";
		} 
		else {
			if(this.checkedRadioButton == -1) return "Time Out!";
			else return "Falsch!";
		}
	}
	
	private void goToNext() {
				
		if(this.isEnd()) this.rankingAndClose();
		else this.launchNextQuestion();	
	}
			
	private boolean isEnd() {
	
		if(this.nQuestion > this.questions.size() - 1) {
		
			this.nQuestion = this.questions.size() - 1;
			return true;			
		} 
		else {
			return false;
		}
	}
	
	private void rankingAndClose() {
	
		String date = this.getDate();
		DBAgent.getInstance(this).insertDDDRanking(this.name, date, this.score);
		this.gameover();
	}
			
	private void launchNextQuestion() {
		
		this.unpressRads();
		this.unselectRads();
		this.setButtonsDefaultStyle();
		this.updateGameStatus();
		this.restoreTimeRemaining();
    	this.writeAnswers();
    	this.showFullQuestion();
    	this.setEnableAnswers(true);
    	this.setCalled(false);
    	this.timerUpdateTimeout();
	}
	
	private void showFullQuestion() {
		
		this.timerShowQuestionView(txQuestion, 0);
		this.timerShowQuestionView(rad0, 100);
		this.timerShowQuestionView(rad1, 200);
		this.timerShowQuestionView(rad2, 300);	
	}
	
	private void hideFullQuestion() {
		
		this.timerHideQuestionView(rad2, 0);
		this.timerHideQuestionView(rad1, 100);
		this.timerHideQuestionView(rad0, 200);
		this.timerHideQuestionView(txQuestion, 300);
	}
		
	private void unpressRads() {
		
		this.rad0.setPressed(false);
		this.rad1.setPressed(false);
		this.rad2.setPressed(false);
	}
	
	private void unselectRads() {
		
		this.rad0.setSelected(false);
		this.rad1.setSelected(false);
		this.rad2.setSelected(false);
		this.checkedRadioButton = -1;
	}
	
	private int radChecked() {
		
		if(this.rad0.isSelected())
			return 1;
		else if(this.rad1.isSelected())
			return 2;
		else if(this.rad2.isSelected())
			return 3;
		else
			return -1;
	}
	
	private void setCalled(boolean value) {
		this.called = value;
	}
	
	private void setButtonsDefaultStyle() {
		
		this.rad0.setBackgroundResource(R.drawable.button_custom);
		this.rad1.setBackgroundResource(R.drawable.button_custom);
		this.rad2.setBackgroundResource(R.drawable.button_custom);
	}
		
	private void updateGameStatus() {
		
        this.txNQuestion.setText("Question: "+ (this.nQuestion + 1));
        this.txQuestion.setText(questions.get(this.nQuestion).getQuestion());
	}
	
	private void updateScore() {
		this.txScore.setText("Score: " + this.score);
	}
	
	private void writeAnswers() {
		
		String ans1 = this.questions.get(this.nQuestion).getAns1();
		String ans2 = this.questions.get(this.nQuestion).getAns2();
		String ans3 = this.questions.get(this.nQuestion).getAns3();
		
		this.rad0.setText(ans1);
		this.rad1.setText(ans2);
		this.rad2.setText(ans3);
	}
	
	private void restoreTimeRemaining() {
		this.remainingTime = this.timeOutValue / 1000;
	}
	
	private int getBaseTime() {
		
		if(this.checkedRadioButton == -1) {
			return this.baseTimeTimeOut;
		} 
		else {
			return this.baseTimeAnswering;
		}
	}

	private String getDate() {
		
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm", Locale.US);
		return dateFormat.format(new Date()).toString();
	}
					
	private void clearTxResult() {
		this.txResult.setVisibility(View.GONE);
		this.txResult.setText("");
	}
	
	private void setEnableAnswers(boolean enabled) {
		
		this.rad0.setEnabled(enabled);
		this.rad1.setEnabled(enabled);
		this.rad2.setEnabled(enabled);
	}

	private void setFullQuestionVisibility(int visib) {
		
		this.rad0.setVisibility(visib);
		this.rad1.setVisibility(visib);
		this.rad2.setVisibility(visib);
	}
	
	private void drawCorrectAnswer() {
		
		switch(this.correctAns) {
			case 1:
				this.rad0.setBackgroundResource(R.drawable.button_correct);
				break;
			case 2:
				this.rad1.setBackgroundResource(R.drawable.button_correct);
				break;
			case 3:
				this.rad2.setBackgroundResource(R.drawable.button_correct);
				break;
			default:
				throw new Error("Correct answer coherence error");
		}	
	}
	
	private void drawWrongAnswer() {
	
		switch(this.checkedRadioButton) {
			
			case 1:
				this.rad0.setBackgroundResource(R.drawable.button_wrong);
				break;
			case 2:
				this.rad1.setBackgroundResource(R.drawable.button_wrong);
				break;
			case 3:
				this.rad2.setBackgroundResource(R.drawable.button_wrong);
				break;
		}
	}
	
	private void gameover() {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(this.result + " Congratulations, test finished!")
		.setCancelable(false)
		.setPositiveButton("Show Ranking", new DialogInterface.OnClickListener() {
	    	   
			public void onClick(DialogInterface dialog, int id) {
				end();
	        }
	    });
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	private void end() {
		
		Intent intent = new Intent(this, RankingActivity.class);
		startActivity(intent);
		this.cleanAndExit();
	}
	
	private void cleanAndExit() {
		this.cancelUpdateTime();
		GameActivity.this.finish();
	}
		
	private void back() {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("¿Are you sure you want to come back to main menu?")
		       .setCancelable(false)
		       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		    	   
		    	   public void onClick(DialogInterface dialog, int id) {
		        	   cleanAndExit();
		           }
		       })
		       .setNegativeButton("No", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                dialog.cancel();
		           }
		       });
		
		AlertDialog alert = builder.create();
		alert.show();
    }
}
