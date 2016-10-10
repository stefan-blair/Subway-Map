package data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

public class Schedule {
	
	public String lineName, direction;
	public String folderName, coordinateFolderName, subCoordinateFolderName, byPassDataFileName;
	public String fileName, xFileName, yFileName, listName, subXFileName, subYFileName;
	public File file, coordFile, list, subCoordFile;
	ArrayList<ArrayList<String>> schedule;
	ArrayList<ArrayList<Coordinate>> subCoordinates;
	ArrayList<String> stops;
	ArrayList<Coordinate>coordinates;
	ArrayList<BypassData>bypassData;
	String name, color, day;	
	
	public ArrayList<Double> x, y, subX, subY;
	//Stops
	public ArrayList<ArrayList<String>> stopList;
	//Stops
	public ArrayList<String> names;
	
	public Schedule(int day, String name){
		this.lineName = name.split("_")[0];
		this.direction = name.split("_")[1];
		//Files
		if(day == 1)this.day = "sun";
		else if(day == 7)this.day = "sat";
		else this.day = "week";
		System.out.println(this.day);
		folderName = "times/"+lineName+"/"+this.day+"/"+direction;
		listName = "stopLists/"+lineName;
		coordinateFolderName = "coordinates/"+lineName;
		subCoordinateFolderName = "/subCoordinates/"+lineName; 
		byPassDataFileName = "bypass/"+name+"/"+this.day+"/";
		fileName = "";
		xFileName = "";
		yFileName = "";
		subXFileName = "";
		subYFileName = "";
		file = new File(folderName);
		list = new File(listName);
		coordFile = new File(coordinateFolderName);
		subCoordFile = new File(subCoordinateFolderName);
		bypassData = new ArrayList<BypassData>();
		//Stops
		stopList = new ArrayList<ArrayList<String>>();
		//Variables
		names = new ArrayList<String>();
		
		//Methods
		addTime();
		setNames();
		setSchedule();
		setName();
		setColor();
		setCoords();
		try {
			setSubCoords();
			setByPassData();
			System.out.println("FOUND SUBCOORDINATES");
		} catch (NullPointerException e) {
			System.out.println("No subCoordinate Files Found...NULLPOINTEREXCEPTION");
		} catch (FileNotFoundException e) {
			System.out.println("No subCoordinate Files Found...FILENOTFOUNDEXCEPTION");
		}
	}
	
