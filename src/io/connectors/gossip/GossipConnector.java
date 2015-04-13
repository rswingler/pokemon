package io.connectors.gossip;

import java.net.InetSocketAddress;
import java.net.SocketException;

import com.jolira.gossip.*;

public class GossipConnector 
{
	public GossipConnector()
	{
		
	}
	
	public void testGossip()
	{
		//Service(InetSocketAddress listener, InetSocketAddress[] seeds)
		
		InetSocketAddress listener = new InetSocketAddress("localhost", 8080);
		InetSocketAddress[] peers = new InetSocketAddress[3];
		peers[0] = new InetSocketAddress("localhost", 8080);
		peers[1] = new InetSocketAddress("localhost", 8080);
		peers[2] = new InetSocketAddress("localhost", 8080);

		try 
		{
			Service gossip = new Service(listener, peers);
			gossip.send("topic", "message");			
		} 
		catch (SocketException e) 
		{
			e.printStackTrace();
		}	
	}
}
