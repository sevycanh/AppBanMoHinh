<?php
include "connect.php";
$page = $_POST['page'];
$total = 10;
$pos = ($page-1)*$total;

$query = "SELECT * FROM `tbl_product` WHERE quantity > 0 ORDER BY product_id DESC LIMIT $pos, $total";
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

?>