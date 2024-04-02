<?php
include "connect.php";

$accountId = $_POST['accountId'];
$productId = $_POST['productId'];
$quantity= $_POST['quantity'];

if($quantity == 0){
	$query = "DELETE FROM `tbl_cart` WHERE `account_id`= '$accountId' AND `product_id`= '$productId'";

}
else{
	$query = "UPDATE `tbl_cart` SET `quantity` = '$quantity' WHERE `account_id`= '$accountId' AND `product_id`= '$productId'";
}


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