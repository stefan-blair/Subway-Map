package map;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

import data.Coordinate;
import data.HUDinfo;

public class Subway {

	ArrayList<Coordinate> subCoordinates, subCoordPercents;
	double x, y, ix, iy, fx, fy, cx, cy, radius, indefWidth, width, rateX, rateY, totalDistance, baseDistance;
	int seconds, timer, passed, currentCoord;
	boolean running, active, HUDactive, subCoordPresent, switchCoord;
	HUDinfo hudinfo;
	Image icon;
	
	public Subway(double ix, double iy, double fx, double fy, int seconds, double cx, double cy, double width, double indefWidth, double radius, HUDinfo hudinfo){
		this.width = width;
		this.indefWidth = indefWidth;
		this.radius = 1/indefWidth;
		this.x = ix/indefWidth;
		this.y = iy/indefWidth;
		this.ix = ix/indefWidth;
		this.iy = iy/indefWidth;
		this.fx = fx/indefWidth;
		this.fy = fy/indefWidth;
		this.cx = cx;
		this.cy = cy;
		this.HUDactive = false;
		this.seconds = seconds-20;
		this.hudinfo = hudinfo;
		this.currentCoord = 0;
		this.switchCoord = true;
		timer = 0;
		passed = 0;
		running = true;
		active = true;
		//Rate
		this.subCoordinates = hudinfo.getSubCoordinate(hudinfo.getStopNumber()+1);
		
		if(hudinfo.getSeconds() <40)this.seconds-=hudinfo.getSeconds();
		else this.seconds-=40;
		
		if(this.subCoordinates != null && this.subCoordinates.size() > 0){
			subCoordPresent = true;
			if(hudinfo.getStartTime()){
				int percent = (int)(hudinfo.getPercentThrough()*10);
				this.ix = (this.subCoordinates.get(percent-1).getX())/this.indefWidth;
				this.iy = (this.subCoordinates.get(percent-1).getY())/this.indefWidth;
				this.x = this.ix;
				this.y = this.iy;
				for(int i = 0; i < percent; i++){
					this.subCoordinates.remove(0);
				}
			}
						
			totalDistance = getDistance(this.ix, (this.subCoordinates.get(0).getX()/indefWidth), this.iy, (this.subCoordinates.get(0).getY()/indefWidth));
			totalDistance += getDistance((this.subCoordinates.get(subCoordinates.size()-1).getX()/indefWidth), this.fx,  (this.subCoordinates.get(subCoordinates.size()-1).getY()/indefWidth), this.fy);
			
			for(int i = 0; i < subCoordinates.size()-1; i++){
				double ixc = subCoordinates.get(i).getX()/indefWidth;
				double fxc = subCoordinates.get(i+1).getX()/indefWidth;
				double iyc = subCoordinates.get(i).getY()/indefWidth;
				double fyc = subCoordinates.get(i+1).getY()/indefWidth;
				totalDistance += getDistance(ixc, fxc, iyc, fyc);
			}
			baseDistance = getDistance(this.ix, (this.subCoordinates.get(0).getX()/indefWidth), this.iy, (this.subCoordinates.get(0).getY()/indefWidth));			
			rateX = -1*((this.ix - this.subCoordinates.get(0).getX()/indefWidth)/((this.seconds*60)*(baseDistance/totalDistance)));
			rateY = -1*((this.iy - this.subCoordinates.get(0).getY()/indefWidth)/((this.seconds*60)*(baseDistance/totalDistance)));
						
		}else{
			this.subCoordPresent = false;
			rateX = -1*((this.ix - this.fx)/(this.seconds*60));
			rateY = -1*((this.iy - this.fy)/(this.seconds*60));
		}		
		
		if(seconds <= 0){
			active = false;
			running = false;
		}
		
		this.icon = hudinfo.getIcon();
	}
	
	public void tick(){
		if(running){
			if(passed >= seconds){
				running = false;
				passed = 0;
				timer = 0;
			}
			this.x+=rateX;
			this.y+=rateY;
			if(subCoordPresent && subCoordinates.size() > 0){
				if(((subCoordinates.get(0).getX()-.05) < (x*indefWidth))&&((subCoordinates.get(0).getX()+.05) > (x*indefWidth))){
					if(((subCoordinates.get(0).getY()-.05) < (y*indefWidth))&&((subCoordinates.get(0).getY()+.05) > (y*indefWidth))){
						subCoordinates.remove(0);
						reSetRate(currentCoord);
					}
				}
			}

			timer++;
			if(timer>=60){timer = 0;passed++;}
		}else{
			timer++;
			if(timer >= (60*60)){
				active = false;
			}
		}
	}
	
