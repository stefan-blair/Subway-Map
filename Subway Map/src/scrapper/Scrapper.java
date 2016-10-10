package scrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;

public class Scrapper {

	public returnType getHTML(String line, String date) throws IOException{
		String text = "";
		
		returnType data;
		
		URL url = new URL("http://travel.mtanyct.info/serviceadvisory/routeStatusResult.aspx?tag="+String.valueOf(line)+"&date="+date+"&time=&method=getstatus4");
		URLConnection urlConnection = url.openConnection();
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
		
		String inputLine;
		ArrayList <String> stops = new ArrayList<String>();
		
		while((inputLine = bufferedReader.readLine()) != null){
			text+=inputLine;
		}
		
		bufferedReader.close();
		
		try{
			text = text.split("png' >")[1];
			text = text.split("</a")[0];
			if(text.contains("skip ")){
				String bound = text.split("bound")[0];
				text = text.split("skip ")[1];
				stops.add(text.split(" and ")[1]);
				text = text.split(" and ")[0];
				for(int i = 0; i < text.split(",").length; i++){
					stops.add(text.split(", ")[i]);
				}
				bound = bound.replaceAll("-", "");
				bound = bound.replaceFirst(" ", "");
				return new returnType(bound, stops);
			}
			else if(text.contains("No trains running")){
//				System.out.println("Skipping All");
				return new returnType(true);
			}
		}catch(ArrayIndexOutOfBoundsException e){
//			System.out.println("No Scheduled Changes");
		}
		
		return new returnType(false);
	
	}
	
}
