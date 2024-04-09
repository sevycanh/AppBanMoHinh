<?php
include "connect.php";
$name=$_POST['name'];
// $code=$_POST['code'];
$count=$_POST['count'];
$discount=$_POST['discount'];
$userId=$_POST['userId'];
$isPublic=$_POST['isPublic'];
$duration=$_POST['duration'];

//lay ngay hien tai
$query = 'SELECT NOW()';
$data = mysqli_query($conn, $query);
$row = mysqli_fetch_assoc($data);
$dateCurrent = $row['NOW()'];

//lay ngay ket thuc sau $duration ngay
$query = 'SELECT DATE_ADD("'.$dateCurrent.'", INTERVAL '.$duration.' DAY) AS dateEnd';
$data = mysqli_query($conn, $query);
$row = mysqli_fetch_assoc($data);
$dateEnd = $row['dateEnd'];

//tao code
$query = "SELECT DATE_FORMAT(NOW(), '%Y%m%d%H%i%s') AS converted_datetime";
$data = mysqli_query($conn, $query);
$row = mysqli_fetch_assoc($data);
$code =$userId. "-" .$row['converted_datetime'];

$query = 'INSERT INTO `tbl_coupon`(`name`, `code`, `count`, `discount`, `dateFrom`, `dateTo`, `user_id`, `isPublic`) 
			VALUES ("'.$name.'","'.$code.'",'.$count.','.$discount.',"'.$dateCurrent.'","'.$dateEnd.'",'.$userId.','.$isPublic.')';
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