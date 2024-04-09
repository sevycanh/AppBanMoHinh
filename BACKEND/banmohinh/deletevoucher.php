<?php
include "connect.php";

$coupon_id = $_POST['id'];

$query = "DELETE FROM `tbl_coupon` WHERE coupon_id = '$coupon_id'";
$data = mysqli_query($conn, $query);

if($data == true){
    $arr = [
        'success' => true,
        'message' => "Xóa thành công"
    ];
}else{
    $arr = [
        'success' => false,
        'message' => "Xóa thất bại"
    ];
}

print_r(json_encode($arr));
?>