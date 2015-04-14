package io.connectors.gossip;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class GossipConnector 
{
	protected static Logger logger = Logger.getLogger("pokemon");
	
	private List<String> peers;
	private String SERVER_A = "http://52.0.103.87:8080/";
	private String SERVER_B = "http://52.5.67.9:8080/";
	public GossipConnector()
	{
		peers = new ArrayList<String>();
		setPeers(); //SET PEERS UPON INSTANTIATION
	}
	
	/**
	 * USE THIS FUNCTION TO SET THE PEERS BEFORE CREATING THE WAR FILE
	 */
	private void setPeers()
	{
		//ONE OF THESE TWO LINES SHOULD BE COMMENTED OUT AT ALL TIMES
		setPeers_ImServer_A(); 		//LOCALHOST IS SERVER "A" - CREATE WAR FILE AND DEPLOY ON 52.0.103.87
		//setPeers_ImServer_B(); 	//LOCALHOST IS SERVER "B" - CREATE WAR FILE AND DEPLOY ON 52.5.67.9
	}
	
	private void setPeers_ImServer_A() //I AM SERVER "A", SET MY PEERS
	{
		peers.clear();
		//peers.add(SERVER_B);
		peers.add(SERVER_A);

	}
	
	private void setPeers_ImServer_B() //I AM SERVER "B", SET MY PEERS
	{
		peers.clear();
		peers.add(SERVER_A);
	}
		
	/**
	 * SEND A MESSAGE TO ALL PEERS
	 * @param querySuffix the query string to be concatenated to each peer's base URL
	 * @param message the message to be sent, in JSON format
	 */
	public void sendMessage(String querySuffix)
	{
		for (String peerURL : peers)
		{
			String fullURL = peerURL + "Lightning/" + "?" +  querySuffix;
			String response = httpGET(fullURL); //ASSUMES OUTGOING MESSAGE IS JSON - CHECK "Content-Type" in httpPOST
		}
	}
	
	private String httpGET(String requestURL)
	{	
		StringBuffer response = new StringBuffer();
		try
		{
			//URL CONNECTION
			URL url = new URL(requestURL);
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
	
	private String httpPOST(String requestURL, String data) 
	{	
		StringBuffer response = new StringBuffer();				
		try
		{
			//System.out.println("POST DATA: " + data);

			//URL CONNECTION
			URL url = new URL(requestURL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			
			//HEADERS
//			con.setRequestMethod("POST");
//			con.setRequestProperty("Authorization", "OAuth " + _API_KEY);
//			con.setRequestProperty("Content-Length", ""+data.length());
			
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(data);

			wr.flush();
			wr.close();
			
			//System.out.println(con.getResponseMessage());
			
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
			logger.severe("!!!!!! HTTP-POST ERROR: " + e.getMessage());
			e.printStackTrace();
			//myResponse.setCode(400);
			return "";
		}
		
		return response.toString();
	}
}
