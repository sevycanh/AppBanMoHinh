<?php
include "connect.php";
$accountId = $_GET['accountId'];
$query = "SELECT
tbl_order.order_id as order_id,
tbl_order.account_id as account_id,
tbl_order.total as total,
tbl_order.order_status as order_status,
tbl_product.name as product_name,
tbl_product.price as perUnit,
tbl_orderdetail.quantity as quantity,
tbl_product.main_image as main_img,
COUNT(tbl_order.order_id) as productsInOrder
FROM tbl_order
JOIN tbl_orderdetail on tbl_orderdetail.order_id = tbl_order.order_id
JOIN tbl_account on tbl_account.account_id = tbl_order.account_id
JOIN tbl_product ON tbl_product.product_id = tbl_orderdetail.product_id
WHERE tbl_account.account_id = $accountId
GROUP BY tbl_order.order_id
ORDER BY tbl_order.order_id DESC;";
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
