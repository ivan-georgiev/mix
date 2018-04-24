<?php
require_once("classes/DBS.php");

if(isset($_GET['fileId'])){

//Create separate connection	
$run = new DBS("(local)", array( "Database"=>"Thesises", "CharacterSet" => "UTF-8"));
$run->connect();

$fileId = $_GET['fileId'];
$run->getFile($fileId);

$run->disconnect();
}  
?>