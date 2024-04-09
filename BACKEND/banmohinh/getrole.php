<?php
include "connect.php";
$role = $_POST['role'];

if($role==0){
	$query = "SELECT * FROM `tbl_account` WHERE `role` = ".$role;
	$data = mysqli_query($conn, $query);
	$result = array();
	while ($row = mysqli_fetch_assoc($data)) {
		// code...
		$result[] = ($row);
	}
	if (!empty($result)){
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
} else if ($role==1){
	$query = "SELECT * FROM `tbl_account` WHERE `role` = ".$role;
	$data = mysqli_query($conn, $query);
	$result = array();
	while ($row = mysqli_fetch_assoc($data)) {
		// code...
		$result[] = ($row);
	}
	if (!empty($result)){
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
}
print_r(json_encode($arr));
?>