package servlets;

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
 * Servlet implementation class GetPokemon
 */
@WebServlet("/randompokemon")
public class GetPokemon extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	protected static Logger logger = Logger.getLogger("pokemon");

    public GetPokemon() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		MasterController masterControl = MasterController.getInstance();

		String pokemon = masterControl.getPokemonController().getRandomPokemon();
		logger.info("*******RANDOM POKEMON: " + pokemon);
		response.addHeader("Content-Type", "application/json");
		PrintWriter writer = response.getWriter();
		writer.write(pokemon);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
