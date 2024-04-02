<?php
include "connect.php";
$page = isset($_GET['page']) ? $_GET['page'] : 1;
$total = 5;
$pos = ($page - 1) * $total;

$category = isset($_GET['category']) ? $_GET['category'] : 1;

$query = "SELECT * FROM `tbl_product` WHERE `caregory_id` = '" . $category . "' LIMIT " . $pos . "," . $total . ";";
$data = $conn->query($query);
$result = array();

if ($data->num_rows > 0) {
    while ($row = $data->fetch_assoc()) {
        $result[] = $row;
        // code ...
    }
}
if (!$data) {
    die("Lỗi truy vấn: " . $conn->error);
}

if(!empty($result)){
    $arr = [
        'success' => true,
        'result' => $result
    ];
}else{
    $arr = [
        'success' => false,
        'result' => $result
    ];
}
print_r(json_encode($arr));
?>