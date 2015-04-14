package controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import io.connectors.pokeAPI.PokeApiConnector;
import io.connectors.pokejava.Pokemon;

public class PokemonController 
{
	private PokeApiConnector pokeapi;
	private Pokemon playerOne;
	private Pokemon playerTwo;
	
	public PokemonController()
	{
		pokeapi = new PokeApiConnector();
	}
	
	public String getRandomPokemon_string()
	{
		Pokemon p = pokeapi.getRandomPokemon();
		return new Gson().toJson(p);
	}
	
	public String toString(Pokemon p)
	{
		return new Gson().toJson(p);
	}
	
	public Pokemon getRandomPokemon()
	{
		return pokeapi.getRandomPokemon();
	}
	
	public void initPokedex()
	{
		pokeapi.initPokedex();
	}
	
	public PokeApiConnector getConnector()
	{
		return pokeapi;
	}

	public Pokemon getPlayerOne() {
		return playerOne;
	}

	public void setPlayerOne(Pokemon playerOne) {
		this.playerOne = playerOne;
	}

	public Pokemon getPlayerTwo() {
		return playerTwo;
	}

	public void setPlayerTwo(Pokemon playerTwo) {
		this.playerTwo = playerTwo;
	}
}
