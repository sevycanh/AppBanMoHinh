<?php
include "connect.php";

$name = $_POST['name'];
$price = $_POST['price'];
$quantity = $_POST['quantity'];
$description = $_POST['description'];
$main_image = $_POST['main_image'];
$sub_image = $_POST['sub_image'];
$coupon = $_POST['coupon'];
$category_id = $_POST['category_id'];
$status = $_POST['status'];

$query = "INSERT INTO `tbl_product`(`name`, `price`, `quantity`, `description`, `main_image`, `sub_image`, `coupon`, `category_id`, `status`) 
VALUES ('$name','$price','$quantity','$description','$main_image','$sub_image','$coupon','$category_id','$status')";
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