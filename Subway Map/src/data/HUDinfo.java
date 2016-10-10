package data;

import java.awt.Image;
import java.util.ArrayList;
import java.util.zip.ZipException;

import javax.swing.ImageIcon;

import map.Line;
import map.Subway;

public class HUDinfo {

	int place, stopNumber, byPassIndex;
	double percentThrough;
	Line line;
	boolean startTime, byPassing;
	String seconds;
	
	Image icon;
	public HUDinfo(int place, Line line, int stopNumber, double percentThrough, String seconds, int number, boolean byPassing, int byPassingIndex){
		this.place = place;
		this.line = line;
		this.stopNumber = stopNumber;
		this.byPassing = byPassing;
		this.byPassIndex = byPassingIndex;
		this.percentThrough = percentThrough;
		if(percentThrough > 0)this.startTime = true;
		else this.startTime = false;
		this.seconds = seconds;
		ImageIcon i = new ImageIcon(getClass().getResource("/images/"+String.valueOf(number)+".png"));
		icon = i.getImage();		
		if(byPassing)System.out.println("THIS SUBWAY IS BYPASSING AT "+stopNumber);
	}
	
	public String getTime(int stop){
		if(line.getLine().get(stop).isActive())
			return this.line.schedule.getSchedule(stop).get(place);
		else
			return "skip";
	}
	
	public Coordinate getCoord(int stop){
		return this.line.schedule.getCoordinates().get(stop);
	}
	public ArrayList<Coordinate> getSubCoordinate(int stop){
		
		if(this.byPassing){
			try{
				System.out.println(this.byPassIndex);
				ArrayList<Coordinate> subCoords = this.line.getSchedule().getBypassData().get(byPassIndex).getSubCoords();

				boolean skipping = true;
				int g = 1;
				while(skipping){
					if(!line.getTime(stop+g, place).equals("skip")){
						skipping = false;
					}else{
						subCoords.addAll(line.schedule.getSubCoordinates().get(stop+g));
						g++;
					}
				}
				return subCoords;
			}catch(IndexOutOfBoundsException e){
				return this.line.schedule.getSubCoordinates().get(stop);
			}
		}else{
			try{
				ArrayList<Coordinate> subCoords = this.line.getSchedule().getSubCoordinates().get(stop);

				boolean skipping = true;
				int g = 1;
				while(skipping){
					if(!line.getTime(stop+g, place).equals("skip")){
						skipping = false;
					}else{
						subCoords.addAll(line.schedule.getSubCoordinates().get(stop+g));
						g++;
					}
				}
				return subCoords;
			}catch(IndexOutOfBoundsException e){
				return this.line.schedule.getSubCoordinates().get(stop);
			}
		}

	}
	public boolean getStartTime(){return this.startTime;}
	public double getPercentThrough(){return this.percentThrough;}
	public int getLineLength(){return this.line.schedule.getCoordinates().size();}
	public int getStopNumber(){return this.stopNumber;}
	public int getSeconds(){return Integer.valueOf(this.seconds);}
	public Image getIcon(){return this.icon;}
	
}
