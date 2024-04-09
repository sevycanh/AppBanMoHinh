<?php
include "connect.php";
$coin=$_POST['coin'];
$luckybox=$_POST['luckybox'];
$id=$_POST['id'];

$query = 'UPDATE `tbl_account` SET `coin`="'.$coin.'", `luckybox`='.$luckybox.' WHERE `account_id`='.$id;
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