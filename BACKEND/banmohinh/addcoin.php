<?php
include "connect.php";
$coin_receive=$_POST['coin_receive'];
$order_id=$_POST['order_id'];

//lay id user cua don hang
$query = 'SELECT `account_id` FROM `tbl_order` WHERE `order_id` = '.$order_id;
$data = mysqli_query($conn, $query);
$row = mysqli_fetch_assoc($data);
$id = $row['account_id'];

//lay coin hien tai
$query = 'SELECT coin FROM `tbl_account` WHERE `account_id` = "'.$id.'"';
$data = mysqli_query($conn, $query);
$row = mysqli_fetch_assoc($data);
$coin_new = $row['coin'] + $coin_receive;

$query = 'UPDATE `tbl_account` SET `coin`="'.$coin_new.'" WHERE `account_id`='.$id;
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