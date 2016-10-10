package map;

import java.awt.Graphics;
import java.io.IOException;
import java.util.ArrayList;

import scrapper.Scrapper;
import scrapper.returnType;
import data.Schedule;

public class Line {

	double cx, cy, indefWidth, width;
	Scrapper scrapper;
	ArrayList<Station> line;
	String name;
	String color;
	String time;
	String seconds;
	String directionBound;
	boolean inactive;
	public Schedule schedule;
	
	public Line(double cx, double cy, double width, String name, String color, Schedule schedule, String startTime, String seconds){
		this.cx = cx;
		this.cy = cy;
		this.indefWidth = width;
		this.width = width;
		this.name = name;
		this.color = color;
		this.schedule = schedule;
		this.time = startTime;
		this.seconds = seconds;
		this.inactive = false;
		this.scrapper = new Scrapper();
		//Line
		line = new ArrayList<Station>();
		for(int i = 0; i < schedule.getStops().size(); i++){
			line.add(new Station(schedule.getXCoords().get(i), schedule.getYCoords().get(i), this.indefWidth, 2, this.cx, this.cy, i, schedule.getNames().get(i), "red", this.time, schedule.getSchedule(i), this));
		}
		this.directionBound = line.get(line.size()-1).getName();
		for(int i = 0; i < line.size(); i++){
			line.get(i).init();
		}
	}
	
	public void tick(){

		for(int i = 0; i < line.size(); i++){line.get(i).tick();}
	}
	
	public void paint(Graphics g){
		
		for(int i = 0; i < line.size(); i++){line.get(i).paint(g);}
		
	}

	//Methods
	
	public void resetStations(){
		for(int i = 0; i < line.size(); i++){
			line.get(i).setCx(this.cx);
			line.get(i).setCy(this.cy);
			line.get(i).setWidth(this.width);
		}
	}
	public ArrayList<Station> getLine(){
		return this.line;
	}	
	public int getLength(){return schedule.getStops().size();}
	public ArrayList<String> getSchedule(int place){
		if(line.get(place).isActive()){
			return schedule.getSchedule(place);
		}else{
			ArrayList<String> newSchedule = new ArrayList<String>();
			for(int i = 0; i < schedule.getSchedule(place).size(); i++){
				newSchedule.add("skip");
			}
			return newSchedule;
		}
	}
	public String getTime(int place, int time){
		if(line.get(place).isActive())
			return schedule.getTime(place, time);
		else
			return "skip";
	}
	public double getX(int station){
		return schedule.getXCoords().get(station);
	}
	public double getY(int station){
		return schedule.getYCoords().get(station);
	}
	public void receiveUpdates(String date){
		returnType data = new returnType(false);;
		try {
			data = scrapper.getHTML(name, date);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Received Updates");
		if(data.isAll()){
			this.inactive = true;
			for(int i = 0; i < line.size(); i++){
				line.get(i).clearSubways();
			}
			System.out.println("inactive = "+inactive);
		}else if(data.isNoChange() == false){
			if(this.directionBound.contains(data.getDirection())){
				System.out.println("DELAYS");
				System.out.println(this.directionBound);
				System.out.println(data.getSkips());
				for(int i = 0; i < line.size(); i++){
					for(int d = 0; d < data.getSkips().size(); d++){
						if(line.get(i).getName().contains(data.getSkips().get(d))){
							line.get(i).setInactive();
						}
					}
				}
			}
		}else{
			for(int i = 0; i < line.size(); i++){
				line.get(i).setActive();
			}
		}
		
	}
	
	//Getters and Setters
	public double getCx(){return this.cx;}
	public double getCy(){return this.cy;}
	public String getName(){return this.name;}
	public double getWidth(){return this.width;}
	public double getIndefWidth(){return this.indefWidth;}
	public String getTime(){return this.time;}
	public String getSeconds(){return this.seconds;}
	
	public void setSeconds(String seconds){this.seconds = seconds;}
	public void setCx(double cx){this.cx = cx;}
	public void setCy(double cy){this.cy = cy;}
	public void setWidth(double width){this.width = width;}
	public void setTime(String time){
		this.time = time;
		for(int i = 0; i < line.size(); i++){
			line.get(i).setTime(time);
		}
	}
	public void checkHUD(int mx, int my){
		for(int i = 0; i < line.size(); i++){
			line.get(i).checkHUD(mx, my);
		}
	}
	public String getDirectionBound(){return this.directionBound;}
	public boolean isInactive(){return this.inactive;}
	public Schedule getSchedule(){return this.schedule;}
}
