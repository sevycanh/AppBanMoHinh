<?php
include "connect.php";

$username = $_POST['username'];
$phone= $_POST['phone'];
$total = $_POST['total'];
$accountId = $_POST['accountId'];
$address = $_POST['address'];
$payment_method = $_POST['payment'];
$coupon_id = "1";
date_default_timezone_set('Asia/Ho_Chi_Minh');
$date = date("Y-m-d H:i:s");

$chitiet = $_POST['chitiet'];

// $query = 'INSERT INTO `tbl_order`(`iduser`, `diachi`, `sodienthoai`, `email`, `soluong`, `tongtien`) VALUES ('.$iduser.',"'.$diachi.'","'.$sdt.'","'.$email.'",'.$soluong.',"'.$tongtien.'")';

$query = "INSERT INTO `tbl_order`(`account_id`,`username`, `address`, `phone`, `payment_method`, `coupon_id`,  `total`,`date` ) VALUES ( '.$accountId.','$username' ,'$address','$phone','$payment_method','.$coupon_id.','.$total.' , '$date')";

//echo $query;
$data = mysqli_query($conn, $query);
if ($data == true){
	$query = 'SELECT order_id FROM `tbl_order` WHERE `account_id`= '.$accountId.' ORDER BY order_id DESC LIMIT 1';
	$data = mysqli_query($conn, $query);
	while($row = mysqli_fetch_assoc($data)){
		$iddonhang = $row;
	}
	if (!empty($iddonhang)){
		//co don hang
		$chitiet = json_decode($chitiet, true);
		foreach($chitiet as $key => $value){
			$truyvan = 'INSERT INTO `tbl_orderdetail`(`order_id`, `product_id`, `quantity`, `unitPrice`) 
			VALUES ('.$iddonhang["order_id"].','.$value["idProduct"].','.$value["quantity"].',"'.$value["price"].'")';
			$data = mysqli_query($conn, $truyvan);
		}
		if ($data == true){
			$arr = [
				'success' => true,
				'message' => "thanh cong",
				'iddonhang' => $iddonhang["order_id"]
			];
		} else {
			$arr = [
				'success' => false,
				'message' => "Khong thanh cong",
			];
		}
		print_r(json_encode($arr));
	}
	
} 
else {
	$arr = [
		'success' => false,
		'message' => "Khong thanh cong",
	];
	print_r(json_encode($arr));
}

?>