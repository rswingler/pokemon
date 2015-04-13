package io.connectors.gossip;

import java.net.InetSocketAddress;
import java.net.SocketException;

import com.jolira.gossip.*;

public class GossipConnector 
{
	private Service gossip;
	public GossipConnector()
	{
		gossip = buildGossipService();
	}
	
	public Service buildGossipService()
	{		
		InetSocketAddress listener = new InetSocketAddress("localhost", 8080);
		InetSocketAddress[] peers = new InetSocketAddress[3];
		peers[0] = new InetSocketAddress("localhost", 8080);
		peers[1] = new InetSocketAddress("localhost", 8080);
		peers[2] = new InetSocketAddress("localhost", 8080);

		Service gossipService = null;
		try 
		{
			gossipService = new Service(listener, peers);
		} 
		catch (SocketException e) 
		{
			e.printStackTrace();
		}	
		
		return gossipService;
	}
	
	public void sendMessage(String topic, String message)
	{
		gossip.send(topic, message);
	}
}
