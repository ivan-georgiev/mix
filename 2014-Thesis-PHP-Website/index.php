<?php
session_start();
require_once("classes/DBS.php");
require_once("classes/Controller.php");

$run = new DBS("(local)", array( "Database"=>"Thesises", "CharacterSet" => "UTF-8"));
$cnt = new Controller($run);

?>
<!DOCTYPE html>
<html>

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>E-Библиотека Демо</title>
		<link rel="stylesheet" href="css/menu_style.css" type="text/css" />
	</head>

	<body>

		<br />
		<div class="menu">
			<ul>
				<?php
				if (!isset($_GET['tab'])) {
					echo '<li><a class="selectedTab" href="index.php?tab=1" >Начало</a></li>';
					echo '<li><a href="index.php?tab=2">Търсене</a>';
				}else{
					if($_GET['tab']==1){
						echo '<li><a class="selectedTab" href="index.php?tab=1" >Начало</a></li>';
						echo '<li><a href="index.php?tab=2">Търсене</a>';
					}

					if ($_GET['tab']==2){
						echo '<li><a " href="index.php?tab=1" >Начало</a></li>';
						echo '<li><a class="selectedTab href="index.php?tab=2">Търсене</a>';
					}
				}
				?>

				<ul>
					<li><a href="index.php?tab=2">Дипломни Работи</a></li>
				</ul>
			</li>
			<li><a>Контакти</a>
				<ul>
					<li><a href="mailto: contact@ethesis.bg">E-mail</a></li>
					<li><a href="callto: +359 2 555 666">Телефон</a></li>
				</ul>
			</li>
		</ul>
	</div>

	<div id="content" style="height:100%">


		<script type="text/javascript">
			function toggleSelectedTab(ev)
			{
				var clickedAnchor = ev.srcElement;
				var menuAnchors = getMenuAnchors();
				for (var i = 0; i < menuAnchors.length; i++)
				{
					menuAnchors[i].className = "";
				}
				clickedAnchor.className = "selectedTab";
			}

			function attachMenuEventHandlers()
			{
				var menuAnchors = getMenuAnchors();
				for (var i = 0; i < menuAnchors.length; i++)
				{
					menuAnchors[i].addEventListener("click", toggleSelectedTab, false);
				}
			}

			function getMenuAnchors()
			{
				var menuRoot = document.getElementById("menu");
				var menuAnchors = menuRoot.getElementsByTagName("a");
				return menuAnchors;
			}
			window.onload = attachMenuEventHandlers();
		</script>

		<?php

		if(isset($_GET['tab'])){
			$tab=$_GET['tab'];
		}else{
			$tab=1;
		}

		switch($tab){
			case 1:
			require ('home.php');
			break;
			case 2:
			require ('search.php');
			break;
			default:
			require ('home.php');
			break;
		}

		?>
	</div>
	<br/>

</body>
</html>

