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
	 
	 $(".attackButton").click(function(){
		 attack();
	 });
}

function startNewBattle()
{
	console.log("*** STARTING NEW BATTLE");
	var url = BASE_URL + "startbattle";
	$.get( url, function( pokemons )
	{
		loadPokemons(pokemons);
	});
}


function getPokemon()
{
	console.log("*** GET POKEMON");
	var url = BASE_URL + "randompokemon";
	$.get( url, function( pokemonData )
	{
		loadPokemon( pokemonData );
	});
}

function loadPokemons( pokemons )
{
	console.log("POKEMONS: " + JSON.stringify(pokemons));
	var pokemon1 = pokemons[0];
	var pokemon2 = pokemons[1];
	
	console.log("LOAD POKEMON");
	var name1 = pokemon1["Name"]; //case sensitive
	var speed1 = pokemon1["Speed"];
	var hp1 = pokemon1["HP"];
	var attack1 = pokemon1["Attack"];
	var defense1 = pokemon1["Defense"];
	var id1 = pokemon1["ID"];
	
	var name2 = pokemon2["Name"]; //case sensitive
	var speed2 = pokemon2["Speed"];
	var hp2 = pokemon2["HP"];
	var attack2 = pokemon2["Attack"];
	var defense2 = pokemon2["Defense"];
	var id2 = pokemon2["ID"];
	
	var infoBox = $(".infoMessage");
	var pokeBox = $("<div class='pokebox'></div>");
	
	infoBox.empty();
	var box1 = pokeBox.clone();
	box1.append("<img class='pokemonImg' src='http://pokeapi.co/media/img/" + id1 + ".png'></img><br>");
	box1.append("NAME: " + name1 + "<br>");
	box1.append("SPEED: " + speed1 + "<br>");
	box1.append("HP: " + hp1 + "<br>");
	box1.append("ATTACK: " + attack1 + "<br>");
	box1.append("DEFENSE: " + defense1 + "<br>");
	infoBox.append(box1);
	
	var box2 = pokeBox.clone();
	box2.append("<img class='pokemonImg' src='http://pokeapi.co/media/img/" + id2 + ".png'></img><br>");
	box2.append("NAME: " + name2 + "<br>");
	box2.append("SPEED: " + speed2 + "<br>");
	box2.append("HP: " + hp2 + "<br>");
	box2.append("ATTACK: " + attack2 + "<br>");
	box2.append("DEFENSE: " + defense2 + "<br>");
	infoBox.append(box2);

}


function attack()
{
	console.log("ATTACK");
}