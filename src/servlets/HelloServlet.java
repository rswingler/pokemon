package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controllers.MasterController;

import java.util.HashMap;
import java.util.UUID;
import servlets.GameState;
import servlets.Player;
import servlets.Messenger;

/**
 * Servlet implementation class HelloServlet
 */
@WebServlet("/helloservlet")
public class HelloServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private String myPeerID;
	
	// Battle Info
	private GameState state;
	private String currentBattleID;
	private String currentBattlePeer;
	private String opponentPokemon;
	private Player player; // Player 1 is always the initiator, Player 2 is the one who "joined" the battle
	private boolean battleInProgress; // As soon as we send or accept a request, we're locked into a battle. No takebacks.
       
	private MasterController masterControl;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HelloServlet()
    {
        super();
        masterControl = MasterController.getInstance();
        
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

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		response.addHeader("Content-Type", "application/json");
		
		String queryString = request.getQueryString();
		String[] parameters = queryString.split("&");
		
		HashMap<String,String> parameterMap = generateParameterMapFromParameters(parameters);
		
		String messageType = "Unknown";
		String fromPeerID = "Unknown";
		String toPeerID = "Unknown";
		String battleID = "Unknown";
				
		// Determine the message type
		if (parameterMap.containsKey(Messenger.queryParam_messageType))
		{
			messageType = parameterMap.get(Messenger.queryParam_messageType);
		}
		
		if (parameterMap.containsKey(Messenger.queryParam_fromPeer))
		{
			fromPeerID = parameterMap.get(Messenger.queryParam_fromPeer);
		}
		
		if (parameterMap.containsKey(Messenger.queryParam_toPeer))
		{
			toPeerID = parameterMap.get(Messenger.queryParam_toPeer);
		}
		
		if (parameterMap.containsKey(Messenger.queryParam_battleUID))
		{
			battleID = parameterMap.get(Messenger.queryParam_battleUID);
		}
		
		// Only respond to this GET request if it was addressed to me, or all peers
		if (toPeerID.equals(Messenger.toPeers_all) || toPeerID.equals(myPeerID))
		{
			switch (state)
			{
			case State_Lobby:
				// From the lobby, only respond to Battle Request events
				if (messageType.equals(Messenger.messageType_battleRequest))
				{
					handleBattleRequestEvent(fromPeerID, battleID);
				}
				break;
			case State_WaitingForReply:
				// Once we've responded to a request, must wait for requester to verify that battle has begun
				// by sending us a battle accepted message. Alternatively, if we're the original requester, we must wait for
				// someone to accept our request. Either way, it comes through here.
				if (messageType.equals(Messenger.messageType_battleAccepted))
				{
					handleBattleAcceptedEvent(fromPeerID, battleID);
				}
				if (messageType.equals(Messenger.messageType_battleRejected))
				{
					handleBattleRejectedEvent(fromPeerID, battleID);
				}
				break;
			case State_WaitingForInfo:
				// We've made a connection, now we need to know the name of the opponent's pokemon before beginning the game.
				if (messageType.equals(Messenger.messageType_opponentInfo))
				{
					String opponentsPokemon = parameterMap.get(Messenger.opponentInfo_pokemon);
					handlePokemonInfoEvent(fromPeerID, battleID, opponentsPokemon);
				}
				break;
			case State_MyTurn:
				// Ignore pretty much every event if it's my turn. The only thing we should do here is allow ourselves to take action.
				break;
			case State_TheirTurn:
				if (messageType.equals(Messenger.messageType_playerAction))
				{
					String abilityName = parameterMap.get(Messenger.actionType_abilityName);
					String damage = parameterMap.get(Messenger.actionType_damage);
					handlePlayerActionEvent(fromPeerID, battleID, abilityName, damage);
				}
				break;
			default:
				break;
			}
		}
	}
	
	protected void handleBattleRequestEvent(String fromPeerID, String battleID)
	{
		if (!battleInProgress)
		{
			// Set the info about the request (used to verify later)
			currentBattlePeer = fromPeerID;
			currentBattleID = battleID;
			
			// TODO: Send back a "Battle Accepted" message via gossip
			Messenger.SendStatus(Messenger.messageType_battleAccepted, myPeerID, fromPeerID, battleID);
			
			state = GameState.State_WaitingForReply;
			player = Player.Player2; // By replying to a battle request, we signify that we are player 2
			battleInProgress = true;
		}
	}
	
	protected void handleBattleAcceptedEvent(String fromPeerID, String battleID)
	{
		if (battleInProgress)
		{
			// TODO: Send back a "Battle Rejected" message via gossip
			Messenger.SendStatus(Messenger.messageType_battleRejected, myPeerID, fromPeerID, battleID);
		}
		else
		{
			if (player == Player.Player1)
			{
					// TODO: Send back a "Battle Accepted" message via gossip
					state = GameState.State_WaitingForInfo;
					Messenger.SendStatus(Messenger.messageType_battleAccepted, myPeerID, fromPeerID, battleID);
			}
			else if (player == Player.Player2)
			{
				// TODO: Send back our pokemon info, signifying that the battle has begun!
				state = GameState.State_WaitingForInfo;
				//TODO: USE POKE API TO GET POKEMON DATA TO SEND IN THIS REQUEST
				Messenger.SendInfo(Messenger.messageType_opponentInfo, myPeerID, fromPeerID, battleID, "Pikachu", "Electric");
			}
			else
			{
				// Error, the player should have been set to 1 or 2 by now (if it's zero, we have a logic flow error)
			}
		}
	}
	
	protected void handleBattleRejectedEvent(String fromPeerID, String battleID)
	{
		if (player == Player.Player1)
		{
			// TODO: RESET ANY STATE DATA RELATED TO THE OTHER PLAYER
			// If all peers send a BattleRejected (everyone else is currently in a battle)
			// Then either broadcast out another battle request, or return to lobby state.
		}
		else if (player == Player.Player2)
		{
			// RESET ANY DATA RELATED TO THE OTHER PLAYER
			currentBattleID = "none";
	        currentBattlePeer = "none";
	        opponentPokemon = "none";
			state = GameState.State_Lobby;
			player = Player.PlayerUnknown;
			battleInProgress = false;
		}
		else
		{
			// Error, the player should have been set to 1 or 2 by now (if it's zero, we have a logic flow error)
		}
	}
	
	protected void handlePokemonInfoEvent(String fromPeerID, String battleID, String pokemonName)
	{
		if (player == Player.Player1)
		{
			// TODO: Send back our pokemon info, signifying that the battle has begun!
			state = GameState.State_MyTurn;
			Messenger.SendInfo(Messenger.messageType_opponentInfo, myPeerID, fromPeerID, battleID, "Pikachu", "Electric");
		}
		else if (player == Player.Player2)
		{
			state = GameState.State_TheirTurn;
		}
		else
		{
			// Error, the player should have been set to 1 or 2 by now (if it's zero, we have a logic flow error)
		}
	}
	
	protected void handlePlayerActionEvent(String fromPeerID, String battleID, String actionName, String damage)
	{
		// TODO: Finish this method.
		
		// Basically, the damage string can be "effective", "not effective", or "critical"
	    // Only "critical" damage ends the game. The other two do absolutely nothing (we don't even keep track of health)
	    // other than display fun messages, but no one has to know that except us devs ;)
		
		if (damage.equals(Messenger.damageType_critical))
		{
			// End game (somehow)
		}
		else if (damage.equals(Messenger.damageType_effective))
		{
			
		}
		else if (damage.equals(Messenger.damageType_notEffective))
		{
			
		}
	}
	
	protected HashMap<String,String> generateParameterMapFromParameters(String[] parameters)
	{
		HashMap<String,String> paramMap = new HashMap<String,String>();
		
		for (String parameter : parameters)
		{
			String[] components = parameter.split("=");
			String paramName = components[0];
			String value = components[0];
			
			paramMap.put(paramName, value);
		}
		
		return paramMap;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
