<?php
include "connect.php";

$idproduct = $_POST['id'];
$name = $_POST['name'];
$price = $_POST['price'];
$quantity = $_POST['quantity'];
$description = $_POST['description'];
$main_image = $_POST['main_image'];
$sub_image = $_POST['sub_image'];
$coupon = $_POST['coupon'];
$category_id = $_POST['category_id'];
$status = $_POST['status'];

$query = "UPDATE `tbl_product` SET `name`='$name',`price`='$price',`quantity`='$quantity',`description`='$description',
`main_image`='$main_image',`sub_image`='$sub_image',`coupon`='$coupon',`category_id`='$category_id',`status`='$status' WHERE product_id = '$idproduct'";
$data = mysqli_query($conn, $query);

if($data == true){
    $arr = [
        'success' => true,
        'message' => "thanh cong"
    ];
}else{
    $arr = [
        'success' => false,
        'message' => "khong thanh cong"
    ];
}

print_r(json_encode($arr));

?>