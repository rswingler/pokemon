package controllers;

import io.connectors.gossip.GossipConnector;

public class GossipController 
{
	private GossipConnector gossip;
	public GossipController()
	{
		gossip = new GossipConnector();
	}
	
	public void blastMessage(String queryString)
	{
		// Glue the query string onto the back of the IP addresses of the other two servers and blast it out
		gossip.sendMessage(queryString);
	}
}
