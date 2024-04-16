<?php
include "connect.php";
$orderid = $_GET['orderId'];
$query = "SELECT
tbl_order.order_id as order_id,
tbl_order.account_id as account_id,
tbl_account.username as account_name,
tbl_account.email as email,
tbl_order.address as address,
tbl_order.phone as phone,
tbl_order.payment_method as payment_method,
tbl_order.order_status AS order_status,
tbl_order.date as date,
tbl_order.total as total,
tbl_order.app_trans_id as app_trans_id
FROM tbl_order
JOIN tbl_account ON tbl_account.account_id = tbl_order.account_id
WHERE order_id = $orderid;";
$data = mysqli_query($conn, $query);
$result = array();
while ($row = mysqli_fetch_assoc($data)) {
	// code...
	$result[] = ($row);
}
if (!empty($result)) {
	$arr = [
		'success' => true,
		'message' => "thanh cong",
		'result' => $result
	];
} else {
	$arr = [
		'success' => false,
		'message' => "khong thanh cong",
		'result' => $result
	];
}
print_r(json_encode($arr));
