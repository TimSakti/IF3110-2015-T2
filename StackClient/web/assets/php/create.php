<?php

$name = $_POST['Name'];	
$email = $_POST['Email'];
$judul = $_POST['Judul'];
$konten = $_POST['Konten'];

//Validation
$error = false;
if(empty($name)){
	echo "<p>Nama tidak boleh kosong</p>";
	$error = true;
}
if(empty($email) || !filter_var($email, FILTER_VALIDATE_EMAIL) ){
	echo "<p>Email kosong atau tidak valid</p>";
	$error = true;
}
if(empty($judul)){
	echo "<p>judul tidak boleh kosong</p>";
	$error = true;
}
if(empty($konten)){
	echo "<p>konten tidak boleh kosong</p>";
	$error = true;
}

if($error)
	echo "<p>pengisisan salah</p>";
else{
	$servername = "localhost";
	$username = "herifauzan";
	$password = "HFR_78itb";

	$con = mysql_connect($servername,$username,$password);
	if (!$con)
	  {
	  die('Could not connect: ' . mysql_error());
	  }
	mysql_select_db("mydb", $con);

	mysql_query("INSERT INTO question (nama, email, topik, konten,  vote)
	VALUES ('$name', '$email', '$judul' ,'$konten', 0)");

	mysql_close($con);
	echo "<p>Pertanyaan berhasil ditulis</p>";
}
	
?>