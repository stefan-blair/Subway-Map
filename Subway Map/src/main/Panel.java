package main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JPanel;

import map.Map;

public class Panel extends JPanel implements Runnable, MouseListener, MouseMotionListener, MouseWheelListener{

	private static final long serialVersionUID = 1L;

	public static final int WIDTH = 800;
	public static final int HEIGHT = 500;
	
	private Thread thread;
	
	private boolean Running;
	
	private int FPS = 60;
	private long desiredTime = 1000/FPS;
		
	Calendar calender = Calendar.getInstance();
	Date date;
	DateFormat hour;
	DateFormat minute;
	DateFormat second;
	DateFormat day;
	
	Map map;
	
	double x, y, cx, cy, width, height , milis, seconds;
	int HOUR, MINUTE, DAY, spawnedMinute, currentMinute;
	boolean currentTime, newDay, newHour;
	
	public Panel(){
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
		setFocusable(true);
		Start();
	}
	
	public void Start(){
		x = 0;
		y = 0;
		cx = 0;
		cy = 0;
		width = 1058;
		height = 1277;
		
		currentTime = true;
		newHour = true;
		newDay = true;
		
		date = new Date();
		calender.setTime(date);
		DAY = calender.get(Calendar.DAY_OF_WEEK);
		hour = new SimpleDateFormat("HH");
		minute = new SimpleDateFormat("mm");
		second = new SimpleDateFormat("ss");
		day = new SimpleDateFormat("MM-dd-yyyy");
		milis = 0;
		seconds = Integer.valueOf(second.format(date));
		HOUR = Integer.valueOf(hour.format(date));
		MINUTE = Integer.valueOf(minute.format(date));
		
		spawnedMinute = MINUTE;
		currentMinute = MINUTE;
		
		map = new Map(HOUR+":"+MINUTE, second.format(date), day.format(date), DAY);
		Running = true;
	
		map.setTime(HOUR+":"+MINUTE);
		//MAP WIDTH
		map.setWidth(2000);
		map.reSetSubways();
		//MAP WIDTH
		thread = new Thread(this);
		thread.start();
	
	}
	
