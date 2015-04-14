package io.connectors.pokeAPI;

import io.connectors.pokejava.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;


public class PokeApiConnector 
{
	protected static Logger logger = Logger.getLogger("pokemon");

	/*
	 * This class handles all direct HTTP communication with the PokeAPI
	 */
	//Pokedex pokedex;
	List<Pokemon> pokemons;
	public PokeApiConnector()
	{
		//pokedex = new Pokedex();
		pokemons = new ArrayList<Pokemon>();
		initPokedex(); //TAKES A FEW MINUTES
	}
		
//	public List<Pokemon> getAllPokemon()
//	{
//		return pokemons;
//	}
	
	public void initPokedex()
	{
		logger.info("\n\n\n\n\n**** DOWNLOADING POKEDEX - THIS TAKES A WHILE - IGNORE THE EXCEPTIONS FOR 'level' ");
		double startTime = System.currentTimeMillis();			 

		Pokedex p = new Pokedex();
		pokemons = p.getPokemons();
		double finishTime = System.currentTimeMillis();
		double elapsedTime = finishTime - startTime;
		double elapsedSeconds = elapsedTime / 1000;
		logger.info("************ UPDATE: " + " POKEDEX " + " COMPLETED IN " + elapsedSeconds + " SECONDS");
		logger.info("\n\n\n\n**** POKEDEX DOWNLOAD COMPLETE");

	}
	
	public List<String> getAllPokemonNames()
	{
		List<String> names = new ArrayList<String>();
		for (Pokemon p : pokemons)
		{
			names.add(p.getName());
		}
		return names;
	}
	
	public List<Type> getPokemonTypes(String pokemonName)
	{
		Pokemon p = getPokemon_byName(pokemonName);		
		return p.getTypes();
	}
	
	public Pokemon getPokemon_byName(String pokemonName)
	{
		for (Pokemon p : pokemons)
		{
			String name = p.getName();
			
			if (name.equalsIgnoreCase(pokemonName))
				return p;
		}
		
		return null;
	}
	
	public String getPokemonImageURL(String pokemonName)
	{
		//http://pokeapi.co/media/img/25.png
		Pokemon p = getPokemon_byName(pokemonName);
		int id = p.getID();
		
		return "http://pokeapi.co/media/img/" + id + ".png";
		//p.get
	}
	
	public Pokemon getRandomPokemon()
	{
		logger.info("***** GETTING RANDOM POKEMON");
		Random r = new Random();
		int randomIndex = r.nextInt(pokemons.size());
		return pokemons.get(randomIndex);
	}
}