	public void paint(Graphics g){
		g.drawImage(icon,(int)(cx+(x*width)), (int)(cy+(y*width)), (int)(radius*width), (int)(radius*width), null);
		//((int)(cx+(x*width)), (int)(cy+(y*width)), (int)(radius*(width/1.5)), (int)(radius*(width/1.5)));
//		g.drawString(String.valueOf(hudinfo.getStopNumber()+1), (int)(cx+(x*width)), (int)(cy+(y*width)));
		if(HUDactive)HUD(g);
	}
	
	public void HUD(Graphics g){
		g.setFont(new Font("Arial", Font.BOLD, 16));
		g.drawImage(icon,(int)(cx+(x*width-(radius*width/2))), (int)(cy+(y*width-(radius*width/2))), (int)(radius*width*2), (int)(radius*width*2), null);
		for (int i = 0; i < this.hudinfo.getLineLength(); i++){
			int xPos = (int)(cx+(this.hudinfo.getCoord(i).getX()/indefWidth*width));
			int yPos = (int)(cy+(this.hudinfo.getCoord(i).getY()/indefWidth*width));
			g.setColor(Color.white);
			g.fillRect(xPos, yPos, 50, 30);
			g.setColor(Color.black);
			try{g.drawString(this.hudinfo.getTime(i), xPos+5, yPos+20);}catch(IndexOutOfBoundsException e){}
		}
		
	}
	
	public void checkHUD(int mx, int my){
		if(my >= (cy+(y*width)) && my <= (cy+(y*width))+(radius*(width/1.5))){	
			if(mx > (cx+(x*width)) && mx <= (cx+(x*width))+(radius*(width/1.5))){
				HUDactive = true;
			}
		}else HUDactive = false;
	}
	
	public void reSetRate(int C){
		try{
			double baseDistance = getDistance(this.x, (this.subCoordinates.get(0).getX()/indefWidth), this.y, (this.subCoordinates.get(0).getY()/indefWidth));			
			rateX = -1*((this.x - this.subCoordinates.get(0).getX()/indefWidth)/((seconds*60)*(baseDistance/totalDistance)));
			rateY = -1*((this.y - this.subCoordinates.get(0).getY()/indefWidth)/((seconds*60)*(baseDistance/totalDistance)));
		}catch(IndexOutOfBoundsException e){
			double baseDistance = getDistance(this.x, this.fx, this.y, this.fy);			
			rateX = -1*((this.x - this.fx)/((seconds*60)*(baseDistance/totalDistance)));
			rateY = -1*((this.y - this.fy)/((seconds*60)*(baseDistance/totalDistance)));
		}
	}
	
	public double getDistance(double ix, double fx, double iy, double fy){
	
		double xSeg = (fx - ix);
		xSeg = xSeg * xSeg;
		double ySeg = (fy - iy);
		ySeg = ySeg*ySeg;
		double totalDistance = Math.sqrt(xSeg+ySeg);
		
		return totalDistance;
	}
	
	//Getters and Setters
	
	public double getX(){return this.x;}
	public double getY(){return this.y;}
	public double getIx(){return this.ix;}
	public double getIy(){return this.iy;}
	public double getFx(){return this.fx;}
	public double getFy(){return this.fy;}
	public double getWidth(){return this.width;}
	public double getRadius(){return this.radius;}
	public double getCx(){return this.cx;}
	public double getCy(){return this.cy;}
	public boolean isRunning(){return this.running;}
	public boolean isActive(){return this.active;}
	public HUDinfo getHUDinfo(){return this.hudinfo;}
	
	public void setX(double x){this.x = x;}
	public void setY(double y){this.y = y;}
	public void setWidth(double width){this.width = width;}
	public void setRadius(double radius){this.radius = radius;}
	public void setCx(double cx){this.cx = cx;}
	public void setCy(double cy){this.cy = cy;}
	public void setRunning(boolean running){this.running = running;}
	
}
