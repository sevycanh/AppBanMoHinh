<?php
include "connect.php";

$accountId = $_POST['accountId'];
$province = $_POST['province'];
$district= $_POST['district'];
$ward= $_POST['ward'];
$administrative_address= $_POST['administrative_address'];

$query = "UPDATE `tbl_account` SET `province`='$province',`district`='$district',`ward`='$ward',`administrative_address` = '$administrative_address'  WHERE `account_id`='$accountId'";
$data = mysqli_query($conn, $query);

if ($data == true){
	$arr = [
        'success' => true,
        'message' => "update address thanh cong",
    ];
    print_r(json_encode($arr));
} 
else {
	$arr = [
		'success' => false,
		'message' => "update address khong thanh cong",
	];
	print_r(json_encode($arr));
}
?>