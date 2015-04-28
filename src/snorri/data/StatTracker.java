package snorri.data;

public class StatTracker {

	private double work;
	private int time;
	
	public StatTracker() {
		time = 0;
	}
	
	public void setWork(double initialWork) {
		work = initialWork;
	}
	
	public void doWork(double workTerm) {
		work += workTerm;
	}
	
	public void tick() {
		time++;
	}
	
	public double getWork() {
		return work;
	}
	
	public int getTime() {
		return time;
	}
	
	public double getPower() {
		return work / time;
	}
	
}
