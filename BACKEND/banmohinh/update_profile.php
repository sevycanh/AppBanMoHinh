<?php
include "connect.php";

$accountId = $_POST['accountId'];
$address = $_POST['address'];
$username= $_POST['username'];
$phone= $_POST['phone'];

$query = "UPDATE `tbl_account` SET `username`='$username',`address`='$address',`phone`='$phone' WHERE `account_id`='$accountId'";
$data = mysqli_query($conn, $query);

if ($data == true){
	$arr = [
        'success' => true,
        'message' => "thanh cong",
    ];
    print_r(json_encode($arr));
} 
else {
	$arr = [
		'success' => false,
		'message' => "Khong thanh cong",
	];
	print_r(json_encode($arr));
}

?>