	@SuppressWarnings("static-access")
	public void run(){
		long start, elapsed, wait;
		while(Running){
			start = System.nanoTime();
			tick();
			repaint();
			elapsed = System.nanoTime()-start;
			wait = desiredTime - elapsed/1000000;
			if(wait < 0)wait = 5;
			try{
				thread.sleep(wait);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public void tick(){
		map.tick();
		date = new Date();
		if(currentTime){
			HOUR = Integer.valueOf(hour.format(date));
			MINUTE = Integer.valueOf(minute.format(date));
			seconds = Integer.valueOf(second.format(date));
			
			if(HOUR == 0 && MINUTE == 0 && newDay){
//				System.out.println("Its a new day, you know how I feel");
				calender.setTime(date);
				double widthSave = map.getWidth();
				double xSave = map.getX();
				double ySave = map.getY();
				DAY = calender.get(Calendar.DAY_OF_WEEK);
				map = new Map(HOUR+":"+MINUTE, second.format(date), day.format(date), DAY);
				map.setWidth(widthSave);
				map.setX(xSave);
				map.setY(ySave);
				map.reSetSubways();
				newDay = false;
			}else if(HOUR == 23 && MINUTE == 59){
				newDay = true;
			}
		}
		else{
			currentMinute = Integer.valueOf(minute.format(date));
			if(currentMinute > spawnedMinute){
				spawnedMinute = currentMinute;
				MINUTE++;
				if(MINUTE>=60){
					HOUR++;
					MINUTE = 0;
					if(HOUR>24){
						HOUR = 0;
						MINUTE = 0;
						calender.setTime(date);
						DAY = calender.get(Calendar.DAY_OF_WEEK);
						map = new Map(HOUR+":"+MINUTE, second.format(date), day.format(date), DAY);
					}
				}
			}
		}
		if(MINUTE == 0 && newHour){
			map.receiveData(day.format(date));
			newHour = false;
		}else if(MINUTE == 59){
			newHour = true;
		}
		map.setTime(HOUR+":"+MINUTE);
		
	}
	
	public void paint(Graphics g){
		g.clearRect(0, 0, WIDTH, HEIGHT);
		map.paint(g);
	}
	
	//Methods
		
	public void setTime(int h, int m){
		this.currentTime = false;
		this.HOUR = h;
		this.MINUTE = m;
		map = new Map(HOUR+":"+MINUTE, second.format(date), day.format(date), DAY);
		map.setWidth(2000);
		map.reSetSubways();
	}
	
	public void setCurrentTime(){
		this.currentTime = true;
		seconds = Integer.valueOf(second.format(date));
		HOUR = Integer.valueOf(hour.format(date));
		MINUTE = Integer.valueOf(minute.format(date));
		map = new Map(HOUR+":"+MINUTE, second.format(date), day.format(date), DAY);
		map.setWidth(2000);
		map.reSetSubways();
	}
	
	public String getTime(){
		return this.HOUR+":"+this.MINUTE;
	}
	
	//Mouse Motions
	public void mouseDragged(MouseEvent e) {
		if(e.getX() > cx && map.getX() < WIDTH/2){map.setX(map.getX()+e.getX()-cx);cx = e.getX();}
		if(e.getX() < cx && map.getX()+map.getWidth() > WIDTH/2){map.setX(map.getX()-(cx-e.getX()));cx = e.getX();}
		if(e.getY() > cy && map.getY() < 0){map.setY(map.getY()+e.getY()-cy);cy = e.getY();}
		if(e.getY() < cy && map.getY()+map.getHeight() > HEIGHT){map.setY(map.getY()-(cy-e.getY()));cy = e.getY();}
		map.reSetSubways();
	}

	public void mouseMoved(MouseEvent e) {
		cx = e.getX();
		cy = e.getY();
	}

	public void mouseClicked(MouseEvent e) {
		map.isTime(true);
		map.callHUD(e.getX(), e.getY());
//		if(e.getButton()==3)System.out.print("Main_Coord:");
//		System.out.println((((e.getX()-map.getX())/map.getWidth())*200)+", "+(((e.getY()-map.getY())/map.getHeight())*241));
//		System.out.println(map.getWidth()+", "+map.getHeight());
	}

	public void mouseEntered(MouseEvent e) {
				
	}

	public void mouseExited(MouseEvent e) {
				
	}

	public void mousePressed(MouseEvent e) {
		
	}

	public void mouseReleased(MouseEvent e) {
			
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		double scaleFactorX, scaleFactorY;
		if(e.getUnitsToScroll() < 0 && map.getWidth()>800 && map.getHeight()>965){
			width += e.getUnitsToScroll();
			height += e.getUnitsToScroll();
			map.setWidth(map.getWidth() + e.getUnitsToScroll()*8);
			scaleFactorX = ((map.getX()-cx)/200)/10;
			scaleFactorY = ((map.getY()-cy)/241)/10;
			map.setX(map.getX()+e.getUnitsToScroll()*(8*scaleFactorX));
			map.setY(map.getY()+e.getUnitsToScroll()*(8*scaleFactorY));
			map.reSetSubways();
		}else if(e.getUnitsToScroll() > 0 && map.getWidth()<3656 && map.getHeight()<4412){
			width += e.getUnitsToScroll();
			height += e.getUnitsToScroll();
			map.setWidth(map.getWidth() + e.getUnitsToScroll()*8);
			scaleFactorX = ((map.getX()-cx)/200)/10;
			scaleFactorY = ((map.getY()-cy)/241)/10;
			map.setX(map.getX()+e.getUnitsToScroll()*(8*scaleFactorX));
			map.setY(map.getY()+e.getUnitsToScroll()*(8*scaleFactorY));
			map.reSetSubways();
		}
		//System.out.println((cx-map.getX())/200+", "+(cy-map.getY())/241);
	}
	
}
