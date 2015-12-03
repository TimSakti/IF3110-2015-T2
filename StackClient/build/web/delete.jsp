<?php
	$servername = "localhost";
	$username = "root";
	$password = "";

	$con = mysql_connect($servername,$username,$password);
	if (!$con)
	  {
	  die('Could not connect: ' . mysql_error());
	  }
	mysql_select_db("mydb", $con);
	$sql = "DELETE FROM question WHERE id=".$_GET['id'];
	echo $sql;
	mysql_query($sql);
	echo mysql_query($sql);
	$sql = "DELETE FROM answer WHERE idquest=".$_GET['id'];
	mysql_query($sql);
	$con= null;
?>