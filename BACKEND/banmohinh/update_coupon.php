<?php
include "connect.php";

$coupon_id = $_POST['coupon_id'];
$count = $_POST['count'];

$query = "UPDATE  `tbl_coupon` SET `count` = '$count' WHERE `coupon_id`= '$coupon_id'";

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