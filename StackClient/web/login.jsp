<%-- 
    Document   : login
    Created on : Nov 27, 2015, 5:44:06 PM
    Author     : Heri Fauzan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>

<meta charset="utf-8">
<link rel="stylesheet" type="text/css" href="assets/css/screen.css" />
<link rel="shortcut icon" type="image/x-icon" href="img/favicon.ico">

<title>Simple StackExchange | Login</title>
<!-- <?php include 'config.php' ?> -->

</head>

<body class="default">
<div class="wrapper">

<nav>
    <h3 style="border:none;" id="logo" ><center>Simple StackExchange</center></h3>
</nav> 
<br>
<br>

<article class="art simple post">
        <div class="art-body-inner">
            <hr>
            <p>Login to Your Account<br></p>

            <div id="contact-area">
                <form method="post" action="answer.php?id=<?php echo $_GET['id']; ?>">
                    <input type="text" name="Name" id="Nama" placeholder="Name">
                    <input type="text" name="Password" id="Password" placeholder="Password"></input>
                    <br>
                    <input type="submit" name="submit" value="Register" class="submit-button">
                </form>
            </div>
        </div>
    </div>

</article>

<footer class="footer">
    <div class="back-to-top"><a href="herryfauzan.com">&copy; Heri Fauzan/13513028 </div>
</footer>
</div>

</body>
</html>
