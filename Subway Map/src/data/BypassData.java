package data;

import java.util.ArrayList;

public class BypassData {

	String startTime, endTime;
	int startStop, targetStop;
	ArrayList<Coordinate>subCoords;
	
	public BypassData(String startTime, String endTime, int startStop, int targetStop, ArrayList<Coordinate>subCoords){
		this.startTime = startTime;
		this.endTime = endTime;
		this.targetStop = targetStop;
		this.startStop = startStop;
		this.subCoords = subCoords;
	}
	
	public String getStartTime(){return this.startTime;}
	public String getEndTime(){return this.endTime;}
	public int getTargetStop(){return this.targetStop;}
	public int getStartStop(){return this.startStop;}
	public ArrayList<Coordinate>getSubCoords(){return this.subCoords;}
	
}
