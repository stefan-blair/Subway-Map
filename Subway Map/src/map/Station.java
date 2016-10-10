package map;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;

import data.BypassData;
import data.HUDinfo;

public class Station {

	BypassData bypassData;
	Line line;
	String name, color;
	String time;
	String spawnedTime;
	int place, hour, minute, seconds, timer, departHour, departMinute, byPassIndex;
	double x, y, indefWidth, width, radius, cx, cy;
	boolean spawned, endLine, active, openingMinute, byPassing;
	//Arrays
	ArrayList<Subway>subways;
	ArrayList<String> times;
	
	public Station(double x,double  y,double  width,double  radius,double  cx,double  cy,int  place, String name, String color, String time, ArrayList<String> times, Line line){
		this.width = width;
		this.indefWidth = width;
		this.x = x/width;
		this.y = y/width;
		this.radius = radius/width;
		this.cx = cx;
		this.cy = cy;
		this.place = place;
		this.times = times;
		this.name = name;
		this.color = color;
		this.time = time;
		this.line = line;
		this.active = true;
		this.openingMinute = true;
		this.byPassing = false;
		
		for(int i = 0; i < line.getSchedule().getBypassData().size(); i++){
			if(line.getSchedule().getBypassData().get(i).getStartStop() == this.place+1){
				this.bypassData = line.getSchedule().getBypassData().get(i);
				this.byPassIndex = i;
			}
		}
		
		spawned = true;
		spawnedTime = "";
		subways = new ArrayList<Subway>();
		timer = 0;
		seconds = 0;
		if(!(place == line.getLength()-1)){
			endLine = false;
		}
		else endLine = true;
	}
	public void init(){
		if(!endLine){
			try{
				startTime(this.time);
			}catch(Exception e){
//				System.out.println("Couldnt complete startime for line");
			}
		}
		byPassingCheck();
	}
	public void tick(){
		
		if(!spawned){
			if(getTimeDifference(spawnedTime, time) == 1){
				spawned = true;
				byPassingCheck();
				if(openingMinute){
					line.setSeconds("0");
					openingMinute = false;
				}
			}
		}
		if(!endLine && active){
			for(int i = 0; i < times.size(); i++){
				if(getTimeDifference(this.time, times.get(i)) == 0){
					if(spawned){
						if(!line.getTime(place+1, i).equals("skip") && !line.getTime(place+1, i).equals("") && byPassing != true){
							subways.add(new Subway(line.getX(place), line.getY(place), line.getX(place+1), line.getY(place+1), (getTimeDifference(this.times.get(i), line.getTime(place+1, i)))*60, cx, cy, width, indefWidth, 4, new HUDinfo(i, this.line, place, 0, line.getSeconds(), Integer.valueOf(line.getName()), this.byPassing, this.byPassIndex)));
						}else{
							boolean skipping = true;
							int g = 2;
							while(skipping){
								if(!line.getTime(place+g, i).equals("skip") && !line.getTime(place+g, i).equals("")){
									if(!byPassing||this.bypassData.getTargetStop() == place+g+1||this.bypassData == null){
										subways.add(new Subway(line.getX(place), line.getY(place), line.getX(place+g), line.getY(place+g), (getTimeDifference(this.times.get(i), line.getTime(place+g, i)))*60, cx, cy, width, indefWidth, 4, new HUDinfo(i, this.line, place, 0, line.getSeconds(), Integer.valueOf(line.getName()),this.byPassing, this.byPassIndex)));
										skipping = false;
									}else{
										g++;
										if(place+g == line.getLength()-1){
											skipping = false;
										}	
									}
								}else{
									g++;
									if(place+g == line.getLength()-1){
										skipping = false;
									}
								}
							}
							
						}
						if(this.place > 0)this.line.getLine().get(place-1).destroyInactive();
						spawned = false;
						spawnedTime = time;
					}
				}
			}
		}
		for(int i = 0; i < subways.size(); i++){subways.get(i).tick();}
		for(Iterator<Subway> subIt = subways.iterator(); subIt.hasNext();){if(!subIt.next().isActive())subIt.remove();}
	}
	
	public void paint(Graphics g){
//		g.setColor(Color.black);
		for(int i = 0; i < subways.size(); i++){subways.get(i).paint(g);}
	}
	
	public void byPassingCheck(){
		if(this.bypassData != null){
			if(getTimeNumber(this.bypassData.getStartTime()) < getTimeNumber(this.time) && getTimeNumber(this.bypassData.getEndTime()) > getTimeNumber(this.time)){
				this.byPassing = true;
				for(int i = bypassData.getStartStop()+1; i < bypassData.getTargetStop(); i++){
					line.getLine().get(i-1).setInactive();
				}
				System.out.println("BYPASSING");
			}else{
				this.byPassing = false;
				for(int i = bypassData.getStartStop()+1; i < bypassData.getTargetStop(); i++){
					line.getLine().get(i-1).setActive();
				}
			}
		}
	}
	
