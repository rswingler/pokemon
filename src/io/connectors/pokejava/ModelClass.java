package io.connectors.pokejava;
/**
 * @author Michael Cohen
 *
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.logging.Logger;

import org.json.*;



/**
 * @author root
 *
 */
public abstract class ModelClass {
	
	protected String Name, URI;
	protected int ID;
	protected Date Created, Modified;
	
	protected static Logger logger = Logger.getLogger("pokemon");


	protected JSONObject parse(String data) {
		JSONObject root;
		try {
		root = new JSONObject(data);		
		return root;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
//	protected String get(String urlAddress) {
//		String data = "";
//		try {
//			data = Request.Get("http://pokeapi.co/api/v1/" + urlAddress).execute().returnContent().asString();
//		} catch (Exception e){
//			e.printStackTrace();
//		}
//		
//		return data;
//	}
	
	protected String get(String requestURL)
	{	
		StringBuffer response = new StringBuffer();
		try
		{
			//URL CONNECTION
			String fullURL = "http://pokeapi.co/api/v1/" + requestURL;
			//System.out.println("FULL URL: " + fullURL);
			URL url = new URL(fullURL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			
			//HEADERS
			con.setRequestMethod("GET");
			con.setRequestProperty("Content-Type", "application/json");
			
			//GET RESPONSE - JSON STRING
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null)
			{
				response.append(inputLine);
			}
			in.close();
		}
		catch (Exception e)
		{
			logger.severe("!!!!!! HTTP-GET ERROR: " + e.getMessage());
			e.printStackTrace();
			return "";
		}
		
		//System.out.println(response.toString());
		
		return response.toString();
	}
	
	/**
	 * @return String Name: Name representation of object
	 */
	public String getName(){ return Name;}
	
	
	/**
	 * @return String URI: URI to the specific Object
	 */
	public String getURI(){ return URI;}
	
	
	/**
	 * @return Date Modified: A date object representing the last time said API was updated
	 */
	public Date getModified(){return Modified;}
	
	
	/**
	 * @return Date Created: A date object representing the API's creation
	 */
	public Date getCreated(){ return Created;}
	
	
	/**
	 * @return int ID: ID number of the object within the pokeapi database
	 */
	public int getID(){return ID;}
	
	public String toString(){
		String data = this.getClass().getSimpleName() + ": " + Name + "\nID: " + ID;
		return data;
	}
	
	/**
	 * Prints the toString() of the object to the console
	 */
	public void printInfo(){
		System.out.println(toString());
	}
}
