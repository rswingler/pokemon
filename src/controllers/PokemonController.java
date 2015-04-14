package controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import io.connectors.pokeAPI.PokeApiConnector;
import io.connectors.pokejava.Pokemon;

public class PokemonController 
{
	private PokeApiConnector pokeapi;
	public PokemonController()
	{
		pokeapi = new PokeApiConnector();
	}
	
	public String getRandomPokemon()
	{
		Pokemon p = pokeapi.getRandomPokemon();
		return new Gson().toJson(p);
	}
	
	public void initPokedex()
	{
		pokeapi.initPokedex();
	}
	
	public PokeApiConnector getConnector()
	{
		return pokeapi;
	}
}
