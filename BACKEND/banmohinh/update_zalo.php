<?php
include "connect.php";

$order_id = $_POST['iddonhang'];
$token = $_POST['token'];

$query = "UPDATE `tbl_order` SET `token` = '$token' WHERE `order_id`= '$order_id'";

//echo $query;
$data = mysqli_query($conn, $query);
if ($data == true){
	$arr = [
        'success' => true,
        'message' => "update token thanh cong",
    ];
    print_r(json_encode($arr));
} 
else {
	$arr = [
		'success' => false,
		'message' => "update token that bai",
	];
	print_r(json_encode($arr));
}

?>