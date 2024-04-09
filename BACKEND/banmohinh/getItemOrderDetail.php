<?php
include "connect.php";
$orderid = $_GET['orderId'];
$query = "SELECT
tbl_order.order_id as order_id,
tbl_orderdetail.product_id,
tbl_product.name,
tbl_product.main_image,
tbl_orderdetail.quantity,
tbl_product.price,
tbl_product.price * tbl_orderdetail.quantity AS total
FROM tbl_order
JOIN tbl_orderdetail ON tbl_orderdetail.order_id = tbl_order.order_id
JOIN tbl_product ON tbl_product.product_id = tbl_orderdetail.product_id
WHERE tbl_order.order_id = $orderid;";
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
