<?php
include "connect.php";

$product_id = $_POST['product_id'];
$quantity = $_POST['quantity'];

$query = "UPDATE  `tbl_product` SET `quantity` = `quantity` - '$quantity' WHERE `product_id`= '$product_id'";

//echo $query;
$data = mysqli_query($conn, $query);
if ($data == true){
	$arr = [
        'success' => true,
        'message' => "Update Product Thanh Cong",
    ];
    print_r(json_encode($arr));
} 
else {
	$arr = [
		'success' => false,
		'message' => "Update Product That Bai",
	];
	print_r(json_encode($arr));
}

?>