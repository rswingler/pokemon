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
	$.get( url, function( data )  //TESTING
	{
		loadPokemon( data );
	});
}


function loadPokemon( data )
{
	console.log("LOAD POKEMON");
	var name = data["Name"]; //case sensitive
	var speed = data["Speed"];
	var hp = data["HP"];
	
	var infoBox = $(".infoMessage");
	
	infoBox.empty();
	infoBox.append("NAME: " + name + "<br>");
	infoBox.append("SPEED: " + speed + "<br>");
	infoBox.append("hp: " + hp + "<br>");

	//console.log(data);
}