	public int getTimeDifference(String a, String b){
		try{
			String[] init = a.split(":");
			String[] fin = b.split(":");
			return (((Integer.valueOf(fin[0])*60)+(Integer.valueOf(fin[1]))-((Integer.valueOf(init[0])*60)+(Integer.valueOf(init[1])))));
		}catch(NumberFormatException e){
			return 1000000000;
		}
	}
	
	public int getTimeNumber(String a){
		String[] time = a.split(":");
		return (Integer.valueOf(time[0])*60)+(Integer.valueOf(time[1]));
	}
	
	public int getClosestTimePos(String time){
		int lowestTime = 10000;
		int lowestValue = 1000;
		int dif;
		for(int i = 0; i < times.size(); i++){
			if(!line.getSchedule(place).get(i).equals("skip") && !line.getSchedule(place).get(i).equals("")){
				dif = getTimeDifference(line.getSchedule(place).get(i), time);
				if(dif >= 0 && dif < lowestValue){
					lowestTime = i;
					lowestValue = dif;
				}
			}
		}
		return lowestTime;
	}
	
	public void startTime(String time){
		int pos = getClosestTimePos(time);
		double percentThrough = 0;
		boolean skipping = true;
		int nextStop = 1;
		double mainDif = 0;
		double curDif = 0;
		double iX = 0;
		double iY = 0;
		while(skipping){
			if(!line.getTime(place+nextStop, pos).equals("skip") && !line.getTime(place+nextStop, pos).equals("")){
				if(!byPassing || this.bypassData.getTargetStop() == place+nextStop+1||this.bypassData == null){
					mainDif = getTimeDifference(times.get(pos), line.getSchedule(place+nextStop).get(pos));
					curDif = mainDif-getTimeDifference(time, line.getSchedule(place+nextStop).get(pos));
					percentThrough = curDif/mainDif;
					iX = line.getX(place)+((line.getX(place+nextStop)-line.getX(place))*percentThrough);
					iY = line.getY(place)+((line.getY(place+nextStop)-line.getY(place))*percentThrough);
					skipping = false;
				}else{
					nextStop++;
				}
			}else{
				nextStop++;
			}
		}
		if(!(percentThrough == 0)){
			subways.add(new Subway(iX, iY, line.getX(place+nextStop), line.getY(place+nextStop), (getTimeDifference(time, line.getSchedule(place+nextStop).get(pos)))*60, cx, cy, width, indefWidth, 4, new HUDinfo(pos, this.line, place, percentThrough, line.getSeconds(), Integer.valueOf(line.getName()),this.byPassing, this.byPassIndex)));	
		}
	}
	
	public void checkHUD(int mx, int my){
		for(int i = 0; i < subways.size(); i++){
			subways.get(i).checkHUD(mx, my);
		}
	}
	
	public void clearSubways(){
		this.subways.clear();
	}
	
	public void destroyInactive(){
		for(int i = 0; i < subways.size(); i++){
			if(subways.get(i).isRunning()==false){
				subways.remove(i);
			}
		}
	}
	public void setActive(){
		this.active = true;
	}
	public void setInactive(){
		this.active = false;
		clearSubways();
	}

	//Getters and Setters
	public boolean isActive(){return this.active;}
	public String getTime(){return this.time;}
	public void setTime(String time){
		this.time = time;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPlace() {
		return place;
	}

	public void setPlace(int place) {
		this.place = place;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	public int getSeconds() {
		return seconds;
	}

	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}

	public int getTimer() {
		return timer;
	}

	public void setTimer(int timer) {
		this.timer = timer;
	}

	public int getDepartHour() {
		return departHour;
	}

	public void setDepartHour(int departHour) {
		this.departHour = departHour;
	}

	public int getDepartMinute() {
		return departMinute;
	}

	public void setDepartMinute(int departMinute) {
		this.departMinute = departMinute;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
		for(int i = 0; i < subways.size(); i++){
			subways.get(i).setCy(y);
		}
	}

	public double getIndefWidth() {
		return indefWidth;
	}

	public void setIndefWidth(double indefWidth) {
		this.indefWidth = indefWidth;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
		for(int i = 0; i < subways.size(); i++){
			subways.get(i).setWidth(width);
		}
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public double getCx() {
		return cx;
	}

	public void setCx(double cx) {
		this.cx = cx;
		for(int i = 0; i < subways.size(); i++){
			subways.get(i).setCx(cx);
		}
	}

	public double getCy() {
		return cy;
	}

	public void setCy(double cy) {
		this.cy = cy;
		for(int i = 0; i < subways.size(); i++){
			subways.get(i).setCy(cy);
		}
	}

	public ArrayList<Subway> getSubways() {
		return subways;
	}

	public void setSubways(ArrayList<Subway> subways) {
		this.subways = subways;
	}

	public ArrayList<String> getTimes() {
		return times;
	}

	public void setTimes(ArrayList<String> times) {
		this.times = times;
	}
	
}
