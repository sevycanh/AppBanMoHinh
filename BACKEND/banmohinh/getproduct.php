<?php
include "connect.php";

$query = "SELECT * FROM `tbl_product` WHERE status='1';";
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