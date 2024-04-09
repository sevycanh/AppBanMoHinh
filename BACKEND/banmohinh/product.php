<?php
$page = isset($_POST['page']) ? $_POST['page'] : 1;
$total = 6;
$pos = ($page - 1) * $total;
$category = isset($_POST['category']) ? $_POST['category'] : 1;
$type = $_POST['type'];
function getAll(){
    include "connect.php";
    global $category;
    global $pos;
    global $total;

    $query = "SELECT * FROM `tbl_product` WHERE `category_id` = '" . $category . "' LIMIT " . $pos . "," . $total . ";";
    $data = $conn->query($query);
    $result = array();
    
    if ($data->num_rows > 0) {
        while ($row = $data->fetch_assoc()) {
            $result[] = $row;
            // code ...
        }
    }
    if (!$data) {
        die("Lỗi truy vấn: " . $conn->error);
    }
    
    if(!empty($result)){
        $arr = [
            'success' => true,
            'result' => $result
        ];
    }else{
        $arr = [
            'success' => false,
            'result' => $result
        ];
    }
    print_r(json_encode($arr));
}
function getNew(){
    include "connect.php";
    global $category;
    global $pos;
    global $total;

    $query = "SELECT * FROM `tbl_product` WHERE `category_id` = '" . $category . "' ORDER BY product_id DESC LIMIT " . $pos . "," . $total . ";";
    $data = $conn->query($query);
    $result = array();
    
    if ($data->num_rows > 0) {
        while ($row = $data->fetch_assoc()) {
            $result[] = $row;
            // code ...
        }
    }
    if (!$data) {
        die("Lỗi truy vấn: " . $conn->error);
    }
    
    if(!empty($result)){
        $arr = [
            'success' => true,
            'result' => $result
        ];
    }else{
        $arr = [
            'success' => false,
            'result' => $result
        ];
    }
    print_r(json_encode($arr));
}
function getPromotion(){
    include "connect.php";
    global $category;
    global $pos;
    global $total;

    $query = "SELECT * FROM `tbl_product` WHERE coupon > 0 AND `category_id` = '" . $category . "' LIMIT " . $pos . "," . $total . ";";
    $data = $conn->query($query);
    $result = array();
    
    if ($data->num_rows > 0) {
        while ($row = $data->fetch_assoc()) {
            $result[] = $row;
            // code ...
        }
    }
    if (!$data) {
        die("Lỗi truy vấn: " . $conn->error);
    }
    
    if(!empty($result)){
        $arr = [
            'success' => true,
            'result' => $result
        ];
    }else{
        $arr = [
            'success' => false,
            'result' => $result
        ];
    }
    print_r(json_encode($arr));
}
function getSort(){
    include "connect.php";
    global $category;
    global $pos;
    global $total;
    global $type;

    $query = "SELECT * FROM `tbl_product` WHERE `category_id` = '" . $category . "' ORDER BY price $type LIMIT " . $pos . "," . $total . ";";
    $data = $conn->query($query);
    $result = array();
    
    if ($data->num_rows > 0) {
        while ($row = $data->fetch_assoc()) {
            $result[] = $row;
            // code ...
        }
    }
    if (!$data) {
        die("Lỗi truy vấn: " . $conn->error);
    }
    
    if(!empty($result)){
        $arr = [
            'success' => true,
            'result' => $result
        ];
    }else{
        $arr = [
            'success' => false,
            'result' => $result
        ];
    }
    print_r(json_encode($arr));
}
if ($type == 'lienquan') {
	getAll();
}
if ($type == 'moinhat') {
	getNew();
}
if ($type == 'khuyenmai') {
	getPromotion();
}
if ($type == 'ASC'){
	getSort();
}
if ($type == 'DESC'){
	getSort();
}


?>