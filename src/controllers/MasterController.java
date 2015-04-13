package controllers;


public class MasterController 
{
	private static MasterController instance = null;
	
	private static GossipController gossipController;
	private static PokemonController pokemonController;
	
	private MasterController()
	{
		
	}
	
	public static MasterController getInstance()
	{
		if (instance == null)
		{
			instance = new MasterController();
			gossipController = new GossipController();
			pokemonController = new PokemonController();
		}
		return instance;
	}
	
	public GossipController getGossipController()
	{
		return gossipController;
	}
	
	public PokemonController getPokemonController()
	{
		return pokemonController;
	}

}
