<?php
$type = $_POST['type'];

function getCouponCurrent()
{
    include "connect.php";
    $isPublic = $_POST['isPublic'];
    // $query = 'SELECT * FROM `tbl_coupon` WHERE `isPublic` = '.$isPublic.' AND NOW() < `dateTo`';
    $query = 'SELECT * FROM `tbl_coupon` WHERE `isPublic` = '.$isPublic.' ORDER BY coupon_id DESC';
    $data = mysqli_query($conn, $query);
    $result = array();
    while ($row = mysqli_fetch_assoc($data)) {
        $result[] = ($row);
    }
    if (!empty($result)){
        $arr = [
            'success' => true,
            'message' => "Thành công",
            'result' => $result
        ];
    } else {
        $arr = [
            'success' => false,
            'message' => "Không có dữ liệu",
            'result' => $result
        ];
    }
    print_r(json_encode($arr));
}

if ($type == 'isPublic'){
    getCouponCurrent();
}
if ($type == 'isPrivate'){
    getCouponCurrent();
}
?>