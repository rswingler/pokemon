package servlets;

import io.connectors.pokejava.Pokemon;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controllers.MasterController;

/**
 * Servlet implementation class Attack
 */
@WebServlet("/attack")
public class Attack extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	protected static Logger logger = Logger.getLogger("pokemon");

    public Attack() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		MasterController masterControl = MasterController.getInstance();
		
		int dieRoll = masterControl.getRandomController().randomIntBetweenOneAndN(100);
		logger.info("****** DICE ROLLED: " + dieRoll);

		Pokemon winner = null;
		if (dieRoll % 2 == 0)
		{
			//POKEMON A WINS
			winner = masterControl.getPokemonController().getPlayerOne();
		}
		else
		{
			//POKEMON B WINS
			winner = masterControl.getPokemonController().getPlayerTwo();
		}
		
		
		String winnerJson = masterControl.getPokemonController().toString(winner);
		
		response.addHeader("Content-Type", "application/json");
		PrintWriter writer = response.getWriter();
		writer.write(winnerJson);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
