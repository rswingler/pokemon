package controllers;

public class QueryParamTypes
{
	public static final String queryParam_battleUID = "battleUID";
	public static final String queryParam_fromPeer = "fromPeer";
	public static final String queryParam_toPeer = "toPeer";
	public static final String toPeers_all = "allPeers";
	
	public static final String queryParam_messageType = "messageType";
	public static final String messageType_battleRequest = "battleRequest";
	public static final String messageType_battleAccepted = "battleAccepted";
	public static final String messageType_battleRejected = "battleRejected";
	public static final String messageType_opponentInfo = "opponentInfo";
	public static final String messageType_playerAction = "playerAction";
	public static final String messageType_gameEnded = "gameEnded";
	
	public static final String actionType_abilityName = "abilityName";
	public static final String actionType_damage = "damage";
	public static final String damageType_critical = "critical";
	public static final String damageType_effective = "superEffective";
	public static final String damageType_notEffective = "notVeryEffective";
	
	public static final String opponentInfo_pokemon = "pokemon"; // Bulbasaur, Pikachu, etc.
	// We don't even need the pokemon type for now. Let's just use the pokemon name for the time being.
	public static final String opponentInfo_pokemonType = "pokemonType"; // Water, Fire, etc.
	
}
