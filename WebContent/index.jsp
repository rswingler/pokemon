<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>Pokemon Battle</title>
<link href='http://fonts.googleapis.com/css?family=Open+Sans'
	rel='stylesheet' type='text/css'>
<link rel="stylesheet" href="css/styleHome.css">
<script type="text/javascript" src="js/jquery/jquery-2.1.3.min.js"></script>
</head>
<body>
	<h1>Pokemon Battle</h1>
	
	<div class="battleContainer">
		<div class="leftNav"></div>
<!-- 		<div class="battleImgContainer">
					<div class="battleImgWrapper">
						<div class="myPokemonImg pokemonImg"></div>
						<div class="opponentPokemonImg pokemonImg"></div>
					</div>
		</div> -->
		<div class="controlPanel">
			<div class="button newBattleButton">New Battle</div>
			<div class="button attackButton">Attack</div>
			<br>
			<div class="textBox infoMessage"></div>
			<div class="textBox winnerMessage"></div>
		</div>
	</div>
	<script type="text/javascript" src="js/dynamics_home.js"></script>
</body>
</html>