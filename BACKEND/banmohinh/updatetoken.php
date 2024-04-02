<?php
include "connect.php";
$token=$_POST['token'];
$id=$_POST['id'];

$query = 'UPDATE `tbl_account` SET `token`="'.$token.'" WHERE `account_id`='.$id;
$data = mysqli_query($conn, $query);

	if ($data == true){
		$arr = [
			'success' => true,
			'message' => "thanh cong"
		];
	} else {
		$arr = [
			'success' => false,
			'message' => "khong thanh cong"
		];
	}

print_r(json_encode($arr));
?>