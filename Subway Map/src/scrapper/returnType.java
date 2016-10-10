package scrapper;

import java.util.ArrayList;

public class returnType {

	String direction;
	
	ArrayList<String> skipStops;
	boolean all, noChange;
	
	public returnType(boolean all){
		if(all == false)
			noChange = true;
		else
			noChange = false;
		this.all = all;
		
	}
	
	public returnType(String direction, ArrayList<String>skipStops){	
		this.skipStops = skipStops;
		this.direction = direction;
		this.all = false;
		noChange = false;
		for(int i = 0; i < this.skipStops.size(); i++){
			if(skipStops.get(i).contains("Pkwy")){
				String buffer = skipStops.get(i);
				this.skipStops.remove(i);
				this.skipStops.add(i, buffer.replace("Pkwy", "Parkway"));
			}
		}		
	}
	
	public boolean isNoChange(){return this.noChange;}
	public boolean isAll(){return this.all;}
	public String getDirection(){return this.direction;}
	public ArrayList<String> getSkips(){return this.skipStops;}
	
}
