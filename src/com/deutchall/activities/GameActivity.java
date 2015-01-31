package com.deutchall.activities;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import com.deutchall.exceptions.InvalidGameIdException;
import com.deutchall.identification.ErrorLauncher;
import com.deutchall.identification.PFQuestion;
import com.deutchall.identification.PFRanking;
import com.deutchall.identification.Question;
import com.deutchall.activities.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import android.graphics.Color;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class GameActivity extends Activity {
	
	public static final String TAG = "com.deutchall.activities.GameActivity";
	private final int timeOutValue = 8000;
	private final int second = 1000;
	private final int baseTimeAnswering = 700;
	private final int baseTimeTimeOut = 0;
	private final int multip = 10;
	private final int afterEvalTimeout = 1000;
	private final int goToNextTimeout = afterEvalTimeout + 600;
	
	private String name = null;
	private int gameId;
	private int score = 0;
	private int nQuestion = 0;
	private String result = "";
	private List<Question> questions;
	private int checkedRadioButton = -1;
	private int correctAns = -1;
	private Timer updateTimeout;
	private int remainingTime;
	private boolean called = false;
	
	private static final int N_ACCESS = 1;
	private final Semaphore lock = new Semaphore(N_ACCESS);
	
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
     
        name = getIntent().getStringExtra(UserActivity.USER);
        gameId = getIntent().getIntExtra(SelectGameActivity.GAME, -1);
        
        try {
			questions = PFQuestion.getGameQuestions(this, gameId);
		} catch (SQLiteException e) {
			Log.e(TAG, e.toString());
			cleanAndExit(true);
		} catch (IndexOutOfBoundsException e) {
			Log.e(TAG, e.toString());
			cleanAndExit(true);
		} catch (IOException e) {
			Log.e(TAG, e.toString());
			cleanAndExit(true);
		} catch (InvalidGameIdException e) {
			Log.e(TAG, e.toString());
			cleanAndExit(true);
		} catch (Exception e) {
			Log.e(TAG, e.toString());
			cleanAndExit(true);
		}
        
        txName = (TextView)findViewById(R.id.txName);
        txScore = (TextView)findViewById(R.id.txScore);
        txNQuestion = (TextView)findViewById(R.id.txNQuestion);
        txQuestion = (TextView)findViewById(R.id.txQuestion);
        txTime = (TextView)findViewById(R.id.txTime);
        rad0 = (Button)findViewById(R.id.radio0);
        rad1 = (Button)findViewById(R.id.radio1);
        rad2 = (Button)findViewById(R.id.radio2);
        txResult = (TextView)findViewById(R.id.txResult);
        
        txName.setText(name);
        setFullQuestionVisibility(View.GONE);
        
        setEnableAnswers(false);
        updateScore();
        launchNextQuestion();
    }
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	@Override
	public void onPause() {
		super.onPause();
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
	    	back();
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	public void resp0(View view) throws InterruptedException {
		((Button)findViewById(R.id.radio0)).setSelected(true);
		((Button)findViewById(R.id.radio1)).setSelected(false);
		((Button)findViewById(R.id.radio2)).setSelected(false);
		answer();
	}
	
	public void resp1(View view) throws InterruptedException {
		((Button)findViewById(R.id.radio0)).setSelected(false);
		((Button)findViewById(R.id.radio1)).setSelected(true);
		((Button)findViewById(R.id.radio2)).setSelected(false);
		answer();
	}
	
	public void resp2(View view) throws InterruptedException {
		((Button)findViewById(R.id.radio0)).setSelected(false);
		((Button)findViewById(R.id.radio1)).setSelected(false);
		((Button)findViewById(R.id.radio2)).setSelected(true);
		answer();
	}
	
	public void menu(View view) {
		back();
	}
			
	private void answer() throws InterruptedException {
		setEnableAnswers(false);
		cancelUpdateTime();
		checkedRadioButton = radChecked();
		
		launchCheckAnswer();
    }
			
	private void launchUpdateTimeout() {
		UpdateTimeoutRunnable utr = new UpdateTimeoutRunnable();
		RemainingTimeHandler.post(utr);
	}
	
	private void launchCheckAnswer() {
		try {
			lock.acquire();
		} catch (InterruptedException e) {
			Log.e(TAG, e.toString());
			ErrorLauncher.throwError(e.toString());
		}
		try {
			if (!called) {
				setCalled(true);
				CheckAnswerRunnable car = new CheckAnswerRunnable();
				timeOutHandler.post(car);
			}
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		} finally {
			lock.release();
		}
	}
	
	private void launchEval() {
		EvalRunnable evar = new EvalRunnable();
		timeHandler.post(evar);
	}
	
	private void launchHide() {
		HideButtonsRunnable hbr = new HideButtonsRunnable();
		timeHandler.post(hbr);
	}
	
	private void launchGoToNext() {
		NextRunnable nr = new NextRunnable();
		timeHandler.post(nr);
	}
		
	private void launchShowQuestionView(View view) {
		final ShowQuestionViewRunnable showRunnable = new ShowQuestionViewRunnable(view);
		animTimeHandler.post(showRunnable);
	}
	
	private void launchHideQuestionView(View view) {
		final HideQuestionViewRunnable hideRunnable = new HideQuestionViewRunnable(view);
		animTimeHandler.post(hideRunnable);
	}
	
	private class UpdateTimeoutRunnable implements Runnable {
		public void run() {
			txTime.setVisibility(View.GONE);
			if (remainingTime >= 0) {
				animTxTime(txTime, remainingTime);
			}
			if (remainingTime < 0) {
				setEnableAnswers(false);
				remainingTime = 0;
				cancelUpdateTime();
				launchCheckAnswer();
			} else {
				remainingTime --;
			}
		}
	};
	
	private class  EvalRunnable implements Runnable {
		public void run() {
			evaluate();
		}
	};
	
	private class CheckAnswerRunnable implements  Runnable {
		public void run() {
			setCorrectAns();
			timerEvaluationSequence();
		}
	};

	private class HideButtonsRunnable implements Runnable {
		public void run() {
			clearTxResult();
			hideFullQuestion();
		}
	};
	
	private class NextRunnable implements Runnable {
		public void run() {
			goToNext();
		}
	};

	private class ShowQuestionViewRunnable implements Runnable {
		View view;
		public ShowQuestionViewRunnable(View view) { this.view = view; }
		public void run() {
			animShowQuestionView(view);
		}
	}
	
	private class HideQuestionViewRunnable implements Runnable {
		View view;
		public HideQuestionViewRunnable(View view) { this.view = view; }
		public void run() {
			animHideQuestionView(view);
		}
	}
	
	private void timerUpdateTimeout() {
		updateTimeout = new Timer();
		UpdateTimeoutTask utt = new UpdateTimeoutTask();
		updateTimeout.schedule(utt, 0, second);
	}
	
	private class UpdateTimeoutTask extends TimerTask {
		@Override
		public void run() {
			launchUpdateTimeout();
		}
	}
	
	private void timerShowQuestionView(final View view, int miliseconds) {
		Timer showTimer = new Timer();
		ShowQuestionTask sqt = new ShowQuestionTask(view);
		showTimer.schedule(sqt, miliseconds);
	}
	
	private class ShowQuestionTask extends TimerTask {
		private View view;
		public ShowQuestionTask(View view) { this.view = view; }
		@Override
		public void run() {
			launchShowQuestionView(view);
		}
	}
	
	private void timerHideQuestionView(final View view, int miliseconds) {
		Timer hideTimer = new Timer();
		HideQuestionTask hqt = new HideQuestionTask(view);
		hideTimer.schedule(hqt, miliseconds);
	}
	
	private class HideQuestionTask extends TimerTask {
		private View view;
		public HideQuestionTask(View view) { this.view = view; }
		@Override
		public void run() {
			launchHideQuestionView(view);
		}
	}

	private void timerEvaluationSequence() {
		Timer ansTimer = new Timer();
		int baseTime = getBaseTime();
		
		EvaluationTask evt = new EvaluationTask();
		HideControlsTask hct = new HideControlsTask();
		GoToNextTask gnt = new GoToNextTask();
		
		ansTimer.schedule(evt, baseTime);
		ansTimer.schedule(hct, baseTime + afterEvalTimeout);
		ansTimer.schedule(gnt, baseTime + goToNextTimeout );
	}
	
	private class EvaluationTask extends TimerTask {
		@Override
		public void run() {
			launchEval();
		}
	}
	
	private class HideControlsTask extends TimerTask {
		@Override
		public void run() {
			launchHide();
		}
	}
	
	private class GoToNextTask extends TimerTask {
		@Override
		public void run() {
			launchGoToNext();
		}
	}
		
	private void cancelUpdateTime() {
		updateTimeout.cancel();
		updateTimeout.purge();
	}
	
	private void setCorrectAns() {
		correctAns  = questions.get(nQuestion).getCorrect();
	}
	
	private void evaluate() {	
		boolean success = evaluateAnswer();
		showResult(success);
		if (success) {
			incScore();
		}
		incQuestionIndex();
	}
	
	private boolean evaluateAnswer() {
		boolean success = checkedRadioButton == correctAns;
		return success;
	}
	
	private void showResult(boolean success) {
		result = getResult(success);
		txTime.setVisibility(View.VISIBLE);
		drawCorrectAnswer(); 
		txResult.setText(result);
		
		if (success) {
			txResult.setTextColor(Color.GREEN);
		} else {
			drawWrongAnswer();
			txResult.setTextColor(Color.RED);
		}
		animScaleView(txResult);
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
		int rem = Integer.parseInt(txTime.getText().toString());
		score = score + (multip * (++ rem));
		updateScore();
	}

	private void incQuestionIndex() {
		nQuestion ++;
	}

	private String getResult(boolean success) {
		if (success) {
			return "Richtig!";
		} else {
			if (checkedRadioButton == -1)  {
				return "Time Out!";
			} else {
				return "Falsch!";
			}
		}
	}
	
	private void goToNext() {	
		if (isEnd()) {
			rankingAndClose();
		} else {
			launchNextQuestion();	
		}
	}
			
	private boolean isEnd() {
		if (nQuestion > questions.size() - 1) {
			nQuestion = questions.size() - 1;
			return true;			
		} else {
			return false;
		}
	}
	
	private void rankingAndClose() {
		try {
			String date = getDate();
			PFRanking.insertIntoRankingGame(this, gameId, name, date, score);
			gameover();
		} catch (SQLiteException e) {
			Log.e(TAG, e.toString());
			cleanAndExit(true);
		} catch (IndexOutOfBoundsException e) {
			Log.e(TAG, e.toString());
			cleanAndExit(true);
		} catch (IOException e) {
			Log.e(TAG, e.toString());
			cleanAndExit(true);
		} catch (InvalidGameIdException e) {
			Log.e(TAG, e.toString());
			cleanAndExit(true);
		} catch (Exception e) {
			Log.e(TAG, e.toString());
			cleanAndExit(true);
		}
	}
			
	private void launchNextQuestion() {
		unpressRads();
		unselectRads();
		setButtonsDefaultStyle();
		updateGameStatus();
		restoreTimeRemaining();
    	writeAnswers();
    	showFullQuestion();
    	setEnableAnswers(true);
    	setCalled(false);
    	timerUpdateTimeout();
	}
	
	private void showFullQuestion() {
		timerShowQuestionView(txQuestion, 0);
		timerShowQuestionView(rad0, 100);
		timerShowQuestionView(rad1, 200);
		timerShowQuestionView(rad2, 300);	
	}
	
	private void hideFullQuestion() {
		timerHideQuestionView(rad2, 0);
		timerHideQuestionView(rad1, 100);
		timerHideQuestionView(rad0, 200);
		timerHideQuestionView(txQuestion, 300);
	}
		
	private void unpressRads() {
		rad0.setPressed(false);
		rad1.setPressed(false);
		rad2.setPressed(false);
	}
	
	private void unselectRads() {
		rad0.setSelected(false);
		rad1.setSelected(false);
		rad2.setSelected(false);
		checkedRadioButton = -1;
	}
	
	private int radChecked() {
		if (rad0.isSelected()) {
			return 1;
		} else if(rad1.isSelected()) {
			return 2;
		} else if(rad2.isSelected()) {
			return 3;
		} else {
			return -1;
		}
	}
	
	private void setCalled(boolean value) {
		called = value;
	}
	
	private void setButtonsDefaultStyle() {
		rad0.setBackgroundResource(R.drawable.button_custom);
		rad1.setBackgroundResource(R.drawable.button_custom);
		rad2.setBackgroundResource(R.drawable.button_custom);
	}
		
	private void updateGameStatus() {
        txNQuestion.setText("Question: "+ (nQuestion + 1));
        txQuestion.setText(questions.get(nQuestion).getHeading());
	}
	
	private void updateScore() {
		txScore.setText("Score: " + score);
	}
	
	private void writeAnswers() {
		String ans1 = questions.get(nQuestion).getAns1();
		String ans2 = questions.get(nQuestion).getAns2();
		String ans3 = questions.get(nQuestion).getAns3();
		rad0.setText(ans1);
		rad1.setText(ans2);
		rad2.setText(ans3);
	}
	
	private void restoreTimeRemaining() {
		remainingTime = timeOutValue / 1000;
	}
	
	private int getBaseTime() {
		if (checkedRadioButton == -1) {
			return baseTimeTimeOut;
		} else {
			return baseTimeAnswering;
		}
	}

	private String getDate() {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm", Locale.US);
		return dateFormat.format(new Date()).toString();
	}
					
	private void clearTxResult() {
		txResult.setVisibility(View.GONE);
		txResult.setText("");
	}
	
	private void setEnableAnswers(boolean enabled) {
		rad0.setEnabled(enabled);
		rad1.setEnabled(enabled);
		rad2.setEnabled(enabled);
	}

	private void setFullQuestionVisibility(int visib) {
		rad0.setVisibility(visib);
		rad1.setVisibility(visib);
		rad2.setVisibility(visib);
	}
	
	private void drawCorrectAnswer() {
		switch (correctAns) {	
		case 1:	
			rad0.setBackgroundResource(R.drawable.button_correct);	
			break;	
		case 2:	
			rad1.setBackgroundResource(R.drawable.button_correct);
			break;
		case 3:
			rad2.setBackgroundResource(R.drawable.button_correct);
			break;
		default:
			ErrorLauncher.throwError("Correct answer coherence error");
		}	
	}
	
	private void drawWrongAnswer() {
		switch (checkedRadioButton) {
		case 1:
			rad0.setBackgroundResource(R.drawable.button_wrong);
			break;
		case 2:
			rad1.setBackgroundResource(R.drawable.button_wrong);
			break;
		case 3:
			rad2.setBackgroundResource(R.drawable.button_wrong);
			break;
		default:
			break; // Timeout !!!
		}
	}
	
	private void gameover() {
		toastMsg(result + " Congratulations, test finished!" + Finals.newLine + "Let's see your Ranking position...");
		end();
	}
	
	private void end() {
		Intent intent = new Intent(this, RankingActivity.class);
		intent.putExtra(SelectGameActivity.GAME, gameId);
		startActivity(intent);
		cleanAndExit(false);
	}
	
	private void cleanAndExit(boolean error) {
		cancelUpdateTime();
		if(error) {
			ErrorLauncher.throwError("Exception caugth at game runtime");
		}
		GameActivity.this.finish();
	}
		
	private void back() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("¿Are you sure you want to come back to main menu?")
		       .setCancelable(false)
		       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		    	   public void onClick(DialogInterface dialog, int id) {
		        	   cleanAndExit(false);
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
	
	private void toastMsg(String msg) {
		Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
	}
}
