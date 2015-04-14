package controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class RandomController {

	public RandomController() {
		
	}
	
	public int randomIntBetweenOneAndN(int n) {	
		
		StringBuffer response = new StringBuffer();
		try
		{
			//URL CONNECTION
			URL url = new URL("http://www.rollthedice.setgetgo.com/get.php?Sides=" + Integer.toString(n));
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			
			//HEADERS
			con.setRequestMethod("GET");
			con.setRequestProperty("Content-Type", "application/json");
			
			//GET RESPONSE BODY - LITERALLY JUST A NUMBER WITH NO OTHER WHITESPACE OR CHARACTERS
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null)
			{
				response.append(inputLine);
			}
			in.close();
			
			String responseString = response.toString();
			return Integer.parseInt(responseString);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return -1;
		}
	}
}
