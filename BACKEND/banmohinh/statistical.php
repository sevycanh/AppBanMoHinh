<?php
include "connect.php";
$query = "SELECT od.product_id,p.name, COUNT(od.quantity) as sum FROM `tbl_product`  p 
			join tbl_orderdetail od on p.product_id = od.product_id
            join tbl_order o on o.order_id = od.order_id
			GROUP BY od.product_id ORDER BY sum DESC LIMIT 3";
$data = mysqli_query($conn, $query);
$result = array();
while ($row = mysqli_fetch_assoc($data)) {
	$result[] = ($row);
}
if (!empty($result)){
	$arr = [
		'success' => true,
		'result' => $result
	];
} else {
	$arr = [
		'success' => false,
		'result' => $result
	];
}
print_r(json_encode($arr));
?>