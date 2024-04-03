<?php
include "connect.php";

$idcategory = $_POST['idcategory'];
$namecategory = $_POST['namecategory'];
$imgcategory = $_POST['imgcategory'];

$query = "UPDATE `tbl_category` SET name='$namecategory', image='$imgcategory' WHERE category_id = '$idcategory'";
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