var BASE_URL;

$(function(){
	loadHome();
});

function loadHome()
{
	BASE_URL = "http://localhost:8080/"
	//BASE_URL = "given server address"

	addListeners_home();
}

function addListeners_home()
{
	 $(".button").hover(function(){
	 	$(this).css( 'cursor', 'pointer' );
	 });
	 
	 $(".newBattleButton").click(function(){
		 startNewBattle();
	 });
}

function startNewBattle()
{
	console.log("*** STARTING NEW BATTLE");
	var url = BASE_URL + "randompokemon";
	$.get( url, function( pokemonData )
	{
		loadPokemon( pokemonData );
	});
}

function loadPokemon( pokemon )
{
	console.log("LOAD POKEMON");
	var name = pokemon["Name"]; //case sensitive
	var speed = pokemon["Speed"];
	var hp = pokemon["HP"];
	var attack = pokemon["Attack"];
	var defense = pokemon["Defense"];
	var id = pokemon["ID"];
	
	var infoBox = $(".infoMessage");
	
	infoBox.empty();
	infoBox.append("<img class='pokemonImg' src='http://pokeapi.co/media/img/" + id + ".png'></img><br>");
	infoBox.append("NAME: " + name + "<br>");
	infoBox.append("SPEED: " + speed + "<br>");
	infoBox.append("HP: " + hp + "<br>");
	infoBox.append("ATTACK: " + attack + "<br>");


	//console.log(data);
}