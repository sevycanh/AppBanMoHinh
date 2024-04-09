<?php
$host = "localhost:3307";
$user = "root";
$pass ="";
$database = "apporder";

$conn = mysqli_connect($host, $user, $pass, $database);
mysqli_set_charset($conn, "utf8");

?>