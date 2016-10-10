package data;

public class Coordinate {

	private double x, y;
	
	public Coordinate(Double double1, Double double2){
		this.x = double1;
		this.y = double2;
	}
	
	public double getX(){return this.x;}
	public double getY(){return this.y;}
	
	public void setX(double x){this.x = x;}
	public void setY(double y){this.y = y;}
	
}
