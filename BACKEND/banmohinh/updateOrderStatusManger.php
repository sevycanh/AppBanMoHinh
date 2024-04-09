<?php
include "connect.php";
$orderid = $_POST['orderId'];
$order_status = $_POST['order_status'];
$query = "UPDATE `tbl_order` SET `order_status`= $order_status WHERE tbl_order.order_id = $orderid;";
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
