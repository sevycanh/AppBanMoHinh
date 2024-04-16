<?php
include "connect.php";

$purchase = $_POST['purchase'];
$purchase = json_decode($purchase, true);

foreach($purchase as $key => $value){
    $query = 'UPDATE  `tbl_product` SET `quantity` = `quantity` + '.$value["quantity"].' WHERE `product_id`= '.$value["idProduct"].'' ;
    $data = mysqli_query($conn, $query);
	if ($data == true){
		$arr = [
			'success' => true,
			'message' => "Update Product Thanh Cong",
		];
	} 
	else {
		$arr = [
			'success' => false,
			'message' => "Update Product That Bai",
		];
	}
}
print_r(json_encode($arr));

?>