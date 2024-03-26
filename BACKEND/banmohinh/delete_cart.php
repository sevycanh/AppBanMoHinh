<?php
include "connect.php";

$accountId = $_POST['accountId'];

$query = "DELETE FROM `tbl_cart` WHERE `account_id`= '$accountId'";

//echo $query;
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