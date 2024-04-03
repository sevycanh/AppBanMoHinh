<?php
include "connect.php";

$idcategory = $_POST['idcategory'];

$query = "DELETE FROM `tbl_category` WHERE category_id = '$idcategory'";
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