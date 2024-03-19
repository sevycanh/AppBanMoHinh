<?php
include "connect.php";
$email=$_POST['email'];
$pass=$_POST['password'];

//checkdata
$query = 'SELECT * FROM `tbl_account` WHERE `email` = "'.$email.'"';
$data = mysqli_query($conn, $query);
$numrow = mysqli_num_rows($data);
if ($numrow > 0){
	$arr = [
		'success' => false,
		'message' => "Email đã tồn tại"
	];
} else {
	//insert
	$query = 'INSERT INTO `tbl_account`(`email`, `password`, `role`, `status`) VALUES ("'.$email.'","'.$pass.'", "1", "1")';
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
}

print_r(json_encode($arr));
?>