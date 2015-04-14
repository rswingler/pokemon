package servlets;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controllers.MasterController;
import controllers.QueryParamTypes;

/**
 * Servlet implementation class Lightning
 */
@WebServlet("/lightning")
public class Lightning extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	private MasterController masterControl;
    public Lightning() {
        super();
        masterControl = MasterController.getInstance();

        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
//		if (toPeerID.equals(QueryParamTypes.toPeers_all) || toPeerID.equals(masterControl.myPeerID))
//		{
			switch (masterControl.state)
			{
			case State_Lobby:
				// From the lobby, only respond to Battle Request events
				if (messageType.equals(QueryParamTypes.messageType_battleRequest))
				{
//					handleBattleRequestEvent(fromPeerID, battleID);
				}
				break;
			case State_WaitingForReply:
				// Once we've responded to a request, must wait for requester to verify that battle has begun
				// by sending us a battle accepted message. Alternatively, if we're the original requester, we must wait for
				// someone to accept our request. Either way, it comes through here.
				if (messageType.equals(QueryParamTypes.messageType_battleAccepted))
				{
//					handleBattleAcceptedEvent(fromPeerID, battleID);
				}
				if (messageType.equals(QueryParamTypes.messageType_battleRejected))
				{
//					handleBattleRejectedEvent(fromPeerID, battleID);
				}
				break;
			default:
				break;
			}
		//}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
	
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

}
