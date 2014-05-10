package com.deutchall.utilities;

public class Timer extends Thread {
	
	private int remaining;
	
	public Timer(int miliseconds) {
		this.remaining = miliseconds;
	}
	
	public void run() {
		
		while(remaining > 0) {
			
			try {
				sleep(1); // Wait for 1000 miliseconds
				this.remaining = this.remaining - 1;	
			} 
			catch (InterruptedException e) {
				throw new Error("Interrupted Exception");
			} 
		}
	}
	
	public int getRemainingTime() {
		return remaining;
	}
	
	public void shutdown()
	{
		
	}
}
