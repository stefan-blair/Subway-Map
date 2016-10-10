package map;

import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import scrapper.returnType;
import data.Schedule;

public class Map {

	//Variables
	private double x, y, width;
	boolean isTime;
	String time;
	//Lines
	ArrayList<Line> lines;
	//Images
	Image submap;	
	ImageIcon i;
	public Map(String startTime, String seconds, String date, int day){
		x = 0;
		y = 0;
		width = 200;
		time = startTime;
		i = new ImageIcon(getClass().getResource("/images/subwayimage.png"));
		this.submap = i.getImage();
		isTime = false;
		lines = new ArrayList<Line>();
		lines.add(new Line(x, y, width, "1", "red", new Schedule(day, "1_0"), startTime, seconds));
		lines.add(new Line(x, y, width, "1", "red", new Schedule(day, "1_1"), startTime, seconds));
		lines.add(new Line(x, y, width, "2", "red", new Schedule(day, "2_0"), startTime, seconds));
		lines.add(new Line(x, y, width, "2", "red", new Schedule(day, "2_1"), startTime, seconds));
		lines.add(new Line(x, y, width, "3", "red", new Schedule(day, "3_0"), startTime, seconds));
		lines.add(new Line(x, y, width, "3", "red", new Schedule(day, "3_1"), startTime, seconds));

		
		for(int x = 0; x < lines.size(); x++){
			lines.get(x).receiveUpdates(date);
		}
		
	}
	
	public void tick(){
		
		for(int i = 0; i < lines.size(); i++){
			if(this.lines.get(i).isInactive() == false)lines.get(i).tick();
		}
	
	}
	
	public void paint(Graphics g){
		g.drawImage(submap, (int)x, (int)y, (int)width, (int)(width*1.207), null);
		for(int i = 0; i < lines.size(); i++){
			if(this.lines.get(i).isInactive() == false)lines.get(i).paint(g);
		}
	}
	
	public void reSetSubways(){		
		for(int i = 0; i < lines.size(); i++){
			if(this.lines.get(i).isInactive() == false){
				lines.get(i).setWidth(width);
				lines.get(i).setCx(x);
				lines.get(i).setCy(y);
				lines.get(i).resetStations();
			}
		}
	}
	
	public void callHUD(int mx, int my){
		for(int i = 0; i < lines.size(); i++){
			if(this.lines.get(i).isInactive() == false)lines.get(i).checkHUD(mx, my);
		}
	}
	
	public void receiveData(String date){
		for(int i = 0; i < lines.size(); i++){
			lines.get(i).receiveUpdates(date);
		}
	}
	
	//Getters and Setters
	public double getX(){return this.x;}
	public double getY(){return this.y;}
	public double getWidth(){return this.width;}
	public String getTime(){return this.time;}
//	public double getHeight(){return this.height;}
	
	public void setX(double x){this.x = x;}
	public void setY(double y){this.y = y;}
	public void setWidth(double width){this.width = width;}
	public void isTime(boolean isTime){this.isTime = isTime;}
	public void setTime(String time){
		this.time = time;
		for(int i = 0; i < lines.size(); i++)if(this.lines.get(i).isInactive() == false)this.lines.get(i).setTime(time);
	}
	public double getHeight() {return this.width*1.207;}
	
}
