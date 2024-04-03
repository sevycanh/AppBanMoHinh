<?php
include "connect.php";

$idcategory = $_POST['idcategory'];

$query = "UPDATE tbl_product SET status = '0' WHERE status = '1' AND category_id = '$idcategory';";
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