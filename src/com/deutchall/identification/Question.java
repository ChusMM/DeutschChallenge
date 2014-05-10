package com.deutchall.identification;

public class Question {
	
	private String question;
	private String ans1;
	private String ans2;
	private String ans3;
	private int correct;
	
	public Question(String question, String ans1, String ans2, String ans3, int correct) {
		
		this.question = question;
		this.ans1 = ans1;
		this.ans2 = ans2;
		this.ans3 = ans3;
		this.correct = correct;
	}
	
	public String getQuestion() {
		return this.question;
	}
	
	public String getAns1() {
		return this.ans1;
	}
	
	public String getAns2() {
		return this.ans2;
	}
	
	public String getAns3() {
		return this.ans3;
	}
	
	public int getCorrect() {
		return this.correct;
	}
	
	public void setQuestion(String question) {
		this.question = question;
	}
	
	public void setAns1(String ans1) {
		this.ans1 = ans1;
	}
	
	public void setAns2(String ans2) {
		this.ans1 = ans2;
	}
	
	public void setAns3(String ans3) {
		this.ans1 = ans3;
	}
	
	public void setCorrect(int correct) {
		this.correct = correct;
	}
	
	@Override
	public boolean equals(Object o) {
		
		if(o instanceof Question) {
			
			Question aux = (Question) o;
			return this.question.equals(aux.question);
		} 
		else {
			return false;
		}
	}
}
