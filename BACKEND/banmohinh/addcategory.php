<?php
include "connect.php";

$namecategory = $_POST['namecategory'];
$imgcategory = $_POST['imgcategory'];

$query = "INSERT INTO tbl_category (name, image) VALUES ('$namecategory', '$imgcategory')";
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