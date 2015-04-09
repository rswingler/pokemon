package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;

/**
 * Servlet implementation class HelloServlet
 */
@WebServlet("/helloservlet")
public class HelloServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final String queryParam_battleUID = "battleUID";
	private static final String queryParam_fromPeer = "fromPeer";
	private static final String queryParam_toPeer = "toPeer";
	private static final String toPeers_all = "allPeers";
	
	private static final String queryParam_messageType = "messageType";
	private static final String messageType_battleRequest = "battleRequest";
	private static final String messageType_battleAccepted = "battleAccepted";
	private static final String messageType_opponentInfo = "opponentInfo";
	private static final String messageType_playerAction = "playerAction";
	private static final String messageType_gameEnded = "gameEnded";
	
	private static final String opponentInfo_pokemon = "pokemon"; // Bulbasaur, Pikachu, etc.
	private static final String opponentInfo_pokemonType = "pokemonType"; // Water, Fire, etc.
	
	private String myPeerID;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HelloServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		response.addHeader("Content-Type", "application/json");
		
		// TESTING COMMIT
		
		String queryString = request.getQueryString();
		String[] parameters = queryString.split("&");
		
		HashMap<String,String> parameterMap = generateParameterMapFromParameters(parameters);
		
		String messageType = "Unknown";
		String fromPeerID = "Unknown";
		String toPeerID = "Unknown";
				
		// Determine the message type
		if (parameterMap.containsKey(queryParam_messageType))
		{
			messageType = parameterMap.get(queryParam_messageType);
		}
		
		if (parameterMap.containsKey(queryParam_fromPeer))
		{
			fromPeerID = parameterMap.get(queryParam_fromPeer);
		}
		
		if (parameterMap.containsKey(queryParam_toPeer))
		{
			toPeerID = parameterMap.get(queryParam_toPeer);
		}
		
		// Only respond to this GET request if it was addressed to me, or all peers
		if (toPeerID.equals(toPeers_all) || toPeerID.equals(myPeerID))
		{
			PrintWriter writer = response.getWriter();
			// Echo the query string back in JSON
			writer.write(String.format("{ 'queryString': '%s' }", queryString));
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
