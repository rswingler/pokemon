package servlets;

import io.connectors.pokejava.Pokemon;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import controllers.MasterController;
import controllers.PokemonController;

/**
 * Servlet implementation class StartBattle
 */
@WebServlet("/startbattle")
public class StartBattle extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StartBattle() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//SEND BATTLE MESSAGE TO PEERS
		MasterController masterControl = MasterController.getInstance();
		masterControl.sendBattleRequestMessageToAll();
		
		//GET RANDOM POKEMON
		PokemonController pokeControl = masterControl.getPokemonController();
		
		Pokemon pokemon1 = pokeControl.getRandomPokemon();
		Pokemon pokemon2 = pokeControl.getRandomPokemon();
		
		//PERSIST PLAYER ONE AND TWO IN MEMORY - IN THE POKEMON CONTROLLER
		pokeControl.setPlayerOne(pokemon1);
		pokeControl.setPlayerTwo(pokemon2);

		//CONVERT POKEMON TO JSON ARRAY
		String pokemon1_str = pokeControl.toString(pokemon1);
		String pokemon2_str = pokeControl.toString(pokemon2);
		JsonParser parser = new JsonParser();
		JsonObject p1 = parser.parse(pokemon1_str).getAsJsonObject();
		JsonObject p2 = parser.parse(pokemon2_str).getAsJsonObject();
		JsonArray pokemonList = new JsonArray();
		pokemonList.add(p1);
		pokemonList.add(p2);
		
		//RESPOND WITH POKEMON ARRAY
		response.addHeader("Content-Type", "application/json");
		PrintWriter writer = response.getWriter();
		writer.write(pokemonList.toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
