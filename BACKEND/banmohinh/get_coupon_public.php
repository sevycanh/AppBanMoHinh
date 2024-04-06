<?php
include "connect.php";
$is_Public = "1";
date_default_timezone_set('Asia/Ho_Chi_Minh');
$dateFrom = date("Y-m-d H:i:s");
$dateTo = date("Y-m-d H:i:s");

//checkdata
$query = 'SELECT * FROM `tbl_coupon` WHERE `isPublic` = "'.$is_Public.'" AND `dateFrom` < "'.$dateFrom.'"  AND  `dateTo` > "'.$dateTo.'"  AND `count` > 0 ';
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