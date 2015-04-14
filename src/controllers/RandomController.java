package controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class RandomController {

	public RandomController() {
		
	}
	
	public int randomIntBetweenOneAndN(int n) {	
		try {
			
			URL randomURL = new URL("http://www.rollthedice.setgetgo.com/get.php?Sides=" + Integer.toString(n));
			URLConnection yc = randomURL.openConnection();
		    BufferedReader in = new BufferedReader(
		                            new InputStreamReader(
		                            yc.getInputStream()));
		    String inputLine;

		    while ((inputLine = in.readLine()) != null) 
		        System.out.println(inputLine);
		    in.close();
		    
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    return 0;
	}
}