	private void setByPassData() {
		String data;
		for(int i = 0; i < stopList.size(); i++){
			try {
				String startTime;
				String endTime;
				int targetStop;
				int startStop;
				ArrayList<Coordinate> subCoords = new ArrayList<Coordinate>();
				
				data = "";
				String fileNames = byPassDataFileName+String.valueOf(i)+"/data.txt";
				BufferedReader bufReader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(fileNames)));
				
				String bufString;
				while((bufString = bufReader.readLine()) != null){
					data+=(bufString);
					data+=";";
				}
				bufReader.close();
				
				startStop = i;
				targetStop = Integer.valueOf(data.split(";")[0]);
				startTime = data.split(";")[1];
				endTime = data.split(";")[2];
				
				fileNames = byPassDataFileName+String.valueOf(i)+"/subCoords.txt";
				bufReader = new BufferedReader(new FileReader(fileNames));
				data = "";
				bufString = "";
				while((bufString = bufReader.readLine()) != null){
					data+=(bufString);
					data+=";";
				}
				bufReader.close();
				for(int s = 0; s < data.split(";").length;s++){
					subCoords.add(new Coordinate(Double.valueOf(data.split(";")[s].split(",")[0]),Double.valueOf(data.split(";")[s].split(",")[1])));
				}
				this.bypassData.add(new BypassData(startTime, endTime, startStop, targetStop, subCoords));
			} catch (IOException e) {

			}
		}
	}

	public void setNames(){
		
		ArrayList<String> namesBuffer = new ArrayList<String>();
		String fileNames = "stopNames/"+this.lineName+".txt";
		
		try {
			
			BufferedReader bufReader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/"+fileNames)));
			
			String bufString;
			while((bufString = bufReader.readLine()) != null){
				namesBuffer.add(bufString);
			}	
			
			bufReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(this.direction.equals("0")){
			for(int a = 0; a < namesBuffer.size(); a++){
				this.names.add(namesBuffer.get(a));
			}
		}else if(this.direction.equals("1")){
			for(int a = namesBuffer.size()-1; a >= 0; a--){
				this.names.add(namesBuffer.get(a));
			}
		}
	}
	
	public void addTime(){
		stops = new ArrayList<String>();
		try{

			BufferedReader bufReader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/"+listName+".txt")));

			
			String text;
			while((text = bufReader.readLine()) != null){
				stops.add(text);
				stopList.add(new ArrayList<String>());
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		
		for(int i = 0; i < stopList.size(); i++){
			fileName = folderName+"/"+stops.get(i)+".txt";
			try {
				BufferedReader bufReader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/"+fileName)));

				
				String text;
				while((text = bufReader.readLine()) != null){
					stopList.get(i).add(text);
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public void setSchedule() {
		schedule = new ArrayList<ArrayList<String>>();
		for(int i = 0; i < stopList.size(); i++){
			schedule.add(stopList.get(i));
		}
	}

	public void setName() {this.name = lineName;}
	public void setColor() {color = "red";}

	public void setCoords() {
		coordinates = new ArrayList<Coordinate>();
		
		x = new ArrayList<Double>();
		y = new ArrayList<Double>();

		xFileName = coordinateFolderName+"/X"+".txt";
		yFileName = coordinateFolderName+"/Y"+".txt";
		try {
			BufferedReader xBufReader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/"+xFileName)));
			
			BufferedReader yBufReader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/"+yFileName)));
			
			String xText;
			String yText;
			while((xText = xBufReader.readLine()) != null){
				x.add(Double.valueOf(xText));
			}
			while((yText = yBufReader.readLine()) != null){
				y.add(Double.valueOf(yText));
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
//		System.out.println(this.direction);
		if(this.direction.equals("0"))for(int i = 0; i < x.size(); i++){
			coordinates.add(new Coordinate(x.get(i), y.get(i)));
		}
		else if(this.direction.equals("1")){
//			System.out.println(this.direction+", LOCATION");
			for(int a = x.size()-1; a >= 0; a--){
				coordinates.add(new Coordinate(x.get(a), y.get(a)));
			}
		}
//		System.out.println(coordinates.size());
		//Coordinatess

	}
	
	public void setSubCoords() throws FileNotFoundException{
		subCoordinates = new ArrayList<ArrayList<Coordinate>>();
		
		for(int i = 1; i < stopList.size()+2; i++){
			subCoordinates.add(new ArrayList<Coordinate>());
		}
		
		for(int t = 1; t < subCoordinates.size()+1; t++){
			subX = new ArrayList<Double>();
			subY = new ArrayList<Double>();
			subXFileName = subCoordinateFolderName+"/"+t+"/X"+".txt";
			subYFileName = subCoordinateFolderName+"/"+t+"/Y"+".txt";
			System.out.println(subXFileName + ", " + subYFileName);
			try {
				BufferedReader subxBufReader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(subXFileName)));
				BufferedReader subyBufReader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(subYFileName)));
				
				String xText;
				String yText;
				while((xText = subxBufReader.readLine()) != null){
					subX.add(Double.valueOf(xText));
				}
				while((yText = subyBufReader.readLine()) != null){
					subY.add(Double.valueOf(yText));
				}
				
				if(this.direction.equals("0"))for(int i = 0; i < subX.size(); i++){
					subCoordinates.get(t-1).add(new Coordinate(subX.get(i), subY.get(i)));
				}
				else if(this.direction.equals("1")){
					for(int a = subX.size()-1; a >= 0; a--){
						subCoordinates.get(t-1).add(new Coordinate(subX.get(a), subY.get(a)));
					}
				}
			} catch (NullPointerException e) {
				
			} catch (NumberFormatException e) {
				System.out.println("File Was Not Found "+t);
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("File Was Not Found "+t);
				e.printStackTrace();
			}
			
		}
		if(this.direction.equals("1"))Collections.reverse(subCoordinates);

	}
	public boolean isTime(int stop, String time){
		for(int i = 0; i<schedule.get(stop).size(); i++){
			if(schedule.get(stop).get(i).equals(time))return true;
		}
		return false;
	}
	
	public String getTime(int place, int time){
//		System.out.println(place+", "+time);
		return schedule.get(place).get(time);
	}
	
	public ArrayList<String> getSchedule(int place){
		return schedule.get(place);
	}
	public ArrayList<BypassData> getBypassData(){return this.bypassData;}
	public ArrayList<String> getNames(){return this.names;}
	public ArrayList<String> getStops(){return this.stops;}
	public ArrayList<Coordinate> getCoordinates(){return this.coordinates;}
	public ArrayList<ArrayList<Coordinate>> getSubCoordinates(){return this.subCoordinates;}
	public ArrayList<Double> getXCoords() {
		ArrayList<Double>xCoords = new ArrayList<Double>();
		for(int x = 0; x < coordinates.size(); x++){xCoords.add(coordinates.get(x).getX());}
		return xCoords;
	}
	public ArrayList<Double> getYCoords() {
		ArrayList<Double>yCoords = new ArrayList<Double>();
		for(int y = 0; y < coordinates.size(); y++){yCoords.add(coordinates.get(y).getY());}
		return yCoords;
	}

}
