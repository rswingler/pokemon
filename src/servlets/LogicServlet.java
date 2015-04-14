package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controllers.GameState;
import controllers.MasterController;
import controllers.QueryParamTypes;
import controllers.Player;

import java.util.HashMap;

/**
 * Servlet implementation class HelloServlet
 */
@WebServlet("/logicservlet")
public class LogicServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	   
	private MasterController masterControl;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LogicServlet()
    {
        super();
        masterControl = MasterController.getInstance();
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
		if (parameterMap.containsKey(QueryParamTypes.queryParam_messageType))
		{
			messageType = parameterMap.get(QueryParamTypes.queryParam_messageType);
		}
		
		if (parameterMap.containsKey(QueryParamTypes.queryParam_fromPeer))
		{
			fromPeerID = parameterMap.get(QueryParamTypes.queryParam_fromPeer);
		}
		
		if (parameterMap.containsKey(QueryParamTypes.queryParam_toPeer))
		{
			toPeerID = parameterMap.get(QueryParamTypes.queryParam_toPeer);
		}
		
		if (parameterMap.containsKey(QueryParamTypes.queryParam_battleUID))
		{
			battleID = parameterMap.get(QueryParamTypes.queryParam_battleUID);
		}
		
		// Only respond to this GET request if it was addressed to me, or all peers
		if (toPeerID.equals(QueryParamTypes.toPeers_all) || toPeerID.equals(masterControl.myPeerID))
		{
			switch (masterControl.state)
			{
			case State_Lobby:
				// From the lobby, only respond to Battle Request events
				if (messageType.equals(QueryParamTypes.messageType_battleRequest))
				{
					handleBattleRequestEvent(fromPeerID, battleID);
				}
				break;
			case State_WaitingForReply:
				// Once we've responded to a request, must wait for requester to verify that battle has begun
				// by sending us a battle accepted message. Alternatively, if we're the original requester, we must wait for
				// someone to accept our request. Either way, it comes through here.
				if (messageType.equals(QueryParamTypes.messageType_battleAccepted))
				{
					handleBattleAcceptedEvent(fromPeerID, battleID);
				}
				if (messageType.equals(QueryParamTypes.messageType_battleRejected))
				{
					handleBattleRejectedEvent(fromPeerID, battleID);
				}
				break;
			case State_WaitingForInfo:
				// We've made a connection, now we need to know the name of the opponent's pokemon before beginning the game.
				if (messageType.equals(QueryParamTypes.messageType_opponentInfo))
				{
					String opponentsPokemon = parameterMap.get(QueryParamTypes.opponentInfo_pokemon);
					handlePokemonInfoEvent(fromPeerID, battleID, opponentsPokemon);
				}
				break;
			case State_MyTurn:
				// Ignore pretty much every event if it's my turn. The only thing we should do here is allow ourselves to take action.
				break;
			case State_TheirTurn:
				if (messageType.equals(QueryParamTypes.messageType_playerAction))
				{
					String abilityName = parameterMap.get(QueryParamTypes.actionType_abilityName);
					String damage = parameterMap.get(QueryParamTypes.actionType_damage);
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
		if (!masterControl.battleInProgress)
		{
			// Set the info about the request (used to verify later)
			masterControl.currentBattlePeer = fromPeerID;
			masterControl.currentBattleID = battleID;
			
			// TODO: Send back a "Battle Accepted" message via gossip
			
			masterControl.state = GameState.State_WaitingForReply;
			masterControl.player = Player.Player2; // By replying to a battle request, we signify that we are player 2
			masterControl.battleInProgress = true;
		}
	}
	
	protected void handleBattleAcceptedEvent(String fromPeerID, String battleID)
	{
		if (masterControl.battleInProgress)
		{
			// Send back a "Battle Rejected" message via gossip
			sendBattleRejectedMessage(fromPeerID);
		}
		else
		{
			if (masterControl.player == Player.Player1)
			{
					// Send back a "Battle Accepted" message via gossip
					String queryString = QueryParamTypes.queryParam_messageType + "=" + QueryParamTypes.messageType_battleAccepted;
					queryString += "&" + QueryParamTypes.queryParam_fromPeer + "=" + MasterController.getInstance().myPeerID;
					queryString += "&" + QueryParamTypes.queryParam_toPeer + "=" + fromPeerID;
					queryString += "&" + QueryParamTypes.queryParam_battleUID + "= " + battleID;
					masterControl.getGossipController().blastMessage(queryString);
					
					masterControl.state = GameState.State_WaitingForInfo;
			}
			else if (masterControl.player == Player.Player2)
			{
				// TODO: Send back our pokemon info, signifying that the battle has begun!
				masterControl.state = GameState.State_WaitingForInfo;
				//TODO: USE POKE API TO GET POKEMON DATA TO SEND IN THIS REQUEST
			}
			else
			{
				// Error, the player should have been set to 1 or 2 by now (if it's zero, we have a logic flow error)
			}
		}
	}
	
	protected void handleBattleRejectedEvent(String fromPeerID, String battleID)
	{
		if (masterControl.player == Player.Player1)
		{
			// TODO: RESET ANY STATE DATA RELATED TO THE OTHER PLAYER
			// If all peers send a BattleRejected (everyone else is currently in a battle)
			// Then either broadcast out another battle request, or return to lobby state.
		}
		else if (masterControl.player == Player.Player2)
		{
			// RESET ANY DATA RELATED TO THE OTHER PLAYER
			masterControl.currentBattleID = "none";
	        masterControl.currentBattlePeer = "none";
	        masterControl.opponentPokemon = "none";
			masterControl.state = GameState.State_Lobby;
			masterControl.player = Player.PlayerUnknown;
			masterControl.battleInProgress = false;
		}
		else
		{
			// Error, the player should have been set to 1 or 2 by now (if it's zero, we have a logic flow error)
		}
	}
	
	protected void handlePokemonInfoEvent(String fromPeerID, String battleID, String pokemonName)
	{
		if (masterControl.player == Player.Player1)
		{
			// TODO: Send back our pokemon info, signifying that the battle has begun!
			
			masterControl.state = GameState.State_MyTurn;
		}
		else if (masterControl.player == Player.Player2)
		{
			masterControl.state = GameState.State_TheirTurn;
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
		
		if (damage.equals(QueryParamTypes.damageType_critical))
		{
			// End game (somehow)
			masterControl.state = GameState.State_GameOver;
			//TODO: Somehow signal the UI to say "You lost"
		}
		else if (damage.equals(QueryParamTypes.damageType_effective) || damage.equals(QueryParamTypes.damageType_notEffective))
		{
			// Game continues, but it's now my turn.
			masterControl.state = GameState.State_MyTurn;
			//TODO: Somehow signal the UI to say "Your Turn"
		}
	}
	
	// Messages
	public void sendBattleRequestMessageToPeer(String toPeerID)
	{
		String queryString = QueryParamTypes.queryParam_messageType + "=" + QueryParamTypes.messageType_battleRequest;
		queryString += "&" + QueryParamTypes.queryParam_fromPeer + "=" + masterControl.myPeerID;
		queryString += "&" + QueryParamTypes.queryParam_toPeer + "=" + toPeerID;
		queryString += "&" + QueryParamTypes.queryParam_battleUID + "=" + masterControl.currentBattleID;
		masterControl.getGossipController().blastMessage(queryString);
	}
	
	public void sendBattleRequestMessageToAll()
	{
		String queryString = QueryParamTypes.queryParam_messageType + "=" + QueryParamTypes.messageType_battleRequest;
		queryString += "&" + QueryParamTypes.queryParam_fromPeer + "=" + masterControl.myPeerID;
		queryString += "&" + QueryParamTypes.queryParam_toPeer + "=" + QueryParamTypes.toPeers_all;
		queryString += "&" + QueryParamTypes.queryParam_battleUID + "=" + masterControl.currentBattleID;
		masterControl.getGossipController().blastMessage(queryString);
	}
	
	public void sendBattleAcceptedMessage(String toPeerID)
	{
		String queryString = QueryParamTypes.queryParam_messageType + "=" + QueryParamTypes.messageType_battleAccepted;
		queryString += "&" + QueryParamTypes.queryParam_fromPeer + "=" + masterControl.myPeerID;
		queryString += "&" + QueryParamTypes.queryParam_toPeer + "=" + toPeerID;
		queryString += "&" + QueryParamTypes.queryParam_battleUID + "=" + masterControl.currentBattleID;
		masterControl.getGossipController().blastMessage(queryString);
	}
	
	public void sendBattleRejectedMessage(String toPeerID)
	{
		String queryString = QueryParamTypes.queryParam_messageType + "=" + QueryParamTypes.messageType_battleRejected;
		queryString += "&" + QueryParamTypes.queryParam_fromPeer + "=" + masterControl.myPeerID;
		queryString += "&" + QueryParamTypes.queryParam_toPeer + "=" + toPeerID;
		queryString += "&" + QueryParamTypes.queryParam_battleUID + "=" + masterControl.currentBattleID;
		masterControl.getGossipController().blastMessage(queryString);
	}
	
	public void sendOpponentInfoMessage(String toPeerID, String pokemonName)
	{
		String queryString = QueryParamTypes.queryParam_messageType + "=" + QueryParamTypes.messageType_opponentInfo;
		queryString += "&" + QueryParamTypes.queryParam_fromPeer + "=" + masterControl.myPeerID;
		queryString += "&" + QueryParamTypes.queryParam_toPeer + "=" + toPeerID;
		queryString += "&" + QueryParamTypes.queryParam_battleUID + "=" + masterControl.currentBattleID;
		queryString += "&" + QueryParamTypes.opponentInfo_pokemon + "=" + pokemonName;
		masterControl.getGossipController().blastMessage(queryString);
	}
	
	public void sendPlayerActionMessage(String toPeerID, String abilityName, String damage)
	{
		String queryString = QueryParamTypes.queryParam_messageType + "=" + QueryParamTypes.messageType_playerAction;
		queryString += "&" + QueryParamTypes.queryParam_fromPeer + "=" + masterControl.myPeerID;
		queryString += "&" + QueryParamTypes.queryParam_toPeer + "=" + toPeerID;
		queryString += "&" + QueryParamTypes.queryParam_battleUID + "=" + masterControl.currentBattleID;
		queryString += "&" + QueryParamTypes.actionType_abilityName + "=" + abilityName;
		queryString += "&" + QueryParamTypes.actionType_damage + "=" + damage;
		masterControl.getGossipController().blastMessage(queryString);
	}
	
	// game over will be triggered on our end if we (or they) send an actionmessage with damage=critical
	// There is no need to explicitly send a gameover message, because it can be determined locally whether the game is over.
	
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
