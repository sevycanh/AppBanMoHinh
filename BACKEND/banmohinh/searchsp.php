<?php
$type = $_POST['type'];
function getSearchAll()
{
	include "connect.php";
	$tensp = $_POST['tensp'];
	$query = "SELECT * FROM `tbl_product` WHERE name LIKE '%" . $tensp . "%' AND quantity > 0;";
	$data = mysqli_query($conn, $query);
	$result = array();
	while ($row = mysqli_fetch_assoc($data)) {
		// code...
		$result[] = ($row);
	}
	if (!empty($result)) {
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
	print_r(json_encode($arr));
}
function getNewSearch()
{
	include "connect.php";
	$tensp = $_POST['tensp'];
	$query = "SELECT * FROM `tbl_product` WHERE name LIKE '%" . $tensp . "%' ORDER BY product_id DESC";
	$data = mysqli_query($conn, $query);
	$result = array();
	while ($row = mysqli_fetch_assoc($data)) {
		// code...
		$result[] = ($row);
	}
	if (!empty($result)) {
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
	print_r(json_encode($arr));
}
function getPromotionSearch()
{
	include "connect.php";
	$tensp = $_POST['tensp'];
	$query = "SELECT * FROM `tbl_product` WHERE coupon > 0 AND name LIKE '%" . $tensp . "%' ";
	$data = mysqli_query($conn, $query);
	$result = array();
	while ($row = mysqli_fetch_assoc($data)) {
		// code...
		$result[] = ($row);
	}
	if (!empty($result)) {
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
	print_r(json_encode($arr));
}

function getSortSearch(){
	include "connect.php";
	$tensp = $_POST['tensp'];
	$type = $_POST['type'];
	$query = "SELECT * FROM `tbl_product` WHERE name LIKE '%" . $tensp . "%' ORDER BY price $type";
	$data = mysqli_query($conn, $query);
	$result = array();
	while ($row = mysqli_fetch_assoc($data)) {
		// code...
		$result[] = ($row);
	}
	if (!empty($result)) {
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
	print_r(json_encode($arr));
}


if ($type == 'lienquan') {
	getSearchAll();
}
if ($type == 'moinhat') {
	getNewSearch();
}
if ($type == 'khuyenmai') {
	getPromotionSearch();
}
if ($type == 'ASC'){
	getSortSearch();
}
if ($type == 'DESC'){
	getSortSearch();
}
