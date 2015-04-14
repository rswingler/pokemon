package servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

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
	protected static Logger logger = Logger.getLogger("pokemon");

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
		
		logger.info("****** LIGTNING doGET: ");
		String queryString = request.getQueryString();
		String[] parameters = queryString.split("&");
		
		HashMap<String,String> parameterMap = generateParameterMapFromParameters(parameters);
		
		String messageType = "Unknown";
				
		// Determine the message type
		if (parameterMap.containsKey(QueryParamTypes.queryParam_messageType))
		{
			messageType = parameterMap.get(QueryParamTypes.queryParam_messageType);
		}
		
		switch (masterControl.state)
		{
		case State_Lobby:
			// From the lobby, only respond to Battle Request events
			if (messageType.equals(QueryParamTypes.messageType_battleRequest))
			{
				handleBattleRequestEvent();
			}
			break;
		default:
			break;
		}
	}
	
	protected void handleBattleRequestEvent()
	{
		System.out.println("\n\n\n\n derp \n\n\n\n");
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
