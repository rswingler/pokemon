package controllers;

import java.util.UUID;


public class MasterController 
{
	public String myPeerID;
    
	// Battle Info
	public GameState state;
	public String currentBattleID;
	public String currentBattlePeer;
	public String opponentPokemon;
	public Player player; // Player 1 is always the initiator, Player 2 is the one who "joined" the battle
	public boolean battleInProgress; // As soon as we send or accept a request, we're locked into a battle. No takebacks.
	
	private static MasterController instance = null;
	
	private static GossipController gossipController;
	private static PokemonController pokemonController;
	private static RandomController randomController;
	
	private MasterController()
	{
        // Generate new unique peerID
        myPeerID = 	UUID.randomUUID().toString();
        
        // Start in the lobby
        state = GameState.State_Lobby;
        
        currentBattleID = "none";
        currentBattlePeer = "none";
        opponentPokemon = "none";
        player = Player.PlayerUnknown;
        battleInProgress = false;
	}
	
	public static MasterController getInstance()
	{
		if (instance == null)
		{
			instance = new MasterController();
			gossipController = new GossipController();
			pokemonController = new PokemonController();
			randomController = new RandomController();
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
	
	public RandomController getRandomController()
	{
		return randomController;
	}

}
