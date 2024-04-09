<?php
include "connect.php";
$coin=$_POST['coin'];
$checkin=$_POST['checkin'];
$id=$_POST['id'];

$query = 'UPDATE `tbl_account` SET `coin`="'.$coin.'", `check_in`='.$checkin.' WHERE `account_id`='.$id;
$data = mysqli_query($conn, $query);

	if ($data == true){
		$arr = [
			'success' => true,
			'message' => "Thành công"
		];
	} else {
		$arr = [
			'success' => false,
			'message' => "Không thành công"
		];
	}

print_r(json_encode($arr));
?>