<?php
include "connect.php";
$name=$_POST['name'];
$count=$_POST['count'];
$discount=$_POST['discount'];
$userId=$_POST['userId'];
$isPublic=$_POST['isPublic'];

$dateFrom = $_POST['dateFrom'];
$dateEnd = $_POST['dateEnd'];

if(strtotime($dateFrom) < strtotime($dateEnd)){
    //tao code
    $query = "SELECT DATE_FORMAT(NOW(), '%Y%m%d%H%i%s') AS converted_datetime";
    $data = mysqli_query($conn, $query);
    $row = mysqli_fetch_assoc($data);
    $code =$userId. "-" .$row['converted_datetime'];

    $query = 'INSERT INTO `tbl_coupon`(`name`, `code`, `count`, `discount`, `dateFrom`, `dateTo`, `user_id`, `isPublic`) 
                VALUES ("'.$name.'","'.$code.'",'.$count.','.$discount.',"'.$dateFrom.'","'.$dateEnd.'",'.$userId.','.$isPublic.')';
    $data = mysqli_query($conn, $query);

    if ($data == true){
        $arr = [
            'success' => true,
            'message' => "Thành công"
        ];
    } else {
        $arr = [
            'success' => false,
            'message' => "Thêm thất bại"
        ];
    }

    print_r(json_encode($arr));
} else {
    $arr = [
        'success' => false,
        'message' => "Ngày bắt đầu và kết thúc không phù hợp"
    ];
    print_r(json_encode($arr));
}
?>