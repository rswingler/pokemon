package io.connectors.pokeAPI;

import java.util.ArrayList;
import java.util.List;

import com.pokejava.Ability;
import com.pokejava.Move;
import com.pokejava.Pokedex;
import com.pokejava.Pokemon;

public class PokeApiConnector 
{
	/*
	 * This class handles all direct HTTP communication with the PokeAPI
	 */
	Pokedex pokedex;
	List<Pokemon> pokemons;
	public PokeApiConnector()
	{
		pokedex = new Pokedex();
		pokemons = pokedex.getPokemons();
	}
		
	public List<Pokemon> getAllPokemon()
	{
		return pokemons;
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
}
