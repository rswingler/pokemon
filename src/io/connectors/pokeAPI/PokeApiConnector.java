package io.connectors.pokeAPI;

public class PokeApiConnector 
{
	/*
	 * This class handles all direct HTTP communication with the PokeAPI
	 */
	private final String pokeURL;
	public PokeApiConnector()
	{
		pokeURL = "http://pokeapi.co/api/v1";
	}
}
