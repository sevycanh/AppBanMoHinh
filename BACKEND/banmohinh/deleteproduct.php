<?php
include "connect.php";

$idproduct = $_POST['id'];

$query = "UPDATE `tbl_product` SET `status`='0' WHERE product_id = '$idproduct'";
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