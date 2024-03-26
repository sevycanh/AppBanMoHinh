<?php
include "connect.php";

$accountId = $_POST['accountId'];
$productId = $_POST['productId'];
$quantity= $_POST['quantity'];

$query = "INSERT INTO `tbl_cart`(`account_id`, `product_id`, `quantity`) VALUES ('.$accountId.','.$productId.','.$quantity.')";

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