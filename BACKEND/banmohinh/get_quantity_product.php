<?php
include "connect.php";
$product_id = $_POST['product_id'];

//checkdata
$query = 'SELECT * FROM `tbl_product` WHERE `product_id` = "'.$product_id.'"';
$data = mysqli_query($conn, $query);
$result = array();
while ($row = mysqli_fetch_assoc($data)) {
	$result[] = ($row);
}
if (!empty($result)){
	$arr = [
		'success' => true,
		'message' => "Thành công",
		'result' => $result
	];
} else {
	$arr = [
		'success' => false,
		'message' => "Không thành công",
		'result' => $result
	];
}
print_r(json_encode($arr));
?>