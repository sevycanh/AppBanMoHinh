<?php
include "connect.php";
$query = "SELECT p.name,SUM(total) AS SumMonth, MONTH(date) AS Month
FROM tbl_order o join tbl_orderdetail od on od.order_id = o.order_id
join tbl_product p on od.product_id = p.product_id
GROUP BY MONTH(date)";
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